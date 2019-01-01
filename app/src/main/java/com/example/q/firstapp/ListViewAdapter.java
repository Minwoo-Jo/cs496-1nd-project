package com.example.q.firstapp;

import android.content.Context;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItems = new ArrayList<ListViewItem>();
    Context context;
    public ListViewAdapter() {
     }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItems.get(i);
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
        // Data Set(listViewItems)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = listViewItems.get(i);
        imageView.setText(((listViewItem.getName()).charAt(0) + ""));
        nameView.setText(listViewItem.getName());
        phoneView.setText(listViewItem.getPhone());
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                View layout = inflater.inflate(R.layout.detail_view, (ViewGroup) parent.findViewById(R.id.detail));
//                TextView name = (TextView) layout.findViewById(R.id.detailname);
//                ImageView profile = (ImageView) layout.findViewById(R.id.profileview);
//                TextView phone = (TextView) layout.findViewById(R.id.phonenumber);
//                TextView email = (TextView) layout.findViewById(R.id.email);
//
//                name.setText(listViewItem.getName());
//                profile.setImageResource(R.drawable.man);
//                phone.setText(listViewItem.getPhone());
//                email.setText(listViewItem.getEmail());
//
//                PopupWindow p = new PopupWindow(layout, 800, 1200, true);
//                p.setAnimationStyle(R.anim.popupanimation);
//                p.showAtLocation(layout, Gravity.CENTER, 0, 0);
//            }
//        });
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                 deleteWin(context, listViewItem, parent);
            }
        };
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                h.postAtTime(r, 1500);
                return false;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.removeCallbacks(r);
                detail(context, listViewItem, parent);

            }
        });
//        view.setOnTouchListener(new View.OnTouchListener() {
//            Handler h = new Handler();
//            boolean longPress = false;
//            float x,y;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        x = event.getX();
//                        y = event.getY();
//                        h.postDelayed(r, 1000);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.d("test@","move");
//                        if(Math.abs(event.getX()-x) > 10 || Math.abs(event.getY() - y)> 10)
//                            h.removeCallbacks(r);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        h.removeCallbacks(r);
//                        if (!longPress)
//                            detail(context, listViewItem, parent);
//                        else
//                            longPress = false;
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        return view;
    }

    public void addItem(String id, String name, String phone, String email) {
        ListViewItem item = new ListViewItem();
        item.setId(id);
        item.setName(name);
        item.setPhone(phone);
        item.setEmail(email);
        listViewItems.add(item);
    }

    public void deleteItem(Context context, ListViewItem listViewItem) {
        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " = " + listViewItem.getId(), null);
        listViewItems.remove(listViewItem);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void detail(Context context, ListViewItem listViewItem, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.detail_view, parent.findViewById(R.id.detail));
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

    public void deleteWin(Context context, ListViewItem listViewItem, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("test@", "er1");
        View layout = inflater.inflate(R.layout.deletewindow, parent.findViewById(R.id.deletewindow));
        Button confirm = layout.findViewById(R.id.confirmdelete);
        Button cancel = layout.findViewById(R.id.canceldelete);
        Log.d("test@", "er2");
        PopupWindow p = new PopupWindow(layout, 800, 200, true);
        Log.d("test@", "er3");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(context, listViewItem);
                p.dismiss();
                ListViewAdapter.this.notifyDataSetChanged();
//                Intent in = new Intent(context, MainActivity_rebuild.class);
//                context.startActivity(in);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.dismiss();
            }
        });
        Log.d("test@", "er4");
        p.setAnimationStyle(R.anim.popupanimation);
        p.showAtLocation(layout, Gravity.CENTER, 0, 0);
        Log.d("test@", "er5");
    }

    public void setListViewItems(ArrayList<ListViewItem> listViewItems){
        this.listViewItems = listViewItems;
    }
}
