package ktp.ktx.smart.algo

import junit.framework.TestCase.assertNotNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import ktp.ktx.smart.algo.data.database.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.AIPromptService
import ktp.ktx.smart.algo.data.schema.ExposedAIPrompt
import ktp.ktx.smart.algo.data.schema.ExposedSubmission
import ktp.ktx.smart.algo.data.schema.ExposedUser
import ktp.ktx.smart.algo.data.schema.SubmissionService
import ktp.ktx.smart.algo.data.schema.UserService
import org.jetbrains.exposed.sql.Database
import org.junit.Before

class AIPromptChallengeSchemaTest {

    private lateinit var database: Database
    private lateinit var aiPromptService: AIPromptService
    private lateinit var userService: UserService
    private lateinit var submissionService: SubmissionService

    @Before
    fun before() {
        database = DatabaseFactory.getDatabase()
        userService = UserService(database)
        submissionService = SubmissionService(database)
        runBlocking {
            submissionService.create(
                ExposedSubmission(
                    id = null,
                    userId = 1,
                    challengeId = 1,
                    testName = "Valid Parentheses", // This should not be null
                    testResult = true,
                    numberOfSubmissions = 1,
                    temporalComplexity = "O(1)",
                    spatialComplexity = "O(1)",
                    problemUnderstanding = "No difficulties",
                    resolutionStrategy = "Used specific algorithm",
                    confidenceLevel = 7,
                    learnings = "Improved problem-solving skills",
                    startTime = System.currentTimeMillis(),
                    endTime = System.currentTimeMillis()
                )
            )
            userService.create(
                ExposedUser(
                    id = null,
                    username = "test",
                    email = "",
                    passwordHash = "",
                    lastLoginDate = null,
                    createdAt = null,
                    isBanned = false,
                )
            )
        }
    }

    @BeforeTest
    fun setup() {
        database = DatabaseFactory.getDatabase()
        aiPromptService = AIPromptService(database)
    }

    private fun createTestAIPromptChallenge(): ExposedAIPrompt {
        return ExposedAIPrompt(
            id = null,
            userId = 1,
            submissionId = 1,
            promptText = "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.",
            createdAt = System.currentTimeMillis()
        )
    }

    // test create AIPromptChallenge

    @Test
    fun givenAnExposedAIPromptChallenge_whenServiceCreate_AIPromptChallengeIsRetrieved() {
        runBlocking {
            val exposedAIPromptChallenge = createTestAIPromptChallenge()
            val id = aiPromptService.create(exposedAIPromptChallenge)
            val retrievedAIPromptChallenge = aiPromptService.read(id)
            assertNotNull(retrievedAIPromptChallenge)
            assertEquals(retrievedAIPromptChallenge?.promptText, exposedAIPromptChallenge.promptText)
        }
    }

    // delete AIPromptChallenge
    @Test
    fun givenAnExposedAIPromptChallenge_whenServiceDelete_AIPromptChallengeIsNotRetrieved() {
        runBlocking {
            val exposedAIPromptChallenge = createTestAIPromptChallenge()
            val id = aiPromptService.create(exposedAIPromptChallenge)
            aiPromptService.delete(id)
            val retrievedAIPromptChallenge = aiPromptService.read(id)
            assertEquals(retrievedAIPromptChallenge, null)
        }
    }
}