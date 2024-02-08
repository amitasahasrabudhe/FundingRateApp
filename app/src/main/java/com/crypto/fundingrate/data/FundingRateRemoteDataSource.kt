package com.crypto.fundingrate.data

import com.crypto.fundingrate.data.dto.ByBitTickerInfo
import com.crypto.fundingrate.data.dto.ByBitTickerResponse
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundingRateRemoteDataSource @Inject constructor (
    private val service: ByBitService
) {
    fun getFundingRates(): Call<ByBitTickerResponse> {
        return service.getTickerInfo()
    }

}
