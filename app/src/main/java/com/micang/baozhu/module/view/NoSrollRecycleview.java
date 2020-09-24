package com.micang.baozhu.module.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/24 16:24
 * @describe describe
 */
public class NoSrollRecycleview extends RecyclerView {
    public NoSrollRecycleview(@NonNull Context context) {
        super(context);
    }

    public NoSrollRecycleview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSrollRecycleview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
