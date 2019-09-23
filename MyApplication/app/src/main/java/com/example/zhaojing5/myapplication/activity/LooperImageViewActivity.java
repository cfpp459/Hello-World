package com.example.zhaojing5.myapplication.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.View.LooperImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaojing 2019/7/22 上午10:56
 */
public class LooperImageViewActivity extends AppCompatActivity {

    List<String> mTipList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.looperimageview_layout);

        LooperImageView looperImageView = findViewById(R.id.albumLooper);
        wrapperTipStrings();
        looperImageView.setImgTipList(mTipList);
    }

    private void wrapperTipStrings(){
        mTipList = new ArrayList<>();
        mTipList.add("tip1");
        mTipList.add("tip2");
        mTipList.add("tip3");
    }

    private List<String> localPic = new ArrayList<String>();
    public List<String> getLocalAlbumPhoto() {
        if(localPic != null){
            localPic.clear();
        }

        String name = "";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/messageBoard/photoImgs";
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for(int i=0;i<files.length;i++){
                    name = files[i].getAbsolutePath();
                    localPic.add(name);
                }
            }
        }
        return localPic;
    }

}
