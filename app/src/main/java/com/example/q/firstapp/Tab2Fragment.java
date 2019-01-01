package com.example.q.firstapp;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class Tab2Fragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2,container,false);
        tab2(view);
        return view;
    }
    public void tab2(View view) {
        ArrayList<Uri> images = MainActivity_rebuild.images;
        GridView gridView;
        CostomImageAdapter imageAdapter;
        imageAdapter = new CostomImageAdapter(view.getContext());
        gridView = (GridView) view.findViewById(R.id.tab2view);
        gridView.setAdapter(imageAdapter);
        for (int i = 0; i < images.size(); i++) {
            imageAdapter.addItem(images.get(i));
        }
    }
}
