package dev.jriley.splash

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dev.jriley.finishAndExitWithAnimation
import dev.jriley.landing.MainActivity
import dev.jriley.login.R
import dev.jriley.login.SignInActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ViewModelProviders.of(this).get(SplashViewModel::class.java).apply {
            loadingObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ b -> start(if (b) MainActivity::class.java else SignInActivity::class.java) }, Timber::e)
        }
    }

    private fun start(clazz: Class<out AppCompatActivity>) {
        startActivity(Intent(this, clazz))
        finishAndExitWithAnimation()
    }
}
