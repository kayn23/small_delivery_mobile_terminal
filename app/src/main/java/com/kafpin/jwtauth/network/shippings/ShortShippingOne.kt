package com.kafpin.jwtauth.network.shippings

import com.google.gson.annotations.SerializedName

data class ShortShippingOne (
    @SerializedName("id"           ) var id           : Int?               = null,
    @SerializedName("createdAt"    ) var createdAt    : String?            = null,
    @SerializedName("endAt"        ) var endAt        : String?            = null,
    @SerializedName("capacity"     ) var capacity     : Int?               = null,
    @SerializedName("startPointId" ) var startPointId : Int?               = null,
    @SerializedName("endPointId"   ) var endPointId   : Int?               = null,
    @SerializedName("userId"       ) var userId       : Int?               = null,
)