package com.mongodb.user.repository

import com.mongodb.user.entity.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByEmail(email: String): User?
}