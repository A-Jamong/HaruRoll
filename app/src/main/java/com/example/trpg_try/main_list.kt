package com.example.trpg_try

//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trpg_try.character_list.Character
import com.example.trpg_try.character_list.send_CharacterList
import com.example.trpg_try.databinding.MainListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class main_list : AppCompatActivity() {
    private lateinit var mainActivityBinding: MainListBinding
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = MainListBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        val testrecycle: RecyclerView = findViewById(R.id.main_recycler_view)
        val itemlist = ArrayList<Character>()

        val boardAdapter = Char_adapter(itemlist)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        testrecycle.adapter = boardAdapter
        testrecycle.layoutManager = layoutManager
        boardAdapter.notifyDataSetChanged()
        
        mainActivityBinding.btNewchar.setOnClickListener{
            val intent = Intent(this@main_list, make_char::class.java)
            startActivity(intent)
        }

        mainActivityBinding.btNewsession.setOnClickListener{
            val intent = Intent(this@main_list, make_session::class.java)
            startActivity(intent)
        }

        send_CharacterList.call().enqueue(object : Callback<List<Character>> {
            override fun onResponse(call: Call<List<Character>>, response: Response<List<Character>>) {
                var res = response.body()
                if (!res.isNullOrEmpty()) {// 캐릭터 정보가 존재하면
                    println(res!![1].charname) //첫번째 캐릭터사진의 주소 출력
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