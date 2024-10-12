package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.dto.LoginRequest
import com.momocoffee.app.network.response.LoginResponse
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {

    private val apiService: ApiService = RetrofitHelper.apiService()
    val loadingState = mutableStateOf(false)
    val loginResultState = mutableStateOf<Result<LoginResponse>?>(null)

    fun login(email: String, password: String) {

        loadingState.value = true
        viewModelScope.launch{
            try{
                val response = apiService.getLogin(LoginRequest(email = email, password = password))
                if(response.isSuccessful){
                    val loginResponse: LoginResponse? = response.body()
                    if(loginResponse != null){
                        loginResultState.value = Result.success(loginResponse)
                    } else {
                        loginResultState.value = Result.failure(Exception("Empty response body"))
                    }
                }else{
                    loginResultState.value = Result.failure(Exception("Login failed"))
                }
            }catch (e: Exception){
                loginResultState.value = Result.failure(e)
                Log.e("Result.ViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.ViewModel", "Finally")
            }
        }
    }
}