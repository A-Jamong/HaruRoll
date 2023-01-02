package com.example.trpg_try

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.trpg_try.lib.getRealPathFromURI
import com.example.trpg_try.session_create.send_SessionCreate
import kotlinx.android.synthetic.main.activity_make_char.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class make_char : AppCompatActivity() {
        //통신을 위한 retrofit 객체
        var retrofit = Retrofit.Builder()
            .baseUrl("https://riul.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val FLAG_REQ_STORAGE = 100

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_make_char)
            make_charprofile.setOnClickListener{
                checkPermission()
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
            if(resultCode==RESULT_OK){
                when(requestCode){
                    FLAG_REQ_STORAGE->{
                        val uri = data?.data
                        make_charprofile.visibility = View.INVISIBLE
                        char_img.setImageURI(uri)
                    }
                }
                // 저장 버튼 누르면 서버로 전송하게끔 하면 좋을듯!!
                val uri = data?.data
                val path = getRealPathFromURI(applicationContext, uri!!)
                val file = File(path)
                //println(file)
                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("character", file.name, requestFile)

                Log.d("file to multipart",file.name)
                send_SessionCreate("session1",body)

                Toast.makeText(this, "절대좌표변환", Toast.LENGTH_SHORT).show()
            }
    }
}