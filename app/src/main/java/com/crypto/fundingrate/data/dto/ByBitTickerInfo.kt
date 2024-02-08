package com.crypto.fundingrate.data.dto

import com.crypto.fundingrate.domain.FundingRate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ByBitTickerInfo (
    @SerialName(value = "symbol") val symbol: String?,
    @SerialName(value = "fundingRate") val fundingRate: String?
)

fun ByBitTickerInfo.toFundingRate(): FundingRate {
    return FundingRate(coin = symbol ?: "", current = fundingRate ?: "", predicted = "")
}