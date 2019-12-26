package com.example.zhaojing5.myapplication.biometric;

import android.os.CancellationSignal;
import android.support.annotation.NonNull;

/**
 * created by zhaojing 2019/12/25 上午11:22
 */
interface IBiometricPromptImpl {

    void authenticate(@NonNull CancellationSignal cancel,
                      @NonNull BiometricPromptManager.OnBiometricIdentifyCallback callback);

}
