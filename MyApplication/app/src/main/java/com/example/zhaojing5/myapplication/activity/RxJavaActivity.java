package com.example.zhaojing5.myapplication.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;


/**
 * created by zhaojing 2019/11/5 下午2:04
 */
public class RxJavaActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        mImageView = findViewById(R.id.img);
    }

    @Override
    protected void onResume() {
        super.onResume();

        printing();
        obtainPic();
    }

    private void test() {

        //观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.d("zhaojing", "onCompleted..");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("zhaojing", "onError...");
            }

            @Override
            public void onNext(String s) {
                Log.d("zhaojing", "onNext...");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                Log.d("zhaojing", "onStart...");
            }

            @Override
            public void onNext(String s) {
                Log.d("zhaojing", "onNext...");
            }

            @Override
            public void onCompleted() {
                Log.d("zhaojing", "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("zhaojing", "Error!");
            }
        };

        //被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }

        });

        Observable<String> observable1 = Observable.just("Hello", "Hi", "Aloha");

        String[] words = {"Hello","Hi","Aloha"};
        Observable observable2 = Observable.from(words);

        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();

        //订阅
        observable.subscribe(observer);
        observable1.subscribe(subscriber);

    }

    private void printing(){
        String[] names = new String[]{"name1","name2","name3"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("zhaojing","name is " + s);
                    }
                });

    }

    private void obtainPic(){
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(R.drawable.cat1);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {

            @Override
            public void onCompleted() {
                Log.d("zhaojing", "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("zhaojing", "Error!");
                ToastUtils.showToast(RxJavaActivity.this, e.toString());
            }

            @Override
            public void onNext(Drawable drawable) {
                Log.d("zhaojing", "onNext!");
                mImageView.setImageDrawable(drawable);
            }
        });
    }




}
