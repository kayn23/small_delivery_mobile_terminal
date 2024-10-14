package com.kafpin.jwtauth.models

enum class InvoiceStatus(val id: Int, val enName: String, val ruName: String) {
    NEW(1, "new", "новый"),
    PAYMENT(2, "payment", "ожидает оплаты"),
    PAID(3, "paid", "оплачен"),
    DELIVERING(4, "delivers", "доставляется"),
    RECEIVED(5, "reseived", "ожидает получения"),
    END(6, "end", "доставлен");

    companion object {
        fun fromId(id: Int): InvoiceStatus? {
            return values().find { it.id == id }
        }
    }
}

