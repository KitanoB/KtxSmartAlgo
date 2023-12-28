package ktp.ktx.smart.algo

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ktp.ktx.smart.algo.plugins.configureAdministration
import ktp.ktx.smart.algo.plugins.configureDatabases
import ktp.ktx.smart.algo.plugins.configureHTTP
import ktp.ktx.smart.algo.plugins.configureMonitoring
import ktp.ktx.smart.algo.plugins.configureRouting
import ktp.ktx.smart.algo.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureAdministration()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureRouting()
}
