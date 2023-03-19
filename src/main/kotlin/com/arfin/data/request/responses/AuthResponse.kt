package com.arfin.data.request.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
