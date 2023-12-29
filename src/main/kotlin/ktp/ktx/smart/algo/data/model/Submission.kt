package ktp.ktx.smart.algo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Submission(
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