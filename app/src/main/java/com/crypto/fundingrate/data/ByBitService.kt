package com.crypto.fundingrate.data

import com.crypto.fundingrate.data.dto.ByBitTickerResponse
import retrofit2.Call
import retrofit2.http.GET

interface ByBitService {

    // https://api.bybit.com/derivatives/v3/public/tickers?category=linear
    @GET("derivatives/v3/public/tickers/")
    fun getTickerInfo(): Call<ByBitTickerResponse>
}