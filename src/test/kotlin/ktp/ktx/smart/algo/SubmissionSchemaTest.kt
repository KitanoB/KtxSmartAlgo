package ktp.ktx.smart.algo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import ktp.ktx.smart.algo.data.database.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.ExposedSubmission
import ktp.ktx.smart.algo.data.schema.SubmissionService
import org.jetbrains.exposed.sql.Database
import org.junit.Before

class SubmissionSchemaTest {

    private lateinit var database: Database

    private lateinit var submissionService: SubmissionService

    @Before
    fun before() {
        database = DatabaseFactory.getDatabase()
        submissionService = SubmissionService(database)
    }

    private fun createTestSubmission(): ExposedSubmission {
        return ExposedSubmission(
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
    }


    @Test
    fun `create submission should return valid id`() {
        runBlocking {
            val testSubmission = createTestSubmission()
            val id = submissionService.create(testSubmission)
            assertNotNull(id, "Submission creation should return a non-null ID")
        }
    }

    @Test
    fun `read submission should return correct data`() {
        runBlocking {
            val testSubmission = createTestSubmission()

            val id = submissionService.create(testSubmission)
            val retrievedSubmission = submissionService.read(id)

            assertNotNull(retrievedSubmission, "Retrieved submission should not be null")
            assertEquals(
                testSubmission.testName,
                retrievedSubmission.testName,
                "Retrieved submission should match the created one"
            )
        }
    }


    @Test
    fun `delete submission should return correct data`() {
        runBlocking {
            val testSubmission = createTestSubmission()
            val id = submissionService.create(testSubmission)
            val retrievedSubmission = submissionService.read(id)
            submissionService.delete(id)

            assertNotNull(retrievedSubmission, "Retrieved submission should not be null")
            assertEquals(
                testSubmission.testName,
                retrievedSubmission.testName,
                "Retrieved submission should match the created one"
            )
        }
    }


}