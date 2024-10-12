package com.momocoffee.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.App
import com.momocoffee.app.network.dto.ClientRequest
import com.momocoffee.app.network.dto.ClientSessionEmailRequest
import com.momocoffee.app.network.dto.ClientSessionPhoneRequest
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import com.momocoffee.app.network.repository.SessionManager
import com.momocoffee.app.network.response.ClientGeneralResponse
import com.momocoffee.app.network.response.ClientResponse
import com.momocoffee.app.network.response.ClientSessionResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ClientViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitHelper.apiService()
    val loadingState = mutableStateOf(false)
    val clientResultState = mutableStateOf<Result<ClientResponse>?>(null)
    val clientResultCheckEmailState = mutableStateOf<Result<ClientGeneralResponse>?>(null)
    val clientResultSession = mutableStateOf<Result<ClientSessionResponse>?>(null)
    fun register(clientDto: ClientRequest) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getRegisterClient(clientDto);
                if (response.isSuccessful) {
                    val clientResponse: ClientResponse? = response.body()
                    if (clientResponse != null) {
                        val sharedPreferences =
                            App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("clientId", clientResponse.clientID)
                            .apply()
                        clientResultState.value = Result.success(clientResponse)
                    } else {
                        clientResultState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    clientResultState.value = Result.failure(Exception("Client Session failed"))
                }

            } catch (e: Exception) {
                Log.e("Result.ClientViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.ClientViewModel", "Finally")
            }
        }
    }

    fun getClient(email: String = "", phone: String = "", clientID: String = "") {
        viewModelScope.launch {
            try {
                val response: Response<ClientGeneralResponse> =
                    apiService.getClient(email, phone, clientID)
                if (response.isSuccessful) {
                    val clientResponse: ClientGeneralResponse? = response.body()
                    if (clientResponse != null) {
                        clientResultCheckEmailState.value = Result.success(clientResponse)
                    } else {
                        clientResultCheckEmailState.value =
                            Result.failure(Exception("Empty response body"))
                    }
                } else {
                    clientResultCheckEmailState.value = Result.failure(Exception("Shopping failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.ClientViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.ClientViewModel", "Finally")
            }
        }
    }

    fun getSessionPhoneClient(clientDto: ClientSessionPhoneRequest){
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response: Response<ArrayList<ClientSessionResponse>> = apiService.getClientPhoneSession(ClientSessionPhoneRequest(phone = clientDto.phone, code= clientDto.code))
                if (response.isSuccessful) {
                    val response: ArrayList<ClientSessionResponse>? = response.body()
                    if (response!= null) {
                        val sessionManager = SessionManager()
                        val sharedPreferences =
                            App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("clientId", response[0].clientID)
                            .apply()
                        sessionManager.startSession()
                        clientResultSession.value = Result.success(response[0])
                    }else{
                        clientResultSession.value =
                            Result.failure(Exception("Empty response body"))
                    }
                } else {
                    clientResultSession.value = Result.failure(Exception("Client session failed"))
                }
            } catch (e: Exception){
                clientResultSession.value = Result.failure(Exception("Client error session"))
                Log.e("Result.ClientViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.ClientViewModel", "Finally")
            }
        }
    }


    fun getSessionEmailClient(email: String){
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response: Response<ArrayList<ClientSessionResponse>> = apiService.getClientEmailSession(
                    ClientSessionEmailRequest(email)
                )
                if(response.isSuccessful){
                    val response: ArrayList<ClientSessionResponse>? = response.body()
                    if (response!= null) {
                        val sessionManager = SessionManager()
                        val sharedPreferences =
                            App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("clientId", response[0].clientID)
                            .apply()
                        sessionManager.startSession()
                        clientResultSession.value = Result.success(response[0])
                    }else{
                        clientResultSession.value =
                            Result.failure(Exception("Empty response body"))
                    }
                }else {
                    clientResultSession.value = Result.failure(Exception("Client session failed"))
                }

            }catch (e: Exception){
                clientResultSession.value = Result.failure(Exception("Client error session"))
                Log.e("Result.ClientViewModel", e.message.toString())
            }finally {
                loadingState.value = false
                Log.d("Result.ClientViewModel", "Finally")
            }
        }
    }





}