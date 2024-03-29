package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trpg_try.api.AppSessionKey
import com.example.trpg_try.api.Login
import com.example.trpg_try.api.send_Login
import com.example.trpg_try.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.btSignup.setOnClickListener {
            val intent = Intent(this, signup_page::class.java)
            startActivity(intent)
        }
        mainBinding.btLogin.setOnClickListener {
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
            var textID = mainBinding.signinId.text.toString()
            var textPW = mainBinding.inputPw.text.toString()
            send_Login.call(textID, textPW).enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    //통신성공
                    var login = response.body()
                    //통신 성공했을 때 화면 넘어가게
                    if (login?.code.equals("0000")) {
                        AppSessionKey = login?.AppSessionKey!!
                        val intent = Intent(this@MainActivity, main_list::class.java)
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