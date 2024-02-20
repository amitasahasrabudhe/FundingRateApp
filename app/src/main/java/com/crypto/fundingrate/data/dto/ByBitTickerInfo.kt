package com.crypto.fundingrate.data.dto

import com.crypto.fundingrate.domain.FundingRate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DecimalFormat

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
@SerialName("byBitTickerInfo")
data class ByBitTickerInfo (
    @SerialName(value = "symbol") val symbol: String?,
    @SerialName(value = "fundingRate") val predictedFundingRate: String?,
    // Notional volume
    @SerialName(value = "turnover24h") val volume: String?
)

fun ByBitTickerInfo.toFundingRate(): FundingRate {
    return FundingRate(
        coin = symbol?.dropLast(4) ?: "",
        predicted = predictedFundingRate?: "",
        // it is important to convert the volume string to double as the api sends it in double format
        // e.g. 1096522749.3875
        volume = volume?.toDouble()?.toLong() ?: 0)
}

fun Long.formatNumberWithThousandsSeparator(): String {
    val formattedString = DecimalFormat("#,###").format(this)
    return formattedString.replace(",", ".")
}

