package ktp.ktx.smart.algo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AIAdvice(
    val id: Int? = null,
    val promptId: Int,
    val adviceText: String,
    val receivedAt: Long = System.currentTimeMillis()
)
