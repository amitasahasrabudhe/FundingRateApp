package com.crypto.fundingrate.data.remote

import com.crypto.fundingrate.data.remote.binance.BinanceService
import com.crypto.fundingrate.data.remote.binance.dto.toFundingRate
import com.crypto.fundingrate.data.remote.bybit.ByBitService
import com.crypto.fundingrate.data.remote.bybit.dto.toFundingRate
import com.crypto.fundingrate.domain.model.CryptoExchange
import com.crypto.fundingrate.domain.model.FundingRate
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundingRateRemoteDataSource @Inject constructor (
    private val byBitService: ByBitService,
    private val binanceService: BinanceService
) {
    suspend fun getFundingRates(exchange: CryptoExchange): List<FundingRate> {
        when(exchange) {
            CryptoExchange.BYBIT -> {
                val response =  try {
                    byBitService.getTickerInfo()
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
                if (!response?.result?.list.isNullOrEmpty()) {
                    val list = response!!.result.list
                    if (list != null) {
                        return list.map { it.toFundingRate() }.sortedByDescending { a -> a.volume }
                    }
                } else {
                    return emptyList()
                }
            }
            CryptoExchange.BINANCE -> {
                val list = try {
                    binanceService.getTickerInfo()
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
                if (!list.isNullOrEmpty()) {
                    return list!!.map { it.toFundingRate() }.sortedByDescending { a -> a.volume }
                }
            }
            CryptoExchange.OKX -> {
                return emptyList()
            }

        }
        return emptyList()
    }



}

/*
fun main(args: Array<String>) {
    val str = """{
  "retCode": 0,
  "retMsg": "OK",
  "result": {
    "category": "linear",
    "list": [
      {
        "symbol": "10000LADYSUSDT",
        "lastPrice": "0.0007491",
        "indexPrice": "0.0007474",
        "markPrice": "0.0007476",
        "prevPrice24h": "0.0007510",
        "price24hPcnt": "-0.002529",
        "highPrice24h": "0.0007714",
        "lowPrice24h": "0.0007202",
        "prevPrice1h": "0.0007440",
        "openInterest": "484271800",
        "openInterestValue": "362041.60",
        "turnover24h": "621172.8559",
        "volume24h": "840283100.0000",
        "fundingRate": "0.0001",
        "nextFundingTime": "1708387200000",
        "predictedDeliveryPrice": "",
        "basisRate": "",
        "deliveryFeeRate": "",
        "deliveryTime": "0",
        "ask1Size": "7928800",
        "bid1Price": "0.0007468",
        "ask1Price": "0.0007489",
        "bid1Size": "26759900",
        "basis": ""
      }
    ]
  },
  "retExtInfo": {},
  "time": 1708363374183
}""".trimMargin()

   // val contentType = "application/json".toMediaType()
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val decoded: ByBitTickerResponse = json.decodeFromString<ByBitTickerResponse>(str)
    decoded.result.list?.get(0)?.symbol?.let {
        System.out.println("Symbol found: $it")
    }

}
 */