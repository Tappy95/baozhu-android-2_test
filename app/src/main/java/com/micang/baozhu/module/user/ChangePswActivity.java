package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.util.MD5Util;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.R;
import com.jaeger.library.StatusBarUtil;


public class ChangePswActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvphonenumber;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private EditText etPsw;
    private ImageView ivShowPsw;
    private Button btChange;

    private boolean isShow = true;
    private String ruleNum = "10020";
    private String sendMode = "1";
    private String moblie = "";
    private String equipmentType = "1";

    @Override
    public int layoutId() {
        return R.layout.activity_change_psw;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        moblie = intent.getStringExtra("moblie");
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("修改密码");
        tvphonenumber = (TextView) findViewById(R.id.tv_phonenumber);
        etCode = (EditText) findViewById(R.id.et_code);
        getCode = (CountDownTimerButton) findViewById(R.id.get_code);
        etPsw = (EditText) findViewById(R.id.et_psw);
        ivShowPsw = (ImageView) findViewById(R.id.iv_show_psw);
        btChange = (Button) findViewById(R.id.bt_change);
        tvphonenumber.setText(moblie);
        initClick();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        ivShowPsw.setOnClickListener(this);
        ivShowPsw.setOnClickListener(this);
        btChange.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.get_code:
                getCode.startAction();

                getCode();
                getCode.setEnableCountDown(false);
                break;
            case R.id.bt_change:
                String trim = etCode.getText().toString().trim();
                if (EmptyUtils.isEmpty(trim)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                String psw = etPsw.getText().toString().trim();
                boolean format = checkPwdFormat(psw);
                if (format) {
                    checkSmsCode(trim);
                } else {
                    ToastUtils.show("请输入6-18位数字、字母");
                }
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
            default:
                break;
        }
    }

    private void getCode() {

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
                ToastUtils.show("修改成功");
                finish();
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                ToastUtils.show(msg);
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCode.removeCountDown();
    }
}
