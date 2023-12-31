package ktp.ktx.smart.algo

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import ktp.ktx.smart.algo.data.database.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.ExposedUser
import ktp.ktx.smart.algo.data.schema.UserService
import org.jetbrains.exposed.sql.Database

class UserSchemaTest {

    private lateinit var database: Database

    private lateinit var userService: UserService


    @BeforeTest
    fun setup() {
        database = DatabaseFactory.getDatabase()
        userService = UserService(database)
    }

    private fun createTestUser(): ExposedUser {
        return ExposedUser(
            id = null,
            username = "test",
            email = "",
            passwordHash = "",
            lastLoginDate = null,
            createdAt = null,
            isBanned = false,
        )
    }

    @Test
    fun givenAnExposedUser_whenServiceCreate_UserIsRetrieved() {
        runBlocking {
            val exposedUser = createTestUser()
            val id = userService.create(exposedUser)
            val retrievedUser = userService.read(id)
            assertNotNull(retrievedUser)
        }
    }

    // test update user
    @Test
    fun givenAnExposedUser_whenServiceUpdate_UserIsRetrieved() {
        runBlocking {
            val exposedUser = createTestUser()
            exposedUser.username = "test2"
            userService.update(1, exposedUser)
            val retrievedUser = userService.read(1)
            assertNotNull(retrievedUser)
            assertEquals(retrievedUser.username, "test2")
        }
    }

    // test delete user
    @Test
    fun givenAnExposedUser_whenServiceDelete_UserIsNotRetrieved() {
        runBlocking {
            val exposedUser = createTestUser()
            val id = userService.create(exposedUser)
            userService.delete(id)
            val retrievedUser = userService.read(id)
            assertEquals(retrievedUser, null)
        }
    }
}