package com.example.trpg_try.session_create
import android.util.Log
import com.example.trpg_try.api.AppSessionKey
import retrofit2.Call
import com.example.trpg_try.api.RetrofitSetting
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
//Session room 관련 함수임!!

data class SessionCreate_o (
    var code: String,
    var msg: String
)
interface SessionCreateService {
    //@FormUrlEncoded
    @Multipart
    @POST ("/sessroom/create/")
    fun SessCreate(
        // input
        @Part ("AppSessionKey") AppSessionKey: String,
        @Part("sessionname") sessionname:String,
    ):Call<SessionCreate_o> //output
}
interface SessionCreateServiceWImg {
    //@FormUrlEncoded
    @Multipart
    @POST ("/sessroom/create_wImg/")
    fun SessCreate(
        // input
        @Part ("AppSessionKey") AppSessionKey: String,
        @Part("sessionname") sessionname:String,
        @Part sessfigure : MultipartBody.Part,
    ):Call<SessionCreate_o> //output
}
//웹서버로 전송
object send_SessionCreate_wImg{
    val service = RetrofitSetting.createBaseService(SessionCreateServiceWImg::class.java) //레트로핏 통신 설정
    fun call(sessionname: String, image : MultipartBody.Part):Call<SessionCreate_o>{
        return service.SessCreate(AppSessionKey,sessionname,image )
    }
}
//웹서버로 전송
object send_SessionCreate{
    val service = RetrofitSetting.createBaseService(SessionCreateService::class.java) //레트로핏 통신 설정
    fun call(sessionname : String ):Call<SessionCreate_o>{
        return service.SessCreate(AppSessionKey,sessionname)
    }
}
