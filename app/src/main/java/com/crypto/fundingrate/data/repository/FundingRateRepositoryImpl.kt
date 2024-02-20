package com.crypto.fundingrate.data.repository

import com.crypto.fundingrate.data.FundingRateRemoteDataSource
import com.crypto.fundingrate.util.Resource
import com.crypto.fundingrate.data.remote.dto.ByBitTickerResponse
import com.crypto.fundingrate.data.remote.dto.toFundingRate
import com.crypto.fundingrate.domain.model.FundingRate
import com.crypto.fundingrate.domain.repository.FundingRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundingRateRepositoryImpl @Inject constructor(
    private val fundingRateRemoteDataSource: FundingRateRemoteDataSource
): FundingRateRepository {
    override suspend fun getFundingRates(): Flow<Resource<List<FundingRate>>> {
        return flow {
            emit(Resource.Loading(true))
            val byBitTickerResponse: ByBitTickerResponse? = try {
                fundingRateRemoteDataSource.getFundingRates()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
           byBitTickerResponse?.also { response ->
                if (!response.result.list.isNullOrEmpty()) {
                    emit(
                        Resource.Success(
                        response
                            .result
                            .list
                            .map { it.toFundingRate() }
                            .sortedByDescending { a -> a.volume }
                    ))
                }
            } ?: emit(Resource.Error("Couldn't load data"))


            // stop loading
            emit(Resource.Loading(false))
        }
    }
}