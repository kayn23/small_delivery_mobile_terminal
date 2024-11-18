package com.kafpin.jwtauth.models.shippings

import com.google.gson.annotations.SerializedName
import com.kafpin.jwtauth.models.stocks.Stock


data class Shipping (

    @SerializedName("id"           ) var id           : Int?               = null,
    @SerializedName("createdAt"    ) var createdAt    : String?            = null,
    @SerializedName("endAt"        ) var endAt        : String?            = null,
    @SerializedName("capacity"     ) var capacity     : Int?               = null,
    @SerializedName("startPointId" ) var startPointId : Int?               = null,
    @SerializedName("endPointId"   ) var endPointId   : Int?               = null,
    @SerializedName("userId"       ) var userId       : Int?               = null,
    @SerializedName("cargoes"      ) var cargoes      : ArrayList<Cargo>   = arrayListOf(),
    @SerializedName("startPoint"   ) var startPoint   : Stock?             = Stock(),
    @SerializedName("endPoint"     ) var endPoint     : Stock?             = Stock()

)