package com.crypto.fundingrate.presentation

import com.crypto.fundingrate.domain.FundingRate

data class FundingRatesState (
    val fundingRates: List<FundingRate> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)