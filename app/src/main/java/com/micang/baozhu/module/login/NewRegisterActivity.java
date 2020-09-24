package com.micang.baozhu.module.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
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

import com.micang.baozhu.http.bean.login.Res;
import com.micang.baozhu.util.CallLogInfo;
import com.micang.baozhu.util.ContactUtils;
import com.micang.baozhu.util.MyContacts;
import com.micang.baozhu.util.PhoneUtil;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.MD5Util;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.bean.login.ServiceBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.web.PigRuleActivity;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.NumberUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;

public class NewRegisterActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private RelativeLayout phone;
    private EditText etPhonenumber;
    private RelativeLayout rlCode;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private RelativeLayout rlPsw;
    private EditText etPsw;
    private ImageView ivShowPsw;
    private RelativeLayout rlFree;
    private EditText etRefree;
    private Button btnLogin;
    private TextView tvRule;
    private String registrationId;
    private String ruleNum = "10000";
    private String sendMode = "1";
    private String qrCode = "";
    private String equipmentType = "1";
    private boolean isShow = true;
    private String imei = "";
    private String mobile = "";
    private int ffffColor;
    private TextView tvService;

    @Override
    public int layoutId() {
        return R.layout.activity_new_register;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        ffffColor = ContextCompat.getColor(this, R.color.color_ffffff);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("欢迎注册");
        phone = findViewById(R.id.phone);
        etPhonenumber = findViewById(R.id.et_phonenumber);
        rlCode = findViewById(R.id.rl_code);
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);
        rlPsw = findViewById(R.id.rl_psw);
        etPsw = findViewById(R.id.et_psw);
        ivShowPsw = findViewById(R.id.iv_show_psw);
        rlFree = findViewById(R.id.rl_free);
        etRefree = findViewById(R.id.et_refree);
        btnLogin = findViewById(R.id.btn_login);
        tvRule = findViewById(R.id.tv_rule);
        tvService = findViewById(R.id.tv_service);
        String str = "如需帮助，请联系客服QQ：<font color='#FF156D'>292898299</font>";
        tvService.setText(Html.fromHtml(str));
        registrationId = JPushInterface.getRegistrationID(this);
        initClick();
        initData();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        ivShowPsw.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRule.setOnClickListener(this);
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
                    //显示为明文
                    ivShowPsw.setImageDrawable(getResources().getDrawable(R.drawable.icon_show));
                    etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPsw.setSelection(etPsw.getText().length());
                    isShow = false;
                } else {
                    //显示为密文
                    ivShowPsw.setImageDrawable(getResources().getDrawable(R.drawable.icon_not_show));
                    etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPsw.setSelection(etPsw.getText().length());
                    isShow = true;
                }
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
                String psw = etPsw.getText().toString().trim();
                String mobile = etPhonenumber.getText().toString().trim();
                if (!NumberUtils.checkMob(mobile)) {
                    return;
                }
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                boolean format = checkPwdFormat(psw);
                if (format) {
                    checkSmsCode(code);
                }
                break;
            case R.id.tv_rule:
                startActivity(new Intent(NewRegisterActivity.this, PigRuleActivity.class));
                break;

        }
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

        new RxPermissions(NewRegisterActivity.this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG/*Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION*/).subscribe(new Consumer<Boolean>() {
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
                    Toast.makeText(NewRegisterActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void register(String codeKey) {
        String psw = etPsw.getText().toString().trim();
        String md5 = MD5Util.getMd5(psw);
        String umeng_channel = AppUtils.getAppMetaData(this, "UMENG_CHANNEL");
        qrCode = etRefree.getText().toString().trim();
        HttpUtils.register(mobile, md5, qrCode, codeKey, imei, umeng_channel, equipmentType, registrationId).enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                AppContext.showNovice = true;
                AppContext.notice = true;
                UserBean data = (UserBean) response.data;
                SPUtils.saveString(NewRegisterActivity.this, CommonConstant.USER_TOKEN, response.token);
                SPUtils.saveString(NewRegisterActivity.this, CommonConstant.MOBIL_IMEI, data.imei);
                String userId = data.userId;
                String imei = data.imei;
                String alias = userId + imei;
                JPushInterface.setAlias(NewRegisterActivity.this, 1, alias);
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
                    SPUtils.saveString(NewRegisterActivity.this, CommonConstant.MOBIL_IMEI, null);
                    SPUtils.saveString(NewRegisterActivity.this, CommonConstant.USER_TOKEN, null);
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
