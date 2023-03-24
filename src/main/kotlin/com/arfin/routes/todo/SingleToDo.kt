package com.arfin.routes.todo

import com.arfin.data.entities.ApiResponse
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
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            if (userId != null) {
                val response = repository.getTodoById(id)
                if (response.success) {
                    call.respond(
                        HttpStatusCode.OK,
                        message = response
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        message = ApiResponse(
                            success = false,
                            message = "Internal Server Error"
                        )
                    )
                }
            }
        }
    }
}
