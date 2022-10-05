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

data class Login (
    var code: String,
    var msg: String,
    var sessionID: String,
)
interface LoginService{
    @FormUrlEncoded
    @POST ("/signin/") //실제 서버 주소 뒤에 붙는 url
    fun requestLogin(
        // input
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<Login> //output

}
object send_Login{
    val service = RetrofitSetting.createBaseService(LoginService::class.java) //레트로핏 통신 설정
    //객체생성
    fun call(username: String ,password : String):Call<Login>{
        return service.requestLogin(username, password)!!
    }
}
fun send_Login2(username: String ,password : String) : String {
    // 사용하지 않는 함수입니당.
    val service = RetrofitSetting.createBaseService(LoginService::class.java) //레트로핏 통신 설정
    val call = service.requestLogin(username, password)!! //통신 API 패스 설정

    var respond = "9999" //통신 실패시 9999, 성공시 0000, 아이디/비번불일치 1001
    call.enqueue(object : Callback<Login> {
        override fun onResponse(call: Call<Login>, response: Response<Login>) {
            if (response?.isSuccessful) {
                var res = response.body()
                Log.d("Send_login: ","code "+res?.code+"/ msg "+res?.msg)
                respond = res?.code!!
                Log.d("Send_login: ",""+respond)
            }
            else {
                Log.d("Send_login: ","fail")
            }
        }

        override fun onFailure(call: Call<Login>, t: Throwable) {
            Log.d("Send_login: ","fail")
        }
    })
    return respond
}
