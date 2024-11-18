package com.kafpin.jwtauth.network

import com.kafpin.jwtauth.models.shippings.CargoList
import com.kafpin.jwtauth.models.shippings.ShippingList
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.models.shippings.ShortShippingOne
import com.kafpin.jwtauth.models.shippings.dto.AcceptCargoResponseDto
import com.kafpin.jwtauth.models.shippings.dto.AddCargoToShippingDto
import com.kafpin.jwtauth.models.shippings.dto.ApplyCargoDto
import com.kafpin.jwtauth.models.shippings.dto.CreateShippingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap


enum class PaginationQueryParam(val value: String) {
    PAGE("page"),
    PER_PAGE("perPage"),
    SEARCH("search"),
    FIELDS("fields")
}

fun createPaginationQueryMap(
    page: Int? = null,
    perPage: Int? = null,
    search: String? = null,
    fields: ArrayList<String>? = null,
): Map<String, String> {
    val queryMap = mutableMapOf<String, String>()

    page?.let { queryMap[PaginationQueryParam.PAGE.value] = it.toString() }
    perPage?.let { queryMap[PaginationQueryParam.PER_PAGE.value] = it.toString() }
    search?.let { queryMap[PaginationQueryParam.SEARCH.value] = it }
    fields?.let { fields }

    return queryMap
}

interface ShippingService {
    @AuthRequired
    @GET("shipping")
    suspend fun getShippings(
        @QueryMap query: Map<String, String>
    ): Response<ShippingList>

    @AuthRequired
    @GET("shipping/{id}")
    suspend fun getShipping(@Path("id") id: Int): Response<ShippingOne>

    @AuthRequired
    @POST("shipping")
    suspend fun createShipping(@Body body: CreateShippingDto): Response<ShortShippingOne>

    @AuthRequired
    @POST("shipping/{id}/add-cargo")
    suspend fun addCargoToShipping(@Path("id") id: Int, @Body addCargoToShippingDto: AddCargoToShippingDto): Response<ShippingOne>

    @AuthRequired
    @GET("shipping/{id}/get-cargo")
    suspend fun getCargoFromShipping(@Path("id") id: Int): Response<CargoList>

    @AuthRequired
    @POST("cargo/acceptCargo")
    suspend fun applyCargo(@Body acceptCargoDto: ApplyCargoDto): Response<AcceptCargoResponseDto>
}