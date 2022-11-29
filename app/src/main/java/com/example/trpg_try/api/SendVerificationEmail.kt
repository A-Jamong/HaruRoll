package com.example.trpg_try.api
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
// 이메일 발송
data class MSG (
    var code: String,
    var msg: String,
)
interface SendVerificationEmailService{
    @FormUrlEncoded
    @POST ("/sendMail/send_ValidationCode/") //실제 서버 주소 뒤에 붙는 url
    fun sendEmail(
        // input
        @Field("email") email:String,
    ):Call<MSG> //output
}
object send_VerificationEmail{
    val service = RetrofitSetting.createBaseService(SendVerificationEmailService::class.java) //레트로핏 통신 설정
    //객체생성
    fun call(email: String):Call<MSG>{
        return service.sendEmail(email)!!
    }
}

interface CheckVerificationEmailService{
    @FormUrlEncoded
    @POST("/sendMail/check_ValidationCode/") //실제 서버 주소 뒤에 붙는 url
    fun checkEmail(
        // input
        @Field("email") email:String,
        @Field("code") code:String,
    ):Call<MSG> //output
}
object check_VerificationEmail{
    val service = RetrofitSetting.createBaseService(CheckVerificationEmailService::class.java) //레트로핏 통신 설정
    //객체생성
    fun call(email: String, code: String):Call<MSG>{
        return service.checkEmail(email,code)!!
    }
}

fun send_VerificationEmail2(email: String) : String {
    // 사용하지 않는 함수입니당.
    val service = RetrofitSetting.createBaseService(SendVerificationEmailService::class.java) //레트로핏 통신 설정
    val call = service.sendEmail(email)!! //통신 API 패스 설정

    var respond = "9999" //통신 실패시 9999, 성공시 0000, 아이디/비번불일치 1001
    call.enqueue(object : Callback<MSG> {
        override fun onResponse(call: Call<MSG>, response: Response<MSG>) {
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

        override fun onFailure(call: Call<MSG>, t: Throwable) {
            Log.d("Send_login: ","fail")
        }
    })
    return respond
}
