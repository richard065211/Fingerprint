package com.example.administrator.fingerprint;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Encryption {
    private static KeyStore keystore;
    private KeyGenerator keyGenerator;
    private Cipher defaultcipher;
    private Cipher cipherNotInvalidated;
    private String SECRET_MESSAGE="需要加密的信息";

    Cipher mcipher;
    public  void setCipher(Cipher cipher){
        mcipher=cipher;
    }

    public void getKey(KeyStore keyStore1, KeyGenerator keyGenerator1){
        try {
            keyStore1 = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("获取安卓秘钥库失败", e);
        }
        try {
            keyGenerator1 = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("实例化AES加密算法的秘钥生成器失败", e);
        }
        this.keystore=keyStore1;
        this.keyGenerator=keyGenerator1;
    }

    /*
    * 获取加密对象
    * */
    public void getcipher(Cipher defaultcipher1,Cipher cipherNotInvalidated1){
        try {
            defaultcipher1 = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipherNotInvalidated1 = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("获取加密对象失败", e);
        }
        defaultcipher=defaultcipher1;
        cipherNotInvalidated=cipherNotInvalidated1;
    }
    /*
    * 初始化密文获取系统内部加密对象
    * */
    private static boolean initCipher(Cipher cipher, String keyname){
        try {
            keystore.load(null);
            SecretKey secretKey=(SecretKey)keystore.getKey(keyname,null);
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            return true;

        }catch(KeyPermanentlyInvalidatedException e){
            return false;
        }
        catch (InvalidKeyException | CertificateException | KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | IOException e){
            throw new RuntimeException("初始化加密对象失败", e);
        }
    }
    /*
    * 对需要加密的信息进行加密
    * */
    private void tryEncrypt(Cipher cipher) {
        try {
            byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());//dofinal方法进行单步加密
            showConfirmation(encrypted);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Log.d("加密过程", "无法用生成的秘钥对象对需要加密的信息进行加密" + e.getMessage());
            showConfirmation(null);
        }
    }
    /*
    * 输出加密的密文
    * */
    private void showConfirmation(byte[] encrypted) {
        if (encrypted != null) {
            String encode=Base64.encodeToString(encrypted, 0 /* flags */);
            Log.d("加密密文",encode);
        }
    }
    /*
    * 创建秘钥
    * */
    public void creatkey(String keyname,boolean InvalidatedByBiometricEnrollment) {
        try{
            keystore.load(null);
            KeyGenParameterSpec.Builder builder=new KeyGenParameterSpec.Builder(keyname,KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //对API的版本进行判定是否达到setIncalidateByBiometricEnrollment的标准
                builder.setInvalidatedByBiometricEnrollment(InvalidatedByBiometricEnrollment);
            }
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        }catch(CertificateException | NoSuchAlgorithmException | IOException | InvalidAlgorithmParameterException e){
            throw new RuntimeException(e);
        }
    }

    /*
    * 定义按钮监听事件，对验证进行监听
    * 初始化加密对象、对信息进行加密。
    * */
    public static class onclicklistener implements View.OnClickListener{
        Cipher cipher;
        String keyname ;
        onclicklistener(Cipher cipher,String keyname){
                this.cipher=cipher;
                this.keyname=keyname;
        }
        @Override
        public void onClick(View v) {
            initCipher(cipher,keyname);
        }
    }
 }
