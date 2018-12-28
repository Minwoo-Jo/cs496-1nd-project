package com.example.q.firstapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Pair;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;


public class MainActivity extends AppCompatActivity {

    TabHost tabHost;
    TabHost.TabSpec ts1;
    TabHost.TabSpec ts2;
    TabHost.TabSpec ts3;
    AssetManager assetManager;
    JSONArray jsonArray;
    ArrayList<Uri> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                getImage();
                tab1();
                tab2();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [App Permissions]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();


        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        ts1 = tabHost.newTabSpec("Tab1");
        ts1.setContent(R.id.listView1);
        ts1.setIndicator("연락처");
        tabHost.addTab(ts1);

        ts2 = tabHost.newTabSpec("Tab2");
        ts2.setContent(R.id.gridview);
        ts2.setIndicator("갤러리");
        tabHost.addTab(ts2);

        ts3 = tabHost.newTabSpec("Tab3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("TAB 3");
        tabHost.addTab(ts3);

    }


    public void getImage() {

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함
        ArrayList<Uri> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {

        } else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                Uri imageUri = Uri.parse(filePath);
                result.add(imageUri);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();

        images = result;
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

    public void tab1() {
        readSamples();
        ListView listView;
        ListViewAdapter listViewAdapter;

        listViewAdapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(listViewAdapter);

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String name = ((JSONObject) (jsonArray.get(i))).get("name").toString();
                String phone = ((JSONObject) (jsonArray.get(i))).get("phone").toString();
                String gender = ((JSONObject) (jsonArray.get(i))).get("gender").toString();
                String email =((JSONObject) (jsonArray.get(i))).get("email").toString();
                listViewAdapter.addItem(name, phone, gender, email);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void tab2() {
        GridView gridView;
        CostomImageAdapter imageAdapter;
        imageAdapter = new CostomImageAdapter(this);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);
        for (int i = 0; i < images.size(); i++) {
            imageAdapter.addItem(images.get(i));
        }
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);
//        imageAdapter.addItem(null);


    }
}
