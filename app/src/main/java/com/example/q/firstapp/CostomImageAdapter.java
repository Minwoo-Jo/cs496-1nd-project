package com.example.q.firstapp;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;


import java.net.URI;
import java.util.ArrayList;

public class CostomImageAdapter extends BaseAdapter {
    ArrayList<Uri> imageViewList = new ArrayList<Uri>();
    Context mContext;

    public CostomImageAdapter(Context context) {
        mContext = context;

    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageViewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final int pos = i;
        final Context context =  parent.getContext();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.galleryitem, parent, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(imageViewList.get(i));
      //  imageView.setImageResource(R.drawable.sample);
        return view;
    }
    public void addItem(Uri src){
        imageViewList.add(src);
    }
}
