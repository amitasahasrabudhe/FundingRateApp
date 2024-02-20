package com.crypto.fundingrate.presentation.fundingratescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.fundingrate.domain.repository.FundingRateRepository
import com.crypto.fundingrate.util.Resource
import com.crypto.fundingrate.presentation.FundingRateChangeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundingRateScreenViewModel @Inject constructor(
    private val repository: FundingRateRepository
) : ViewModel() {
    var state by mutableStateOf(FundingRatesState())
    private set

    init {
        getFundingRates()
    }
    fun onChangeEvent(event: FundingRateChangeEvent) {
        when (event) {
            is FundingRateChangeEvent.Refresh -> {
                getFundingRates()
            }
        }
    }

    private fun getFundingRates() {
        viewModelScope.launch {
            repository
                .getFundingRates()
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(isLoading = false, fundingRates = it)
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