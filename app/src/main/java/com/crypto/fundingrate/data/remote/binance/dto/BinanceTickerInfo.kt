package com.crypto.fundingrate.data.remote.binance.dto

import com.crypto.fundingrate.domain.model.FundingRate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * estimatedSettlePrice: "0.91910000"
 * indexPrice: "0.91910000"
 * interestRate: "0.00010000"
 * lastFundingRate: "0.00010000"
 * markPrice: "0.91913707"
 * nextFundingTime: 1708473600000
 * symbol: "BTCSTUSDT"
 * time: 1708450367000
 */
@Serializable
@SerialName(value = "binanceTickerInfo")
data class BinanceTickerInfo(
    @SerialName(value = "symbol") val symbol: String?,
    @SerialName(value = "lastFundingRate") val fundingRate: String?
)

fun BinanceTickerInfo.toFundingRate(): FundingRate {
    return FundingRate(
        symbol = symbol ?: "",
        predictedFundingRate = fundingRate ?: "",
        volume = 0
    )
}