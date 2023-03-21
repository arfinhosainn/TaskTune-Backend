package com.arfin.routes.todo

import com.arfin.data.entities.ToDo
import com.arfin.domain.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.updateToDo() {
    authenticate {
        val repository: ToDoRepository by inject()
        put("/todos/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val toDo = call.receive<ToDo>()
                val existingToDo = repository.getTodoById(id)
                if (existingToDo != null && existingToDo.userId == userId) {
                    val updatedToDo = toDo.copy(id = id, userId = userId)
                    val result = repository.updateToDo(id, updatedToDo)
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

