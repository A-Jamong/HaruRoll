package com.example.trpg_try

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.example.trpg_try.api.SignUp
import com.example.trpg_try.api.send_SignUp
import kotlinx.android.synthetic.main.activity_signup_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class signup_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        bt_endsignup.setOnClickListener{
            var userid = signup_id.text.toString()
            var pw = signup_pw.text.toString()
            var pw2 = signup_checkpw.text.toString()
            var email = signup_mail.text.toString()
            var B_EmailCheck = true
            var B_PwCheck = false
            if (userid.isBlank()){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("아이디를 입력해주세요") //response가 null일수도 있어서 '?'추가
                dialog.show()
            }
            else if (pw.isBlank()){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("패스워드를 입력해주세요") //response가 null일수도 있어서 '?'추가
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
            if (! B_EmailCheck){
                var dialog = AlertDialog.Builder(this@signup_page)
                dialog.setMessage("이메일을 확인해주세요") //response가 null일수도 있어서 '?'추가
                dialog.show()
            }
            if(B_PwCheck && B_EmailCheck) {

                // password 맞으면 통신
                send_SignUp.call(email, userid, pw).enqueue(object : Callback<SignUp> {
                    override fun onResponse(
                        call: Call<SignUp>,
                        response: Response<SignUp>
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

                    override fun onFailure(call: Call<SignUp>, t: Throwable) {
                        //웹통신 실패시
                        //Log.d("DEBUG",t.message)
                        var dialog = AlertDialog.Builder(this@signup_page)
                        dialog.setMessage("서버 연결에 실패했습니다.")
                        dialog.show()
                    }
                })
            }

        }
    }
}

