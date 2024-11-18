package com.kafpin.jwtauth.network

import com.kafpin.jwtauth.data.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.8:3000/")
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