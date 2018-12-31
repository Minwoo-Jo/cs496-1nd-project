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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    int score = 0;
    int level = 1;
    ArrayList<Rect> rects = new ArrayList<>();
    Button startBtn;
    TextView scoreView;
    ArrayList<Timer> timers = new ArrayList<>();
    Timer canceler = new Timer();

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
        startBtn = (Button) view.findViewById(R.id.startbtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                startBtn.setVisibility(View.INVISIBLE);
            }
        });
        scoreView = (TextView) view.findViewById(R.id.scoreView);

    }

    public void defaultSet() {
        for (int i = 0; i < randomFence.length; i++) {
            randomFence[i] = i * 3 + 1;
            randomIndex[i] = (int) (Math.random() * 3);
        }

        for (int i = 0; i < n.length; i++) {
            speed[i] = i;
            n[i] = 60 * (i + 1);
        }
    }

    public void startGame() {
        canceler.cancel();
        score = 0;
        level = 1;
        timers.clear();
        scoreView.setText(score + "점");
        //timers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            timers.add(new Timer());
        }
       canceler = new Timer();
        defaultSet();
        for (int i = 0; i < 6; i++) {
            timers.get(i).schedule(new TimerTask() {
                @Override
                public void run() {

                    flow();

                }
            }, i * 4500+1, 80 - i * 7);
        }
        TimerTask cancelerTask = new TimerTask() {
            int j = 0;
            @Override
            public void run() {
                if (j < 5) {
                    timers.get(j).cancel();
                    timers.get(j).purge();
                    j++;
                    level++;
                } else
                    level++;
            }
        };
        canceler.schedule(cancelerTask, 4500, 4500);
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
                    timers.forEach(x -> {x.cancel();
                    x.purge();});
                }
            }
        }
        score = score + level;
        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.BLACK);
        playerPaint.setStrokeWidth(20f);
        canvas.drawCircle(point[current], y + 400, 40, playerPaint);
        scoreView.setText(score + "점");

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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < point[current] && current != 0) {
                    current--;
                } else if (event.getX() > point[current] && current != 2) {
                    current++;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
        }
        return true;
    }

    public void flow() {

        for (int i = 0; i < n.length; i++) {
            speed[i] = speed[i]+1;
            n[i] = n[i] + speed[i];
            if (y - n[i] < 0) {
                n[i] = 40;
                speed[i] = 0;

            }
        }
        invalidate();
    }


}
