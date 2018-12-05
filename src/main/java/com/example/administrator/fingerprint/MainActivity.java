package com.example.administrator.fingerprint;

import android.annotation.SuppressLint;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {

    public CancellationSignal cancellationSignal;
    private Button check,cancelCheck;
    private FingerprintManagerCompat manager ;
    private Cipher mcipher;  //定义加密对象
    private String keyname;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        check = (Button) findViewById(R.id.check);
        cancelCheck=(Button)findViewById(R.id.cancelCheck);
        manager = FingerprintManagerCompat.from(this); //获取一个FingerprintmanagerCompat实例,用于调用指纹传感器
        check.setOnClickListener(new Encryption.onclicklistener(mcipher,keyname) {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"请验证指纹",Toast.LENGTH_SHORT).show();
                StartListening(mcipher);
            }
        });
        cancelCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelListening();
            }
        });
    }
   /*
   * 取消监听指纹传感器
   * */
    private void cancelListening() {
        if(cancellationSignal!=null){
            cancellationSignal.cancel();
            cancellationSignal=null;
        }
    }
    /*
    * 开始监听指纹传感器
    * */
    private void StartListening(Cipher mcipher) {
        cancellationSignal=new CancellationSignal();
        manager.authenticate(new FingerprintManagerCompat.CryptoObject(mcipher),0,cancellationSignal,new MyCallBack(),null);
    }
}
