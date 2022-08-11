package com.example.trpg_try
// 아웃풋 정의 (django의 json 리턴값과 동일한 변수여야 함)
data class Login (
    var code: String,
    var msg: String
)