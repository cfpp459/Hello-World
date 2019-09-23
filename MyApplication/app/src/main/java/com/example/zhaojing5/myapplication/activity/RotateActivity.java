package com.example.zhaojing5.myapplication.activity;

import android.app.Activity;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.Rotate3dAnimation;
import com.example.zhaojing5.myapplication.adapter.PictureAdapter;
import com.example.zhaojing5.myapplication.bean.Picture;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RotateActivity extends Activity {

    public static final String TAG = RotateActivity.class.getSimpleName();

    @BindView(R.id.layout)
    public RelativeLayout layout;

    @BindView(R.id.pic_list_view)
    public ListView pic_list_view;

    @BindView(R.id.picture)
    public ImageView picture;

    private PictureAdapter adapter;

    private List<Picture> picList = new ArrayList<Picture>();

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rotate_image_layout);
        unbinder = ButterKnife.bind(this);
        initPic();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initPic(){
        Picture cat1 = new Picture("cat1",R.drawable.cat1);
        Picture cat2 = new Picture("cat2",R.drawable.cat2);
        Picture cat3 = new Picture("cat3",R.drawable.cat3);
        Picture cat4 = new Picture("cat4",R.drawable.cat4);
        Picture cat5 = new Picture("cat5",R.drawable.cat5);
        picList.add(cat1);
        picList.add(cat2);
        picList.add(cat3);
        picList.add(cat4);
        picList.add(cat5);
    }
    private void initView(){
        adapter = new PictureAdapter(this,0,picList);
        pic_list_view.setAdapter(adapter);
        pic_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                picture.setImageResource(picList.get(position).getResource());

                float centerX = layout.getWidth() / 2f;
                float centerY = layout.getHeight() / 2f;

                final Rotate3dAnimation  rotation = new Rotate3dAnimation(0,90,
                        centerX,centerY,310.0f,true);
                rotation.setDuration(500);
                rotation.setInterpolator(new AccelerateInterpolator());
                rotation.setAnimationListener(new TurnToImageView());
                layout.startAnimation(rotation);
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float centerX = layout.getWidth() / 2f;
                float centerY = layout.getHeight() / 2f;

                final Rotate3dAnimation rotation = new Rotate3dAnimation(360,270,
                        centerX,centerY,310.0f,true);
                rotation.setDuration(500);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new AccelerateInterpolator());
                rotation.setAnimationListener(new TurnToListView());
                layout.startAnimation(rotation);
            }
        });
    }

    public class TurnToListView implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            float centerX = layout.getWidth() / 2f;
            float centerY = layout.getHeight() / 2f;

            pic_list_view.setVisibility(View.VISIBLE);
            pic_list_view.requestFocus();
            picture.setVisibility(View.GONE);
            final Rotate3dAnimation rotation = new Rotate3dAnimation(90,0,
                    centerX,centerY,310.0f,false);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            layout.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    public class TurnToImageView implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
            Log.i(TAG,"TurnToImageView animation --start--.");
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            Log.i(TAG,"TurnToImageView animation --end--.");

            float centerX = layout.getWidth() / 2f;
            float centerY = layout.getHeight() / 2f;

            pic_list_view.setVisibility(View.GONE);
            picture.setVisibility(View.VISIBLE);
            picture.requestFocus();
            final Rotate3dAnimation rotation = new Rotate3dAnimation(270,360,
                    centerX,centerY,310.0f,false);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            layout.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    private void testCamera(){
        Camera camera = new Camera();
//        camera.rotate();
    }
}
