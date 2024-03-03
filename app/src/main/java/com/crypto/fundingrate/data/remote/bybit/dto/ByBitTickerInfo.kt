package com.crypto.fundingrate.data.remote.bybit.dto

import com.crypto.fundingrate.domain.model.FundingRate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DecimalFormat

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
        symbol = symbol?.dropLast(4) ?: "",
        predictedFundingRate = predictedFundingRate?: "",
        // it is important to convert the volume string to double as the api sends it in double format
        // e.g. 1096522749.3875
        volume = volume?.toDouble()?.toLong() ?: 0)
}

fun Long.formatNumberWithThousandsSeparator(): String {
    val formattedString = DecimalFormat("#,###").format(this)
    return formattedString.replace(",", ".")
}

