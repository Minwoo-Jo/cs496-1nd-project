package com.example.q.firstapp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup parent) {
        final int pos = i;
        final Context context = parent.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview1_item, parent, false);
        }
        TextView imageView = (TextView) view.findViewById(R.id.profile);
        TextView nameView = (TextView) view.findViewById(R.id.textView1);
        TextView phoneView = (TextView) view.findViewById(R.id.textView2);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = listViewItemList.get(i);
        imageView.setText(((listViewItem.getName()).charAt(0)+""));
        nameView.setText(listViewItem.getName());
        phoneView.setText(listViewItem.getPhone());
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.detail_view, (ViewGroup) parent.findViewById(R.id.detail));
                TextView name = (TextView) layout.findViewById(R.id.detailname);
                ImageView profile = (ImageView) layout.findViewById(R.id.profileview);
                TextView phone = (TextView) layout.findViewById(R.id.phonenumber);
                TextView email = (TextView) layout.findViewById(R.id.email);

                name.setText(listViewItem.getName());
                profile.setImageResource(R.drawable.man);
                phone.setText(listViewItem.getPhone());
                email.setText(listViewItem.getEmail());


                PopupWindow p = new PopupWindow(layout, 800, 1200, true);
                p.setAnimationStyle(R.anim.popupanimation);
                p.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
        return view;
    }

    public void addItem(String name, String phone, String email) {
        ListViewItem item = new ListViewItem();

        item.setName(name);
        item.setPhone(phone);
//        item.setGender(gender);
        item.setEmail(email);

        listViewItemList.add(item);
    }
}
