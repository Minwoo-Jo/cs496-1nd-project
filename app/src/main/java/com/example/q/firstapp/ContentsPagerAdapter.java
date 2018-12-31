package com.example.q.firstapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int pageIndex;
    Tab1Fragment tab1Fragment;
    Tab2Fragment tab2Fragment;
    Tab3Fragment tab3Fragment;
    ArrayList<Uri> images;


    public ContentsPagerAdapter(FragmentManager fm, int index) {
        super(fm);


        pageIndex = index;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(tab1Fragment==null){
                    return new Tab1Fragment();
                }
                return tab1Fragment;
            case 1:
                if(tab2Fragment==null){
                    return new Tab2Fragment();
                }
                return tab2Fragment;
            case 2:
                if(tab3Fragment==null){
                    return new Tab3Fragment();
                }
                return tab3Fragment;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageIndex;
    }


    public void onClick(){

    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;

    }


}
