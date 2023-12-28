package ktp.ktx.smart.algo.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import ktp.ktx.smart.algo.data.model.AlgoTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureRouting() {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/test") {
            val algo: AlgoTest = call.receive<AlgoTest>()
            val jsonStr = Json.encodeToString(AlgoTest.serializer(), algo)
            logger.info("============================================")
            logger.info("Received POST /test with payload: $jsonStr")
            call.respond(HttpStatusCode.OK, algo)
        }
    }
}
