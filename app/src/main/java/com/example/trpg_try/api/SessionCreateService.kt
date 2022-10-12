package com.example.trpg_try.session_create
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import com.example.trpg_try.api.APPsession_id
import retrofit2.Call
import com.example.trpg_try.api.RetrofitSetting
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
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
        @Part("APPsession_id") APPsession_id:String,
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
fun send_SessionCreate(context: Context, sessionname : String, image : MultipartBody.Part) {
    val service = RetrofitSetting.createBaseService(SessionCreateService::class.java) //레트로핏 통신 설정
    val call = service.SessCreate(APPsession_id, sessionname, image)!! //통신 API 패스 설정
    call.enqueue(object : Callback<SessionCreate_o> {
        override fun onResponse(call: Call<SessionCreate_o>, response: Response<SessionCreate_o>) {
            if (response?.isSuccessful) {
                var res = response.body()
                var code = res?.code
                if (code != "0000") {
                    var dialog = AlertDialog.Builder(context)
                    dialog.setMessage("[" + res?.code + "]" + res?.msg)
                    dialog.show()
                }
            }
            else {
                var dialog = AlertDialog.Builder(context)
                dialog.setMessage("서버와 통신이 원활하지 않습니다.")
                dialog.show()
            }
        }

        override fun onFailure(call: Call<SessionCreate_o>, t: Throwable) {
            var dialog = AlertDialog.Builder(context)
            dialog.setMessage("서버와 통신이 원활하지 않습니다.")
            dialog.show()
        }
    })
}