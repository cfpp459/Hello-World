package com.example.zhaojing5.myapplication.annotation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zhaojing5.myapplication.R;

import butterknife.OnClick;

/**
 * created by zhaojing 2020/3/12 下午2:40
 */
public class TextAnnotationActivity extends AppCompatActivity {
    public static final String TAG = TextAnnotationActivity.class.getSimpleName();

    @InjectView(R.id.bind_view_btn)
    Button mBindView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_anntation);
        init();
        mBindView.setText("绑定Success");
    }

    private void init(){
        AnnotationUtil.injectView(this);
        AnnotationUtil.injectEvent(this);
    }

    @onClick(R.id.bind_click_btn)
    public void onClick(View view){

        if (view != null) {
            Log.i(TAG, "click the bind button...");
            mBindView.setText("点击Success");
        }

    }




}
