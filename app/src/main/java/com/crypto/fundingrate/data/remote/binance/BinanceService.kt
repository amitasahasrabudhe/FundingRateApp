package com.crypto.fundingrate.data.remote.binance

import com.crypto.fundingrate.data.remote.binance.dto.BinanceTickerInfo
import retrofit2.http.GET

interface BinanceService {

    // https://fapi.binance.com/v1/premiumIndex
    @GET("/fapi/v1/premiumIndex")
    suspend fun getTickerInfo(): List<BinanceTickerInfo>?
}