package com.micang.baozhu.module.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.util.AppUtils;
import com.jaeger.library.StatusBarUtil;

public class AboutPigActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvVersions;


    @Override
    public int layoutId() {
        return R.layout.activity_about_pig;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvVersions = findViewById(R.id.tv_Versions);
        tvTitle.setText("关于麒麟");
        String appVersionName = AppUtils.getAppVersionName("com.micang.baozhu");
        tvVersions.setText("当前版本  " + appVersionName);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
