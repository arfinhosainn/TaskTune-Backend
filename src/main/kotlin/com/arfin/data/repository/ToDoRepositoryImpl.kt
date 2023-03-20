package com.arfin.data.repository

import com.arfin.data.entities.ToDo
import com.arfin.data.entities.ToDoDraft
import com.arfin.domain.repository.ToDoRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ToDoRepositoryImpl(
    db: CoroutineDatabase
) : ToDoRepository {

    private val dbtodo = db.getCollection<ToDo>()

    override suspend fun getAllToDos(userId: String): List<ToDo> {
        return dbtodo.find(ToDo::userId eq userId).toList()
    }

    override suspend fun getToDoById(id: String): ToDo? {
        return dbtodo.findOne(ToDo::id eq id)
    }

    override suspend fun addToDo(toDo: ToDo): Boolean {
        return dbtodo.insertOne(toDo).wasAcknowledged()
    }

    override suspend fun removeToDo(id: String): Boolean {
        val deleteResult = dbtodo.deleteOne(ToDo::id eq id)
        return deleteResult.deletedCount > 0
    }

    override suspend fun updateToDo(id: String, toDo: ToDo): Boolean {
        val result = dbtodo.updateOne(ToDo::id eq id, toDo)
        return result.wasAcknowledged()
    }
}