package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.models.InvoicePreview
import com.kafpin.jwtauth.network.InvoiceService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val invoiceService: InvoiceService
) : BaseViewModel() {
    private val _invoices = MutableLiveData<List<InvoicePreview>>()
    val invoices: LiveData<List<InvoicePreview>> get() = _invoices

    fun getInvoices() {
        _loading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = invoiceService.getInvoices()
                if (response.isSuccessful) {
                    _invoices.value = response.body()?.invoices ?: emptyList()
                } else {
                    _invoices.value = emptyList()
                }
            } catch (e: IllegalArgumentException) {
                handleException(e)
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _loading.value = false
            }
        }
    }
}