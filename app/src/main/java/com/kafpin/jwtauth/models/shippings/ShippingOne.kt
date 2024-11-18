package  com.kafpin.jwtauth.models.shippings

import com.google.gson.annotations.SerializedName


data class ShippingOne (
    @SerializedName("id"           ) var id           : Int?               = null,
    @SerializedName("createdAt"    ) var createdAt    : String?            = null,
    @SerializedName("endAt"        ) var endAt        : String?            = null,
    @SerializedName("capacity"     ) var capacity     : Int?               = null,
    @SerializedName("startPointId" ) var startPointId : Int?               = null,
    @SerializedName("endPointId"   ) var endPointId   : Int?               = null,
    @SerializedName("userId"       ) var userId       : Int?               = null,
    @SerializedName("startPoint"   ) var startPoint   : StartPoint?        = StartPoint(),
    @SerializedName("endPoint"     ) var endPoint     : EndPoint?          = EndPoint(),
    @SerializedName("cargoes"      ) var cargoes      : ArrayList<Cargo> = arrayListOf(),
    @SerializedName("user"         ) var user         : User?              = User()

)

