package com.copart.rtlaisdk

import android.app.Application
import com.copart.rtlaisdk.data.model.RTLClientParams
import com.copart.rtlaisdk.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RTLAIApplication : Application() {

    companion object {
        var rtlClientParams: RTLClientParams? = null
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@RTLAIApplication)
            modules(appModules)
        }
    }
}