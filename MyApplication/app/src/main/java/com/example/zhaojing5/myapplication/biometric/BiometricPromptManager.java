package com.example.zhaojing5.myapplication.biometric;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;

/**
 * created by zhaojing 2019/12/25 上午10:07
 */
public class BiometricPromptManager {

    private IBiometricPromptImpl mImpl;
    private Activity mActivity;

    public static BiometricPromptManager from(Activity activity) {
        return new BiometricPromptManager(activity);
    }

    private BiometricPromptManager(Activity activity){
        if (activity == null) {
            throw new RuntimeException("context should not be null");
        }

        mActivity =activity;
        if(isAboveApi28()){
           mImpl = new BiometricPromptApi28(activity);
        }else if(isAboveApi23()){
            mImpl = new BiometricPromptApi23(activity);
        }

    }

    private boolean isAboveApi23(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean isAboveApi28(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public void authenticate(@NonNull OnBiometricIdentifyCallback callback){
        mImpl.authenticate(new CancellationSignal(), callback);
    }

    public void authenticate(@NonNull CancellationSignal cancellationSignal, @NonNull OnBiometricIdentifyCallback callback){
        mImpl.authenticate(cancellationSignal, callback);
    }

    /**
     * Determine if there is at least one fingerprint enrolled.
     *
     * @return true if at least one fingerprint is enrolled, false otherwise
     */
    public boolean hasEnrolledFingerprints() {
        if (isAboveApi28()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            final FingerprintManager manager = mActivity.getSystemService(FingerprintManager.class);
            return manager != null && manager.hasEnrolledFingerprints();
        } else if (isAboveApi23()) {
            return ((BiometricPromptApi23)mImpl).hasEnrolledFingerprints();
        } else {
            return false;
        }
    }

    /**
     * Determine if fingerprint hardware is present and functional.
     *
     * @return true if hardware is present and functional, false otherwise.
     */
    public boolean isHardwareDetected() {
        if (isAboveApi28()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            final FingerprintManager fm = mActivity.getSystemService(FingerprintManager.class);
            return fm != null && fm.isHardwareDetected();
        } else if (isAboveApi23()) {
            return ((BiometricPromptApi23)mImpl).isHardwareDetected();
        } else {
            return false;
        }
    }

    public boolean isKeyguardSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {
            return true;
        }

        return false;
    }

    /**
     * Whether the device support biometric.
     *
     * @return
     */
    public boolean isBiometricPromptEnable() {
        return isAboveApi23()
                && isHardwareDetected()
                && hasEnrolledFingerprints()
                && isKeyguardSecure();
    }

    /**
     * Whether fingerprint identification is turned on in app setting.
     *
     * @return
     */
    public boolean isBiometricSettingEnable() {
        return SPUtils.getBoolean(mActivity, SPUtils.KEY_BIOMETRIC_SWITCH_ENABLE, false);
    }

    /**
     * Set fingerprint identification enable in app setting.
     *
     * @return
     */
    public void setBiometricSettingEnable(boolean enable) {
        SPUtils.put(mActivity, SPUtils.KEY_BIOMETRIC_SWITCH_ENABLE, enable);
    }

    public interface OnBiometricIdentifyCallback {
        void onUsePassword();

        void onSucceeded();

        void onFailed();

        void onError(int code, String reason);

        void onCancel();

    }

}
