package dev.jriley.login

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TokenRepository(private val ioScheduler: Scheduler = Schedulers.io()) {
    fun isValid(): Single<Boolean> = Single.just(true)
}

object TokenRepositoryFactory {
    val tokenRepository by lazy { TokenRepository() }
}
