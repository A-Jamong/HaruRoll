package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trpg_try.character_list.Character
import com.example.trpg_try.character_list.send_CharacterList
import kotlinx.android.synthetic.main.main_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class main_list : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list)
//        var dialog = AlertDialog.Builder(this@main_list)
//        dialog.setMessage("dddddd") //response가 null일수도 있어서 '?'추가
//        dialog.show()

        bt_newchar.setOnClickListener{
            val intent = Intent(this@main_list, make_char::class.java)
            startActivity(intent)
        }

        bt_newsession.setOnClickListener{
            val intent = Intent(this@main_list, make_session::class.java)
            startActivity(intent)
        }

        send_CharacterList.call().enqueue(object : Callback<List<Character>> {
            override fun onResponse(call: Call<List<Character>>, response: Response<List<Character>>) {
                var res = response.body()
                if (!res.isNullOrEmpty()) {//
                    println(res!![9].charfigure)
                }
                    var dialog = AlertDialog.Builder(this@main_list)
                    dialog.setMessage("done") //response가 null일수도 있어서 '?'추가
                    dialog.show()
                //}
//                else {
//                    var dialog = AlertDialog.Builder(this@main_list)
//                    dialog.setMessage("["+res?.code + "]" + res?.msg) //response가 null일수도 있어서 '?'추가
//                    dialog.show()
//                }
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                //웹통신 실패시
                var dialog = AlertDialog.Builder(this@main_list)
                dialog.setMessage("서버 연결에 실패했습니다.")
                dialog.show()
            }
        })
    }
}