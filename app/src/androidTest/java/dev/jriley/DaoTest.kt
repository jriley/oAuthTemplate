package dev.jriley

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import dev.jriley.auth.TokenDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DaoTest<T> {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var database: TokenDatabase

    protected val testObject : T by lazy { createDao() }

    abstract fun createDao() : T

    @Before
    open fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, TokenDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }
}
