package com.crypto.fundingrate.presentation.fundingratescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.fundingrate.domain.repository.FundingRateRepository
import com.crypto.fundingrate.domain.model.CryptoExchange
import com.crypto.fundingrate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val REFRESH_TIMER_IN_SECONDS = 60L
@HiltViewModel
class FundingRateScreenViewModel @Inject constructor(
    private val repository: FundingRateRepository
) : ViewModel() {
    var state by mutableStateOf(FundingRatesState())
    private set

    private var _timer = MutableStateFlow(0L);
  //  val timer = _timer.asStateFlow()

    private var timerJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    init {
        getFundingRates()
    }

    fun updateExchange(newExchange: CryptoExchange) {
        if (!state.exchange.equals(newExchange)) {
            state = state.copy(exchange = newExchange)
            getFundingRates()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(true) {
                delay(TimeUnit.SECONDS.toMillis(REFRESH_TIMER_IN_SECONDS))
                _timer.value++;
                Log.d("FundingRateTimer", "timer ran out $_timer.value")
                pauseTimer()
                getFundingRates()

            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    fun stopTimer() {
        _timer.value = 0
        timerJob?.cancel()
    }

    fun getFundingRates() {
        viewModelScope.launch {
            // copy over the value to this scope as it may change by the time coroutine finishes
            val exchange = state.exchange
            repository
                .getFundingRates(exchange)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                when(exchange) {
                                    CryptoExchange.BYBIT -> state = state.copy(isByBitDataLoading = false, bybitFundingRates = it)
                                    CryptoExchange.BINANCE -> state = state.copy(isBinanceDataLoading = false, binanceFundingRates = it)
                                    CryptoExchange.OKX -> state = state.copy(isOKXDataLoading = false, okxFundingRates = it)
                                }

                                startTimer()
                            }
                        }
                        is Resource.Error -> {
                            when(exchange) {
                                CryptoExchange.BYBIT ->
                                    state = state.copy(isByBitDataLoading = false, bybitFundingRates = emptyList())
                                CryptoExchange.BINANCE ->
                                    state = state.copy(isBinanceDataLoading = false, binanceFundingRates = emptyList())
                                CryptoExchange.OKX ->
                                    state = state.copy(isOKXDataLoading = false, okxFundingRates = emptyList())
                            }
                        }
                        is Resource.Loading -> {
                            when(exchange) {
                                CryptoExchange.BYBIT -> state = state.copy(isByBitDataLoading = result.isLoading)
                                CryptoExchange.BINANCE -> state = state.copy(isBinanceDataLoading = result.isLoading)
                                CryptoExchange.OKX -> state = state.copy(isOKXDataLoading = result.isLoading)
                            }
                        }
                    }
                }
        }
    }
}