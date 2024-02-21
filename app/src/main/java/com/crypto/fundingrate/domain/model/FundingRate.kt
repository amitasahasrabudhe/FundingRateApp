package com.crypto.fundingrate.domain.model

data class FundingRate(
    val symbol: String,
    val predictedFundingRate: String,
    val volume: Long)
