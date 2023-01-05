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
import com.example.trpg_try.character_create.send_CharacterCreate
import com.example.trpg_try.lib.getRealPathFromURI
import com.example.trpg_try.session_create.send_SessionCreate
import kotlinx.android.synthetic.main.activity_make_char.*
import kotlinx.android.synthetic.main.activity_signup_page.*
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
        //통신을 위한 retrofit 객체
        var retrofit = Retrofit.Builder()
            .baseUrl("https://riul.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val uri=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_char)
        make_charprofile.setOnClickListener{
            checkPermission()
        }
        make_bt.setOnClickListener {
            var charactername = make_charname.text.toString()
            var characterbio = edit_word.text.toString()

            //이미지 크기 제한 두어야 함!! 혹은 여기서 변환해줄것!!
            if(charactername.length in 1..10 && characterbio.length in 0..20){//길이 제한 및 입력확인
                send_CharacterCreate.call(charactername, characterbio,image).enqueue(object : Callback<Login>{
                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                //통신성공
                var login = response.body()
                //통신 성공했을 때 화면 넘어가게
                if (login?.code.equals("0000")) {
                    AppSessionKey = login?.AppSessionKey!!
                    val intent = Intent(this@MainActivity, main_list::class.java)
                    startActivity(intent)
                }
                else {
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setMessage("["+login?.code + "]" + login?.msg) //response가 null일수도 있어서 '?'추가
                    dialog.show()
                }
            }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    //웹통신 실패시
                    //Log.d("DEBUG",t.message)
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setMessage("서버 연결에 실패했습니다.")
                    dialog.show()
                }
            }
            Toast.makeText(this, "절대좌표변환", Toast.LENGTH_SHORT).show()
            var dialog = AlertDialog.Builder(this@make_char)
            dialog.setMessage("clicked") //response가 null일수도 있어서 '?'추가
            dialog.show()
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

                }
            }
        }
    }
}