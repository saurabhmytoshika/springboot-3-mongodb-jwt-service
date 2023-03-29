package com.mongodb.user.service

import com.mongodb.auth.dto.AuthRequest
import com.mongodb.exception.APIException
import com.mongodb.jwt.JwtService
import com.mongodb.user.dto.UserRequest
import com.mongodb.user.dto.UserResponse
import com.mongodb.user.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val userRepository: UserRepository
)
{

    fun registerUser(request: UserRequest): UserResponse {
        val user = userRepository.findByEmail(request.email)
        if (user != null) throw APIException("Email already exist", HttpStatus.BAD_REQUEST)
        return userRepository.save(request.mapToUser()).mapToUserResponse()
    }

    fun authenticate(request: AuthRequest): String {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username,
                    request.password
                )
            )
        } catch (ex: BadCredentialsException) {
            throw APIException("Bad credentials, please provide correct username and password", HttpStatus.BAD_REQUEST)
        }

        val userDetails = userDetailsService.loadUserByUsername(request.username)
        return jwtService.generateToken(userDetails)
    }
}