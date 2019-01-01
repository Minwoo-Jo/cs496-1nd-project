package com.example.q.firstapp;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    AssetManager assetManager;
    JSONArray jsonArray;
    Context context;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<ListViewItem> listViewItems;
    ArrayList<ListViewItem> forSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        tab1(view);
        Button b = view.findViewById(R.id.newContents);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.newconview, (ViewGroup) container.findViewById(R.id.newconlayout));
                Button add = layout.findViewById(R.id.addCon);
                EditText name = layout.findViewById(R.id.newnameedit);
                EditText phone = layout.findViewById(R.id.newphoneedit);
                EditText email = layout.findViewById(R.id.newemailedit);
                PopupWindow p = new PopupWindow(layout, 800, 700, true);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContents(name.getText().toString(), phone.getText().toString(), email.getText().toString(), context);
                        Intent in = new Intent(context, MainActivity_rebuild.class);
                        p.dismiss();
                        startActivity(in);
                    }
                });

                p.setAnimationStyle(R.anim.popupanimation);
                p.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        return view;
    }

    public void tab1(View v) {
        context = v.getContext();
        listViewAdapter = new ListViewAdapter();
        listViewAdapter.setContext(v.getContext());
        listViewItems = new ArrayList<>();
        listView = (ListView) v.findViewById(R.id.tab1view);
        listView.setAdapter(listViewAdapter);
        if (MainActivity_rebuild.contentsIds != null) {
            for (String id : MainActivity_rebuild.contentsIds) {
                String pid = MainActivity_rebuild.contents.get(id).get("id");
                String name = MainActivity_rebuild.contents.get(id).get("name");
                String phone = MainActivity_rebuild.contents.get(id).get("phone");
                String email = MainActivity_rebuild.contents.get(id).get("email");
                addItem(pid, name, phone, email);
            }
        }
        listViewAdapter.setListViewItems(listViewItems);
        listViewAdapter.notifyDataSetChanged();
        EditText t = v.findViewById(R.id.editText2);

        t.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                forSearch = new ArrayList<>();
                for (ListViewItem item : listViewItems) {
                    if (item.contains(s)) {
                        forSearch.add(item);
                    }
                }
                listViewAdapter.setListViewItems(forSearch);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.toString()==""){
                        listViewAdapter.setListViewItems(listViewItems);
                        listViewAdapter.notifyDataSetChanged();
                    }
            }
        });


    }

    public void setContents(String name, String phone, String email, Context context) {
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
            ContentProviderResult[] res = context.getContentResolver().applyBatch(
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

    public void addItem(String id, String name, String phone, String email) {
        ListViewItem item = new ListViewItem();
        item.setId(id);
        item.setName(name);
        item.setPhone(phone);
        item.setEmail(email);
        listViewItems.add(item);
    }
}
