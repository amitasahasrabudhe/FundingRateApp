package com.crypto.fundingrate.data

import com.crypto.fundingrate.domain.FundingRate
import kotlinx.coroutines.flow.Flow

interface FundingRateRepository {
    suspend fun getFundingRates(): Flow<Resource<List<FundingRate>>>
}