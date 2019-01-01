package dev.jriley.login

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TokenRepository(private val ioScheduler: Scheduler = Schedulers.io()) : TokenRepo {
    override fun isValid(): Single<Boolean> = Single.just(false)

    override fun logInAttempt(loginCredentials: LoginCredentials): Completable = Completable.error(InvalidGrantException())
}

interface TokenRepo {
    fun isValid(): Single<Boolean>
    fun logInAttempt(loginCredentials: LoginCredentials): Completable
}
