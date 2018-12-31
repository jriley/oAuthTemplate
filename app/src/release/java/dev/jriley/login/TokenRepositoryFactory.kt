package dev.jriley.login

object TokenRepositoryFactory {
    val tokenRepository: TokenRepo by lazy { TokenRepository() }
}
