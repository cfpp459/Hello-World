package com.example.zhaojing5.myapplication.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;
import com.example.zhaojing5.myapplication.fragment.FingerDialogFragment;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class LoginActivity extends Activity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final String DEFAULT_KEY_NAME = "default_key";
    KeyStore keyStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        if(supportFingerPrint()){
            initKey();
            initCipher();
        }
    }

    public boolean supportFingerPrint(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            //官方Android 6.0及以上才支持指纹功能
            ToastUtils.showToast(this,"您的系统版本过低，不支持指纹功能！");
            return false;
        }else{
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
            if(!fingerprintManager.isHardwareDetected()){
                ToastUtils.showToast(this,"您的手机不支持指纹功能");
                return false;
            }else if(!keyguardManager.isKeyguardSecure()){
                //如果没设置密码，指纹又没通过，那么就没有办法打开手机了，所以指纹认证的前提是要设置锁屏密码
                ToastUtils.showToast(this,"您还未设置锁屏，请先设置并添加一个指纹！");
                return false;
            }else if(!fingerprintManager.hasEnrolledFingerprints()){
                ToastUtils.showToast(this,"您至少需要在系统设置中添加一个指纹！");
                return false;
            }
        }
        return true;
    }

    /***
     * 初始化生成对称加密的key
     */
    @TargetApi(23)
    private void initKey(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /***
     * 初始化生成一个cipher对象
     */
    @TargetApi(23)
    private void initCipher(){
        try {
            SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME,null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"+ KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            showFingerPrintDialog(cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showFingerPrintDialog(Cipher cipher){
        FingerDialogFragment fragment = new FingerDialogFragment();
        fragment.setCipher(cipher);
        fragment.show(getFragmentManager(),"fingerprint");
    }

    public void onAuthenticated(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
