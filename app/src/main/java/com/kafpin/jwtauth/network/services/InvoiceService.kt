package com.kafpin.jwtauth.network.services

import com.kafpin.jwtauth.models.Invoices
import com.kafpin.jwtauth.models.invoice.InvoiceDetails
import com.kafpin.jwtauth.network.interceptors.AuthRequired
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface InvoiceService {
    @AuthRequired
    @GET("invoices")
    suspend fun getInvoices(
        @QueryMap options: Map<String, String>
    ): Response<Invoices>

    @AuthRequired
    @GET("invoices/{id}")
    suspend fun getInvoice(@Path("id") id: Int): Response<InvoiceDetails>
}