package com.kafpin.jwtauth.network.services

import com.kafpin.jwtauth.models.stocks.StockList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface StockService {
    @GET("stocks")
    suspend fun getStocks(
        @QueryMap queryMap: Map<String, String>,
        @Query("search") search: String,
        @Query("fields") fields: List<String> = emptyList()
    ): Response<StockList>
}