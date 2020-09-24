package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;


public class BindlingAlipayActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etZhifubaoAccount;
    private EditText etZhifubaoName;
    private TextView tvMoblie;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private Button btBindling;
    private String mobile = "";
    private String ruleNum = "10030";
    private String sendMode = "1";
    private String type = "1";
    private String openId = "";
    private String zhifubaoAccount;
    private String zhifubaoName;
    private int code;
    private String userName;

    @Override
    public int layoutId() {
        return R.layout.activity_bindling_alipay;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        Intent intent = getIntent();
        mobile = intent.getStringExtra("Mobile");
        userName = intent.getStringExtra("userName");
        code = intent.getIntExtra("CODE", 0);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("支付宝绑定");
        etZhifubaoAccount = findViewById(R.id.et_zhifubao_account);
        etZhifubaoName = findViewById(R.id.et_zhifubao_name);
        tvMoblie = findViewById(R.id.tv_moblie);
        etCode = findViewById(R.id.et_code);
        tvMoblie.setText(mobile);
        getCode = findViewById(R.id.get_code);
        btBindling = findViewById(R.id.bt_bindling);
        if (EmptyUtils.isNotEmpty(userName)) {
            etZhifubaoName.setText(userName);
        }
        initClick();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        btBindling.setOnClickListener(this);
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
            case R.id.bt_bindling:
                zhifubaoAccount = etZhifubaoAccount.getText().toString().trim();
                zhifubaoName = etZhifubaoName.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (EmptyUtils.isEmpty(zhifubaoAccount)) {
                    ToastUtils.show("支付宝账号不能为空");
                    return;
                }
                if (EmptyUtils.isEmpty(zhifubaoName)) {
                    ToastUtils.show("姓名不能为空");
                    return;
                }
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.show("验证码不能为空");
                    return;
                }
                checkSmsCode(code);
                break;
            default:
                break;
        }
    }

    private void checkSmsCode(String code) {
        HttpUtils.checkSmscode(mobile, ruleNum, sendMode, code).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    GetSmsBean data = (GetSmsBean) response.data;
                    if (data.res) {
                        String codeKey = data.codeKey;
                        bindling(codeKey);
                    } else {
                        ToastUtils.show(data.message);
                    }
                }

            }
        });
    }

    private void bindling(String codeKey) {
        HttpUtils.bindingAlipay(type, zhifubaoAccount, zhifubaoName, openId, codeKey).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                String message = response.message;
                ToastUtils.show(message);
                if (EmptyUtils.isNotEmpty(code) && code == 1) {
                    Intent intent = getIntent();
                    setResult(4, intent);
                    finish();
                } else {
//                    Intent intent = new Intent(BindlingAlipayActivity.this, NewWithdrawActivity.class);
//                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void getCode() {
        HttpUtils.getSmscode(mobile, ruleNum, sendMode).enqueue(new Observer<BaseResult<GetSmsBean>>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCode.removeCountDown();
    }
}
