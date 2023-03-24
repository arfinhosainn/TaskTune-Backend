package com.arfin.domain.repository

import com.arfin.data.entities.ApiResponse
import com.arfin.data.entities.ToDo

interface ToDoRepository {
    suspend fun getAllToDos(userId: String): ApiResponse

    suspend fun searchToDoByTitle(userId: String, query: String): ApiResponse

    suspend fun insertToDo(toDo: ToDo): ApiResponse

    suspend fun deleteToDo(id: String): ApiResponse

    suspend fun updateToDo(id: String, toDo: ToDo): ApiResponse

    suspend fun getTodoById(id: String): ApiResponse

}