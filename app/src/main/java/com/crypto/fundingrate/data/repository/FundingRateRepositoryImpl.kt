package com.crypto.fundingrate.data.repository

import com.crypto.fundingrate.data.FundingRateRemoteDataSource
import com.crypto.fundingrate.util.Resource
import com.crypto.fundingrate.data.remote.bybit.dto.ByBitTickerResponse
import com.crypto.fundingrate.data.remote.bybit.dto.toFundingRate
import com.crypto.fundingrate.domain.model.FundingRate
import com.crypto.fundingrate.domain.repository.FundingRateRepository
import com.crypto.fundingrate.domain.model.CryptoExchange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundingRateRepositoryImpl @Inject constructor(
    private val fundingRateRemoteDataSource: FundingRateRemoteDataSource
): FundingRateRepository {
    override suspend fun getFundingRates(exchange: CryptoExchange): Flow<Resource<List<FundingRate>>> {
        return flow {
            emit(Resource.Loading(true))
            val fundingRates = fundingRateRemoteDataSource.getFundingRates(exchange)
            if (fundingRates.isNullOrEmpty()) {
                emit(Resource.Error("Couldn't load data"))
            } else {
                emit(Resource.Success(fundingRates))
            }
            // stop loading
            emit(Resource.Loading(false))
        }
    }
}