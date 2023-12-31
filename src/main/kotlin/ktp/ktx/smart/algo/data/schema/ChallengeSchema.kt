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
data class ExposedChallenge(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
)

data class ChallengeService(private val database: Database) {

    object Challenges : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 255)
        val description = text("description")
        val tags = text("tags")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Challenges)
        }
    }

    suspend fun create(challenge: ExposedChallenge): Int = transaction(database) {
        Challenges.insert {
            it[name] = challenge.name
            it[description] = challenge.description
            it[tags] = challenge.tags.joinToString(",")
        }[Challenges.id]
    }

    suspend fun read(id: Int): ExposedChallenge? = transaction(database) {
        Challenges.select { Challenges.id eq id }
            .map {
                ExposedChallenge(
                    it[Challenges.id],
                    it[Challenges.name],
                    it[Challenges.description],
                    it[Challenges.tags].split(",")
                )
            }.singleOrNull()
    }

    suspend fun readAll(): List<ExposedChallenge> = transaction(database) {
        Challenges.selectAll()
            .map {
                ExposedChallenge(
                    it[Challenges.id],
                    it[Challenges.name],
                    it[Challenges.description],
                    it[Challenges.tags].split(",")
                )
            }
    }

    suspend fun update(challenge: ExposedChallenge): Int = transaction(database) {
        Challenges.update({ Challenges.id eq challenge.id }) {
            it[name] = challenge.name
            it[description] = challenge.description
            it[tags] = challenge.tags.joinToString(",")
        }
    }

    suspend fun delete(id: Int): Int = transaction(database) {
        Challenges.deleteWhere { Challenges.id eq id }
    }


    suspend fun search(query: String): List<ExposedChallenge> = transaction(database) {
        Challenges.select { Challenges.name like "%$query%" }
            .map {
                ExposedChallenge(
                    it[Challenges.id],
                    it[Challenges.name],
                    it[Challenges.description],
                    it[Challenges.tags].split(",")
                )
            }
    }

    suspend fun searchByTag(tag: String): List<ExposedChallenge> = transaction(database) {
        Challenges.select { Challenges.tags like "%$tag%" }
            .map {
                ExposedChallenge(
                    it[Challenges.id],
                    it[Challenges.name],
                    it[Challenges.description],
                    it[Challenges.tags].split(",")
                )
            }
    }

    suspend fun searchByTags(tags: List<String>): List<ExposedChallenge> = transaction(database) {
        Challenges.select { Challenges.tags like "%${tags.joinToString(",")}%" }
            .map {
                ExposedChallenge(
                    it[Challenges.id],
                    it[Challenges.name],
                    it[Challenges.description],
                    it[Challenges.tags].split(",")
                )
            }
    }
}
