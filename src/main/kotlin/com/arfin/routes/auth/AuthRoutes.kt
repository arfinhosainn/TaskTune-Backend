package com.arfin.routes.auth

import com.arfin.data.request.AuthRequest
import com.arfin.data.request.responses.AuthResponse
import com.arfin.data.user.User
import com.arfin.data.user.UserDataSource
import com.arfin.security.hashing.HashingService
import com.arfin.security.hashing.SaltedHash
import com.arfin.security.token.TokenClaim
import com.arfin.security.token.TokenConfig
import com.arfin.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val areFieldBlank = request.userName.isBlank() || request.password.isBlank()
        val isPassTooShort = request.password.length < 8
        if (areFieldBlank || isPassTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            userName = request.userName,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val user = userDataSource.getUserByUserName(request.userName)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrects username or password")
            return@post
        }
        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrects username or password")
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }

}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "your userId is $userId")
        }
    }
}