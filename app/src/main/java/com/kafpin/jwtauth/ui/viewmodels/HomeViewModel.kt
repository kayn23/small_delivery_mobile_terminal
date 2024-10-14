package com.kafpin.jwtauth.ui.viewmodels

import android.widget.Toast
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
    // save filters
    var filters: Map<String, String> = emptyMap()

    // Состояние для навигации
    private val _navigateToDetail = MutableLiveData<String?>()
    val navigateToDetail: LiveData<String?> get() = _navigateToDetail
    fun clearNavigoteToDetail() {
        _navigateToDetail.value = null
    }

    fun saveFilter(value: Map<String, String>) {
        filters = value
    }

    fun getInvoices(filters: Map<String, String> = emptyMap()) {
        _loading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = invoiceService.getInvoices(filters)
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

    fun getInvoice(filters: Map<String, String> = emptyMap()) {
        _loading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = invoiceService.getInvoices(filters)
                if (response.isSuccessful) {
                    val invoiceList = response.body()?.invoices ?: emptyList()
                    if (invoiceList.isNotEmpty() && invoiceList.size == 1) {
                        // Получаем ID первого элемента и устанавливаем состояние навигации
                        _navigateToDetail.value = invoiceList[0].id.toString() // Предполагается, что у вас есть id
                    } else {
                        _errorMessage.value = "Not found"
                    }
                } else {
                    // this notification
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