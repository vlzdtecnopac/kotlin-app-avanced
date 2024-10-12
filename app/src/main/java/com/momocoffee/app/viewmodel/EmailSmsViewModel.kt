package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.dto.ClientReceptorEmailRequest
import com.momocoffee.app.network.dto.ClientReceptorSMSRequest
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import com.momocoffee.app.network.response.ClientEmailSMSResponse
import kotlinx.coroutines.launch

class EmailSmsViewModel: ViewModel()  {
    private val apiService: ApiService = RetrofitHelper.apiService()
    private val emailSmsResultState = mutableStateOf<Result<ClientEmailSMSResponse>?>(null)
    val loadingState = mutableStateOf(false)

    fun sendEmail(receptor: ClientReceptorEmailRequest){
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getClientEmailConfirm(ClientReceptorEmailRequest(receptor.from, receptor.to, receptor.subject))
                if (response.isSuccessful) {
                    val responseEmailSms: ClientEmailSMSResponse? = response.body()
                    Log.e("Result.EmailSmsViewModel", responseEmailSms.toString())
                    if (responseEmailSms != null) {
                        emailSmsResultState.value = Result.success(responseEmailSms)
                    } else {
                        emailSmsResultState.value =
                            Result.failure(Exception("Empty response body"))
                    }
                } else {
                    emailSmsResultState.value = Result.failure(Exception("SMS or Email failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.EmailSmsViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.EmailSmsViewModel", "Finally")
            }
        }
    }

    fun sendSms(receptor: ClientReceptorSMSRequest){
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getClientSMSConfirm(ClientReceptorSMSRequest(receptor.topic, receptor.phone, receptor.subject, receptor.message))
                if (response.isSuccessful) {
                    val responseEmailSms: ClientEmailSMSResponse? = response.body()
                    Log.e("Result.EmailSmsViewModel", responseEmailSms.toString())
                    if (responseEmailSms != null) {
                        emailSmsResultState.value = Result.success(responseEmailSms)
                    } else {
                        emailSmsResultState.value =
                            Result.failure(Exception("Empty response body"))
                    }
                } else {
                    emailSmsResultState.value = Result.failure(Exception("SMS or Email failed"))
                }

            } catch (e: Exception) {
                Log.e("Result.EmailSmsViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.EmailSmsViewModel", "Finally")
            }
        }
    }




}