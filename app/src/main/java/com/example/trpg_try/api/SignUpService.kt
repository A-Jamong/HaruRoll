package com.example.trpg_try.api
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.IOException

data class SignUp (
    var code: String,
    var msg: String,
)
interface SignUpService{
    @FormUrlEncoded
    @POST ("/signup/") //실제 서버 주소 뒤에 붙는 url
    fun requestSignUp(
        // input
        @Field("email") email:String,
        @Field("username") username:String,
        @Field("password") password:String
    ):Call<SignUp> //output

}
object send_SignUp{
    val service = RetrofitSetting.createBaseService(SignUpService::class.java) //레트로핏 통신 설정
    //객체생성
    fun call(email:String, username: String ,password : String):Call<SignUp>{
        return service.requestSignUp(email, username, password)!!
    }
}