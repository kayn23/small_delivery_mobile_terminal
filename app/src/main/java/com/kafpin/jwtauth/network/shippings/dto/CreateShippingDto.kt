package com.kafpin.jwtauth.network.shippings.dto

data class CreateShippingDto(
    var capacity: Int,
    var startPointId: Int,
    var endPointId: Int,
    var userId: Int
)