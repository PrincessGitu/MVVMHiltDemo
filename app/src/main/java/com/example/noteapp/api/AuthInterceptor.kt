package com.example.noteapp.api

import com.example.noteapp.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() :Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

      //  val token = tokenManager.getToken()
        request.addHeader("access_token", "64edcb824975d")
        return chain.proceed(request.build())
    }
}