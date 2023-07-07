package com.example.trpg_try

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trpg_try.character_create.CharacterCreate_o
import com.example.trpg_try.character_create.send_CharacterCreate
import com.example.trpg_try.character_create.send_CharacterCreate_wImg
import com.example.trpg_try.databinding.ActivityMakeSessionBinding
import com.example.trpg_try.lib.getRealPathFromURI
import com.example.trpg_try.session_create.SessionCreate_o
import com.example.trpg_try.session_create.send_SessionCreate
import com.example.trpg_try.session_create.send_SessionCreate_wImg
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import com.example.trpg_try.resize

class make_session : AppCompatActivity() {
    val FLAG_REQ_STORAGE = 100
    var file : File? = null
    private lateinit var makeSessionBinding: ActivityMakeSessionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeSessionBinding = ActivityMakeSessionBinding.inflate(layoutInflater)
        setContentView(makeSessionBinding.root)
        makeSessionBinding.btSessioncard.setOnClickListener {
            checkPermission()
        }
        makeSessionBinding.finMakeses.setOnClickListener(){
            var sessionname = makeSessionBinding.sessionName.text.toString()
            var Validity_SessionName : Boolean = checkValidation_SessionName(this,sessionname)
            if (Validity_SessionName){
                if (file != null) { //이미지 있음
                    //이미지 자르기 기능 넣어주면 좋을듯 ㅠㅠ

                    var filesmall = resize(file!!,102400)//1MB = 1048576 bytes //이미지 크기 변환

                    //println("filelength:"+filesmall.length())
                    val requestFile = RequestBody.create(MediaType.parse("image/*"), filesmall)
                    val body =MultipartBody.Part.createFormData("sessfig", file!!.name, requestFile)

                    send_SessionCreate_wImg.call(sessionname,body)
                        .enqueue(object : Callback<SessionCreate_o> {
                            override fun onResponse(
                                call: Call<SessionCreate_o>,
                                response: Response<SessionCreate_o>
                            ) {
                                var res = response.body()
                                if (res?.code.equals("0000")) {
                                    var dialog = AlertDialog.Builder(this@make_session)
                                    dialog.setMessage(res?.msg)
                                    dialog.show()
                                    //성공적으로 캐릭터 생성됨. 여기에 이후 동작 제공
                                } else {
                                    var dialog = AlertDialog.Builder(this@make_session)
                                    dialog.setMessage("[" + res?.code + "]" + res?.msg)
                                    dialog.show()
                                }
                            }
                            override fun onFailure(
                                call: Call<SessionCreate_o>,
                                t: Throwable
                            ) {
                                var dialog = AlertDialog.Builder(this@make_session)
                                dialog.setMessage("서버 연결에 실패했습니다.")
                                dialog.show()
                            }
                        })

                } else {//이미지가 없는 경우
                    send_SessionCreate.call(sessionname)
                        .enqueue(object : Callback<SessionCreate_o> {
                            override fun onResponse(
                                call: Call<SessionCreate_o>,
                                response: Response<SessionCreate_o>
                            ) {
                                var res = response.body()
                                if (res?.code.equals("0000")) {
                                    var dialog = AlertDialog.Builder(this@make_session)
                                    dialog.setMessage(res?.msg)
                                    dialog.show()
                                    //성공적으로 캐릭터 생성됨. 여기에 이후 동작 제공
                                } else {
                                    var dialog = AlertDialog.Builder(this@make_session)
                                    dialog.setMessage("[" + res?.code + "]" + res?.msg)
                                    dialog.show()
                                }
                            }
                            override fun onFailure(
                                call: Call<SessionCreate_o>,
                                t: Throwable
                            ) {
                                var dialog = AlertDialog.Builder(this@make_session)
                                dialog.setMessage("서버 연결에 실패했습니다.")
                                dialog.show()
                            }
                        })

                }
            }
            }

    }
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
                    file = getFileFromUri(applicationContext, uri!!)
                    makeSessionBinding.btSessioncard.setImageURI(uri)
                }
            }
        }
    }
}
fun checkValidation_SessionName(context: AppCompatActivity,sessionname:String): Boolean{
    if (sessionname.length in 1..20 ) {
        return true
    }
    else {
        Toast.makeText(context, "캐릭터 이름은 1자 이상 25자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
        return false
    }
}

