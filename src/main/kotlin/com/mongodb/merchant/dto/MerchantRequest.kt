package com.mongodb.merchant.dto

import com.mongodb.merchant.entity.Merchant
import org.bson.types.ObjectId

data class MerchantRequest(
    val name: String,
    val address: String,
    val mobileNumber: String,
    val merchantType: MerchantType
) {
    fun mapToMerchant(merchantStatus: MerchantStatus) = Merchant(
        ObjectId.get(), name, address, mobileNumber, merchantType, merchantStatus
    )
}