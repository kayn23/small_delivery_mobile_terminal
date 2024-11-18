package com.kafpin.jwtauth.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class AcceptCargoBody(var cargoId: Int, var stockId: Int)
data class AcceptCargoResponse(
    @SerializedName("endInvoice")  var endInvoice    : Int? = null,
    @SerializedName("endShipping") var endShipping   : Int? = null
)

interface CargoService {
    @AuthRequired
    @POST("cargo/acceptCargo")
    suspend fun acceptCargo(@Body body: AcceptCargoBody): Response<AcceptCargoResponse>
}