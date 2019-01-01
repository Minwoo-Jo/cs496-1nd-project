package com.example.q.firstapp;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;



import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics dn = context.getResources().getDisplayMetrics();
                int width = dn.widthPixels;
                detail(context,parent,imageViewList.get(i));

            }
        });
        return imageView;
    }
    public void addItem(Uri src){
        imageViewList.add(src);
    }

    public void detail(Context context, ViewGroup parent, Uri uri) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.imagedetail, parent.findViewById(R.id.imagedetail));
        ImageView detail = (ImageView) layout.findViewById(R.id.imageView);
        detail.setImageURI(uri);
        detail.setScaleType(ImageView.ScaleType.FIT_XY);
        PhotoViewAttacher pv = new PhotoViewAttacher(detail);
        PopupWindow p = new PopupWindow(layout, 960, 1000, true);
        p.setAnimationStyle(R.anim.popupanimation);
        p.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }

}
