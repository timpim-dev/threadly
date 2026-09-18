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
import kotlinx.coroutines.launch

class ThreadlyApplication : Application() {
    lateinit var database: ThreadlyDatabase
    lateinit var repository: ThreadlyRepository
    lateinit var providerRegistry: ProviderRegistry
    lateinit var syncCoordinator: SyncCoordinator
    lateinit var secretsStore: SecretsStore

    override fun onCreate() {
        super.onCreate()
        secretsStore = SecretsStore(this)
        database = ThreadlyDatabase.create(this)
        repository = ThreadlyRepository(database)
        providerRegistry = ProviderRegistry(this, secretsStore)
        syncCoordinator = SyncCoordinator(this)
        CoroutineScope(Dispatchers.IO).launch { repository.ensureDefaults() }
        SyncScheduler.schedule(this)
    }
}
