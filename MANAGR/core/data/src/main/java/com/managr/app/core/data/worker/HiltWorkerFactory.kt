package com.managr.app.core.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class HiltWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ListenableWorker>>
) : WorkerFactory() {
    
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
        val provider = workerFactories[workerClass] ?: return null
        return provider.get()
    }
}
