package com.kafpin.jwtauth.models.cities

import com.google.gson.annotations.SerializedName


data class City (

  @SerializedName("id"        ) var id        : Int?    = null,
  @SerializedName("name"      ) var name      : String? = null,
  @SerializedName("createdAt" ) var createdAt : String? = null

)