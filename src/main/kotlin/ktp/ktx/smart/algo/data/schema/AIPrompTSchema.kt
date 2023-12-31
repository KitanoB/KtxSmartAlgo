package ktp.ktx.smart.algo.data.schema

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

@Serializable
data class ExposedAIPrompt(
    val id: Int? = null,
    val userId: Int,
    val submissionId: Int,
    val promptText: String,
    val createdAt: Long = System.currentTimeMillis(),
)

class AIPromptService(private val database: Database) {

    object AIPromptTable : Table("ai_prompts") {
        val id = integer("id").autoIncrement()
        val userId = integer("user_id").references(UserService.Users.id)
        val submissionId = integer("submission_id").references(SubmissionService.Submissions.id)
        val promptText = text("prompt_text")
        val createdAt = long("created_at")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(AIPromptTable)
        }
    }

    suspend fun create(prompt: ExposedAIPrompt): Int = transaction(database) {
        AIPromptTable.insert {
            it[userId] = prompt.userId
            it[submissionId] = prompt.submissionId
            it[promptText] = prompt.promptText
            it[createdAt] = prompt.createdAt
        }[AIPromptTable.id]
    }

    suspend fun read(id: Int): ExposedAIPrompt? = transaction(database) {
        AIPromptTable.select { AIPromptTable.id eq id }
            .map {
                ExposedAIPrompt(
                    it[AIPromptTable.id],
                    it[AIPromptTable.userId],
                    it[AIPromptTable.submissionId],
                    it[AIPromptTable.promptText],
                    it[AIPromptTable.createdAt]
                )
            }.singleOrNull()
    }

    suspend fun readAll(): List<ExposedAIPrompt> = transaction(database) {
        AIPromptTable.selectAll()
            .map {
                ExposedAIPrompt(
                    it[AIPromptTable.id],
                    it[AIPromptTable.userId],
                    it[AIPromptTable.submissionId],
                    it[AIPromptTable.promptText],
                    it[AIPromptTable.createdAt]
                )
            }
    }

    suspend fun readAllBySubmissionId(submissionId: Int): List<ExposedAIPrompt> = transaction(database) {
        AIPromptTable.select { AIPromptTable.submissionId eq submissionId }
            .map {
                ExposedAIPrompt(
                    it[AIPromptTable.id],
                    it[AIPromptTable.userId],
                    it[AIPromptTable.submissionId],
                    it[AIPromptTable.promptText],
                    it[AIPromptTable.createdAt]
                )
            }
    }

    suspend fun update(id: Int, prompt: ExposedAIPrompt) = transaction(database) {
        AIPromptTable.update({ AIPromptTable.id eq id }) {
            it[userId] = prompt.userId
            it[submissionId] = prompt.submissionId
            it[promptText] = prompt.promptText
            it[createdAt] = prompt.createdAt
        }
    }

    suspend fun delete(id: Int) = transaction(database) {
        AIPromptTable.deleteWhere { AIPromptTable.id eq id }
    }

}