package com.example.zhaojing5.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ThreeDBoxActivity extends Activity {

    public static final String TAG = ThreeDBoxActivity.class.getSimpleName();

    private Unbinder unbinder;

    /*@BindView(R.id.img1)
    public ImageView img1;*/

    /*@BindView(R.id.btn_distribute)
    public Button btn_distribute;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.three_d_rotate_layout);
//        setContentView(R.layout.distribute_layout);
//        setContentView(R.layout.my3d_layout);
//        setContentView(R.layout.power_image_layout);
        unbinder = ButterKnife.bind(this);
//        initDistributeFunc();
    }

    /*private void initDistributeFunc(){
        btn_distribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(ThreeDBoxActivity.this,"click image1!");
                Log.i(TAG,"click imag1!");
            }
        });
    }*/

    /*private void init(){
        img1.setEnabled(true);
        img1.setClickable(true);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(ThreeDBoxActivity.this,"click image1!");
                Log.i(TAG,"click imag1!");
            }
        });
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
