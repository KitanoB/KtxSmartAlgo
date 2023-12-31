package ktp.ktx.smart.algo

import junit.framework.TestCase.assertNotNull
import kotlin.test.Test
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import ktp.ktx.smart.algo.data.database.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.AIAdviceService
import ktp.ktx.smart.algo.data.schema.ExposedAIAdvice
import org.jetbrains.exposed.sql.Database
import org.junit.Before

class AIAdviceSchemaTest {

    private lateinit var database: Database
    private lateinit var aiAdviceService: AIAdviceService

    @Before
    fun setup() {
        database = DatabaseFactory.getDatabase()
        aiAdviceService = AIAdviceService(database)
    }

    private fun createTestAIAdvice(): ExposedAIAdvice {
        return ExposedAIAdvice(
            id = null,
            promptId = 1,
            adviceText = "Use a stack to keep track of the opening brackets. Every time you encounter a closing bracket, check whether the last opening bracket you encountered is the corresponding closing bracket. If it is, pop it off the stack. If it isn't, or if the stack is empty, return false. After processing the string, check whether the stack is empty. If it is, return true, otherwise return false.",
            receivedAt = System.currentTimeMillis()
        )
    }

    @Test
    fun givenAnExposedAIAdvice_whenServiceCreate_AIAdviceIsRetrieved() {
        runBlocking {
            val exposedAIAdvice = createTestAIAdvice()
            val id = aiAdviceService.create(exposedAIAdvice)
            val retrievedAIAdvice = aiAdviceService.read(id)
            assertNotNull(retrievedAIAdvice)
        }
    }

    @Test
    fun givenAnExposedAIAdvice_whenServiceUpdate_AIAdviceIsRetrieved() {
        runBlocking {
            val exposedAIAdvice = createTestAIAdvice()
            exposedAIAdvice.adviceText =
                "Use a stack to keep track of the opening brackets. Every time you encounter a closing bracket, check whether the last opening bracket you encountered is the corresponding closing bracket. If it is, pop it off the stack. If it isn't, or if the stack is empty, return false. After processing the string, check whether the stack is empty. If it is, return true, otherwise return false. This is a new advice."
            aiAdviceService.update(1, exposedAIAdvice)
            val retrievedAIAdvice = aiAdviceService.read(1)
            assertNotNull(retrievedAIAdvice)
        }
    }

    @Test
    fun givenAnExposedAIAdvice_whenServiceDelete_AIAdviceIsDeleted() {
        runBlocking {
            val exposedAIAdvice = createTestAIAdvice()
            val id = aiAdviceService.create(exposedAIAdvice)
            aiAdviceService.delete(id)
            val retrievedAIAdvice = aiAdviceService.read(id)
            assertNull(retrievedAIAdvice)
        }
    }


}