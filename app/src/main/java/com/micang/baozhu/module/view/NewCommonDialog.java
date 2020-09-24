package com.micang.baozhu.module.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.micang.baozhu.R;

/**
 * Created by xuyun on 2018/6/14.
 */

public class NewCommonDialog extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private Context context;
    private int gravity;

    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public NewCommonDialog(Context context, boolean isCancelable, boolean isBackCancelable, int gravity) {
        super(context, R.style.custom_dialog3);
        this.context = context;
        this.iscancelable = isCancelable;//isCancelable;
        this.isBackCancelable = isBackCancelable;
        this.gravity = gravity;
    }
    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public NewCommonDialog(Context context, boolean isCancelable, boolean isBackCancelable, int gravity,int style) {
        super(context, R.style.custom_dialog2);
        this.context = context;
        this.iscancelable = isCancelable;//isCancelable;
        this.isBackCancelable = isBackCancelable;
        this.gravity = gravity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);
        Window window = this.getWindow();
        window.setGravity(gravity);


        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }
}
