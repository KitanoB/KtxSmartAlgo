package ktp.ktx.smart.algo.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import ktp.ktx.smart.algo.data.database.DatabaseFactory
import ktp.ktx.smart.algo.data.schema.ExposedUser
import ktp.ktx.smart.algo.data.schema.UserService
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
