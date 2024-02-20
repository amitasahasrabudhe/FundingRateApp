package com.crypto.fundingrate.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(value = "result")
data class ByBitTickerResult(
    @SerialName( value = "category") val category: String?,
    @SerialName(value = "list") val list: List<ByBitTickerInfo>?
)
