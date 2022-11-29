package com.example.trpg_try.api

import com.example.trpg_try.session_create.SessionCreateService
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
var AppSessionKey = "-1"
object RetrofitSetting{
    val httpClient = OkHttpClient.Builder()
    //retrofit 객체
    val baseBuilder = Retrofit.Builder()
        .baseUrl("http://192.168.0.5:8000")//"http://192.168.0.5:8000")//"https://riul.pythonanywhere.com/")//
        .addConverterFactory(GsonConverterFactory.create())
    //객체생성
    fun <S> createBaseService(serviceClass: Class<S>?):S{
        val retrofit = baseBuilder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }
}
