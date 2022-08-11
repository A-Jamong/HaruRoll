package com.example.trpg_try
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{
    @FormUrlEncoded
    @POST ("/signin/") //실제 서버 주소 뒤에 붙는 url
    fun requestLogin(
        // input
        @Field("username") username:String,
        @Field("password") password:String
    ):Call<Login> //output
}