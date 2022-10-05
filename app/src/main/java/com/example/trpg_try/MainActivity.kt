package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.trpg_try.api.Login
import com.example.trpg_try.api.send_Login
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
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
        bt_login.setOnClickListener {
//            var textID = signin_id.text.toString()
//            var textPW = input_pw.text.toString()
//            var respond = send_Login(textID, textPW)
//
//            if (respond.equals("0000")) {
//                //다음페이지로 이동
//                Log.d("Send_login: ","success")
//                val intent = Intent(this@MainActivity, main_session::class.java)
//                startActivity(intent)
//            }
            var textID = signin_id.text.toString()
            var textPW = input_pw.text.toString()
            send_Login.call(textID, textPW).enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    //통신성공
                    var login = response.body()
                    var sessionID = login?.sessionID
                    //통신 성공했을 때 화면 넘어가게
                    if (login?.code.equals("0000")) {
                        val intent = Intent(this@MainActivity, main_session::class.java)
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
            )
        }

    }
}