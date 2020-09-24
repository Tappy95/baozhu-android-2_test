package com.micang.baozhu.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.MD5Util;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

public class FindPswActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvPhone;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private EditText etPsw;
    private ImageView ivShowPsw;
    private Button btnRegister;
    private String moblie;
    private boolean isShow = true;
    private String ruleNum = "10020";
    private String sendMode = "1";
    private String equipmentType = "1";
    private int ffffColor;

    @Override
    public int layoutId() {
        return R.layout.activity_find_psw;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        ffffColor = ContextCompat.getColor(this, R.color.FF2555);
        Intent intent = getIntent();
        moblie = intent.getStringExtra("moblie");
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("找回密码");
        tvPhone = findViewById(R.id.tv_phone);
        if (EmptyUtils.isNotEmpty(moblie)) {
            tvPhone.setText(moblie);
        }
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);
        etPsw = findViewById(R.id.et_psw);
        ivShowPsw = findViewById(R.id.iv_show_psw);
        btnRegister = findViewById(R.id.btn_register);
        initClick();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        ivShowPsw.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.get_code:

                //获取验证码
                getCode();
                getCode.setEnableCountDown(false);
                break;
            case R.id.iv_show_psw:
                if (isShow) {
                    ivShowPsw.setImageDrawable(getResources().getDrawable(R.drawable.icon_show));
                    etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPsw.setSelection(etPsw.getText().length());
                    isShow = false;
                } else {

                    ivShowPsw.setImageDrawable(getResources().getDrawable(R.drawable.icon_not_show));
                    etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPsw.setSelection(etPsw.getText().length());
                    isShow = true;
                }
                break;
            case R.id.btn_register:
                String code = etCode.getText().toString().trim();
                String psw = etPsw.getText().toString().trim();
                //验证验证码
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                boolean format = checkPwdFormat(psw);
                if (format) {
                    checkSmsCode(code);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkPwdFormat(String psw) {
        if (EmptyUtils.isEmpty(psw)) {
            ToastUtils.show("请输入密码");
            return false;
        } else if (psw.length() < 6 || psw.length() > 18) {
            ToastUtils.show("请输入6-18位密码");
            return false;
        } else {
            return true;
        }
    }

    private void getCode() {
        getCode.startAction();
        getCode.setColor(ffffColor);
        HttpUtils.getSmscode(moblie, ruleNum, sendMode).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                GetSmsBean data = (GetSmsBean) response.data;
                if (data.res) {
                    ToastUtils.show(data.message);
                } else {
                    getCode.removeCountDown();
                    ToastUtils.show(data.message);
                }
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                getCode.removeCountDown();

            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                getCode.removeCountDown();
            }
        });
    }

    private void checkSmsCode(String trim) {

        HttpUtils.checkSmscode(moblie, ruleNum, sendMode, trim).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                GetSmsBean data = (GetSmsBean) response.data;
                if (data.res) {
                    String codeKey = data.codeKey;
                    changePsw(codeKey);
                } else {
                    ToastUtils.show(data.message);
                }
            }
        });
    }


    private void changePsw(String codeKey) {
        String psw = etPsw.getText().toString().trim();
        String md5 = MD5Util.getMd5(psw);

        HttpUtils.changePsw(moblie, codeKey, md5, equipmentType).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show(FindPswActivity.this, "修改成功");
                NewForgetActivity.instance.finish();
                finish();
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                ToastUtils.show(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCode.removeCountDown();
    }
}
