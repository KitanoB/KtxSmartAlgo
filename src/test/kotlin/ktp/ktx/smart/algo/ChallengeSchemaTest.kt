package ktp.ktx.smart.algo

import junit.framework.TestCase.assertNotNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import ktp.ktx.smart.algo.data.database.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.ChallengeService
import ktp.ktx.smart.algo.data.schema.ExposedChallenge
import org.jetbrains.exposed.sql.Database

class ChallengeSchemaTest {

    private lateinit var database: Database
    private lateinit var challengeService: ChallengeService


    @BeforeTest
    fun setup() {
        database = DatabaseFactory.getDatabase()
        challengeService = ChallengeService(database)
    }

    private fun createTestChallenge(): ExposedChallenge {
        return ExposedChallenge(
            id = 1,
            name = "Valid Parentheses",
            description = "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.",
            tags = listOf("stack, string"),
        )
    }

    @Test
    fun givenAnExposedChallenge_whenServiceCreate_ChallengeIsRetrieved() {
        runBlocking {
            val exposedChallenge = createTestChallenge()
            val id = challengeService.create(exposedChallenge)
            val retrievedChallenge = challengeService.read(id)
            assertNotNull(retrievedChallenge)
            assertEquals(retrievedChallenge?.name, "Valid Parentheses")
        }
    }

    @Test
    fun givenAnExposedChallenge_whenServiceReadAll_ChallengeIsRetrieved() {
        runBlocking {
            val exposedChallenge = createTestChallenge()
            val id = challengeService.create(exposedChallenge)
            val retrievedChallenge = challengeService.readAll()
            assertNotNull(retrievedChallenge)
            assertEquals(retrievedChallenge[0].name, "Valid Parentheses")
        }
    }

    @Test
    fun givenAnExposedChallenge_whenServiceDelete_ChallengeIsDeleted() {
        runBlocking {
            val exposedChallenge = createTestChallenge()
            val id = challengeService.create(exposedChallenge)
            challengeService.delete(id)
            val retrievedChallenge = challengeService.read(id)
            assertEquals(retrievedChallenge, null)
        }
    }
}