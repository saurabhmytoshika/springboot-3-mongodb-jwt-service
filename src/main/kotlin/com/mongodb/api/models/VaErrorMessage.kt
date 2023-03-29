package com.mongodb.api.models

data class VaErrorMessage(
    val nodeName: String? = null,
    val taskResourceUri: String? = null,
    val error: String? = null
)