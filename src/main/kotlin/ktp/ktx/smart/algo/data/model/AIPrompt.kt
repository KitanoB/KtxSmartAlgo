package ktp.ktx.smart.algo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AIPrompt(
    val id: Int? = null,
    val userId: Int,
    val submissionId: Int,
    val promptText: String,
    val createdAt: Long = System.currentTimeMillis()
)
