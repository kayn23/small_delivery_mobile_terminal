package com.kafpin.jwtauth.models.shippings

import com.google.gson.annotations.SerializedName

data class CargoList (

    @SerializedName("items"     ) var items     : ArrayList<Cargo> = arrayListOf(),
    @SerializedName("total"     ) var total     : Int?             = null,
    @SerializedName("totalPage" ) var totalPage : String?          = null,
    @SerializedName("page"      ) var page      : Int?             = null
)