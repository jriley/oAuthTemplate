package dev.jriley.auth

import dev.jriley.DaoTest
import org.junit.Assert.assertEquals
import org.junit.Test
import test
import kotlin.random.Random

class TokenDaoTest : DaoTest<TokenDao>() {

    override fun createDao(): TokenDao = database.tokenEntityDao()

    @Test
    fun insertNewTokenIntoDatabase() {
        val expectedToken = Token.test()

        assertEquals(null, testObject.getToken())

        testObject.insertToken(expectedToken)

        assertEquals(expectedToken, testObject.getToken())

        assertEquals(1, testObject.removeTokens())
    }

    @Test
    fun areTheInsertsLyingToMe() {
        val expectedCount = Random.nextInt(15)
        (1..expectedCount).map { id -> testObject.insertToken(Token.test(expectedId = id)) }

        assertEquals(expectedCount, testObject.getCount())
        assertEquals(expectedCount, testObject.removeTokens())
    }

    @Test
    fun onInsertionConflictJustReplace() {
        val firstExpectedToken = Token.test()
        val expectedReplacement = firstExpectedToken.copy(refreshToken = "Refresh-ME", scope = "NewScopeForMeThankYou")

        testObject.insertToken(firstExpectedToken)

        assertEquals(firstExpectedToken, testObject.getToken())

        testObject.insertToken(expectedReplacement)

        assertEquals(expectedReplacement, testObject.getToken())
        assertEquals(1, testObject.getCount())
    }
}