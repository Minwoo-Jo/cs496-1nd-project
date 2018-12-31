package com.example.q.firstapp;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class Tab3Fragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3,container,false);


        GraphicView g = new GraphicView(view.getContext(), view);
        LinearLayout l = (LinearLayout)view.findViewById(R.id.linearLayout3);


        l.addView(g);
        Log.d("test@", "q1");


        return view;
    }
}
