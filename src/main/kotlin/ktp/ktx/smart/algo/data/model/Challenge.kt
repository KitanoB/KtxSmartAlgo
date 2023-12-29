package ktp.ktx.smart.algo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
)
