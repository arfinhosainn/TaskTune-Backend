package com.arfin.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByUserName(userName: String): User? {
        return users.findOne(User::userName eq userName)
    }

    override suspend fun insertUser(user: User): Boolean {
        println("this is a created user $user")
        return users.insertOne(user).wasAcknowledged()
    }
}