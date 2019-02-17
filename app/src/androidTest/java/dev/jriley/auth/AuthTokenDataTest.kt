package dev.jriley.auth

import dev.jriley.DaoTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import test

class AuthTokenDataTest : DaoTest<TokenDao>() {
    override fun createDao(): TokenDao = database.tokenEntityDao()

    private lateinit var testDataObject: AuthTokenData

    @Before
    override fun setUp() {
        super.setUp()
        testDataObject = testObject
    }

    @Test
    fun assertTheInsertThroughTheInterface() {
        val token = Token.test()

        testDataObject.currentToken().test().apply {
            assertNoErrors()
            assertNoValues()
            assertComplete()
        }

        testDataObject.insert(token).test().apply {
            assertNoErrors()
            assertNoValues()
            assertComplete()
        }

        testDataObject.currentToken().test().apply {
            assertNoErrors()
            assertValues(token)
            assertComplete()
        }
    }

    @Test
    fun thereShouldOnlyBeOneTokenAtAnyGivenTime() {
        (0..10).map {
            testDataObject.insert(Token.test()).test().apply {
                assertNoErrors()
                assertNoValues()
                assertComplete()
            }
        }

        assertEquals(1, testObject.getCount())
    }

    @Test
    fun exposeLogoutAsAnOption() {

        testDataObject.insert(Token.test()).test().apply {
            assertNoErrors()
            assertNoValues()
            assertComplete()
        }

        assertEquals(1, testObject.getCount())

        testDataObject.clear().test().apply {
            assertNoErrors()
            assertValue(1)
            assertComplete()
        }

        assertEquals(0, testObject.getCount())

        testDataObject.currentToken().test().apply {
            assertNoErrors()
            assertNoValues()
            assertComplete()
        }
    }
}