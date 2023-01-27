package com.example.trpg_try.character_list
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

//data class CharacterList_o (
//    var charnameList : ArrayList<String>,
//    var code: String,
//    var msg: String
//)
data class Character (
    var charname : String
)
interface CharacterListService {
    @GET ("/character/getList/")
    fun CharList(
        // input
        @Query ("AppSessionKey") AppSessionKey: String,
    ):Call<List<Character>> //output
}
//웹서버로 전송
object send_CharacterList{
    val service = RetrofitSetting.createBaseService(CharacterListService::class.java) //레트로핏 통신 설정
    fun call():Call<List<com.example.trpg_try.character_list.Character>>{
        return service.CharList(AppSessionKey)
    }
}