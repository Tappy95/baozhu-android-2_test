package com.micang.baozhu.util;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ToastUtils {

    private static Toast sToast;

    /**
     * Gets context.
     *
     * @return the context
     */
    public static Context getContext() {
        return AppContext.getInstance();
    }
    public static void show(String text) {
        //判断是否在主线程
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            loadToast(text);
        } else {
            Observable.just(text)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            loadToast(s);
                        }
                    });
        }
    }
    public static void show(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void loadToast(String msg) {
//        Looper.prepare();
        if (sToast == null) {
            sToast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);

        } else {
            sToast.setText(msg);
        }
        sToast.show();
//        Looper.loop();
    }

    /**
     * 自定义toast
     *
     * @param context 上下文对象
     * @param titles  toast 标题
     */
    public static void toastProcess(Context context, String titles) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.custom_toast, null);
        TextView title = view.findViewById(R.id.toast_title);

        title.setText(titles); //toast的标题

        Toast sToast = new Toast(context.getApplicationContext());

        sToast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        sToast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        sToast.setView(view); //添加视图文件
        sToast.show();
    }

}
