package com.crypto.fundingrate.domain

data class FundingRate(
    val coin: String,
    val predicted: String,
    val volume: Long)
