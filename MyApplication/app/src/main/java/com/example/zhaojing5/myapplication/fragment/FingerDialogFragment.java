package com.example.zhaojing5.myapplication.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;
import com.example.zhaojing5.myapplication.activity.LoginActivity;

import javax.crypto.Cipher;

public class FingerDialogFragment extends DialogFragment {

    private FingerprintManager fingerprintManager;

    private CancellationSignal mCancellationSignal;

    private Cipher mCipher;

    private LoginActivity mActivity;

    private TextView errorMsg;

    /**
     * 标识是否是用户主动取消的认证。
     */
    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher){
        mCipher = cipher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = getContext().getSystemService(FingerprintManager.class);
            setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Material_Light_Dialog);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fingerprint_dialog,container,false);
        errorMsg = v.findViewById(R.id.error_msg);
        TextView cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
                stopListening();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //start to listen the authentication of finger
        startListening(mCipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop to listen the authentication of finger
        stopListening();
    }

    private void startListening(Cipher mCipher){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            isSelfCancelled = false;
            mCancellationSignal = new CancellationSignal();

            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(mCipher);
            FingerprintManager.AuthenticationCallback callback = new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    if(!isSelfCancelled){
                        errorMsg.setText(errString);
                        if(errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT){
                            ToastUtils.showToast(getActivity(),errString.toString());
                        }
                    }
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    errorMsg.setText(helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    ToastUtils.showToast(getActivity(),"指纹认证成功！");
                    mActivity.onAuthenticated();
                }

                @Override
                public void onAuthenticationFailed() {
                    errorMsg.setText("指纹认证失败，请再试一次！");
                }
            };
            fingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0,callback,null);
        }
    }
    private void stopListening(){
        if(mCancellationSignal != null){
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }
}
