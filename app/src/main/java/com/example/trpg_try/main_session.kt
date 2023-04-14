package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.trpg_try.databinding.MainSessionBinding

class main_session : AppCompatActivity() {
    private lateinit var mainSessionBinding: MainSessionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainSessionBinding = MainSessionBinding.inflate(layoutInflater)
        setContentView(mainSessionBinding.root)
        mainSessionBinding.sessionSelect01.setOnClickListener{
            val intent = Intent(this, roadimage::class.java)
            startActivity(intent)
        }
        mainSessionBinding.sessionSelect02.setOnClickListener{
            val intent = Intent(this, roadimage::class.java)
            startActivity(intent)
        }
    }
}

