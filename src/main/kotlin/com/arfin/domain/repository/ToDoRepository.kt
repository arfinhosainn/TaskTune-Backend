package com.arfin.domain.repository

import com.arfin.data.entities.ToDo

interface ToDoRepository {
    suspend fun getAllToDos(userId: String): List<ToDo>

    suspend fun getToDoById(id: String): ToDo?

    suspend fun addToDo(toDo: ToDo): Boolean

    suspend fun removeToDo(id: String): Boolean

    suspend fun updateToDo(id: String, toDo: ToDo): Boolean

}