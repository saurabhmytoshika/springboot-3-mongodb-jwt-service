package com.mongodb.merchant.dto

data class MerchantResponse(
    val id: String,
    val name: String,
    val address: String,
    val mobileNumber: String,
    val merchantType: MerchantType,
    val merchantStatus: MerchantStatus
)