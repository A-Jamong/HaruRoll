package com.example.trpg_try.character_create
import android.util.Log
import com.example.trpg_try.api.AppSessionKey
import com.example.trpg_try.api.Login
import com.example.trpg_try.api.LoginService
import retrofit2.Call
import com.example.trpg_try.api.RetrofitSetting
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

data class CharacterCreate_o (
    var code: String,
    var msg: String
)
interface CharacterCreateService {
    //@FormUrlEncoded
    @Multipart
    @POST ("/character/create/")
    fun CharCreate(
        // input
        @Part ("AppSessionKey") AppSessionKey: String,
        @Part("charname") charname:String,
        @Part("charbio") charbio:String,
        @Part charfigure : MultipartBody.Part,
    ):Call<CharacterCreate_o> //output
}

//웹서버로 전송
object send_CharacterCreate{
    val service = RetrofitSetting.createBaseService(CharacterCreateService::class.java) //레트로핏 통신 설정
    fun call(charactername: String ,characterbio : String ,image : MultipartBody.Part):Call<CharacterCreate_o>{
        return service.CharCreate(AppSessionKey,charactername,characterbio,image )
    }
}
