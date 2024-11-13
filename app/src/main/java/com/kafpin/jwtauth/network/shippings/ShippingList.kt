package com.kafpin.jwtauth.network.shippings

import com.google.gson.annotations.SerializedName


data class ShippingList (

  @SerializedName("items"     ) var items     : ArrayList<Shipping> = arrayListOf(),
  @SerializedName("total"     ) var total     : Int?             = null,
  @SerializedName("totalPage" ) var totalPage : String?          = null,
  @SerializedName("page"      ) var page      : Int?             = null

)