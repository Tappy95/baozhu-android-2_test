package com.micang.baozhu.module.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.MobilExistBean;
import com.micang.baozhu.http.bean.login.ServiceBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.NumberUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class NewForgetActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etPhonenumber;
    private Button btnLogin;
    private TextView tvService;
    public static NewForgetActivity instance = null;
    private String moblie;

    @Override
    public int layoutId() {
        return R.layout.activity_new_forget;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        instance = this;
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("找回密码");
        etPhonenumber = findViewById(R.id.et_phonenumber);
        btnLogin = findViewById(R.id.btn_login);
        tvService = findViewById(R.id.tv_service);

        String str = "如需帮助，请联系客服QQ：<font color='#FF156D'>292898299</font>";
        tvService.setText(Html.fromHtml(str));
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLogin.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                moblie = etPhonenumber.getText().toString().trim();
                if (!NumberUtils.checkMob(moblie)) {
                    return;
                } else {
                    checkPermission();
                }
            }
        });
        initData();
    }

    private void initData() {
        HttpUtils.contactInformation().enqueue(new Observer<BaseResult<ServiceBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                ServiceBean bean = ((ServiceBean) response.data);
                String str = "如需帮助，请联系客服QQ：<font color='#FF156D'>" + bean.serviceQq + "</font>";
                tvService.setText(Html.fromHtml(str));

            }
        });
    }

    private void checkPermission() {

        new RxPermissions(NewForgetActivity.this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    checkUserExist();
                } else {

                    ToastUtils.show("未授权权限，部分功能不能使用");
                }
            }

        });
    }

    private void checkUserExist() {

        HttpUtils.mobileExist(moblie).enqueue(new Observer<BaseResult<MobilExistBean>>() {

            @Override
            public void onSuccess(BaseResult response) {
                MobilExistBean data = (MobilExistBean) response.data;
                if (data.flag) {
                    Intent intent = new Intent(NewForgetActivity.this, FindPswActivity.class);
                    intent.putExtra("moblie", moblie);
                    startActivity(intent);
                } else {
                    ToastUtils.show("该手机号尚未注册");
                }
            }
        });
    }

}
