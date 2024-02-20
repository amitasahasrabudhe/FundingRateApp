package com.crypto.fundingrate.domain.repository

import com.crypto.fundingrate.util.Resource
import com.crypto.fundingrate.domain.model.FundingRate
import kotlinx.coroutines.flow.Flow

interface FundingRateRepository {

    // don't use Call if the following is a suspend function
    suspend fun getFundingRates(): Flow<Resource<List<FundingRate>>>
}