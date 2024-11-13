package com.kafpin.jwtauth.network.shippings

import com.google.gson.annotations.SerializedName


data class User (

  @SerializedName("id"             ) var id             : Int?    = null,
  @SerializedName("email"          ) var email          : String? = null,
  @SerializedName("roleId"         ) var roleId         : Int?    = null,
  @SerializedName("name"           ) var name           : String? = null,
  @SerializedName("surname"        ) var surname        : String? = null,
  @SerializedName("lastname"       ) var lastname       : String? = null,
  @SerializedName("documentNumber" ) var documentNumber : String? = null,
  @SerializedName("createdAt"      ) var createdAt      : String? = null

)