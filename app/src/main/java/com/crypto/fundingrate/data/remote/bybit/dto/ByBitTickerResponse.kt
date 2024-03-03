package com.crypto.fundingrate.data.remote.bybit.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * {
 *   "retCode": 0,
 *   "retMsg": "OK",
 *   "result": {
 *     "category": "linear",
 *     "list": [
 *       {
 *         "symbol": "DOGEUSDT",
 *         "lastPrice": "0.08721",
 *         "indexPrice": "0.08704",
 *         "markPrice": "0.08721",
 *         "prevPrice24h": "0.08624",
 *         "price24hPcnt": "0.011247",
 *         "highPrice24h": "0.09113",
 *         "lowPrice24h": "0.08590",
 *         "prevPrice1h": "0.08721",
 *         "openInterest": "197736697",
 *         "openInterestValue": "17244617.35",
 *         "turnover24h": "1096522749.3875",
 *         "volume24h": "12432574269.0000",
 *         "fundingRate": "0.0006492",
 *         "nextFundingTime": "1708444800000",
 *         "predictedDeliveryPrice": "",
 *         "basisRate": "",
 *         "deliveryFeeRate": "",
 *         "deliveryTime": "0",
 *         "ask1Size": "2905063",
 *         "bid1Price": "0.08720",
 *         "ask1Price": "0.08721",
 *         "bid1Size": "78",
 *         "basis": ""
 *       }
 *     ]
 *   },
 *   "retExtInfo": {},
 *   "time": 1708425116777
 * }
 */
@Serializable
data class ByBitTickerResponse (
    @SerialName(value = "result") val result: ByBitTickerResult
)