package com.mongodb.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.api.models.APIResponse
import com.mongodb.api.models.Status
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
): OncePerRequestFilter() {

    val log: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val excludedURLs = mutableListOf(
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png",
            "/v3/api-docs/swagger-config",
            "/v3/api-docs")
        if (excludedURLs.stream().noneMatch { request.requestURL.toString().contains(it.toString())
                    && !request.method.equals("OPTIONS")}) {
            println("RequestURL : " + request.requestURL.toString())
            println("Method Type : " + request.method.toString())
            val authHeader: String? = request.getHeader("Authorization")
            val userEmail: String?
            if (authHeader == null) {
                log.error("Token can't be left blank")
                prepareErrorRes(response, "Token can't be left blank", HttpStatus.UNAUTHORIZED)
                return
            }
            if (!authHeader.startsWith("Bearer ")) {
                log.error("Token must be starts with Bearer")
                prepareErrorRes(response, "Token must be starts with Bearer", HttpStatus.FORBIDDEN)
                return
            }

            val jwt: String = authHeader.substring(7)
            try {
                userEmail = jwtService.extractUserName(jwt)
            } catch (ex: MalformedJwtException) {
                logger.info("The given token is malformed. Please try with a valid token")
                prepareErrorRes(
                    response,
                    "The given token is malformed. Please try with a valid token",
                    HttpStatus.FORBIDDEN
                )
                return
            } catch (ex: SignatureException) {
                logger.info("Token is not valid")
                prepareErrorRes(response, "Token is not valid", HttpStatus.FORBIDDEN)
                return
            } catch (ex: ExpiredJwtException) {
                logger.info("Your token is expired. Please try with new token")
                prepareErrorRes(response, "Your token is expired. Please try with new token", HttpStatus.FORBIDDEN)
                return
            } catch (ex: IllegalArgumentException) {
                logger.info("Invalid request token found")
                prepareErrorRes(response, "Invalid request token found", HttpStatus.FORBIDDEN)
                return
            } catch (ex: UnsupportedJwtException) {
                logger.info("Unsupported Jwt Exception")
                prepareErrorRes(response, "Unsupported Jwt Exception", HttpStatus.FORBIDDEN)
                return
            }

            if (userEmail == null) {
                log.error("Invalid user")
                prepareErrorRes(response, "Invalid user", HttpStatus.FORBIDDEN)
                return
            }

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = this.userDetailsService.loadUserByUsername(userEmail)
                if (jwtService.isValidToken(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    log.error("Token is expired or invalid.")
                    prepareErrorRes(response, "Token is expired or invalid.", HttpStatus.FORBIDDEN)
                }

            }
        }
        filterChain.doFilter(request, response)
    }

    fun prepareErrorRes(response: HttpServletResponse, errorMsg: String, statusCode: HttpStatus) {
        response.status = statusCode.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val body = APIResponse(
            status = Status.FAILURE,
            data = null,
            error = com.mongodb.api.models.Error(errorMsg, statusCode.toString())
        )
        response.outputStream.println(ObjectMapper().writeValueAsString(body))
    }
}