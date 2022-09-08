package com.example.trpg_try

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.roadimage.*
import java.io.InputStream
import java.util.jar.Manifest

class roadimage : AppCompatActivity() {
    private var uri: Uri? =null
    val FLAG_REQ_STORAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roadimage)
        img_bt.setOnClickListener{
            checkPermission()
        }
        button2.setOnClickListener {
            sessionimg.setImageResource(R.drawable.box_rectangular)
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
        Toast.makeText(this, "갤러리를 실행합니다.", Toast.LENGTH_LONG).show()
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
        if(requestCode==RESULT_OK){
            when(requestCode){
                FLAG_REQ_STORAGE->{
                    uri = data?.data
                    sessionimg.setImageURI(uri)
                }
            }
        }else{
        Toast.makeText(this, "?", Toast.LENGTH_SHORT).show()}
    }


}