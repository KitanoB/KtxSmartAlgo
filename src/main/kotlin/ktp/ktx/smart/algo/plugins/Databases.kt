package ktp.ktx.smart.algo.plugins

import io.ktor.server.application.Application

import ktp.ktx.smart.algo.data.database.DatabaseFactory

fun Application.configureDatabases() {
    DatabaseFactory.init()
}
