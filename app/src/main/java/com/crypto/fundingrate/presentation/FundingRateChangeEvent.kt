package com.crypto.fundingrate.presentation

sealed class FundingRateChangeEvent {
    object Refresh: FundingRateChangeEvent()
}