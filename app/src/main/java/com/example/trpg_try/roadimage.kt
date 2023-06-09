package com.example.trpg_try

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trpg_try.databinding.RoadimageBinding
import com.example.trpg_try.lib.getRealPathFromURI
import com.example.trpg_try.session_create.send_SessionCreate
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class roadimage : AppCompatActivity() {
    private lateinit var roadimageBinding: RoadimageBinding

    //통신을 위한 retrofit 객체
    var retrofit = Retrofit.Builder()
        .baseUrl("https://riul.pythonanywhere.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val FLAG_REQ_STORAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roadimageBinding = RoadimageBinding.inflate(layoutInflater)
        setContentView(roadimageBinding.root)
        roadimageBinding.imgBt.setOnClickListener{
            checkPermission()
        }
    }
    private fun checkPermission(){
        val imagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(imagePermission ==PackageManager.PERMISSION_GRANTED){
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
                if(grantResult[0]==PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else{
                    Toast.makeText(this, "승인을 거절했습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,FLAG_REQ_STORAGE)
        Toast.makeText(this, "갤러리를 실행합니다.", Toast.LENGTH_SHORT).show()

        //intent를 써야해서 여기다 작성했는데... 수정가능합니까?

//        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"
//        chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
//        chooserIntent.putExtra(Intent.EXTRA_TITLE,"사용할 앱을 선택해주세요.")

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
                    roadimageBinding.sessionimg.setImageURI(uri)
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
            //ㅅsend_SessionCreate("session1",body)

            Toast.makeText(this, "절대좌표변환", Toast.LENGTH_SHORT).show()

    }
}
}