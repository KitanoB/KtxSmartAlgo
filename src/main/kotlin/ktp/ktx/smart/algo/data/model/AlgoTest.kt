package ktp.ktx.smart.algo.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.Table

@Serializable
data class AlgoTest(
    val id: Int? = null,
    val testName: String,
    @Serializable(with = TestResultSerializer::class)
    val testResult: Boolean,
    val numberOfSubmissions: Int,
    val temporalComplexity: String,
    val spatialComplexity: String,
    val problemUnderstanding: String,
    val resolutionStrategy: String,
    val confidenceLevel: Int,
    val learnings: String,
    val startTime: Long?= null,
    val endTime: Long?= null,
    val createdAt: Long? = null,
)
object AlgoTests : Table() {
    val id = integer("id").autoIncrement()
    val testName = varchar("test_name", length = 50)
    val testResult = bool("test_result")
    val numberOfSubmissions = integer("number_of_submissions")
    val temporalComplexity = varchar("temporal_complexity", length = 50)
    val spatialComplexity = varchar("spatial_complexity", length = 50)
    val problemUnderstanding = varchar("problem_understanding", length = 50)
    val resolutionStrategy = varchar("resolution_strategy", length = 50)
    val confidenceLevel = integer("confidence_level")
    val learnings = varchar("learnings", length = 50)
    val startTime = long("start_time").nullable()
    val endTime = long("end_time").nullable()
    val createdAt = long("created_at")
    val timeToResolve = long("time_to_resolve").nullable()
    override val primaryKey = PrimaryKey(id, name = "PK_AlgoTest_Id")
}

object TestResultSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TestResultSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeString(if (value) "Success" else "Failure")
    }

    override fun deserialize(decoder: Decoder): Boolean {
        return decoder.decodeString().equals("Success", ignoreCase = true)
    }
}