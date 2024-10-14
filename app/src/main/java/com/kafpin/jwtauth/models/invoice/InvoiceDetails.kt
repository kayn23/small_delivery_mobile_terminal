package com.kafpin.jwtauth.models.invoice

import com.google.gson.annotations.SerializedName


data class InvoiceDetails (

  @SerializedName("invoice" ) var invoice : Invoice? = Invoice()

)