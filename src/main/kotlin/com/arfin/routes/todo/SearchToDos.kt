package com.arfin.routes.todo

import com.arfin.data.repository.ToDoRepositoryImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.searchToDos() {

//    val repository = ToDoRepositoryImpl()
//
//    authenticate {
//        get("/todos/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id == null) {
//                call.respond(HttpStatusCode.BadRequest, "id param has to be number")
//                return@get
//            }
//            val todo = repository.getToDoById(id)
//
//            if (todo == null) {
//                call.respond(HttpStatusCode.NotFound, "Todo not found")
//            } else {
//                call.respond(todo)
//            }
//        }
//
//    }

}