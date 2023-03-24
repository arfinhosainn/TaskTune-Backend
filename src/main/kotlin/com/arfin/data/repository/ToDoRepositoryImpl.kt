package com.arfin.data.repository

import com.arfin.data.entities.ApiResponse
import com.arfin.data.entities.ToDo
import com.arfin.domain.repository.ToDoRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ToDoRepositoryImpl(
    db: CoroutineDatabase
) : ToDoRepository {

    private val todoDB = db.getCollection<ToDo>()

    override suspend fun getAllToDos(userId: String): ApiResponse {
        val allToDos = todoDB.find(ToDo::userId eq userId).toList()
        return ApiResponse(
            success = true,
            todos = allToDos,
            message = "Ok"
        )
    }

    override suspend fun searchToDoByTitle(userId: String, query: String): ApiResponse {
        val data = if (query.length >= 3) {
            val filter = Filters.and(
                Filters.eq("userId", userId),
                Filters.regex("title", query, "i")
            )
            todoDB.find(filter).toList()
        } else {
            emptyList()
        }
        return ApiResponse(
            todos = data,
            message = "ok",
            success = true
        )
    }


    override suspend fun insertToDo(toDo: ToDo): ApiResponse {
        val response = todoDB.insertOne(toDo).wasAcknowledged()
        return ApiResponse(
            success = response,
            message = "ToDo Created Successfully"
        )
    }

    override suspend fun deleteToDo(id: String): ApiResponse {
        todoDB.deleteOne(ToDo::id eq id)
        return ApiResponse(
            message = "ToDo deleted",
            success = true
        )
    }

    override suspend fun updateToDo(id: String, toDo: ToDo): ApiResponse {
        val filter = ToDo::id eq id
        val update = combine(
            Updates.set("title", toDo.title),
            Updates.set("done", toDo.done),
            Updates.set("startTime", toDo.startTime),
            Updates.set("endTime", toDo.endTime),
            Updates.set("date", toDo.date)
        )
        val result = todoDB.updateOne(filter, update)
        return if (result.wasAcknowledged() && result.matchedCount > 0) {
            val updatedToDo = todoDB.findOne(filter)
            ApiResponse(success = true, todo = updatedToDo, message = "ToDo updated successfully")
        } else {
            ApiResponse(success = false, message = "Update failed")
        }
    }

    override suspend fun getTodoById(id: String): ApiResponse {
        val todo = todoDB.findOne(ToDo::id eq id)
        return ApiResponse(
            success = true,
            message = "Ok",
            todo = todo
        )
    }
}