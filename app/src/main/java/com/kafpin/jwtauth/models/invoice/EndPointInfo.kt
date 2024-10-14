package com.kafpin.jwtauth.models.invoice

import com.google.gson.annotations.SerializedName


data class EndPointInfo (

  @SerializedName("id"      ) var id      : Int?    = null,
  @SerializedName("name"    ) var name    : String? = null,
  @SerializedName("address" ) var address : String? = null,
  @SerializedName("city_id" ) var cityId  : Int?    = null,
  @SerializedName("deleted" ) var deleted : Int?    = null,
  @SerializedName("city"    ) var city    : String? = null

)