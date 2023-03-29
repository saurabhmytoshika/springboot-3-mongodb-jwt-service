package com.mongodb.user.entity

import com.mongodb.user.dto.Role
import com.mongodb.user.dto.UserResponse
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "user")
data class User(
    @MongoId
    private val id: ObjectId,
    private val firstName: String,
    private val lastName: String,
    private val email: String,
    private val password: String,
    private val role: Role
): UserDetails {

    fun mapToUserResponse() = UserResponse(id.toHexString(), firstName, lastName, email, null, role)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
       return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}