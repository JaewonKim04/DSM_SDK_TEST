package com.example.dsm_sdk_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dsm_sdk_v1.DTOtoken
import com.example.dsm_sdk_v1.DTOuser
import com.example.dsm_sdk_v1.DsmSdk

class MainActivity : AppCompatActivity() {
    val instance=DsmSdk.instance
    var accessToken=""
    var refreshToken=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        instance.initSDK("qwer","qwer","http://www.google.com")

        val callback:(DTOtoken?,Throwable?)->Unit={token,error->                                      //토큰을 받아왔을때의 콜백함수
            if(error!=null){
                Toast.makeText(this,"오류가 발생했습니다",Toast.LENGTH_SHORT).show()
            }
            else if(token!=null){
                accessToken=token.access_token
                refreshToken=token.refresh_token
                Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
            }
        }

        val loginCallBack:(DTOuser?)->Unit={                                            //사용자 정보를 받아왔을때의 콜백함수
            val name=it?.name                //이름
            val email=it?.email              //이메일
            val gcn=it?.gcn                  //학번
            if (name != null) {
                Log.d("사용자",name)
            }
            if(it!=null){
                Toast.makeText(this,"$name,$email,$gcn",Toast.LENGTH_SHORT).show()
                getUser()
                getToken()
            }
            else{
                Toast.makeText(this,"null",Toast.LENGTH_SHORT).show()
            }
        }
        instance.loginWithAuth(this,callback,loginCallBack)


    }
    fun getUser(){
        val checkToken:(DTOuser?)->Unit={
            Log.d("사용자","start")
            if (it != null) {
                Log.d("사용자",it.name)
                Log.d("사용자",it.email)
                Log.d("사용자",it.gcn)
            }
        }
        instance.getUserInformation(accessToken,checkToken)
    }
    fun getToken(){
        val changeToken:(accessToken:String)->Unit={
            Log.d("토큰",it)
        }
        instance.refreshToken(refreshToken,changeToken)
    }
}