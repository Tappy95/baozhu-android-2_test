package com.micang.baozhu.module.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.component.dly.xzzq_ywsdk.YwSDK;
import com.component.dly.xzzq_ywsdk.YwSDK_WebActivity;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.module.home.MainActivity;
import com.micang.baselibrary.base.BaseActivity;

public class NewTaskActivity extends BaseActivity {
    private LinearLayout llBack;
    private TextView tvFastTask;
    private ImageView ivTaskprogress;
    private int blackTextColor;
    private int grayTextColor;

    private static final int FAST_TASK_FRAGMENT = 0;
    private static final int FOREBODE_TASK_FRAGMENT = 1;
    private FragmentManager mFragmentManager;
    private FastTaskFragment fastTaskFragment;
    private ForebodeTaskFragment forebodeTaskFragment;

    @Override
    public int layoutId() {
        return R.layout.activity_new_task;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        blackTextColor = ContextCompat.getColor(this, R.color.color_000000);
        grayTextColor = ContextCompat.getColor(this, R.color.color_B3BAC7);
        llBack = findViewById(R.id.ll_back);
        tvFastTask = findViewById(R.id.tv_fast_task);
        ivTaskprogress = findViewById(R.id.iv_taskprogress);
        mFragmentManager = getSupportFragmentManager();
        fastTaskFragment = (FastTaskFragment) mFragmentManager.findFragmentByTag("fast_task");
        forebodeTaskFragment = (ForebodeTaskFragment) mFragmentManager.findFragmentByTag("forebode_task");
        initClick();
        initFragment();
    }

    private void initFragment() {
        if (fastTaskFragment == null) {
            fastTaskFragment = new FastTaskFragment();
            addFragment(R.id.framelayout, fastTaskFragment, "fast_task");
        }
        if (forebodeTaskFragment == null) {
            forebodeTaskFragment = new ForebodeTaskFragment();
            addFragment(R.id.framelayout, forebodeTaskFragment, "forebode_task");
        }
        mFragmentManager.beginTransaction().show(fastTaskFragment).hide(forebodeTaskFragment)
                .commitAllowingStateLoss();
    }

    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        final android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initClick() {
        llBack.setOnClickListener(new TabClick());
        tvFastTask.setOnClickListener(new TabClick());
        ivTaskprogress.setOnClickListener(new TabClick());
    }


    public void selecteTab(int position) {
        android.support.v4.app.FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (position == FAST_TASK_FRAGMENT) {
            transaction
                    .hide(forebodeTaskFragment)
                    .show(fastTaskFragment)
                    .commitAllowingStateLoss();
            transaction.addToBackStack(null);
        } else if (position == FOREBODE_TASK_FRAGMENT) {
            mFragmentManager.beginTransaction()

                    .hide(fastTaskFragment)
                    .show(forebodeTaskFragment)
                    .commitAllowingStateLoss();
            transaction.addToBackStack(null);
        }

    }


    private class TabClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    finish();
                    break;
                case R.id.iv_taskprogress:
                    startActivity(new Intent(NewTaskActivity.this, TaskProgressActivity.class));
                    break;
                case R.id.tv_fast_task:
                    selecteTab(0);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
