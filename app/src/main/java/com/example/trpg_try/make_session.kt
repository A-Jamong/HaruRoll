package com.example.trpg_try

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trpg_try.databinding.ActivityMakeSessionBinding
import com.example.trpg_try.lib.getRealPathFromURI

class make_session : AppCompatActivity() {
    val FLAG_REQ_STORAGE = 100
    var path=""
    private lateinit var makeSessionBinding: ActivityMakeSessionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeSessionBinding = ActivityMakeSessionBinding.inflate(layoutInflater)
        setContentView(makeSessionBinding.root)
        makeSessionBinding.btSessioncard.setOnClickListener {
            checkPermission()
        }}
    private fun checkPermission() {
        val imagePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (imagePermission == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestPermission()
        }
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permission: Array<out String>,
        grantResult: IntArray
    ) {
        when (requestCode) {
            99 -> {
                if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "승인을 거절했습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun openGallery() {
        Toast.makeText(this, "갤러리를 실행합니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, FLAG_REQ_STORAGE)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            99
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                FLAG_REQ_STORAGE -> {
                    val uri = data?.data
                    makeSessionBinding.btSessioncard.setImageURI(uri)
                    makeSessionBinding.btSessioncard.visibility = View.INVISIBLE
                    path = getRealPathFromURI(applicationContext, uri!!)!!
                }
            }
        }
    }
}
