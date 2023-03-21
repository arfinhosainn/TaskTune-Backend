package com.arfin.data.repository

import com.arfin.data.entities.ToDo
import com.arfin.data.entities.ToDoDraft
import com.arfin.domain.repository.ToDoRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.types.ObjectId
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.regex

class ToDoRepositoryImpl(
    db: CoroutineDatabase
) : ToDoRepository {

    private val dbtodo = db.getCollection<ToDo>()

    override suspend fun getAllToDos(userId: String): List<ToDo> {
        return dbtodo.find(ToDo::userId eq userId).toList()
    }

    override suspend fun searchToDos(userId: String, query: String): List<ToDo> {
        return if (query.length >= 3) {
            val filter = Filters.and(
                Filters.eq("userId", userId),
                Filters.regex("title", query, "i")
            )
            dbtodo.find(filter).toList()
        } else {
            emptyList()
        }
    }


    override suspend fun addToDo(toDo: ToDo): Boolean {
        return dbtodo.insertOne(toDo).wasAcknowledged()
    }

    override suspend fun removeToDo(id: String): Boolean {
        val deleteResult = dbtodo.deleteOne(ToDo::id eq id)
        return deleteResult.deletedCount > 0
    }

    override suspend fun updateToDo(id: String, toDo: ToDo): Boolean {
        val filter = ToDo::id eq id
        val update = combine(
            Updates.set("title", toDo.title),
            Updates.set("done", toDo.done),
            Updates.set("startTime", toDo.startTime),
            Updates.set("endTime", toDo.endTime),
            Updates.set("date", toDo.date)
        )
        val result = dbtodo.updateOne(filter, update)
        return result.wasAcknowledged() && result.matchedCount > 0
    }


    override suspend fun getTodoById(id: String): ToDo? {
        return dbtodo.findOne(ToDo::id eq id)
    }
}