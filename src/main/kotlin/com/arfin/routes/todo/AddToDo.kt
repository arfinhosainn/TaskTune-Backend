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
fun Route.addToDo() {
    authenticate {
        val repository: ToDoRepository by inject()
        post("/todos/new") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val inputToDo = call.receive<ToDo>()
                val newToDo = inputToDo.copy(userId = userId)
                val success = repository.addToDo(newToDo)
                if (success) {
                    call.respond(HttpStatusCode.Created, newToDo)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}