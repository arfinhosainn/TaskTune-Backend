package com.arfin.routes.todo

import com.arfin.domain.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchToDos() {
    authenticate {
        val repository: ToDoRepository by inject()
        get("/todos/search") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val query = call.parameters["query"] ?: ""
                val results = repository.searchToDoByTitle(userId, query)
                call.respond(HttpStatusCode.OK, message = results)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}