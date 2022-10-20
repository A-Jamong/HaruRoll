package com.example.trpg_try.session_create
import android.util.Log
import com.example.trpg_try.api.AppSessionKey
import retrofit2.Call
import com.example.trpg_try.api.RetrofitSetting
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

data class SessionCreate_o (
    var code: String,
    var msg: String
)
interface SessionCreateService {
    //@FormUrlEncoded
    @Multipart
    @POST ("/character/create/")
    fun SessCreate(
        // input
        @Part ("AppSessionKey") AppSessionKey: String,
        @Part("charname") charname:String,
        @Part charfigure : MultipartBody.Part,
    ):Call<SessionCreate_o> //output
}

//interface SessionCreateService {
//    //@FormUrlEncoded
//    @Multipart
//    @POST ("/session/create/")
//    fun SessCreate(
//        // input
//        @Part("sessionname") sessionname:String,
//        @Part image : MultipartBody.Part,
//    ):Call<SessionCreate_o> //output
//}

//웹서버로 전송
fun send_SessionCreate(sessionname : String ,image : MultipartBody.Part) {
    val service = RetrofitSetting.createBaseService(SessionCreateService::class.java) //레트로핏 통신 설정
    val call = service.SessCreate(AppSessionKey,sessionname, image)!! //통신 API 패스 설정

    call.enqueue(object : Callback<SessionCreate_o> {
        override fun onResponse(call: Call<SessionCreate_o>, response: Response<SessionCreate_o>) {
            if (response?.isSuccessful) {
                var res = response.body()
                Log.d("Send_login: ","code "+res?.code+"/ msg "+res?.msg)
            }
            else {
                Log.d("Send_sessionCreate: ","fail")
            }
        }

        override fun onFailure(call: Call<SessionCreate_o>, t: Throwable) {
            Log.d("Send_sessionCreate: ","fail")
        }
    })
}