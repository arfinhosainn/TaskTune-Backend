package com.arfin.data.user

interface UserDataSource {

    suspend fun getUserByUserName(userName: String): User?
    suspend fun insertUser(user: User): Boolean
}