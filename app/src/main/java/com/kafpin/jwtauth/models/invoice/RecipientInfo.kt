package com.kafpin.jwtauth.models.invoice

import com.google.gson.annotations.SerializedName


data class RecipientInfo (

  @SerializedName("id"       ) var id       : Int?    = null,
  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("surname"  ) var surname  : String? = null,
  @SerializedName("lastname" ) var lastname : String? = null,
  @SerializedName("email"    ) var email    : String? = null,
  @SerializedName("role_id"  ) var roleId   : Int?    = null

)