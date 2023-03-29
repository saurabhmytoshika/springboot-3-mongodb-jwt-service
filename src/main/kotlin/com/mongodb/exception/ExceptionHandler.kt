package com.mongodb.exception

import com.mongodb.api.models.APIResponse
import com.mongodb.api.models.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(APIException::class)
    fun handleAPIException(ex: APIException): ResponseEntity<APIResponse<String>> {
        return ResponseEntity(
            APIResponse(
                status = Status.FAILURE,
                error = com.mongodb.api.models.Error(
                    errorCode = ex.code.toString(),
                    errorMsg = ex.message)
            ),
            HttpStatus.OK
        )
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(ex: org.springframework.security.access.AccessDeniedException): ResponseEntity<APIResponse<String>> {
        return ResponseEntity(
            APIResponse(
                status = Status.FAILURE,
                error = com.mongodb.api.models.Error(
                    errorCode = HttpStatus.FORBIDDEN.toString(),
                    errorMsg = "You don't have permission to hit this API.")
            ),
            HttpStatus.FORBIDDEN
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<APIResponse<String>> {
        println(ex.printStackTrace())
        return ResponseEntity(
            APIResponse(
                status = Status.FAILURE,
                error = com.mongodb.api.models.Error(
                    errorCode = HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    errorMsg = ex.message)
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}

data class APIException(override var message: String, var code: HttpStatus) : java.lang.RuntimeException(message)
