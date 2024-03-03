package com.crypto.fundingrate.data.remote.bybit

import com.crypto.fundingrate.data.remote.bybit.dto.ByBitTickerResponse
import retrofit2.http.GET

interface ByBitService {

    // new api v5
    // https://api.bybit.com/v5/market/tickers
    @GET("/v5/market/tickers?category=linear")
    suspend fun getTickerInfo(): ByBitTickerResponse
}