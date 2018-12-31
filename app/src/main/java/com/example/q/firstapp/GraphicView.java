package com.example.q.firstapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GraphicView extends View {
    int x, y;
    int[] n = new int[21];
    int[] speed = new int[21];
    int width;
    int height;
    int left, center, right;
    int[] point = new int[3];
    int current;
    int[] randomFence = new int[5];
    int[] randomIndex = {0, 1, 2, 2, 1};
    ArrayList<Rect> rects = new ArrayList<>();
    Timer t;
    Timer levelT;
    Button startBtn;
    int level = 1;
    ArrayList<Timer> timers = new ArrayList<>();
    public GraphicView(Context context, View view) {
        super(context);

        DisplayMetrics dn = context.getResources().getDisplayMetrics();
        width = dn.widthPixels;
        height = dn.heightPixels;
        x = width / 2 - 10;
        y = height / 2;
        left = width / 6;
        center = width / 2 - 20;
        right = (5 * width / 6) - 20;
        current = 1;
        point[0] = left;
        point[1] = center;
        point[2] = right;
        defaultSet();
        startBtn = (Button)view.findViewById(R.id.startbtn);
        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startGame();
                startBtn.setVisibility(View.INVISIBLE);
            }
        });



    }
    public void defaultSet(){
        for(int i = 0 ; i < randomFence.length ; i++){
            randomFence[i] = i*3+1;
            randomIndex[i] = (int) (Math.random() * 3);
        }

        for (int i = 0; i < n.length; i++) {
            speed[i] = i;
            n[i] = 60 * (i + 1);
        }
    }

    public void startGame() {
        timers = new ArrayList<>();
        for(int i = 0 ; i < 8 ; i ++){
            timers.add(new Timer());
        }
        ArrayList<TimerTask> tasks = new ArrayList<>();
        Timer canceler = new Timer();
        defaultSet();
        t = new Timer();
        levelT = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                flow();
            }
        };
        for(int i = 0 ; i < 8 ; i ++){
            timers.get(i).schedule(new TimerTask() {
                @Override
                public void run() {
                    flow();
                   }
            },i*5000,100-i*5);
        }

        TimerTask cancelerTask = new TimerTask() {
            int j = 0;
            @Override
            public void run() {
                if(j<7) {
                    timers.get(j).cancel();
                    Log.d("test@", j+"");
                    j++;
                } else
                    canceler.cancel();
            }
        };
        canceler.schedule(cancelerTask,5000,5000);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15f);
        paint.setColor(Color.LTGRAY);
        rects.clear();
        for (int i = 0; i < n.length; i++) {
            rects.add(new Rect(x - 30 - n[i], y - 40 - n[i], x + 30 + n[i], y + n[i]));
        }
        Paint fencePaint = new Paint();
        fencePaint.setColor(Color.DKGRAY);
        fencePaint.setStrokeWidth(18f);

        rects.forEach(r -> canvas.drawRect(r, paint));
        for (int i = 0; i < randomFence.length; i++) {
            if (y + n[randomFence[i]] > height - 100) {
                randomIndex[i] = (int) (Math.random() * 3);
            }
            drawFence(canvas, randomIndex[i], randomFence[i], fencePaint);
            if (y + n[randomFence[i]] > y + 400 && y + n[randomFence[i]] < y + 440) {
                Log.d("test@", "ee1");
                if (current != randomIndex[i]) {
                    Log.d("test@", "ee2");
                    startBtn.setVisibility(VISIBLE);
                    timers.forEach(x-> x.cancel());
                }
            }
        }

        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.BLACK);
        playerPaint.setStrokeWidth(20f);
        canvas.drawCircle(point[current], y + 400, 40, playerPaint);

    }

    public void drawFence(Canvas canvas, int index, int i, Paint paint) {
//            Paint w = new Paint();
//            w.setColor(Color.WHITE);
        if (index == 0) {
//            canvas.drawLine(x-30-n[i], y + n[i],  (x-30-n[i])*2/3+(x+30 + n[i])/3, y + n[i], w);
            canvas.drawLine((x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3, y + n[i], ((x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3 + x + 30 + n[i]) / 2, y + n[i], paint);
            canvas.drawLine(((x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3 + x + 30 + n[i]) / 2, y + n[i], x + 30 + n[i], y + n[i], paint);
        }
        if (index == 1) {
            canvas.drawLine(x - 30 - n[i], y + n[i], (x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3, y + n[i], paint);
//            canvas.drawLine((x-30-n[i])*2/3+(x+30 + n[i])/3, y + n[i],   ((x-30-n[i])*2/3+(x+30 + n[i])/3+x+30 + n[i])/2, y + n[i], w);
            canvas.drawLine(((x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3 + x + 30 + n[i]) / 2, y + n[i], x + 30 + n[i], y + n[i], paint);
        }
        if (index == 2) {

            canvas.drawLine(x - 30 - n[i], y + n[i], (x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3, y + n[i], paint);
            canvas.drawLine((x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3, y + n[i], ((x - 30 - n[i]) * 2 / 3 + (x + 30 + n[i]) / 3 + x + 30 + n[i]) / 2, y + n[i], paint);
//            canvas.drawLine(((x-30-n[i])*2/3+(x+30 + n[i])/3+x+30 + n[i])/2,y + n[i], x+30+n[i],y + n[i],w);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 화면에 터치가 발생했을 때 호출되는 콜백 메서드
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < point[current] && current != 0) {
                    current--;
                } else if (event.getX() > point[current] && current != 2) {
                    current++;
                }
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
//                x = (int) event.getX();
//                y = (int) event.getY();

                invalidate(); // 화면을 다시 그려줘라 => onDraw() 호출해준다
        }
        return true;
    }

    public void flow() {

        for (int i = 0; i < n.length; i++) {
            speed[i] = speed[i] + 1;
            n[i] = n[i] + speed[i];
            if (y - n[i] < 0) {
                n[i] = 40;
                speed[i] = 0;

            }
        }
        invalidate();
    }


}
