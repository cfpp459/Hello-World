package com.example.zhaojing5.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.zhaojing5.myapplication.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FingerUnlockActivity extends Activity {

    public static final String TAG = FingerUnlockActivity.class.getSimpleName();

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_dialog);
        unbinder = ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
