package dev.jriley

import android.annotation.SuppressLint
import android.util.Log
import dev.jriley.login.BuildConfig
import timber.log.Timber

@SuppressLint("LogNotTimber")
class LogTree : Timber.DebugTree() {
    init {
        Log.e(
            "Debug-DB",
            "You have to run the command:" +
                    "\nadb forward tcp:${BuildConfig.PORT_NUMBER} tcp:${BuildConfig.PORT_NUMBER}" +
                    "\nbefore you can see the database" +
                    "\nto stop run :" +
                    "\nadb forward --remove tcp:${BuildConfig.PORT_NUMBER}" +
                    "\n or " +
                    "\nadb forward --remove-all"
        )
    }
}