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

public class PayPswActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvPhonenumber;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private EditText etPsw;
    private ImageView ivShowPsw;
    private Button btChange;

    private boolean isShow = true;
    private String ruleNum = "10070";
    private String sendMode = "1";
    private String moblie = "";
    private String equipmentType = "1";
    private String key = "";

    @Override
    public int layoutId() {
        return R.layout.activity_pay_psw;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        moblie = intent.getStringExtra("moblie");
        key = intent.getStringExtra("release.keystore");
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);

        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvPhonenumber = findViewById(R.id.tv_phonenumber);
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);
        etPsw = findViewById(R.id.et_psw);
        ivShowPsw = findViewById(R.id.iv_show_psw);
        btChange = findViewById(R.id.bt_change);
        if ("1".equals(key)) {
            tvTitle.setText("修改支付密码");
            btChange.setText("确定修改");
        } else {
            tvTitle.setText("设置支付密码");
            btChange.setText("确定");
        }
        tvPhonenumber.setText(moblie);
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
                if (EmptyUtils.isEmpty(psw) || psw.length() < 6) {
                    ToastUtils.show("请输入6位数字密码");
                    return;
                }
                checkSmsCode(trim);
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

        HttpUtils.updatePayPassword(codeKey, md5).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("操作成功");
                Intent intent = getIntent();
                setResult(5, intent);
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
