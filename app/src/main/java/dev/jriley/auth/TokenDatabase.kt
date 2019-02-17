package dev.jriley.auth

import android.arch.persistence.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

const val token_table = "token"

object DatabaseProvider {
    lateinit var tokenDatabase: TokenDatabase
}

@Database(entities = [Token::class], version = 1)
abstract class TokenDatabase : RoomDatabase() {
    abstract fun tokenEntityDao(): TokenDao
}

@Dao
abstract class TokenDao : AuthTokenData {
    override fun currentToken(): Maybe<Token> = Maybe.fromCallable { getToken() }

    @Transaction
    override fun insert(token: Token): Completable = Completable.fromCallable {
        removeTokens()
        insertToken(token)
    }

    @Transaction
    override fun clear(): Single<Int> = Single.fromCallable {
        removeTokens()
    }

    @Query("SELECT * from $token_table")
    abstract fun getToken(): Token?

    @Query("SELECT COUNT(*) from $token_table")
    abstract fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertToken(token: Token)

    @Query("DELETE FROM $token_table")
    abstract fun removeTokens(): Int
}