package com.mongodb.merchant.service

import com.mongodb.merchant.entity.Merchant
import com.mongodb.merchant.dto.MerchantRequest
import com.mongodb.merchant.dto.MerchantResponse

interface IMerchantService {
    fun saveMerchant(request: MerchantRequest): Merchant
    fun fetchMerchant(id: String): MerchantResponse
    fun fetchAllMerchant(): List<MerchantResponse>
    fun deleteMerchant(id: String): Boolean
}