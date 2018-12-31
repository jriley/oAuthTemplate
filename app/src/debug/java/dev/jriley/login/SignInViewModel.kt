package dev.jriley.login

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable

class SignInViewModel : ViewModel() {
    fun loginAttempt(loginCredentials: LoginCredentials): Completable =
            Completable.error(InvalidGrantException())
//        userRepository.loginAttempt(loginCredentials)
}

data class LoginCredentials(val userName: String, val password: String)