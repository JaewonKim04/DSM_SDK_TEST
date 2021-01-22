package com.example.dsm_sdk_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.semicolon.dsm_sdk_v1.DTOtoken
import com.semicolon.dsm_sdk_v1.DTOuser
import com.semicolon.dsm_sdk_v1.DsmSdk

class MainActivity : AppCompatActivity() {
    val instance=DsmSdk.instance  //DsmSdk.instance 로 메서드를 많이 호출하기 떄문에 프로퍼티로 만들어 놓음
    var accessToken=""
    var refreshToken=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        * instance.initSDK("client Id","client Password","redirect url")
        * sdk 를 사용하기전 맨 처음에 실행해야하는 메서드
         */
        instance.initSDK("qwer","qwer","http://www.google.com")

        val tokenCallback:(DTOtoken?, Throwable?)->Unit={ token, error-> //토큰을 받아왔을때의 콜백함수
            if(error!=null){
                Log.e("token",error.toString())
            }
            else if(token!=null){
                accessToken=token.access_token
                refreshToken=token.refresh_token
                Log.d("access_token",accessToken)
                Log.d("refresh_token",refreshToken)
            }
        }

        val loginCallback:(DTOuser?)->Unit={  //사용자 정보를 받아왔을때의 콜백함수
            if(it!=null){
                Log.d("user_name",it.name)//이름
                Log.d("user_email",it.email)//이메일
                Log.d("user_gcn",it.gcn)//학번(4자리)
            }
        }
        /*
        * __instance.loginWithAuth()__ 에서 instance 는 초기화할때 사용한 instance 와 동일한 것 이어야함
        * instance.loginWithAuth(context, 토큰을 받아왔을떄 실행할 콜백 메서드, 사용자 정보를 받아왔을때 실행할 콜백 메서드)
        */
        instance.loginWithAuth(this,tokenCallback,loginCallback)

        /*
        *이부분에서 getUser()&getToken 메서드를 호출하면 null 값이 나오게 됩니다(아직 accessToken 을 받기 전이기 때문)
         */

    }
    fun getUser(accessToken:String){
        val userInfoCallback:(DTOuser?)->Unit={//사용자 정보를 받아왔을때 실행될 메서드
            if (it != null) {
                Log.d("사용자",it.name)//이름
                Log.d("사용자",it.email)//이메일
                Log.d("사용자",it.gcn)//학번
            }
        }
        instance.getUserInformation(accessToken,userInfoCallback)
    }
    fun getToken(refreshToken:String){
        val changeTokenCallback:(accessToken:String)->Unit={//토큰을 받아왔을때 실행될 메서드
            Log.d("토큰",it)//accessToken
        }
        instance.refreshToken(refreshToken,changeTokenCallback)
    }
}