package com.kafpin.jwtauth.network

import com.kafpin.jwtauth.network.stocks.StockList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface StockService {
    @GET("stock")
    suspend fun getStocks(@QueryMap queryMap: Map<String, List<String>>): Response<StockList>
}