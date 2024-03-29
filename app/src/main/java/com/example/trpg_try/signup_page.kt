package com.example.trpg_try

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trpg_try.api.*
import com.example.trpg_try.databinding.ActivitySignupPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class signup_page : AppCompatActivity() {
    private lateinit var activitySignUpBinding: ActivitySignupPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = ActivitySignupPageBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup_page)
        var B_EmailCheck = false
        var B_idoverlap = false
        activitySignUpBinding.btIdoverlap.setOnClickListener {
            var userid = activitySignUpBinding.signupId.text.toString()
            if(! userid.isBlank()){
                check_IDOverlap.call(userid).enqueue(object : Callback<MSG> {
                    override fun onResponse(
                        call: Call<MSG>,
                        response: Response<MSG>
                    ) {
                        var signup = response.body()
                        if (signup?.code.equals("0000")) {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage(signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                            B_idoverlap = true
                        } else {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage("[" + signup?.code + "]" + signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<MSG>, t: Throwable) {
                        //웹통신 실패시
                        //Log.d("DEBUG",t.message)
                        var dialog = AlertDialog.Builder(this@signup_page)
                        dialog.setMessage("서버 연결에 실패했습니다.")
                        dialog.show()
                    }
                })
            }
        }
        activitySignUpBinding.checkEmail.setOnClickListener {
            var email = activitySignUpBinding.email.text.toString()
            if(! email.isBlank()){
                //send_VerificationEmail(email)
                send_VerificationEmail.call(email).enqueue(object : Callback<MSG> {
                    override fun onResponse(
                        call: Call<MSG>,
                        response: Response<MSG>
                    ) {
                        var signup = response.body()
                        if (signup?.code.equals("0000")) {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage(signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                            B_EmailCheck = true
                        } else {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage("[" + signup?.code + "]" + signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<MSG>, t: Throwable) {
                        //웹통신 실패시
                        //Log.d("DEBUG",t.message)
                        var dialog = AlertDialog.Builder(this@signup_page)
                        dialog.setMessage("서버 연결에 실패했습니다.")
                        dialog.show()
                    }
                })
            }
            var timer = 180
            activitySignUpBinding.countTimer.visibility = View.VISIBLE
            object :CountDownTimer(300000, 1000){
                override fun onTick(p0: Long) {
                    activitySignUpBinding.countTimer.text = timer.toString() + "초"
                    timer --
                }

                override fun onFinish() {
                    activitySignUpBinding.countTimer.visibility = View.INVISIBLE
                }
            }.start()
        }
        activitySignUpBinding.checkAuthentication.setOnClickListener {
            var email = activitySignUpBinding.email.text.toString()
            var I_authentication = activitySignUpBinding.inputAuthentication.text.toString()
            if(! (email.isBlank()||I_authentication.isBlank()) ){
                check_VerificationEmail.call(email,I_authentication).enqueue(object : Callback<MSG> {
                    override fun onResponse(
                        call: Call<MSG>,
                        response: Response<MSG>
                    ) {
                        var signup = response.body()
                        if (signup?.code.equals("0000")) {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage(signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                        } else {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage("[" + signup?.code + "]" + signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<MSG>, t: Throwable) {
                        //웹통신 실패시
                        //Log.d("DEBUG",t.message)
                        var dialog = AlertDialog.Builder(this@signup_page)
                        dialog.setMessage("서버 연결에 실패했습니다.")
                        dialog.show()
                    }
                })
            }
        }

        activitySignUpBinding.signupEnd.setOnClickListener{
            var userid = activitySignUpBinding.signupId.text.toString()
            var nickname = activitySignUpBinding.nickname.text.toString()
            var pw = activitySignUpBinding.signupPassword.text.toString()
            var pw2 = activitySignUpBinding.overlapPassword.text.toString()
            var email = activitySignUpBinding.email.text.toString()
            var B_PwCheck = false
            if (userid.isBlank()){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("아이디를 입력해주세요") //response가 null일수도 있어서 '?'추가
                dialog.show()
            }
            else if (! B_idoverlap){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("아이디 중복여부를 확인해주세요") //response가 null일수도 있어서 '?'추가
                dialog.show()
            }
            else if (pw.isBlank()){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("패스워드를 입력해주세요") //response가 null일수도 있어서 '?'추가
                dialog.show()
            }
            else if (! B_EmailCheck){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("이메일을 인증해주세요") //response가 null일수도 있어서 '?'추가
                dialog.show()
            }
            else {
                //password 확인
                //길이 8이상 20이하, 영어,숫자,특수문자(!@#$%^&*)만 입력 가능(2종 이상 포함), pw==pw2
                val pwPattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,20}$"
                val pwPattern2 = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*])[A-Za-z!@#$%^&*]{8,20}$"
                val pwPattern3 = "^(?=.*[0-9])(?=.*[!@#$%^&*])[[0-9]!@#$%^&*]{8,20}$"
                val pwPattern4 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z[0-9]!@#$%^&*]{8,20}$"
                if (Pattern.matches(pwPattern1, pw) || Pattern.matches(pwPattern2,pw) || Pattern.matches(pwPattern3, pw) || Pattern.matches(pwPattern4, pw)) {
                    if (pw!=pw2){
                        var dialog = AlertDialog.Builder(this@signup_page)
                        dialog.setMessage("비밀번호 확인이 일치하지 않습니다.") //response가 null일수도 있어서 '?'추가
                        dialog.show()
                    }
                    else B_PwCheck = true
                }
                else {
                    var dialog = AlertDialog.Builder(this@signup_page)
                    dialog.setMessage("비밀번호는 8자 이상 20자 이하여야 합니다. 숫자,알파벳,특수문자(!@#$%^&*)중 2가지 이상을 포함해야 합니다.") //response가 null일수도 있어서 '?'추가
                    dialog.show()
                }
            }

            if(B_PwCheck) {

                // password 맞으면 통신 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                send_SignUp.call(userid, email, nickname, pw).enqueue(object : Callback<SignUp> {
                    override fun onResponse(
                        call: Call<SignUp>,
                        response: Response<SignUp>
                    ) {
                        var signup = response.body()
                        if (signup?.code.equals("0000")) {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage(signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                            val intent = Intent(this@signup_page, main_list::class.java)
                            startActivity(intent)

                        } else {
                            var dialog = AlertDialog.Builder(this@signup_page)
                            dialog.setMessage("[" + signup?.code + "]" + signup?.msg) //response가 null일수도 있어서 '?'추가
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<SignUp>, t: Throwable) {
                        //웹통신 실패시
                        //Log.d("DEBUG",t.message)
                        var dialog = AlertDialog.Builder(this@signup_page)
                        dialog.setMessage("서버 연결에 실패했습니다.")
                        dialog.show()
                    }
                })
                //@@@@@@@@@@@@@@@@@@@@@@여기까지 찐 통신부분~~~~~~~~~~@@@@@@@@@@@@@@@@@@@@@@
            }

        }
    }
}

