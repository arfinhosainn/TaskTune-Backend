package com.arfin.di

import com.arfin.data.repository.ToDoRepositoryImpl
import com.arfin.domain.repository.ToDoRepository
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        val mongoPassword = System.getenv("MONGO_PASS")

        KMongo.createClient(
            connectionString = "mongodb+srv://arfinhosain:$mongoPassword@cluster0.nrjh3xe.mongodb.net/?retryWrites=true&w=majority"
        ).coroutine.getDatabase("tasktune-backend")
    }
    single<ToDoRepository> {
        ToDoRepositoryImpl(get())
    }
}
