package com.kafpin.jwtauth.models.shippings.dto

data class CreateShippingDto(
    var capacity: Int,
    var startPointId: Int,
    var endPointId: Int,
    var userId: Int
)