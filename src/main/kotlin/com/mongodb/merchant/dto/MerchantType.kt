package com.mongodb.merchant.dto

import com.mongodb.converter.EnumWithValue

enum class MerchantType(override val value: Int): EnumWithValue {

    OWNER(100),
    DIRECTOR(101),
    REPRESENTATIVE(102);

}

enum class MerchantStatus(override val value: Int): EnumWithValue {
    PENDING(200),
    ACTIVE(201),
    INACTIVE(202);
}


