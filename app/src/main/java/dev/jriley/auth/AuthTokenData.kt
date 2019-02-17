package dev.jriley.auth

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface AuthTokenData {

    fun currentToken(): Maybe<Token>

    fun insert(token: Token): Completable

    fun clear(): Single<Int>
}
