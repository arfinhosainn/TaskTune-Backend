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

fun Route.getAllToDos() {
    val repository: ToDoRepository by inject()

    authenticate {
        get("/todos") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId != null) {
                val response = repository.getAllToDos(userId)
                if (response.success) {
                    call.respond(
                        HttpStatusCode.OK,
                        message = ApiResponse(
                            success = true,
                            todos = response.todos,
                            message = response.message ?: "Ok"
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
            } else {
                call.respond(
                    HttpStatusCode.BadGateway,
                    message = ApiResponse(
                        success = false,
                        message = "bad gateway"
                    )
                )
            }
        }
    }
}
