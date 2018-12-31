package com.example.q.firstapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tab1Fragment extends Fragment {
    AssetManager assetManager;
    JSONArray jsonArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        tab1(view);
//        Button b = view.findViewById(R.id.newContents);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                View layout = inflater.inflate(R.layout.newcontentsview, (ViewGroup) container.findViewById(R.id.linearLayout));
//                PopupWindow p = new PopupWindow(layout, 1100, 1450, true);
//                p.setAnimationStyle(R.anim.popupanimation);
//                p.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
//            }
//        });
        return view;
    }

    public void tab1(View v) {
//        readSamples();
        ListView listView;
        ListViewAdapter listViewAdapter;
        listViewAdapter = new ListViewAdapter();
        listView = (ListView) v.findViewById(R.id.tab1view);
        listView.setAdapter(listViewAdapter);
        if (MainActivity_rebuild.contentsIds != null) {
            for (String id : MainActivity_rebuild.contentsIds) {
                String name = MainActivity_rebuild.contents.get(id).get("name");
                String phone = MainActivity_rebuild.contents.get(id).get("phone");
                String email = MainActivity_rebuild.contents.get(id).get("email");
                listViewAdapter.addItem(name, phone, email);
            }
        }


    }

    public void readSamples() {
        assetManager = getResources().getAssets();
        try {
            AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream) assetManager.open("sample.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(ais));
            StringBuilder sb = new StringBuilder();
            int bufferSize = 1024 * 1024;
            char readBuf[] = new char[bufferSize];
            int resultSize = 0;
            while ((resultSize = br.read(readBuf)) != -1) {
                if (resultSize == bufferSize) {
                    sb.append(readBuf);
                } else {
                    for (int i = 0; i < resultSize; i++) {
                        //StringBuilder 에 append
                        sb.append(readBuf[i]);
                    }
                }
            }
            String jString = sb.toString();
            //JSONObject 얻어 오기
            jsonArray = new JSONArray(jString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
