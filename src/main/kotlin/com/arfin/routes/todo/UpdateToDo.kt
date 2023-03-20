package com.arfin.routes.todo

import com.arfin.data.entities.ToDoDraft
import com.arfin.data.repository.ToDoRepositoryImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateToDo() {

//    val repository = ToDoRepositoryImpl()
//    authenticate {
//        put("/todos/{id}") {
//            val todoId = call.parameters["id"]?.toIntOrNull()
//            val todoDraft = call.receive<ToDoDraft>()
//
//            if (todoId == null) {
//                call.respond(
//                    HttpStatusCode.BadRequest,
//                    "id parameter has to be number"
//                )
//                return@put
//            }
//            val updated = repository.updateToDo(id = todoId, draft = todoDraft)
//            if (updated) {
//                call.respond(HttpStatusCode.OK)
//            } else {
//                call.respond(HttpStatusCode.NotFound, "Found no todo with the id  $todoId")
//            }
//        }
//    }
}