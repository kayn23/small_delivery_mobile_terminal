package com.kafpin.jwtauth.models

import com.google.gson.annotations.SerializedName


data class InvoicePreview(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("sender") var sender: Int? = null,
    @SerializedName("recipient") var recipient: Int? = null,
    @SerializedName("end_point") var endPoint: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("price") var price: String? = null
)