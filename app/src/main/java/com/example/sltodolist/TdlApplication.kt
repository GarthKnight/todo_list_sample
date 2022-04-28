package com.example.sltodolist

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.example.sltodolist.internal.DI
import timber.log.Timber

class TdlApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        buildDependencyGraph()
    }

    private fun buildDependencyGraph() {
        DI.init(this)

        DI.app.provideComponent().inject(this)
    }

}