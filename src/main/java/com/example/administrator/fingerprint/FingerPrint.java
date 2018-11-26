package com.example.administrator.fingerprint;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FingerPrint extends AppCompatActivity {

    private  static final String TAG="MainActivity";
    private Button check;
    private FingerprintManagerCompat manager;
    private  Handler mHandler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        check = (Button) findViewById(R.id.check);
        manager = FingerprintManagerCompat.from(this); //获取一个FingerprintmanagerCompat实例
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.authenticate(null, 0, null, new MyCallBack(), null);
            }
        });
        //定义handler重启指纹模块
        Handler handler =new Handler(){
          @Override
          public void handleMessage(Message msg){
              super.handleMessage(msg);
              Log.d(TAG,"handleMessage:重启指纹模块");
              manager.authenticate(null,0,null,new MyCallBack(),null);
          }
        };
    }

}
