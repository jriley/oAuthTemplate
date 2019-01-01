package dev.jriley.login

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable

class SignInViewModel(private val tokenRepo: TokenRepo = TokenRepositoryFactory.tokenRepository) : ViewModel() {
    fun loginAttempt(loginCredentials: LoginCredentials): Completable = tokenRepo.logInAttempt(loginCredentials)
}

data class LoginCredentials(val userName: String, val password: String)