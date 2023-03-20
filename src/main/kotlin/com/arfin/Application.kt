package com.arfin

import com.arfin.data.user.MongoUserDataSource
import com.arfin.di.koinModule
import com.arfin.plugins.*
import com.arfin.security.hashing.SHA256HashingService
import com.arfin.security.token.JwtTokenService
import com.arfin.security.token.TokenConfig
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

fun main(args: Array<String>): Unit {

    startKoin {
        modules(koinModule)
    }
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(DelicateCoroutinesApi::class)
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val db: CoroutineDatabase by inject()

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
    configureSerialization()
    configureMonitoring()

}
