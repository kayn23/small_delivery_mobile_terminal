package com.kafpin.jwtauth.models.shippings.dto

data class ApplyCargoDto(
    var stockId: Int,
    var cargoId: Int
)

data class AcceptCargoResponseDto(
    var endInvoice: Int?, var endShipping: Int?
)