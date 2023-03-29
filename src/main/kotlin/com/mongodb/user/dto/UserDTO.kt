package com.mongodb.user.dto

import com.mongodb.converter.EnumWithValue
import com.mongodb.user.entity.User
import org.bson.types.ObjectId
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {

    fun mapToUser() = User(
        ObjectId.get(),
        firstName,
        lastName,
        email,
        BCryptPasswordEncoder().encode(password),
        if (firstName.contains("admin", true)) Role.ADMIN else Role.USER
    )

}

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String?,
    val role: Role
)

enum class Role(override val value: Int): EnumWithValue {
    USER(300),
    ADMIN(301)
}