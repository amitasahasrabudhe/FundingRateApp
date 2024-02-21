package com.crypto.fundingrate.presentation.fundingratescreen

import androidx.compose.runtime.Stable
import com.crypto.fundingrate.domain.model.CryptoExchange
import com.crypto.fundingrate.domain.model.FundingRate
@Stable
data class FundingRatesState (
    val exchange: CryptoExchange = CryptoExchange.BYBIT,
    val bybitFundingRates: List<FundingRate> = emptyList(),
    val binanceFundingRates: List<FundingRate> = emptyList(),
    val okxFundingRates: List<FundingRate> = emptyList(),
    val isByBitDataLoading: Boolean = false,
    val isBinanceDataLoading: Boolean = false,
    val isOKXDataLoading: Boolean = false,
    val isRefreshing: Boolean = false
) {
    var isLoading: Boolean = when(exchange) {
        CryptoExchange.BYBIT -> isByBitDataLoading
        CryptoExchange.BINANCE -> isBinanceDataLoading
        CryptoExchange.OKX -> isOKXDataLoading
    }
    private set
    fun getFundingRates(): List<FundingRate> {
        return when(exchange) {
            CryptoExchange.BYBIT -> bybitFundingRates
            CryptoExchange.BINANCE -> binanceFundingRates
            CryptoExchange.OKX -> okxFundingRates
        }
    }
}