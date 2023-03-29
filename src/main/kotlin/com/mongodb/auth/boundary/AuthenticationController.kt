package com.mongodb.auth.boundary

import com.mongodb.api.models.APIResponse
import com.mongodb.auth.dto.AuthRequest
import com.mongodb.auth.dto.AuthResponse
import com.mongodb.user.dto.UserRequest
import com.mongodb.user.dto.UserResponse
import com.mongodb.user.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(private val userService: UserService) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthRequest): APIResponse<AuthResponse> {
        val token = userService.authenticate(request)
        return APIResponse(
            message = "Authentication token generated successfully",
            data = AuthResponse(token)
        )
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    fun userRegister(@RequestBody request: UserRequest): APIResponse<UserResponse> {
        val user = userService.registerUser(request)
        return APIResponse(
            message = "User registered successfully",
            data = user
        )
    }
}