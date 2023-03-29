package com.mongodb.merchant.entity

import com.mongodb.merchant.dto.MerchantResponse
import com.mongodb.merchant.dto.MerchantStatus
import com.mongodb.merchant.dto.MerchantType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document
data class Merchant(
    @MongoId
    val id: ObjectId,
    private val name: String,
    private val address: String,
    private val mobileNumber: String,
    private val merchantType: MerchantType,
    private val merchantStatus: MerchantStatus
) {

    fun mapToMerchantResponse() = MerchantResponse(
        id.toHexString(), name, address, mobileNumber, merchantType, merchantStatus
    )
}