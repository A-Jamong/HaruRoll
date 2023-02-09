package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_session.*

class main_session : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_session)
        session_select_01.setOnClickListener{
            val intent = Intent(this, roadimage::class.java)
            startActivity(intent)
        }
        session_select_02.setOnClickListener{
            val intent = Intent(this, roadimage::class.java)
            startActivity(intent)
        }
    }
}

