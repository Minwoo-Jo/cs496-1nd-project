package com.example.q.firstapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        LinearLayout l = view.findViewById(R.id.tab1layout);
        Button b = view.findViewById(R.id.button5);
        return view;
    }


    public void tab1(View v) {
//        readSamples();
        ListView listView;
        ListViewAdapter listViewAdapter;
        listViewAdapter = new ListViewAdapter();
        listView = (ListView) v.findViewById(R.id.tab1view);
        listView.setAdapter(listViewAdapter);
        if(MainActivity_rebuild.contentsIds!=null) {
            for (String id : MainActivity_rebuild.contentsIds) {
                String name = MainActivity_rebuild.contents.get(id).get("name");
                String phone = MainActivity_rebuild.contents.get(id).get("phone");
                String email = MainActivity_rebuild.contents.get(id).get("email");
                listViewAdapter.addItem(name, phone, email);
            }
        }
       //        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                String name = ((JSONObject) (jsonArray.get(i))).get("name").toString();
//                String phone = ((JSONObject) (jsonArray.get(i))).get("phone").toString();
//                String gender = ((JSONObject) (jsonArray.get(i))).get("gender").toString();
//                String email = ((JSONObject) (jsonArray.get(i))).get("email").toString();
//                listViewAdapter.addItem(name, phone, gender, email);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
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
