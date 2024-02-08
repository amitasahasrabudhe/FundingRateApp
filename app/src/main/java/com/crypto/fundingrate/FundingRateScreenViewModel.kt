package com.crypto.fundingrate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.fundingrate.data.FundingRateRepository
import com.crypto.fundingrate.data.Resource
import com.crypto.fundingrate.presentation.FundingRateChangeEvent
import com.crypto.fundingrate.presentation.FundingRatesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getFundingRates()
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                // transform
                                state = state.copy(fundingRates = it)
                            }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                          //  state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}