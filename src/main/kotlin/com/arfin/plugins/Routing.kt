package com.arfin.plugins

import com.arfin.data.user.UserDataSource
import com.arfin.routes.auth.authenticate
import com.arfin.routes.auth.getSecretInfo
import com.arfin.routes.auth.signIn
import com.arfin.routes.auth.signUp
import com.arfin.routes.todo.*
import com.arfin.security.hashing.HashingService
import com.arfin.security.token.TokenConfig
import com.arfin.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
        getAllToDos()
        searchToDos()
        addToDo()
        deleteToDo()
        updateToDo()

    }
}
