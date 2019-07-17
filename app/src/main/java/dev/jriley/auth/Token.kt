package dev.jriley.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = token_table)
data class Token(
    @PrimaryKey(autoGenerate = false)
    val accessToken: String,
    val username: String,
    val refreshToken: String,
    val scope: String
) {
    constructor(atr: AuthTokenResponse) : this(atr.accessToken, atr.resourceOwner, atr.refreshToken, atr.scope)

    companion object
}
