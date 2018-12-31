package com.example.q.firstapp;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity_rebuild extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentsPagerAdapter;
    AssetManager assetManager;
    JSONArray jsonArray;
    static ArrayList<Uri> images;
    static ArrayList<String> contentsIds;
    static HashMap<String, Map<String, String>> contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reform);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity_rebuild.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                getImage();
                getContents();
//                readSamples();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    try {
//                        setContents(
//                                ((JSONObject) (jsonArray.get(i))).get("name").toString(),
//                                ((JSONObject) (jsonArray.get(i))).get("phone").toString(),
//                                ((JSONObject) (jsonArray.get(i))).get("email").toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//               getContents();
                mContentsPagerAdapter = new ContentsPagerAdapter(
                        getSupportFragmentManager(), mTabLayout.getTabCount());
                mContentsPagerAdapter.setImages(images);
                mViewPager.setAdapter(mContentsPagerAdapter);
                mViewPager.addOnPageChangeListener(
                        new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity_rebuild.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [App Permissions]")
                .setPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS)
                .check();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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

//    public void setContents() {
//        ContentValues cv = new ContentValues();
//        Log.d("test@", cv.keySet().toString());
//        Log.d("test@", ContactsContract.Contacts._ID);
//        cv.put( ContactsContract.Contacts._ID, "asdasd21");
//        cv.put(ContactsContract.Contacts.DISPLAY_NAME, "민우");
//        cv.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "00123123123");
//        cv.put(ContactsContract.CommonDataKinds.Email.DATA,"ASdasd");
//
//
//       getContentResolver().insert(ContactsContract.Contacts.CONTENT_URI, cv);
//
//    }

    public void getContents() {
        HashMap<String, Map<String, String>> users = new HashMap<>(); // id, data
        String[] arrProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        String[] arrPhoneProjection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String[] arrEmailProjection = {
                ContactsContract.CommonDataKinds.Email.DATA
        };
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, arrProjection, ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null, null);
        while (cursor.moveToNext()) {
            String conId = cursor.getString(0);
            HashMap<String, String> user = new HashMap<String, String>();
            user.put("id", cursor.getString(0));
            user.put("name", cursor.getString(1));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrPhoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + conId, null, null);
            while (phoneCursor.moveToNext()) {
                user.put("phone", phoneCursor.getString(0));
            }
            phoneCursor.close();
            Cursor emailCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, arrEmailProjection, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + conId, null, null);
            while (emailCursor.moveToNext()) {
                user.put("email", emailCursor.getString(0));
            }
            emailCursor.close();
            users.put(cursor.getString(0), user);
        }
        cursor.close();

        contentsIds = (ArrayList) sortByValue(users);
        contents = users;


    }

    public void setContents(String name, String phone, String email) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());
        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "2").build());
        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("test@", "z4");

    }

    public List sortByValue(final Map<String, Map<String, String>> map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1).get("name");
                Object v2 = map.get(o2).get("name");
                return ((Comparable) v2).compareTo(v1);
            }
        });
        Collections.reverse(list); // 주석시 오름차순
        return list;
    }
}
