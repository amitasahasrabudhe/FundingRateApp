package com.crypto.fundingrate.hilt

import com.crypto.fundingrate.data.remote.ByBitService
import com.crypto.fundingrate.data.FundingRateRemoteDataSource
import com.crypto.fundingrate.domain.repository.FundingRateRepository
import com.crypto.fundingrate.data.repository.FundingRateRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    // "https://api.bybit.com/"
    private val BYBIT_BASE_URL_V5 = "https://api-testnet.bybit.com"
    @Provides
    @Singleton
    fun provideByBitService(
    ): ByBitService {
        val contentType = "application/json".toMediaType()

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        //    prettyPrint = true
         //   encodeDefaults = true
        }

        return Retrofit.Builder()
            .baseUrl(BYBIT_BASE_URL_V5)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE }).build())

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