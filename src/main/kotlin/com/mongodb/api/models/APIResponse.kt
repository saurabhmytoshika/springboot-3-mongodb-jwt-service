package com.mongodb.api.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.mongodb.api.models.Status

@JsonInclude(value = JsonInclude.Include.NON_NULL)
class APIResponse<T>(
    val message: String? = null,
    val status: Status = Status.SUCCESS,
    val data: T? = null,
    val error: Error? = null
)

data class Error(val errorMsg: String?, val errorCode: String)