package dev.jriley.login

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TokenRepository(private val ioScheduler: Scheduler = Schedulers.io()) : TokenRepo {
    override fun isValid(): Single<Boolean> = Single.just(false)
}

interface TokenRepo {
    fun isValid(): Single<Boolean>
}
