package com.arfin.routes.todo

import com.arfin.data.entities.ApiResponse
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
        post("todos/create") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val inputToDo = call.receive<ToDo>()
                val newToDo = inputToDo.copy(userId = userId)
                val response = repository.insertToDo(newToDo)
                if (response.success) {
                    call.respond(
                        HttpStatusCode.Created,
                        message = ApiResponse(
                            success = true,
                            todo = newToDo,
                            message = response.message ?: "ToDo created successfully"
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        message = ApiResponse(
                            success = false,
                            message = response.message ?: "Unknown internal server error"
                        )
                    )
                }
            }
        }

    }

}