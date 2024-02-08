package com.crypto.fundingrate.hilt

import com.crypto.fundingrate.data.ByBitService
import com.crypto.fundingrate.data.FundingRateRemoteDataSource
import com.crypto.fundingrate.data.FundingRateRepository
import com.crypto.fundingrate.data.FundingRateRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [MyProviderModule::class])
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindFundingRepository(
        fundingRateRepositoryImpl: FundingRateRepositoryImpl
    ): FundingRateRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal object MyProviderModule {
    @Provides
    @Singleton
    fun provideByBitService(
    ): ByBitService {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true}

        return Retrofit.Builder()
            .baseUrl("https://api.bybit.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ByBitService::class.java)
    }

    @Provides
    @Singleton
    fun provideFundingRateRemoteDataSource(
        byBitService: ByBitService
    ): FundingRateRemoteDataSource {
        return FundingRateRemoteDataSource(byBitService)
    }
}