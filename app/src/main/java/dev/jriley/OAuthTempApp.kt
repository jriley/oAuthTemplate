package dev.jriley

import android.app.Application
import android.arch.persistence.room.Room
import dev.jriley.auth.DatabaseProvider
import dev.jriley.auth.TokenDatabase
import timber.log.Timber

class OAuthTempApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(LogTree())

        DatabaseProvider.tokenDatabase = Room.databaseBuilder(this, TokenDatabase::class.java, "token.db")
            .build()
    }
}