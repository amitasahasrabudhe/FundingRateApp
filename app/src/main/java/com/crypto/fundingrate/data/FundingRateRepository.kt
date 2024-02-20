package com.crypto.fundingrate.data

import com.crypto.fundingrate.domain.FundingRate
import kotlinx.coroutines.flow.Flow

interface FundingRateRepository {

    // don't use Call if the following is a suspend function
    suspend fun getFundingRates(): Flow<Resource<List<FundingRate>>>
}