package com.kafpin.jwtauth.network.stocks

import com.google.gson.annotations.SerializedName
import com.kafpin.jwtauth.network.cities.City


data class Stock (

    @SerializedName("id"        ) var id        : Int?     = null,
    @SerializedName("name"      ) var name      : String?  = null,
    @SerializedName("address"   ) var address   : String?  = null,
    @SerializedName("cityId"    ) var cityId    : Int?     = null,
    @SerializedName("acitve"    ) var acitve    : Boolean? = null,
    @SerializedName("createdAt" ) var createdAt : String?  = null,
    @SerializedName("city"      ) var city      : City?    = City()

)