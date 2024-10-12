package com.momocoffee.app.network.repository

import android.content.Context
import com.momocoffee.app.App
import com.momocoffee.app.BuildConfig
import com.momocoffee.app.network.dto.RefreshToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val okHttpClient = OkHttpClient.Builder()
        .authenticator(TokenAuthenticator())
        .addInterceptor(TokenInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()



    fun apiService() : ApiService {
        return retrofit.create(ApiService::class.java)
    }


}

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null) ?: ""
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("x-token", token)
            .build()

        return chain.proceed(modifiedRequest)
    }
}

class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            // Token is expired or invalid, request a new one here
            val newToken = runBlocking {requestNewToken()}
            // If newToken is not null, return a new request with the new token
            newToken?.let {
                return response.request.newBuilder()
                    .header("x-token", it)
                    .build()
            }
        }
        return null // If unable to get a new token, return null
    }

    private suspend fun requestNewToken(): String? {
        val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        val employeeId = sharedPreferences.getString("employeeId", null) ?: ""

        val response = RetrofitHelper.apiService().getRefreshToken( RefreshToken(id= employeeId))

        return if (response.isSuccessful) {
            val newToken = response.body()?.token
            newToken?.let {
                sharedPreferences.edit().putString("token", it).apply()
            }
            newToken
        } else {
            return null
        }
    }
}