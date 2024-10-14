package com.kafpin.jwtauth.network

import com.kafpin.jwtauth.models.Invoices
import com.kafpin.jwtauth.models.invoice.InvoiceDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InvoiceService {
    @AuthRequired
    @GET("invoices")
    suspend fun getInvoices(): Response<Invoices>

    @AuthRequired
    @GET("invoices/{id}")
    suspend fun getInvoice(@Path("id") id: Int): Response<InvoiceDetails>
}