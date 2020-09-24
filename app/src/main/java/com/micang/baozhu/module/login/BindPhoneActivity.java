package com.micang.baozhu.module.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.bean.login.Res;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.web.PigRuleActivity;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.CallLogInfo;
import com.micang.baozhu.util.ContactUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.MyContacts;
import com.micang.baozhu.util.NumberUtils;
import com.micang.baozhu.util.PhoneUtil;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.MD5Util;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etPhonenumber;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private EditText etPsw;
    private Button btnLogin;

    private String ruleNum = "10000";
    private String sendMode = "1";
    private String equipmentType = "1";
    private String imei = "";
    private String mobile = "";
    private int ffffColor;
    private UserBean userBean;

    @Override
    public int layoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        ffffColor = ContextCompat.getColor(this, R.color.color_ffffff);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("绑定手机号");
        etPhonenumber = findViewById(R.id.et_phonenumber);
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);
        etPsw = findViewById(R.id.et_psw);
        btnLogin = findViewById(R.id.btn_login);
        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("UserBean");
        initClick();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
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
            case R.id.btn_login:
//                if (PhoneUtil.notHasLightSensorManager(this)
//                        || PhoneUtil.isFeatures()
//                        || PhoneUtil.checkIsNotRealPhone()
//                        || PhoneUtil.checkPipes()) {
//                    ToastUtils.show(this, "请用手机登录");
//                    return;
//                }
                String code = etCode.getText().toString().trim();
//                String psw = etPsw.getText().toString().trim();
                String mobile = etPhonenumber.getText().toString().trim();
                if (!NumberUtils.checkMob(mobile)) {
                    return;
                }
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
//                boolean format = checkPwdFormat(psw);
//                if (format) {
                checkSmsCode(code);
//                }
                break;
        }
    }

    private void getCode() {
        mobile = etPhonenumber.getText().toString().trim();
        if (!NumberUtils.checkMob(mobile)) {
            return;
        }
        getCode.setColor(ffffColor);
        getCode.startAction();
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

    private void checkSmsCode(String trim) {
        mobile = etPhonenumber.getText().toString().trim();
        if (!NumberUtils.checkMob(mobile)) {
            return;
        }
        HttpUtils.checkSmscode(mobile, ruleNum, sendMode, trim).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    GetSmsBean data = (GetSmsBean) response.data;
                    if (data.res) {
                        String codeKey = data.codeKey;
                        checkPermission(codeKey);
                    } else {
                        ToastUtils.show(data.message);
                    }
                }
            }
        });
    }

    private void checkPermission(final String codeKey) {

        new RxPermissions(BindPhoneActivity.this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG/*Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION*/).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //申请的权限全部允许
                    imei = getIMEI();
                    register(codeKey);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getContacts();
                        }
                    }.start();
                } else {
                    //只要有一个权限被拒绝，就会执行
                    Toast.makeText(BindPhoneActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void register(String codeKey) {
        if (EmptyUtils.isEmpty(userBean)) {
            ToastUtils.show("网络状态不好,请退出重试");
            return;
        }
        String umeng_channel = AppUtils.getAppMetaData(this, "UMENG_CHANNEL");
        String registrationId = JPushInterface.getRegistrationID(this);
        HttpUtils.bindMobile(mobile, codeKey, imei, umeng_channel, equipmentType, registrationId, userBean.openId, userBean.aliasName, userBean.profile).enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                AppContext.showNovice = true;
                AppContext.notice = true;
                UserBean data = (UserBean) response.data;
                SPUtils.saveString(BindPhoneActivity.this, CommonConstant.USER_TOKEN, response.token);
                SPUtils.saveString(BindPhoneActivity.this, CommonConstant.MOBIL_IMEI, data.imei);
                String userId = data.userId;
                String imei = data.imei;
                String alias = userId + imei;
                JPushInterface.setAlias(BindPhoneActivity.this, 1, alias);
                ToastUtils.show(response.message);
                NewLoginActivity.instance.finish();
                finish();
            }
        });
    }

    private void getContacts() {
        ArrayList<MyContacts> contacts = ContactUtils.getAllContacts(this);
        List<CallLogInfo> callLog = ContactUtils.getCallLog(this);
        int contactssize = contacts.size();
        TLog.e("NewRegisterActivity", "contacts contacts" + contactssize);
        int callLogsize = callLog.size();
        TLog.e("NewRegisterActivity", "contacts log" + callLogsize);
        HttpUtils.checkingMailList(contactssize, callLogsize).enqueue(new Observer<BaseResult<Res>>() {
            @Override
            public void onSuccess(BaseResult response) {
                Res code = ((Res) response.data);
                TLog.e("NewLoginActivity", "contacts log" + code.res);
                if (code.res == 2) {
                    SPUtils.saveString(BindPhoneActivity.this, CommonConstant.MOBIL_IMEI, null);
                    SPUtils.saveString(BindPhoneActivity.this, CommonConstant.USER_TOKEN, null);
                }
            }
        });
    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        if (EmptyUtils.isEmpty(imei)) {
            //由于Android 10系统限制，无法获取IMEI等标识，如果为空再去获取Android ID作为标识进行传递
            String deviceId = Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            imei = deviceId;
        }
        return imei;
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
