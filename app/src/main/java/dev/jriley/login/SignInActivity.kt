package dev.jriley.login

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import dev.jriley.finishAndExitWithAnimation
import dev.jriley.isValidEmail
import dev.jriley.landing.MainActivity
import dev.jriley.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_sign_in.*
import timber.log.Timber

class SignInActivity : AppCompatActivity() {
    private lateinit var viewModel: SignInViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)

        password.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    Timber.d("Send ...")
                    attemptLogin()
                    true
                }
                else -> {
                    Timber.e("Unknown IME action $actionId")
                    false
                }
            }
        }

        loginBtn.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        userNameLayout.error = null
        passwordLayout.error = null

        when {
            userName.text.toString().isBlank() -> userNameLayout.error = getString(R.string.error_empty_field)
            password.text.toString().isBlank() -> passwordLayout.error = getString(R.string.error_empty_field)
            BuildConfig.IS_USER_EMAIL && userName.text.toString().isValidEmail().not() -> userNameLayout.error = getString(R.string.error_invalid_email)
            else -> loginWithServer()
        }
    }

    private fun loginWithServer() {
        compositeDisposable.add(viewModel.loginAttempt(LoginCredentials(userName.text.toString(), password.text.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewComponentsState() }
            .doFinally {
                viewComponentsState(true)
                compositeDisposable.clear()
            }
            .subscribe({ startMain() }) { error ->
                when (error) {
                    is InvalidGrantException -> {
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.failed_login_title))
                            .setMessage(getString(R.string.failed_login))
                            .setPositiveButton(getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                    else -> {
                        Timber.e(error, "Problem logging in")
                        Toast.makeText(this, getString(R.string.failed_login), Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun viewComponentsState(enabled: Boolean = false) {
        progressBar.visible(!enabled)
        loginBtn.isEnabled = enabled
        passwordLayout.isEnabled = enabled
        userNameLayout.isEnabled = enabled
        imageHolder.isEnabled = enabled
    }

    private fun startMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finishAndExitWithAnimation()
    }
}
