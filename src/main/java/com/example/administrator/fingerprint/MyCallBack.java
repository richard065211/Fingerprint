package com.example.administrator.fingerprint;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;

public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
    private static final String TAG="MyCallBack";
    /*
     * 当出现错误的时候回调此函数onNAuthenticationError,多次验证失败后回调失败函数onAuthenticationFailed
     * */
    @Override
    public void onAuthenticationError(int errMsgId,CharSequence errString){
        Log.d(TAG,"onAuthenticationError"+errString);
    }
    @Override
    public void onAuthenticationFailed(){
        Log.d(TAG,"onAuthenticationFailed"+"验证失败");
    }
    @Override
    public void onAuthenticationHelp(int helpMsgId,CharSequence helpString){
        Log.d(TAG,"onAuthenticatonHelp:"+"helpString");
    }

    //当指纹验证成功时回调onAuthenticationSucceeded
    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result){
        Log.d(TAG,"onAuthenticationSucceeded:"+"验证成功");
    }
}