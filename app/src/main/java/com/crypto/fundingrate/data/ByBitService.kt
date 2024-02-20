package com.crypto.fundingrate.data

import com.crypto.fundingrate.data.dto.ByBitTickerResponse
import retrofit2.Call
import retrofit2.http.GET

interface ByBitService {

    // old api v3
    // https://api.bybit.com/derivatives/v3/public/tickers?category=linear

    // new api v5
    // https://api-testnet.bybit.com/v5/market/tickers
    @GET("/v5/market/tickers?category=linear")
    suspend fun getTickerInfo(): ByBitTickerResponse
}