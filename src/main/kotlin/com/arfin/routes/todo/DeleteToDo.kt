package com.arfin.routes.todo

import com.arfin.domain.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.deleteToDo() {
    val repository: ToDoRepository by inject()
    authenticate {
        delete("/todos/{id}/delete") {
            val todoId = call.parameters["id"]
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val response = repository.deleteToDo(todoId!!)
                if (response.success) {
                    call.respond(
                        HttpStatusCode.OK,
                        message = response
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        message = response
                    )
                }
            }
        }
    }
}