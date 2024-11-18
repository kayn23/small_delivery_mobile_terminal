package com.kafpin.jwtauth.models.shippings

import com.google.gson.annotations.SerializedName
import com.kafpin.jwtauth.models.cities.City
import com.kafpin.jwtauth.models.stocks.Stock


data class Cargo (
  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("createdAt"   ) var createdAt   : String? = null,
  @SerializedName("size"        ) var size        : Int?    = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("qrcode"      ) var qrcode      : String? = null,
  @SerializedName("invoiceId"   ) var invoiceId   : Int?    = null,
  @SerializedName("stockId"     ) var stockId     : Int?    = null,
  @SerializedName("placement"   ) var placement   : Stock?   = null
)