package com.crypto.fundingrate.domain.model

data class FundingRate(
    val coin: String,
    val predicted: String,
    val volume: Long)
