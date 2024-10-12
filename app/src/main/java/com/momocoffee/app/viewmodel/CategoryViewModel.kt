package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import com.momocoffee.app.network.response.CategoriesResponse
import kotlinx.coroutines.launch
import org.json.JSONArray

class CategoryViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitHelper.apiService()
    val loadingState = mutableStateOf(false)
    val selectCategory = mutableStateOf("")
    var subCategorySelected = mutableStateOf(JSONArray())
    val categoriesResultState = mutableStateOf<Result<List<CategoriesResponse>>?>(null)
    fun categorys() {
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response = apiService.getCategories()
                if (response.isSuccessful) {
                    val clientResponse: ArrayList<CategoriesResponse>? = response.body()
                    if (clientResponse != null) {
                        categoriesResultState.value = Result.success(clientResponse)
                    } else {
                        categoriesResultState.value = Result.failure(Exception("Empty response body"))
                    }
                }
            } catch (e: Exception){
                Log.e("Result.CategoryViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.CategoryViewModel", "Finally")
            }

        }
    }



}