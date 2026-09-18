package com.threadly.felixx.dev.sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.threadly.felixx.dev.R
import com.threadly.felixx.dev.ThreadlyApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class MailSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as ThreadlyApplication
        return runCatching { app.syncCoordinator.syncAll() }.fold({ Result.success() }, { Result.retry() })
    }
}

class SyncCoordinator(private val app: ThreadlyApplication) {
    companion object { private const val TAG = "ThreadlySync" }

    suspend fun syncAll() {
        val accounts = app.repository.accounts.first()
        android.util.Log.d(TAG, "syncAll: ${accounts.size} accounts")
        accounts.filter { it.enabled }.forEach { account ->
            runCatching { syncAccount(account.id) }
                .onFailure { android.util.Log.e(TAG, "syncAccount failed for ${account.email}", it) }
        }
    }

    suspend fun syncAccount(accountId: String) {
        val account = app.database.accounts().get(accountId) ?: return
        android.util.Log.d(TAG, "syncAccount start: ${account.email} provider=${account.provider}")
        val state = app.database.syncStates().get(accountId)
        android.util.Log.d(TAG, "syncAccount cursor: ${state?.lastCursor}")
        val page = app.providerRegistry.forAccount(account).receive(account, state?.lastCursor)
        android.util.Log.d(TAG, "syncAccount received: ${page.messages.size} messages")
        val matcher = com.threadly.felixx.dev.mail.ClubMatcher(app.database.clubs())
        page.messages.forEach { app.repository.ingest(account, it, matcher) }
        app.database.syncStates().save(com.threadly.felixx.dev.data.SyncStateEntity(accountId, page.cursor, System.currentTimeMillis(), null))
        app.database.accounts().updateStatus(accountId, com.threadly.felixx.dev.data.AccountStatus.CONNECTED, null)
        android.util.Log.d(TAG, "syncAccount done: ${account.email}")
    }
}

object SyncScheduler {
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<MailSyncWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("mail-sync", ExistingPeriodicWorkPolicy.KEEP, request)
    }
}

class FastSyncService : android.app.Service() {
    override fun onBind(intent: android.content.Intent?): android.os.IBinder? = null
    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= 26) manager.createNotificationChannel(NotificationChannel("fast_sync", "Faster sync", NotificationManager.IMPORTANCE_LOW))
        startForeground(42, NotificationCompat.Builder(this, "fast_sync").setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle("Faster sync active").setContentText("Threadly is checking mail more often").setOngoing(true).build())
        CoroutineScope(Dispatchers.IO).launch { while (true) { delay(120_000); (application as ThreadlyApplication).syncCoordinator.syncAll() } }
    }
}
