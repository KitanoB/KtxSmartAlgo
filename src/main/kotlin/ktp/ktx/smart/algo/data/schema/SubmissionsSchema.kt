package ktp.ktx.smart.algo.data.schema


import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
@Serializable
data class ExposedSubmission(
    val id: Int? = null, // Nullable for new submissions
    val userId: Int? = null, // TODO: Remove nullable once we have user auth
    val challengeId: Int,
    val testName: String,
    val testResult: Boolean,
    val numberOfSubmissions: Int,
    val temporalComplexity: String,
    val spatialComplexity: String,
    val problemUnderstanding: String,
    val resolutionStrategy: String,
    val confidenceLevel: Int,
    val learnings: String,
    val startTime: Long,
    val endTime: Long
)

class SubmissionService(private val database: Database) {

    object Submissions : Table() {
        val id = integer("id").autoIncrement()
        val userId = integer("userId").nullable()
        val challengeId = integer("challengeId")
        val testName = varchar("testName", 50)
        val testResult = bool("testResult")
        val numberOfSubmissions = integer("numberOfSubmissions").default(1)
        val temporalComplexity = varchar("temporalComplexity", 50).default("")
        val spatialComplexity = varchar("spatialComplexity", 50).default("")
        val problemUnderstanding = varchar("problemUnderstanding", 50).default("")
        val resolutionStrategy = varchar("resolutionStrategy", 50).default("")
        val confidenceLevel = integer("confidenceLevel").default(5)
        val learnings = varchar("learnings", 50).default("")
        val startTime = long("startTime").default(System.currentTimeMillis())
        val endTime = long("endTime").default(System.currentTimeMillis())
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Submissions)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(submission: ExposedSubmission): Int = dbQuery {
        Submissions.insert {
            it[userId] = submission.userId
            it[challengeId] = submission.challengeId
            it[testName] = submission.testName
            it[testResult] = submission.testResult
            it[numberOfSubmissions] = submission.numberOfSubmissions
            it[temporalComplexity] = submission.temporalComplexity
            it[spatialComplexity] = submission.spatialComplexity
            it[problemUnderstanding] = submission.problemUnderstanding
            it[resolutionStrategy] = submission.resolutionStrategy
            it[confidenceLevel] = submission.confidenceLevel
            it[learnings] = submission.learnings
            it[startTime] = submission.startTime
            it[endTime] = submission.endTime
        }[Submissions.id]
    }
        suspend fun read(id: Int): ExposedSubmission? {
            return dbQuery {
                Submissions.select { Submissions.id eq id }
                    .map {
                        ExposedSubmission(
                            it[Submissions.id],
                            it[Submissions.userId],
                            it[Submissions.challengeId],
                            it[Submissions.testName],
                            it[Submissions.testResult],
                            it[Submissions.numberOfSubmissions],
                            it[Submissions.temporalComplexity],
                            it[Submissions.spatialComplexity],
                            it[Submissions.problemUnderstanding],
                            it[Submissions.resolutionStrategy],
                            it[Submissions.confidenceLevel],
                            it[Submissions.learnings],
                            it[Submissions.startTime],
                            it[Submissions.endTime]
                        )
                    }.singleOrNull()
            }
        }

        suspend fun update(id: Int, submission: ExposedSubmission) {
            dbQuery {
                Submissions.update({ Submissions.id eq id }) {
                    it[userId] = submission.userId
                    it[challengeId] = submission.challengeId
                    it[testName] = submission.testName
                    it[testResult] = submission.testResult
                    it[numberOfSubmissions] = submission.numberOfSubmissions
                    it[temporalComplexity] = submission.temporalComplexity
                    it[spatialComplexity] = submission.spatialComplexity
                    it[problemUnderstanding] = submission.problemUnderstanding
                    it[resolutionStrategy] = submission.resolutionStrategy
                    it[confidenceLevel] = submission.confidenceLevel
                    it[learnings] = submission.learnings
                    it[startTime] = submission.startTime
                    it[endTime] = submission.endTime
                }
            }

            suspend fun delete(id: Int) {
                dbQuery {
                    Submissions.deleteWhere { Submissions.id.eq(id) }
                }
            }
    }
}
