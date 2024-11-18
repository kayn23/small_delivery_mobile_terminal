package com.kafpin.jwtauth.models.stocks

import com.google.gson.annotations.SerializedName


data class StockList (

    @SerializedName("items"     ) var items     : ArrayList<Stock> = arrayListOf(),
    @SerializedName("total"     ) var total     : Int?             = null,
    @SerializedName("totalPage" ) var totalPage : Int?             = null,
    @SerializedName("page"      ) var page      : Int?             = null,
    @SerializedName("perPage"   ) var perPage   : Int?             = null

)