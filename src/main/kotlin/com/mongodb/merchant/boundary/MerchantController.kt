package com.mongodb.merchant.boundary

import com.mongodb.api.models.APIResponse
import com.mongodb.merchant.dto.MerchantRequest
import com.mongodb.merchant.dto.MerchantResponse
import com.mongodb.merchant.service.IMerchantService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/merchant")
class MerchantController(
    private val merchantService: IMerchantService
) {

    @PostMapping("/save")
    fun saveMerchant(@RequestBody request: MerchantRequest): APIResponse<String> {
        return APIResponse(
            "Merchant saved successfully",
            data = merchantService.saveMerchant(request).id.toHexString()
        )
    }

    @GetMapping("/{id}")
    fun fetchMerchant(@PathVariable id: String): APIResponse<MerchantResponse> {
        val data = merchantService.fetchMerchant(id)
        return APIResponse(
            "Merchant fetched successfully",
            data = data
        )
    }

    @GetMapping
    fun fetchAllMerchant(): APIResponse<List<MerchantResponse>> {
        return APIResponse(
            "All Merchants fetched successfully",
            data = merchantService.fetchAllMerchant()
        )
    }

    @DeleteMapping("/{id}")
    fun deleteMerchant(@PathVariable id: String): APIResponse<Boolean> {
        return APIResponse(
            "Merchant deleted successfully",
            data = merchantService.deleteMerchant(id)
        )
    }
}