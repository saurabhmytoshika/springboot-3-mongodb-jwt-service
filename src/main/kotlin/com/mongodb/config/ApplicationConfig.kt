package com.mongodb.config

import com.mongodb.user.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfig(
    val userRepository: UserRepository
) {

    @Bean
    fun userDetailsService(): UserDetailsService {
       return UserDetailsService { username ->
           userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found")
       }
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setPasswordEncoder(passwordEncoder())
        authProvider.setUserDetailsService(userDetailsService())
        return authProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}