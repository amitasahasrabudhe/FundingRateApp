package com.crypto.fundingrate.presentation.fundingratescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.fundingrate.domain.repository.FundingRateRepository
import com.crypto.fundingrate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
            repository
                .getFundingRates()
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(isLoading = false, fundingRates = it)
                                startTimer()
                            }
                        }
                        is Resource.Error -> {
                            state = state.copy(isLoading = false, fundingRates = emptyList())
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}