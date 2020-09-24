package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventWXBind;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class BindWXActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etZhifubaoAccount;
    private EditText etZhifubaoName;
    private TextView tvMoblie;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private Button btBindling;
    private String mobile;
    private int code;
    private String ruleNum = "10030";
    private String sendMode = "1";
    private String type = "2";
    private String zhifubaoAccount = "";
    private String zhifubaoName;
    private String codeKey;

    @Override
    public int layoutId() {
        return R.layout.activity_bind_wx;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);

        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        etZhifubaoAccount = findViewById(R.id.et_zhifubao_account);
        etZhifubaoName = findViewById(R.id.et_zhifubao_name);
        tvMoblie = findViewById(R.id.tv_moblie);
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);
        btBindling = findViewById(R.id.bt_bindling);
        Intent intent = getIntent();
        mobile = intent.getStringExtra("Mobile");
        code = intent.getIntExtra("CODE", 0);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("绑定微信");
        etZhifubaoAccount = findViewById(R.id.et_zhifubao_account);
        etZhifubaoName = findViewById(R.id.et_zhifubao_name);
        tvMoblie = findViewById(R.id.tv_moblie);
        etCode = findViewById(R.id.et_code);
        tvMoblie.setText(mobile);
        getCode = findViewById(R.id.get_code);
        btBindling = findViewById(R.id.bt_bindling);
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


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXBind<Object> event) {
        if (event.code == EventCode.wxcode) {
            String wxcode = event.data.toString();
            bindling(wxcode);
            ToastUtils.show(wxcode);
            EventBus.getDefault().removeStickyEvent(EventWXBind.class);
        }
    }

    private void checkSmsCode(String code) {
        HttpUtils.checkSmscode(mobile, ruleNum, sendMode, code).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    GetSmsBean data = (GetSmsBean) response.data;
                    if (data.res) {
                        codeKey = data.codeKey;
                        wxLogin();
                    } else {
                        ToastUtils.show(data.message);
                    }
                }

            }
        });
    }

    public void wxLogin() {

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        AppContext.api.sendReq(req);
    }

    private void bindling(String wxcode) {

        HttpUtils.bindingAlipay(type, zhifubaoAccount, zhifubaoName, wxcode, codeKey).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                String message = response.message;
                ToastUtils.show(message);
                if (EmptyUtils.isNotEmpty(code) && code == 1) {
                    Intent intent = getIntent();
                    setResult(4, intent);
                    finish();
                } else {
                    Intent intent = new Intent(BindWXActivity.this, NewWithdrawActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCode.removeCountDown();
    }
}
