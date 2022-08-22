package com.example.appwithtoken

import com.example.appwithtoken.login.LogIn
import com.example.appwithtoken.login.LogInModel
import com.example.appwithtoken.register.Register
import com.example.appwithtoken.register.RegisterModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object RetrofitClient {
    private const val BASE_URL = "https://reqres.in/api/"

    val interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build();


    val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val authService by lazy {
        retrofitBuilder.create(AuthService::class.java)
    }
}

interface AuthService {
    @POST("login")
    suspend fun logIn(@Body body: LogInModel): Response<LogIn>

    @POST("register")
    suspend fun register(@Body body: RegisterModel): Response<Register>
}