// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.micang.baozhu.module.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baozhu.R;

import cn.iwgang.countdownview.CustomCountDownTimer;

import java.util.HashMap;

public class CountdownView extends LinearLayout {

    long countDownInterval;
    private HashMap<Integer, CustomCountDownTimer> countdownViewMap;
    private CustomCountDownTimer mCustomCountDownTimer;
    private CountdownView.OnCountdownEndListener mOnCountdownEndListener;
    private HashMap<Integer, Long> timeMap;
    private TextView tv_des;
    private TextView tv_left_time;

    public CountdownView(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public CountdownView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CountdownView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.countDownInterval = 1000L;
        this.timeMap = new HashMap();
        this.countdownViewMap = new HashMap();
        this.initView(var1, var2);
    }

    private void initView(Context context, AttributeSet attributeset) {
        LayoutInflater.from(context).inflate(R.layout.countdown_view, this, true);
        tv_left_time = (TextView) findViewById(R.id.tv_time);
        tv_des = (TextView) findViewById(R.id.tv_des);

    }

    public HashMap<Integer, CustomCountDownTimer> getCountdownViewMap() {
        return this.countdownViewMap;
    }

    public HashMap<Integer, Long> getTimeMap() {
        return this.timeMap;
    }

    public void setCountdownViewMap(HashMap<Integer, CustomCountDownTimer> var1) {
        this.countdownViewMap = var1;
    }

    public void setOnCountdownEndListener(CountdownView.OnCountdownEndListener var1) {
        this.mOnCountdownEndListener = var1;
    }

    public void setTimeMap(HashMap<Integer, Long> var1) {
        this.timeMap = var1;
    }

    public void start(long var1) {
        if (this.mCustomCountDownTimer != null) {
            this.mCustomCountDownTimer.stop();
            this.mCustomCountDownTimer = null;
        }

        this.mCustomCountDownTimer = new CustomCountDownTimer(var1, this.countDownInterval) {
            public void onFinish() {
                if (CountdownView.this.mOnCountdownEndListener != null) {
                    CountdownView.this.mOnCountdownEndListener.onEnd(CountdownView.this);
                }

            }

            public void onTick(long var1) {
                CountdownView.this.updateShow(var1);
            }
        };
        this.mCustomCountDownTimer.start();
    }

    public void start(long var1, final int var3) {
        if (this.mCustomCountDownTimer != null) {
            this.mCustomCountDownTimer.stop();
            this.mCustomCountDownTimer = null;
        }

        this.mCustomCountDownTimer = new CustomCountDownTimer(var1, this.countDownInterval) {
            public void onFinish() {
                if (CountdownView.this.mOnCountdownEndListener != null) {
                    CountdownView.this.mOnCountdownEndListener.onEnd(CountdownView.this);
                }

            }

            public void onTick(long var1) {
                CountdownView.this.updateShow(var1);
                CountdownView.this.updateShow(var1, var3);
            }
        };
        this.countdownViewMap.put(var3, this.mCustomCountDownTimer);
        this.mCustomCountDownTimer.start();
    }

    public void stop() {
        if (this.mCustomCountDownTimer != null) {
            this.mCustomCountDownTimer.stop();
        }

    }

    public void updateShow(long var1) {
        if (var1 < 1000L) {
            this.tv_left_time.setText("0");
        } else {
            long var3 = var1 / 1000L;
            this.tv_left_time.setText("" + var3);
        }
    }

    public void updateShow(long var1, int var3) {
        if (var1 < 1000L) {
            this.timeMap.put(var3, 0L);
        } else {
            long var4 = var1 / 1000L;
            this.timeMap.put(var3, var4);
        }
    }

    public interface OnCountdownEndListener {
        void onEnd(CountdownView var1);
    }
}