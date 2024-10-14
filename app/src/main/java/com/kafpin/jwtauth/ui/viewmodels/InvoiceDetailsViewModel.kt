package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.models.invoice.InvoiceDetails
import com.kafpin.jwtauth.network.ErrorHandler
import com.kafpin.jwtauth.network.InvoiceService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailsViewModel @Inject constructor(
    private val invoiceService: InvoiceService
) : BaseViewModel() {
    private var invoiceId: String? = null

    private val _invoice = MutableLiveData<InvoiceDetails>()
    val invoice: LiveData<InvoiceDetails> get() = _invoice

    fun setInvoiceId(id: String?) {
        invoiceId = id
        if (id == null) return
        getInvoice(id)
        // Здесь вы можете выполнять дополнительные действия с invoiceId, например, загружать данные
    }

    fun getInvoice(id: String) {
        _loading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = invoiceService.getInvoice(id.toInt())
                if (response.isSuccessful) {
                    _invoice.value = response.body();
                } else {
                    errorHandle(response)
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