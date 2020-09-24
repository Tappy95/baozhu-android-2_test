package com.micang.baozhu.module.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.micang.baozhu.R;


/**
 * @Description: 自定义加载中对话框
 * @date 2017/12/8 11:33
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.my_loading);

        setCanceledOnTouchOutside(false);
    }

}
