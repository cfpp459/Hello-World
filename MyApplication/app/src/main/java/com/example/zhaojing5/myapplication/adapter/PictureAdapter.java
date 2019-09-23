package com.example.zhaojing5.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zhaojing5.myapplication.bean.Picture;

import java.util.List;

public class PictureAdapter extends ArrayAdapter<Picture> {

    public PictureAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Picture picture = getItem(position);
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,null);
        }else{
            view = convertView;
        }
        TextView picName = view.findViewById(android.R.id.text1);
        picName.setText(picture.getName());
        return view;
    }

}

