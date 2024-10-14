package com.kafpin.jwtauth.models.invoice

import com.google.gson.annotations.SerializedName


data class Invoice (

  @SerializedName("id"             ) var id            : Int?               = null,
  @SerializedName("sender"         ) var sender        : Int?               = null,
  @SerializedName("recipient"      ) var recipient     : Int?               = null,
  @SerializedName("end_point"      ) var endPoint      : Int?               = null,
  @SerializedName("status"         ) var status        : Int?               = null,
  @SerializedName("price"          ) var price         : String?            = null,
  @SerializedName("recipient_info" ) var recipientInfo : RecipientInfo?     = RecipientInfo(),
  @SerializedName("sender_info"    ) var senderInfo    : SenderInfo?        = SenderInfo(),
  @SerializedName("end_point_info" ) var endPointInfo  : EndPointInfo?      = EndPointInfo(),
  @SerializedName("cargoes"        ) var cargoes       : ArrayList<Cargoes> = arrayListOf()

)