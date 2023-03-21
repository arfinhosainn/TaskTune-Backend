package com.arfin.routes.todo

import com.arfin.domain.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getToDoById() {
    authenticate {
        val repository: ToDoRepository by inject()
        get("/todos/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val toDo = repository.getTodoById(id)
                if (toDo != null && toDo.userId == userId) {
                    call.respond(toDo)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
