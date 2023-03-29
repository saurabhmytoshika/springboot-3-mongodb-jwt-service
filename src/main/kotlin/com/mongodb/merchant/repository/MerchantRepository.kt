package com.mongodb.merchant.repository

import com.mongodb.merchant.entity.Merchant
import org.springframework.data.mongodb.repository.MongoRepository

interface MerchantRepository: MongoRepository<Merchant, String> {
    fun findByNameAndMobileNumber(name: String, mobileNo: String): Merchant?
}