package com.example.zhaojing5.myapplication.View.rollingbarrage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.bean.SimpleRollingBarrageBean;

import java.util.List;

/**
 * created by zhaojing 2020/1/2 下午3:30
 */
public class SimpleRollingBarrageAdapter extends RollingBarrageAdapter<SimpleRollingBarrageBean> {

    public SimpleRollingBarrageAdapter(Context context, List<SimpleRollingBarrageBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position) {
        View rootView = getRootView(R.layout.simple_item);

        if (rootView != null) {

            ImageView imageView = rootView.findViewById(R.id.img);
            TextView contentView = rootView.findViewById(R.id.tv_text);
            String imgUrl = mData.get(position).imgUrl;
            String content = mData.get(position).content;

            Glide.with(rootView.getContext()).asBitmap().load(imgUrl).into(imageView);
            contentView.setText(content);

        }

        return rootView;
    }
}
