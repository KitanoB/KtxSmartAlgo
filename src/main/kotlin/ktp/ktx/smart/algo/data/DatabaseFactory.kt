package ktp.ktx.smart.algo.data

import ktp.ktx.smart.algo.data.schema.AIAdviceService
import ktp.ktx.smart.algo.data.schema.AIPromptService
import ktp.ktx.smart.algo.data.schema.ChallengeService
import ktp.ktx.smart.algo.data.schema.SubmissionService
import ktp.ktx.smart.algo.data.schema.UserService
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Database
import org.h2.tools.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DatabaseFactory {

    private var isInitialized = false
    private lateinit var databaseInstance: Database

    val logger: Logger = LoggerFactory.getLogger(javaClass)


    fun init() {
        if (!isInitialized) {
            startServers()
            setupDatabase()
            initializeSchema()
            isInitialized = true
        }
    }

    private fun startServers() {
        try {
            // Start TCP Server for database access
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()

            // Start Web Server for H2 Console
            Server.createWebServer("-webPort", "8082", "-webAllowOthers").start()

            logger.info("H2 servers started")
        } catch (e: Exception) {
            // Log the exception or handle it as per your logging setup
            logger.error("Error starting H2 servers: ${e.message}")
        }
    }

    private fun setupDatabase() {
        try {
            // Use a file-based URL for debugging
            databaseInstance = Database.connect(
                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS TEST",
                user = "root",
                driver = "org.h2.Driver",
                password = ""
            )
        } catch (e: Exception) {
            logger.error("Error setting up database: ${e.message}")
            e.printStackTrace()  // More detailed error log
        }
    }

    private fun initializeSchema() {
        try {
            transaction(databaseInstance) {
                    create(UserService.Users)
                    create(AIAdviceService.AIAdvices)
                    create(AIPromptService.AIPromptTable)
                    create(ChallengeService.Challenges)
                    create(SubmissionService.Submissions)

            }
            logger.info("Database schema initialized")
        } catch (e: Exception) {
            // Log the exception or handle it as per your logging setup
            logger.error("Error initializing database schema: ${e.message}")
        }
    }

    fun getDatabase(): Database {
        if (!isInitialized) {
            init()
        }
        return databaseInstance
    }
}