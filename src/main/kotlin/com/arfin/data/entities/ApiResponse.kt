package com.arfin.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val todos: List<ToDo>? = emptyList(),
    val todo: ToDo? = null
)