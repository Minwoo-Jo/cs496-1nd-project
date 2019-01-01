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
    int[] n = new int[16];
    int[] speed = new int[16];
    ArrayList<Paint> ps = new ArrayList<>();
    int width;
    int height;
    int left, center, right;
    int[] point = new int[3];
    int current;
    int[] randomFence = new int[4];
    int[] randomIndex = {1, 1, 1, 1};
    ArrayList<Paint> fps = new ArrayList<>();
    int score = 0;
    int level = 1;
    int playerY;
    ArrayList<Rect> rects = new ArrayList<>();
    Button startBtn;
    TextView scoreView;
    ArrayList<Timer> timers = new ArrayList<>();
    Timer canceler = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GraphicView(Context context, View view) {
        super(context);
        DisplayMetrics dn = context.getResources().getDisplayMetrics();
        width = dn.widthPixels;
        height = dn.heightPixels;
        x = width / 2 - 10;
        y = height / 2 - 700;
        playerY = y + 1100;
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                startGame();
                startBtn.setVisibility(View.INVISIBLE);
            }
        });
        scoreView = (TextView) view.findViewById(R.id.scoreView);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void defaultSet() {
        for (int i = 0; i < randomFence.length; i++) {
            randomFence[i] = i * 4;
            randomIndex[i] = 1;
            fps.add(new Paint());
        }
        current = 1;
        for (int i = 0; i < n.length; i++) {
            speed[i] = i * 2 + 1;
            n[i] = 80 * (i + 1);
            ps.add(new Paint());
        }
        ps.forEach(x -> x.setColor(Color.WHITE));
        fps.forEach(x -> x.setColor(Color.DKGRAY));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            }, i * 4500, 80 - i * 9);
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
                } else {
                    level= level+3;
                    canceler.cancel();
                }
            }
        };
        canceler.schedule(cancelerTask, 4500, 4500);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < n.length; i++) {
            ps.get(i).setStrokeWidth(speed[i]);

        }
        for (int i = 0; i < n.length; i++) {
            canvas.drawLine(x - 50 - n[i] / 2, y - n[i], x - 50 - n[i] / 2, y + n[i], ps.get(i));
            canvas.drawLine(x - 50 - n[i] / 2, y + n[i], x + 50 + n[i] / 2, y + n[i], ps.get(i));
            canvas.drawLine(x + 50 + n[i] / 2, y - n[i], x + 50 + n[i] / 2, y + n[i], ps.get(i));
        }
        for (int i = 0; i < randomFence.length; i++) {
            fps.get(i).setStrokeWidth(speed[randomFence[i] + 1]);
            drawFence(canvas, randomIndex[i], randomFence[i], fps.get(i));
            if (y + n[randomFence[i]] > playerY && y + n[randomFence[i]] < playerY + 40) {
                Log.d("test@", "ee1");
                if (current != randomIndex[i]) {
                    Log.d("test@", "ee2");
                    startBtn.setVisibility(VISIBLE);
                    timers.forEach(x -> {
                        x.cancel();
                        x.purge();
                    });
                }
            }
        }
        score = score + level;
        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.BLACK);
        playerPaint.setStrokeWidth(20f);
        canvas.drawCircle(point[current], playerY, 40, playerPaint);
        scoreView.setText(score + "점");

    }

    public void drawFence(Canvas canvas, int index, int i, Paint paint) {
        int length = 50;
        int p0 = x - length - n[i] / 2;
        int p3 = x + length + n[i] / 2;
        int p1 = p0 * 2 / 3 + p3 / 3;
        int p2 = (p1 + p3) / 2;
        if (index == 0) {
            canvas.drawLine(p1, y + n[i], p3, y + n[i], paint);
        }
        if (index == 1) {
            canvas.drawLine(p0, y + n[i], p1, y + n[i], paint);
            canvas.drawLine(p2, y + n[i], p3, y + n[i], paint);
        }
        if (index == 2) {
            canvas.drawLine(p0, y + n[i], p2, y + n[i], paint);
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
            speed[i] = speed[i] + 1;
            n[i] = n[i] + speed[i];
            if (y + n[i] > height - 300) {
                n[i] = 50;
                speed[i] = 0;
            }
        }
        for (int i = 0; i < randomFence.length; i++) {
            if (speed[randomFence[i]] == 0) {
                randomIndex[i] = (int) (Math.random() * 3);
            }
        }
        invalidate();
    }
}
