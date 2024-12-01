package com.kafpin.jwtauth.network

import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.data.dataStore.TokenManager
import com.kafpin.jwtauth.network.interceptors.AuthInterceptor
import com.kafpin.jwtauth.network.interceptors.BaseUrlInterceptor
import com.kafpin.jwtauth.network.services.AuthService
import com.kafpin.jwtauth.network.services.CargoService
import com.kafpin.jwtauth.network.services.InvoiceService
import com.kafpin.jwtauth.network.services.ShippingService
import com.kafpin.jwtauth.network.services.StockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager, ipServerManager: IpServerManager): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS // Вы можете изменить уровень логирования
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(BaseUrlInterceptor(ipServerManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val defaultBaseUrl = "http://10.0.2.2:3000/"
        return Retrofit.Builder()
            .baseUrl(defaultBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideInvoiceService(retrofit: Retrofit): InvoiceService {
        return retrofit.create(InvoiceService::class.java)
    }

    @Provides
    @Singleton
    fun provideShippingService(retrofit: Retrofit): ShippingService {
        return retrofit.create(ShippingService::class.java)
    }

    @Provides
    @Singleton
    fun provideStockService(retrofit: Retrofit): StockService {
        return retrofit.create(StockService::class.java)
    }

    @Provides
    @Singleton
    fun provideCargoService(retrofit: Retrofit): CargoService {
        return retrofit.create(CargoService::class.java)
    }
}