package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.data.GeneralResponse
import com.momocoffee.app.network.dto.BuildingRequest
import com.momocoffee.app.network.dto.ClientEmailInvoiceRequest
import com.momocoffee.app.network.dto.UpdateBilingRequest
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import com.momocoffee.app.network.response.BuildingResponse
import com.momocoffee.app.network.response.ClientEmailSMSResponse
import com.momocoffee.app.network.response.TurnoResponse
import kotlinx.coroutines.launch


class BuildingViewModel : ViewModel() {
    val loadingState = mutableStateOf(false)
    private val apiEmailSmsService: ApiService = RetrofitHelper.apiService()
    private val apiService: ApiService = RetrofitHelper.apiService()
    val buildingResultState = mutableStateOf<Result<BuildingResponse>?>(null)
    val emailResultState = mutableStateOf<Result<ClientEmailSMSResponse>?>(null)
    val buildingTurnoResultState =  mutableStateOf<Result<TurnoResponse>?>(null)
    val buildingUpdateResultState = mutableStateOf<Result<GeneralResponse>?>(null)

    fun payment(invoice: BuildingRequest) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.createBuilding(invoice);
                if (response.isSuccessful) {
                    val buildingResponse: BuildingResponse? = response.body()
                    if (buildingResponse != null) {
                        buildingResultState.value = Result.success(buildingResponse)
                    } else {
                        buildingResultState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    buildingResultState.value =
                        Result.failure(Exception(response.body().toString()))
                }
            } catch (e: Exception) {
                Log.e("Result.BuildingViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.BuildingViewModel", "Finally")
            }
        }
    }

    fun updatePayment(bildingId: String, invoice: UpdateBilingRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.updateBilding(bildingId, invoice)

                if (response.isSuccessful) {
                    val buildingResponse: GeneralResponse? = response.body()
                    if (buildingResponse != null) {
                        buildingUpdateResultState.value = Result.success(buildingResponse)
                    } else {
                        buildingUpdateResultState.value =
                            Result.failure(Exception("Empty response body"))
                    }
                } else {
                    buildingUpdateResultState.value =
                        Result.failure(Exception(response.body().toString()))
                }
            } catch (e: Exception) {
                Log.e("Result.BuildingViewModel", e.message.toString())
            } finally {
                Log.d("Result.CategoryViewModel", "Finally")
            }
        }
    }

    fun sendClientEmailInvoice(clientEmail: ClientEmailInvoiceRequest) {
        loadingState.value = true
        Log.i("Result.ShoppingModel", clientEmail.toString())
        viewModelScope.launch {
            try {
                val response = apiEmailSmsService.sendEmailInvoice(clientEmail)
                Log.e("Result.ShoppingModel", response.toString())
                if (response.isSuccessful) {
                    val emailResponse: ClientEmailSMSResponse? = response.body()
                    if (emailResponse != null) {
                        emailResultState.value = Result.success(emailResponse)
                    } else {
                        emailResultState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    emailResultState.value = Result.failure(Exception("Email send invoice failed"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Result.BuildingViewModel", e.message.toString())
            } finally {
                loadingState.value = false
            }

            Log.d("Result.EmailSmsViewModel", emailResultState.value.toString());
        }

    }

    fun validPayment(bildingId:  String, state: String) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getVerifyBildings(bildingId,  state)
                if (response.isSuccessful) {
                    val buildingResponse: TurnoResponse? = response.body()
                    if (buildingResponse != null) {
                        buildingTurnoResultState.value = Result.success(buildingResponse)
                    } else {
                        buildingTurnoResultState.value =
                            Result.failure(Exception("Empty response body"))
                    }
                } else {
                    buildingTurnoResultState.value =
                        Result.failure(Exception(response.body().toString()))
                }
            } catch (e: Exception) {
                Log.e("Result.BuildingViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.CategoryViewModel", "Finally")
            }
        }
    }
}