package com.arfin.routes.todo

import com.arfin.domain.repository.ToDoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.deleteToDo() {
    val repository: ToDoRepository by inject()
    authenticate {
        delete("/todos/{id}") {
            val todoId = call.parameters["id"]
            if (todoId != null) {
                val success = repository.removeToDo(todoId)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Todo deleted successfully $todoId")
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}