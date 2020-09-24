package com.micang.baselibrary.view;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * @author
 * @version 1.0
 * @Package com.dizoo.baselibrary.view
 * @time 2019/3/18 11:39
 * @describe 点击外部不消失pop
 */
public class NewPopWindow extends PopupWindow {
    private Context mContext;

    private WindowManager mWindowManager;
    public boolean canDismiss = true;// 设为false可以控制dismiss()无参方法不调用 主要是为了点击PopupWindow外部不消失

    public NewPopWindow(View contentView, int width, int height, boolean focusable) {
        if (contentView != null) {
            mContext = contentView.getContext();
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    @Override
    public void dismiss() {
        if (canDismiss) {
            dismiss2();
        } else {
            StackTraceElement[] stackTrace = new Exception().getStackTrace();
            if (stackTrace.length >= 2 && "dispatchKeyEvent".equals(stackTrace[1].getMethodName())) {
                dismiss2();
            }
        }
    }

    public void dismiss2() {
        super.dismiss();
    }
}
