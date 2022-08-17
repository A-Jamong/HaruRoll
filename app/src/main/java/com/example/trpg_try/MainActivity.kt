package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_signup.setOnClickListener {
            val intent = Intent(this, signup_page::class.java)

            startActivity(intent)

        }

        //retrofit 객체
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.5:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var LoginService = retrofit.create(LoginService::class.java) //retrofit 객체를 서비스에 얹어줌

        bt_login.setOnClickListener {
            var textID=signin_id.text.toString()
            var textPW=input_pw.text.toString()
            LoginService.requestLogin(textID,textPW).enqueue(object: Callback<Login>{
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    //통신성공
                    var login=response.body()
                    var dialog=AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("통신성공!")
                    dialog.setMessage("code= "+login?.code + ", msg"+login?.msg) //response가 null일수도 있어서 '?'추가
                    dialog.show()
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    //웹통신 실패시
                    //Log.d("DEBUG",t.message)
                    var dialog=AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("통신실패!")
                    dialog.setMessage("통신에 실패했습니다.")
                    dialog.show()
                }
            })
        }

    }
}