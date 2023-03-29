package com.mongodb.merchant.service

import com.mongodb.exception.APIException
import com.mongodb.merchant.dto.MerchantRequest
import com.mongodb.merchant.dto.MerchantResponse
import com.mongodb.merchant.dto.MerchantStatus
import com.mongodb.merchant.dto.MerchantType
import com.mongodb.merchant.entity.Merchant
import com.mongodb.merchant.repository.MerchantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

@Service
class MerchantService(
    private val merchantRepository: MerchantRepository
): IMerchantService {

    override fun saveMerchant(request: MerchantRequest): Merchant {
        val existingMerchant = merchantRepository.findByNameAndMobileNumber(request.name, request.mobileNumber)
        if (existingMerchant != null)
            throw APIException(
                "Merchant with the provided name and mobile number already exists",
                HttpStatus.BAD_REQUEST
            )
        val merchantStatus = if (request.merchantType == MerchantType.OWNER) MerchantStatus.ACTIVE
        else MerchantStatus.PENDING
        return merchantRepository.save(request.mapToMerchant(merchantStatus))
    }

    override fun fetchMerchant(id: String): MerchantResponse {
        val merchant = merchantRepository.findByIdOrNull(id)
            ?: throw APIException("Merchant Not Found", HttpStatus.NOT_FOUND)
        return merchant.mapToMerchantResponse()
    }

    override fun fetchAllMerchant(): List<MerchantResponse> {
        val response = mutableListOf<MerchantResponse>()
        val merchants = merchantRepository.findAll()
        if (!CollectionUtils.isEmpty(merchants)) {
            merchants.forEach {
                response.add(it.mapToMerchantResponse())
            }
        }
        return response
    }

    override fun deleteMerchant(id: String): Boolean {
        val merchant = merchantRepository.findByIdOrNull(id)
            ?: throw APIException("Merchant doesn't exists to delete", HttpStatus.BAD_REQUEST)
        merchantRepository.delete(merchant)
        return true
    }
}