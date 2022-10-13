package com.example.trpg_try

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_signup_page.*

class signup_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)
        bt_idoverlap.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("Title")
                .setMessage("입력하신 아이디는 이미 사용중입니다")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Start 버튼 선택시 수행
                    })
            builder.create()
            builder.show()
        }
    }
}

