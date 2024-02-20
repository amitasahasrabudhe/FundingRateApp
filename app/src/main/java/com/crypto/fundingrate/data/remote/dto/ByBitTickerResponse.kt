package com.crypto.fundingrate.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ByBitTickerResponse (
    @SerialName(value = "result") val result: ByBitTickerResult
)