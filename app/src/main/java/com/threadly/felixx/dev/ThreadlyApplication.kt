package com.threadly.felixx.dev

import android.app.Application
import com.threadly.felixx.dev.data.ThreadlyDatabase
import com.threadly.felixx.dev.data.ThreadlyRepository
import com.threadly.felixx.dev.mail.ProviderRegistry
import com.threadly.felixx.dev.security.SecretsStore
import com.threadly.felixx.dev.sync.SyncCoordinator
import com.threadly.felixx.dev.sync.SyncScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThreadlyApplication : Application() {
    lateinit var database: ThreadlyDatabase
    lateinit var repository: ThreadlyRepository
    lateinit var providerRegistry: ProviderRegistry
    lateinit var syncCoordinator: SyncCoordinator
    lateinit var secretsStore: SecretsStore

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val appScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        secretsStore = SecretsStore(this)
        database = ThreadlyDatabase.create(this)
        repository = ThreadlyRepository(database)
        providerRegistry = ProviderRegistry(this, secretsStore)
        syncCoordinator = SyncCoordinator(this)
        appScope.launch {
            repository.ensureDefaults()
            // Immediate sync on start
            triggerSyncInternal()
        }
        SyncScheduler.schedule(this)
    }

    fun triggerSync() {
        appScope.launch { triggerSyncInternal() }
    }

    private suspend fun triggerSyncInternal() {
        if (_isSyncing.value) return
        _isSyncing.value = true
        try { syncCoordinator.syncAll() } catch (_: Exception) {}
        finally { _isSyncing.value = false }
    }
}
