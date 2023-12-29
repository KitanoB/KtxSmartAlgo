package ktp.ktx.smart.algo.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import ktp.ktx.smart.algo.data.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.ExposedUser
import ktp.ktx.smart.algo.data.schema.UserService
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureRouting() {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    val database = DatabaseFactory.getDatabase()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/submit") {

            // receive json and convert to Submission object
           /* val algo = call.receive<Submi>()

            val jsonStr = Json.encodeToString(AlgoTest.serializer(), algo)
            logger.info("============================================")
            logger.info("Received POST /test with payload: $jsonStr")*/
            call.respond(HttpStatusCode.OK, "OK RECEIVED")
        }

        post("/user") {
            val user = call.receive<ExposedUser>()
            val userService = UserService(database)
            userService.create(user)

            val jsonStr = Json.encodeToString(ExposedUser.serializer(), user)
            logger.info("============================================")
            logger.info("Received POST /user with payload: $jsonStr")
            call.respond(HttpStatusCode.OK, "OK RECEIVED")
        }
    }
}
