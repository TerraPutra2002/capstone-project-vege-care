package com.example.vegecare.data.repository

import com.example.vegecare.data.database.User
import com.example.vegecare.data.database.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun checkEmail(email: String): Boolean {
        return userDao.checkEmail(email) != null
    }
}
