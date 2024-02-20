package com.crypto.fundingrate.presentation.fundingratescreen

import com.crypto.fundingrate.domain.model.FundingRate

data class FundingRatesState (
    val fundingRates: List<FundingRate> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)