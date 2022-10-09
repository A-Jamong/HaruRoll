package com.example.trpg_try
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.text.DecimalFormat

interface CharacterService{
    @FormUrlEncoded
    @POST ("/character/create/") //실제 서버 주소 뒤에 붙는 url
    fun create(
        // input
        //@Field("characternum") characternum:DecimalFormat,
        @Field("charactername") charactername:String
        //@Field("password") password:String
    ):Call<CharacterRes> //output
}
//