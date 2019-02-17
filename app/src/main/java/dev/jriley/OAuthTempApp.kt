package dev.jriley

import android.app.Application
import timber.log.Timber

class OAuthTempApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(LogTree())
    }
}