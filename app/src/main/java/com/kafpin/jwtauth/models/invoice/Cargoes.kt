package com.kafpin.jwtauth.models.invoice

import com.google.gson.annotations.SerializedName


data class Cargoes (

  @SerializedName("id"         ) var id        : Int? = null,
  @SerializedName("weight"     ) var weight    : Int? = null,
  @SerializedName("invoice_id" ) var invoiceId : Int? = null

)