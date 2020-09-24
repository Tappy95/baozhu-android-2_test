package com.micang.baozhu.module.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/9/26 19:04
 * @describe describe
 */
public class NoScrollRecyclerView extends RecyclerView {
    public NoScrollRecyclerView(Context var1) {
        super(var1);
    }

    public NoScrollRecyclerView(Context var1, @Nullable AttributeSet var2) {
        super(var1, var2);
    }

    public NoScrollRecyclerView(Context var1, @Nullable AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void onMeasure(int var1, int var2) {
        super.onMeasure(var1, MeasureSpec.makeMeasureSpec(536870911, AT_MOST));
    }
}
