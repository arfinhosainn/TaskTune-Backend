package com.arfin

import com.arfin.data.user.MongoUserDataSource
import com.arfin.data.user.User
import com.arfin.plugins.*
import com.arfin.security.hashing.SHA256HashingService
import com.arfin.security.token.JwtTokenService
import com.arfin.security.token.TokenConfig
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@OptIn(DelicateCoroutinesApi::class)
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val dbName = "tasktune-backend"
    val mongoPassword = System.getenv("MONGO_PASS")
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://arfinhosain:$mongoPassword@cluster0.nrjh3xe.mongodb.net/?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    GlobalScope.launch {
        val user = User(
            userName = "this is nanme",
            password = "23423424234",
            salt = "salt"
        )
        userDataSource.insertUser(user)
    }

    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
    configureSerialization()
    configureMonitoring()


}
