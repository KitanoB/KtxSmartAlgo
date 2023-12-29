package ktp.ktx.smart.algo.data.schema

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

@Serializable
data class ExposedAIAdvice(
    val id: Int? = null,
    val promptId: Int,
    val adviceText: String,
    val receivedAt: Long = System.currentTimeMillis()
)

class AIAdviceService(private val database: Database) {

    object AIAdvices : Table() {
        val id = integer("id").autoIncrement()
        val promptId = integer("promptId")
        val adviceText = text("adviceText")
        val receivedAt = long("receivedAt")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(AIAdvices)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
    suspend fun create(advice: ExposedAIAdvice): Int = dbQuery {
        AIAdvices.insert {
            it[promptId] = advice.promptId
            it[adviceText] = advice.adviceText
            it[receivedAt] = advice.receivedAt
        }[AIAdvices.id]
    }

    suspend fun read(id: Int): ExposedAIAdvice? {
        return dbQuery {
            AIAdvices.select { AIAdvices.id eq id }
                .map {
                    ExposedAIAdvice(
                        it[AIAdvices.id],
                        it[AIAdvices.promptId],
                        it[AIAdvices.adviceText],
                        it[AIAdvices.receivedAt]
                    )
                }.singleOrNull()
        }
    }

    suspend fun readAll(): List<ExposedAIAdvice> {
        return dbQuery {
            AIAdvices.selectAll()
                .map {
                    ExposedAIAdvice(
                        it[AIAdvices.id],
                        it[AIAdvices.promptId],
                        it[AIAdvices.adviceText],
                        it[AIAdvices.receivedAt]
                    )
                }
        }
    }

    suspend fun readAllForPrompt(promptId: Int): List<ExposedAIAdvice> {
        return dbQuery {
            AIAdvices.select { AIAdvices.promptId eq promptId }
                .map {
                    ExposedAIAdvice(
                        it[AIAdvices.id],
                        it[AIAdvices.promptId],
                        it[AIAdvices.adviceText],
                        it[AIAdvices.receivedAt]
                    )
                }
        }
    }

    suspend fun update(id: Int, advice: ExposedAIAdvice) {
        dbQuery {
            AIAdvices.update({ AIAdvices.id eq id }) {
                it[promptId] = advice.promptId
                it[adviceText] = advice.adviceText
                it[receivedAt] = advice.receivedAt
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            AIAdvices.deleteWhere {
                AIAdvices.id.eq(id)
            }
        }
    }



}