package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.data.GeneralResponse
import com.momocoffee.app.network.dto.KioskoRequest
import com.momocoffee.app.network.dto.VerifyKioskoRequest
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import com.momocoffee.app.network.response.DataKiosko
import com.momocoffee.app.network.response.KioskoResponse
import kotlinx.coroutines.launch

class KioskoViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitHelper.apiService()
    val loadingState = mutableStateOf(false)
    val kioskoResultState = mutableStateOf<Result<KioskoResponse>?>(null)
    val kioskoVefiryResultState = mutableStateOf<Result<DataKiosko>?>(null)
    val kioskoByIdState = mutableStateOf<Result<ArrayList<DataKiosko>>?>(null)
    val kioskoUpdateResultState = mutableStateOf<Result<GeneralResponse>?>(null)

    fun kioskoById(shoppingId: String, kioskoId: String){
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response = apiService.getkioskoById(shoppingId, kioskoId)

                if (response.isSuccessful) {
                    val kioskoResponse: ArrayList<DataKiosko>? = response.body()
                    if (kioskoResponse != null) {
                        kioskoByIdState.value = Result.success(kioskoResponse)
                    } else {
                        kioskoByIdState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    kioskoByIdState.value = Result.failure(Exception("Kiosko by Id failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.KioskoModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.KioskoModel", "Finally")
            }
        }
    }

    fun activeKiosko(shoppingID: String) {
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response = apiService.activateKiosko(shoppingID, state = false)
                if (response.isSuccessful) {
                    val kioskoResponse: KioskoResponse? = response.body()
                    if (kioskoResponse != null) {
                        kioskoResultState.value = Result.success(kioskoResponse)
                    } else {
                        kioskoResultState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    kioskoResultState.value = Result.failure(Exception("Active Kiosko failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.KioskoModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.KioskoModel", "Finally")
            }
        }
    }

    fun verifyKiosko(kioskoId: String){
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response = apiService.verifyKiosko(VerifyKioskoRequest(kioskoId))
                if (response.isSuccessful) {
                    val kioskoResponse: ArrayList<DataKiosko>? = response.body()
                    if (kioskoResponse != null) {
                        kioskoVefiryResultState.value = Result.success(kioskoResponse[0])
                    } else {
                        kioskoVefiryResultState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    kioskoVefiryResultState.value = Result.failure(Exception("Active Kiosko failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.KioskoModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.KioskoModel", "Finally")
            }
        }
    }


    fun updateKiosko(kioskoId: String, shoppingID: String, state: Boolean){
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response = apiService.updatekiosko(kioskoId, KioskoRequest(
                    state = state,
                    shoppingID = shoppingID
                ))
                Log.w("Result.KioskoModelView", response.toString())
                if (response.isSuccessful) {
                    val kioskoResponse: GeneralResponse? = response.body()
                    if (kioskoResponse != null) {
                        kioskoUpdateResultState.value = Result.success(kioskoResponse)
                    } else {
                        kioskoUpdateResultState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    kioskoUpdateResultState.value = Result.failure(Exception("Active Kiosko failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.KioskoModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.KioskoModel", "Finally")
            }
        }

    }
}