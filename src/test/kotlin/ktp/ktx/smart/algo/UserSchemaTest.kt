package ktp.ktx.smart.algo

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import ktp.ktx.smart.algo.data.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.ExposedUser
import ktp.ktx.smart.algo.data.schema.UserService
import org.jetbrains.exposed.sql.Database

class UserSchemaTest {

    lateinit var database: Database
    lateinit var userService: UserService
    lateinit var exposedUser: ExposedUser

    @BeforeTest
    fun setup() {
        if (!::database.isInitialized) {
            database = DatabaseFactory.getDatabase()
        }
        if (!::userService.isInitialized) {
            userService = UserService(database)
        }

        if (!::exposedUser.isInitialized) {
            exposedUser = ExposedUser(
                id = null,
                username = "test",
                email = "",
                passwordHash = "",
                lastLoginDate = null,
                createdAt = null,
                isBanned = false,
            )
        }
    }

    @Test
    fun givenAnExposedUser_whenServiceCreate_returnAnIdNotNull() {
        runBlocking {
            userService.clear()
            val id = userService.create(exposedUser)
            println("id: $id")
            assertNotNull(id)
            assertNotNull(userService.read(id))
            assertEquals(userService.getNbUsers(), 1)
            userService.create(exposedUser)
            assertNotEquals(userService.getNbUsers(), 1)
        }
    }

    @Test
    fun givenAnExposedUserId_WhenServiceReadThisUser_returnTheCorrectUser() {
        runBlocking {
            userService.clear()
            val id = userService.create(exposedUser)
            val user = userService.read(id)
            assertNotNull(user)
            assertEquals(user.id, id)
        }
    }

    @Test
    fun givenAnExposedUser_whenServiceUpdateThisUser_returnTheCorrectUser() {
        runBlocking {
            userService.clear()
            val id = userService.create(exposedUser)
            val user = userService.read(id)
            assertNotNull(user)
            assertEquals(user.id, id)
            user.username = "NewUsername"
            userService.update(id, user)
            val updatedUser = userService.read(id)
            assertEquals(user.username, updatedUser?.username)
        }
    }

    @Test
    fun givenAnExposedUser_whenServiceDeleteThisUser_returnTheCorrectUser() {
        runBlocking {
            userService.clear()
            val id = userService.create(exposedUser)
            val user = userService.read(id)
            assertNotNull(user)
            assertEquals(user.id, id)
            userService.delete(id)
            val deletedUser = userService.read(id)
            assertNull(deletedUser)
        }
    }

}