package com.arfin.data.entities

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class ToDoDraft(
    var title: String,
    var done: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
)
