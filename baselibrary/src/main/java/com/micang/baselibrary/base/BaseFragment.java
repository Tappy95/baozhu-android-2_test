package com.micang.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.util.EventBusUtils;


public abstract class BaseFragment extends Fragment {

    public Activity activity;
    public Context context;
    protected View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        if (activity == null){
            activity =getActivity();
            if (activity == null){
                activity = (Activity) getContext();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layoutId(), container, false);
        context = getActivity ();
        // 若使用BindEventBus注解，则绑定EventBus
        if(this.getClass().isAnnotationPresent(BindEventBus.class)){
            EventBusUtils.register(this);
        }
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );
        initData();
    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    public boolean onTouchEvent(MotionEvent event) {
        if(null != activity.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) activity. getSystemService( Context.INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return activity.onTouchEvent(event);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 若使用BindEventBus注解，则解绑定EventBus
        if(this.getClass().isAnnotationPresent(BindEventBus.class)){
            EventBusUtils.unregister(this);
        }
    }

    protected abstract int layoutId();
    protected abstract void init(View rootView);
    protected abstract void initData() throws NullPointerException;

}
