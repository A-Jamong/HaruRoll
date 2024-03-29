package com.example.trpg_try

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
//import android.support.v4.app.ActivityCompat
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trpg_try.character_create.CharacterCreate_o
import com.example.trpg_try.character_create.send_CharacterCreate
import com.example.trpg_try.character_create.send_CharacterCreate_wImg
import com.example.trpg_try.lib.getRealPathFromURI
//import id.zelory.compressor.Compressor
//import id.zelory.compressor.constraint.format
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.util.Log
import com.example.trpg_try.databinding.ActivityMakeCharBinding
import java.io.FileOutputStream
//import id.zelory.compressor.constraint.size
import java.lang.String.format

//suspend fun compressfile(context: android.content.Context ,path: String):File{
    //file의 크기가 size보다 크다면 size로 줄여서 리턴

    //val file = File(path)
    //val compressedImageFile = Compressor.compress(context,file) {
  //      format(Bitmap.CompressFormat.JPEG)
   //     size(2_097_152) // 2 MB = 2_097_152
//    }
//    return compressedImageFile
//}
class make_char : AppCompatActivity() {
    val FLAG_REQ_STORAGE = 100
    var file : File? = null
    private lateinit var makeCharBinding: ActivityMakeCharBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeCharBinding = ActivityMakeCharBinding.inflate(layoutInflater)
        setContentView(makeCharBinding.root)
        makeCharBinding.makeCharimg.setOnClickListener{
            checkPermission()
        }
        makeCharBinding.finMakechar.setOnClickListener {
            println("ddcc")
            var charactername = makeCharBinding.makeCharname.text.toString()
            var characterbio = makeCharBinding.editWord.text.toString()
            if (charactername.length in 1..20 ) {
                if (characterbio.length in 0..30) {
                    if (file!=null) {
                        println("hered")
                        //이미지 자르기 기능 넣어주면 좋을듯 ㅠㅠ

                        println("heredeee")
                        var filesmall = resize(file!!,102400)//100kB = 1048576 bytes //이미지 크기 변환
                        //println("filelength:"+filesmall.length())
                        val requestFile = RequestBody.create(MediaType.parse("image/*"), filesmall)
                        val body =MultipartBody.Part.createFormData("character", file!!.name, requestFile)
                        //Toast.makeText(this, "이미지잇음!!!ㅅ.", Toast.LENGTH_SHORT).show()

                        send_CharacterCreate_wImg.call(charactername, characterbio, body)
                            .enqueue(object : Callback<CharacterCreate_o> {
                                override fun onResponse(
                                    call: Call<CharacterCreate_o>,
                                    response: Response<CharacterCreate_o>
                                ) {
                                    var res = response.body()
                                    if (res?.code.equals("0000")) {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage(res?.msg)
                                        dialog.show()
                                        //성공적으로 캐릭터 생성됨. 여기에 이후 동작 제공
                                    } else {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage("[" + res?.code + "]" + res?.msg)
                                        dialog.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CharacterCreate_o>,
                                    t: Throwable
                                ) {
                                    var dialog = AlertDialog.Builder(this@make_char)
                                    dialog.setMessage("서버 연결에 실패했습니다.")
                                    dialog.show()
                                }
                            })
                    } else {//이미지가 없는 경우
                        send_CharacterCreate.call(charactername, characterbio)
                            .enqueue(object : Callback<CharacterCreate_o> {
                                override fun onResponse(
                                    call: Call<CharacterCreate_o>,
                                    response: Response<CharacterCreate_o>
                                ) {
                                    var res = response.body()
                                    if (res?.code.equals("0000")) {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage(res?.msg)
                                        dialog.show()
                                        //성공적으로 캐릭터 생성됨. 여기에 이후 동작 제공
                                    } else {
                                        var dialog = AlertDialog.Builder(this@make_char)
                                        dialog.setMessage("[" + res?.code + "]" + res?.msg)
                                        dialog.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CharacterCreate_o>,
                                    t: Throwable
                                ) {
                                    var dialog = AlertDialog.Builder(this@make_char)
                                    dialog.setMessage("서버 연결에 실패했습니다.")
                                    dialog.show()
                                }
                            })

                    }
                }
                else {
                    Toast.makeText(this, "캐릭터 한마디는 30자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "캐릭터 이름은 1자 이상 25자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission(){
        val imagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(imagePermission == PackageManager.PERMISSION_GRANTED){
            openGallery()
        }else{
            requestPermission()
        }
    }
    fun onRequestPermissionResult(
        requestCode: Int,
        permission: Array<out String>,
        grantResult: IntArray
    ){
        when(requestCode){
            99->{
                if(grantResult[0]== PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else{
                    Toast.makeText(this, "승인을 거절했습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun openGallery(){
        Toast.makeText(this, "갤러리를 실행합니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,FLAG_REQ_STORAGE)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 99)
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if(resultCode==RESULT_OK&&data!=null){
            when(requestCode){
                FLAG_REQ_STORAGE->{
                    val uri = data?.data
                    file = getFileFromUri(applicationContext, uri!!)
                    makeCharBinding.makeCharimg.setImageURI(uri)
                }
            }
        }
    }
}
fun getFileFromUri(context: Context, uri: Uri):File?{
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "img.jpg")
    tempFile.createNewFile()
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}
fun resize(file: File, size: Int): File{
    var quality = 100
    var imgsize =  file.length()
    var inputstream:InputStream? = null
    var buffsize = Integer.MAX_VALUE
    //var filesmall= File(imgPath)
    val bitmap_org = BitmapFactory.decodeFile(file.absolutePath)

    // 정사각형으로 자르기
    val w = bitmap_org.width
    val h = bitmap_org.height
    var len = 0
    if (w>h) {len=h}
    else {len=w}
    val bitmap = Bitmap.createBitmap(bitmap_org,0,0,len,len)
    // 자르기 끝

    val byteArrayOutputStream = ByteArrayOutputStream()
    //if (imgsize > size) {//1MB = 1048576 bytes
        var filesmall: File = createTempFile()!!
        try{
            do{
                if(bitmap != null){
                    byteArrayOutputStream.reset()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
                    buffsize = byteArrayOutputStream.size()
                    quality -=10
                    //println("buffsize:" + buffsize)
                }
            } while (buffsize > size)

        } catch(e:Exception){
            Log.d( "Except-compressing:","${e.message}" )
        }
    inputstream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    byteArrayOutputStream.close()
    // input stream -> File
    inputstream.use{input -> filesmall.outputStream().use{output->input.copyTo(output)} }
    return filesmall
}

