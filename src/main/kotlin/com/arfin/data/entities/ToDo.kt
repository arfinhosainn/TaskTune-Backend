package com.arfin.data.entities

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Serializable
data class ToDo(
    var title: String,
    @BsonId val id: String = UUID.randomUUID().toString(),
    val userId: String? = null,
    var done: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    var startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    var endTime: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    var date: LocalDate
)



