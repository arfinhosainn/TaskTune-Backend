package com.arfin.routes.todo

import com.arfin.domain.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getAllToDos() {
    val repository: ToDoRepository by inject()

    authenticate {
        get("/todos") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val todos = repository.getAllToDos(userId)
                call.respond(HttpStatusCode.OK, todos)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
