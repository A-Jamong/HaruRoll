package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_list.*

class main_list : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list)

        bt_newchar.setOnClickListener{
            val intent = Intent(this@main_list, make_char::class.java)
            startActivity(intent)
        }
    }
}