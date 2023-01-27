package com.example.trpg_try

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.trpg_try.api.AppSessionKey
import com.example.trpg_try.api.Login
import com.example.trpg_try.character_create.CharacterCreate_o
import com.example.trpg_try.character_create.send_CharacterCreate
import com.example.trpg_try.character_create.send_CharacterCreate_wImg
import com.example.trpg_try.lib.getRealPathFromURI
import com.example.trpg_try.session_create.send_SessionCreate
import kotlinx.android.synthetic.main.activity_make_char.*
import kotlinx.android.synthetic.main.activity_signup_page.*
import kotlinx.android.synthetic.main.main_list.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class make_char : AppCompatActivity() {
    val FLAG_REQ_STORAGE = 100
    var path=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_char)
        make_charprofile.setOnClickListener{
            checkPermission()
        }

        bt_newchar.setOnClickListener {
            var charactername = make_charname.text.toString()
            var characterbio = edit_word.text.toString()
            if (charactername.length in 1..20 ) {
                if (characterbio.length in 0..30) {
                    if (path.isNotEmpty()) {
                        val file = File(path)
                        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                        val body =MultipartBody.Part.createFormData("character", file.name, requestFile)
                        //이미지 크기 제한 두어야 함!! 혹은 여기서 변환해줄것!!
                        Toast.makeText(this, "이미지잇음!!!ㅅ.", Toast.LENGTH_SHORT).show()

                        send_CharacterCreate_wImg.call(charactername, characterbio, body)
                            .enqueue(object : Callback<CharacterCreate_o> {
                                override fun onResponse(
                                    call: Call<CharacterCreate_o>,
                                    response: Response<CharacterCreate_o>
                                ) {
                                    var res = response.body()
                                    if (res?.code.equals("0000")) {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage(res?.msg)
                                        dialog.show()
                                        //성공적으로 캐릭터 생성됨. 여기에 이후 동작 제공
                                    } else {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage("[" + res?.code + "]" + res?.msg)
                                        dialog.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CharacterCreate_o>,
                                    t: Throwable
                                ) {
                                    var dialog = AlertDialog.Builder(this@make_char)
                                    dialog.setMessage("서버 연결에 실패했습니다.")
                                    dialog.show()
                                }
                            })
                    } else {//이미지가 없는 경우
                        send_CharacterCreate.call(charactername, characterbio)
                            .enqueue(object : Callback<CharacterCreate_o> {
                                override fun onResponse(
                                    call: Call<CharacterCreate_o>,
                                    response: Response<CharacterCreate_o>
                                ) {
                                    var res = response.body()
                                    if (res?.code.equals("0000")) {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage(res?.msg)
                                        dialog.show()
                                        //성공적으로 캐릭터 생성됨. 여기에 이후 동작 제공
                                    } else {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage("[" + res?.code + "]" + res?.msg)
                                        dialog.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CharacterCreate_o>,
                                    t: Throwable
                                ) {
                                    var dialog = AlertDialog.Builder(this@make_char)
                                    dialog.setMessage("서버 연결에 실패했습니다.")
                                    dialog.show()
                                }
                            })

                    }
                }
                else {
                    Toast.makeText(this, "캐릭터 한마디는 30자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "캐릭터 이름은 1자 이상 25자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission(){
        val imagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(imagePermission == PackageManager.PERMISSION_GRANTED){
            openGallery()
        }else{
            requestPermission()
        }
    }
    fun onRequestPermissionResult(
        requestCode: Int,
        permission: Array<out String>,
        grantResult: IntArray
    ){
        when(requestCode){
            99->{
                if(grantResult[0]== PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else{
                    Toast.makeText(this, "승인을 거절했습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun openGallery(){
        Toast.makeText(this, "갤러리를 실행합니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,FLAG_REQ_STORAGE)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 99)
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if(resultCode==RESULT_OK&&data!=null){
            when(requestCode){
                FLAG_REQ_STORAGE->{
                    val uri = data?.data
                     char_img.setImageURI(uri)
                     make_charprofile.visibility=View.INVISIBLE
                    path=getRealPathFromURI(applicationContext, uri!!)!!
                }
            }
        }
    }
}