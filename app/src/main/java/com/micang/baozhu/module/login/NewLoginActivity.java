package com.micang.baozhu.module.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.auth.AlipaySDK;
import com.micang.baozhu.http.bean.login.Res;
import com.micang.baozhu.module.home.MainActivity;
import com.micang.baozhu.module.user.SettingActivity;
import com.micang.baozhu.util.CallLogInfo;
import com.micang.baozhu.util.ContactUtils;
import com.micang.baozhu.util.MyContacts;
import com.micang.baozhu.util.PhoneUtil;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventWXBind;
import com.micang.baselibrary.util.MD5Util;
import com.micang.baselibrary.util.RSAUtils;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.NumberUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;

@BindEventBus
public class NewLoginActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvPswLogin;
    private TextView tvCodeLogin;
    private ImageView ivPhone;
    private EditText etPhonenumber;
    private ImageView ivPsw;
    private EditText etPsw;
    private ImageView ivShowPsw;
    private RelativeLayout rlPsw;
    private RelativeLayout rlCode;
    private ImageView ivCode;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgetPsw;
    private int blackTextColor;
    private int grayTextColor;
    private String ruleNum = "10010";
    private String sendMode = "1";
    private boolean isShow = true;
    private TextView tvWxlogin;
    private boolean isPswLogin = true;
    private String imei;
    private String registrationId;
    private String equipmentType = "1";
    private int ffffColor;
    public static NewLoginActivity instance = null;
    private String mobile;
    private Activity act;
    private Button btnAli;
    String key = "MIIEogIBAAKCAQEAzgMl2x1fSTqyDmy2iEHcgz6B4wPi8w2G3RnRbq0WphdzEUlp\n" +
            "fiGTkuWfUup0e4mTMFJou7acSAImRHsNe0dPMXRRDHANuk1wzen2wWXjeBVFqy4O\n" +
            "9heJz5c33tWDUdpaBwEFvsGPGLmIsw3qsHEJUyGH3NabLSIT/6QXNApJsdpp5P1j\n" +
            "9JWFwIuB+6ao5u5C6CkW+s6rLEnJ9uY9R1cmTjYVpQfxl0Kd02qI0mFJ4VmgJBOg\n" +
            "avUtjcadQfI+BjiayQxIZo2my9/W7V/hQNnhZIfJ9hBJnPTjuvbqAH/O1gFBSZEc\n" +
            "ujNdhUUOgIEzPLrPo5sAhiJ32Bdoe0zxqiIcxwIDAQABAoIBAF5oQOPd3PnBBTHp\n" +
            "Dej5TaVzcrBm6oz5iPEv0LMIbRWmL1wfqVFc77Tkt5OWz9SBIkuFqGtFbu7s1T6S\n" +
            "qYXyEs+V/0JU42WZ8XTbn/HW0FwKW4frrn2GIehY4wiiDfDnwFPvWwOkzf/Fnmh3\n" +
            "mzJKSrym5A0G58vSDxoax2tYv5bZro9ATlegj/U5mq79tsbC/Pxmu44YSr4zWzCa\n" +
            "9HJ+wKjHgeOCvNUzPA/xyhCDINKTXjG8J1/kP9GvDbzEgW55UYG1V9/+4poe4+8d\n" +
            "5Wwkhot+74rgG9S8eoQxoMgOTK/nQZ+JiZq7+EJLViDwaD5HpGRBtDSTZspclFRa\n" +
            "ApkENskCgYEA99mkmGJugj3kOBNYpBWXHQ8vII9oMH4soCVw+acZM33kotCPb13T\n" +
            "FMm1kbVtlLL8tgH6inlU96uLrU9R+DbR8WTC7ous/5Vn31LcojWtqh8BOyeowAaS\n" +
            "NejivaeF/55ckn4uRCnVv2OhQ4Ut+CYCcZgU6PLDq6WBocvpAsUyUBUCgYEA1MlS\n" +
            "SU7xGmyw4zBNoPJus6G5/ZtQlSJhPamMM6GUG3odLIhhbEI2/DHuxlHDyH+49U6m\n" +
            "VdSdDPGXayvuOPtV67mWSIOt9MOAdxIdH+nwMV9xYB9GMblMBSjtnG6IPzhtj/k+\n" +
            "Xuhe40i1ZCnV9hB9emezeD5Ug774swMtjy21FGsCgYAjxMMeQsZUAX6YISKfu4Xt\n" +
            "aa2Rc2DD7tGd4wlk3hqi9b4zSQoKNm4qB9ouRxKjbRlPYwW3Sj9a5uTnhBT+T1sJ\n" +
            "DVOcfYff8r3k2t6AgmKntIXG9bhCzi/3m75tRGnwM/iEI7WlnKv+TPCO//9cWo/r\n" +
            "uNj7rV1TvjTxFq5v+emC6QKBgFH+ugyxtMq/83G4XVGV2f6yZV9KiETPmLUQLox+\n" +
            "yPnFlpRggVocMeAAxcf6Hf2W6gjtI7+TTGl+dyC/Lu8aOsO5IiYTR+c3prs7aamw\n" +
            "lfQQmTm3E295vceHO/i5xxrFfcKrAtPDqzzd+bf9Pwuw0wlmCIxfSGX0IxWoNdgk\n" +
            "ftC9AoGANZjgHoS99R3QqskFZFsAuDX0piSXhbkB+fCRV73+qSPUbd2TIq8WA/x+\n" +
            "Zb2KMhWs+AsP6C81K27dE7eaK+MRCvpIUAXHlEXTruEfc3gHeavlWjc0U1hN59w3\n" +
            "f2heARDDBjIWdklSvy0YXiqgSrcz9CbPVQdRF19UJ82BDJiHnAY=";

    @Override
    public int layoutId() {
        return R.layout.activity_new_login;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        instance = this;
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        blackTextColor = ContextCompat.getColor(this, R.color.color_000000);
        grayTextColor = ContextCompat.getColor(this, R.color.color_9ea9bc);
        ffffColor = ContextCompat.getColor(this, R.color.color_ffffff);
        llBack = findViewById(R.id.ll_back);
        tvPswLogin = findViewById(R.id.tv_psw_login);
        tvCodeLogin = findViewById(R.id.tv_code_login);
        ivPhone = findViewById(R.id.iv_phone);
        etPhonenumber = findViewById(R.id.et_phonenumber);
        ivPsw = findViewById(R.id.iv_psw);
        rlPsw = findViewById(R.id.rl_psw);
        etPsw = findViewById(R.id.et_psw);
        ivShowPsw = findViewById(R.id.iv_show_psw);
        rlCode = findViewById(R.id.rl_code);
        ivCode = findViewById(R.id.iv_code);
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        registrationId = JPushInterface.getRegistrationID(this);
        tvForgetPsw = findViewById(R.id.tv_forget_psw);
        tvWxlogin = findViewById(R.id.tv_wxlogin);
        btnAli = findViewById(R.id.button2);
        initClick();
        imei = SPUtils.getString(NewLoginActivity.this, CommonConstant.MOBIL_IMEI, "");
        act = this;

    }

    private void initClick() {
        llBack.setOnClickListener(this);
        tvPswLogin.setOnClickListener(this);
        tvCodeLogin.setOnClickListener(this);
        getCode.setOnClickListener(this);
        ivShowPsw.setOnClickListener(this);
        tvForgetPsw.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvWxlogin.setOnClickListener(this);
        btnAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithAlipay();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_psw_login:
                tvPswLogin.setTextColor(blackTextColor);
                tvCodeLogin.setTextColor(grayTextColor);
                rlPsw.setVisibility(View.VISIBLE);
                rlCode.setVisibility(View.GONE);
                isPswLogin = true;
                etPsw.setText("");
                break;
            case R.id.tv_code_login:
                tvPswLogin.setTextColor(grayTextColor);
                tvCodeLogin.setTextColor(blackTextColor);
                rlPsw.setVisibility(View.GONE);
                rlCode.setVisibility(View.VISIBLE);
                isPswLogin = false;
                etCode.setText("");
                break;
            case R.id.get_code:
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
            case R.id.btn_login:
//                if (PhoneUtil.notHasLightSensorManager(this)
//                        || PhoneUtil.isFeatures()
//                        || PhoneUtil.checkIsNotRealPhone()
//                        || PhoneUtil.checkPipes()) {
//                    ToastUtils.show(this, "请用手机登录");
//                    return;
//                }
                if (isPswLogin) {
                    new RxPermissions(NewLoginActivity.this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG/* Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, */).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                imei = getIMEI();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        getContacts();
                                    }
                                }.start();
                                loginPsw();
                            } else {
                                ToastUtils.show("未授权权限，部分功能不能使用");
                            }
                        }

                    });
                } else {
                    String code = etCode.getText().toString().trim();
                    String mobile = etPhonenumber.getText().toString().trim();
                    if (!NumberUtils.checkMob(mobile)) {
                        return;
                    }
                    if (EmptyUtils.isEmpty(code)) {
                        ToastUtils.show("请输入验证码");
                        return;
                    }
                    checkSmsCode(code);
                }

                break;
            case R.id.tv_wxlogin:
                checkPermission();
                break;
            case R.id.tv_forget_psw:
                startActivity(new Intent(NewLoginActivity.this, NewForgetActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(NewLoginActivity.this, NewRegisterActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXBind<Object> event) {
        if (event.code == EventCode.wxcode) {
            String wxcode = event.data.toString();
            bindling(wxcode);
            EventBus.getDefault().removeStickyEvent(EventWXBind.class);
        }
    }

    private void bindling(String wxcode) {
        HttpUtils.wechatLogin(imei, registrationId, wxcode).enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                onSuccessPsw(response);
            }

            @Override
            public void onFailed(BaseResult body) {
                if ("7001".equals(body.statusCode)) {//未注册
                    UserBean data = (UserBean) body.data;
                    Intent intent = new Intent(NewLoginActivity.this, BindPhoneActivity.class);
                    intent.putExtra("UserBean", data);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailed(String code, String msg) {

            }
        });
    }

    private void getContacts() {
        ArrayList<MyContacts> contacts = ContactUtils.getAllContacts(this);
        List<CallLogInfo> callLog = ContactUtils.getCallLog(this);
        int contactssize = contacts.size();
        TLog.e("NewLoginActivity", "contacts contacts" + contactssize);
        int callLogsize = callLog.size();
        TLog.e("NewLoginActivity", "contacts log" + callLogsize);
        HttpUtils.checkingMailList(contactssize, callLogsize).enqueue(new Observer<BaseResult<Res>>() {
            @Override
            public void onSuccess(BaseResult response) {
                Res code = ((Res) response.data);
                TLog.e("NewLoginActivity", "contacts log" + code.res);
                if (code.res == 2) {
                    SPUtils.saveString(NewLoginActivity.this, CommonConstant.MOBIL_IMEI, null);
                    SPUtils.saveString(NewLoginActivity.this, CommonConstant.USER_TOKEN, null);
                }
            }
        });
    }

    private void getCode() {
        if (!NumberUtils.checkMob(mobile)) {
            mobile = etPhonenumber.getText().toString().trim();
            return;
        }
        getCode.startAction();
        getCode.setColor(ffffColor);
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

    private void checkSmsCode(String code) {
        String mobile = etPhonenumber.getText().toString().trim();
        if (!NumberUtils.checkMob(mobile)) {
            return;
        }
        HttpUtils.checkSmscode(mobile, ruleNum, sendMode, code).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                GetSmsBean data = (GetSmsBean) response.data;
                if (data.res) {
                    String codeKey = data.codeKey;
                    checkPermission(codeKey);
                } else {
                    ToastUtils.show(data.message);
                }
            }
        });

    }

    private void checkPermission(final String codeKey) {

        new RxPermissions(NewLoginActivity.this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //申请的权限全部允许
                    //Toast.makeText(LoginActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                    imei = getIMEI();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getContacts();
                        }
                    }.start();
                    loginSms(codeKey);
                } else {
                    //只要有一个权限被拒绝，就会执行
                    ToastUtils.show("未授权权限，部分功能不能使用");
                }
            }

        });
    }

    private void checkPermission() {

        new RxPermissions(NewLoginActivity.this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //申请的权限全部允许
                    //Toast.makeText(LoginActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                    imei = getIMEI();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getContacts();
                        }
                    }.start();
                    if (AppContext.api.isWXAppInstalled()) {
                        wxLogin();
                    } else {
                        ToastUtils.show("请先安装微信");
                    }
                } else {
                    //只要有一个权限被拒绝，就会执行
                    ToastUtils.show("未授权权限，部分功能不能使用");
                }
            }

        });
    }

    private void loginPsw() {
        String mobile = etPhonenumber.getText().toString().trim();
        String psw = etPsw.getText().toString().trim();
        if (!NumberUtils.checkMob(mobile)) {
            return;
        }
        if (EmptyUtils.isEmpty(psw)) {
            ToastUtils.show("请输入密码");
            return;
        }
        String md5 = MD5Util.getMd5(psw);
        imei = getIMEI();
        HttpUtils.loginPsw(mobile, md5, imei, equipmentType, registrationId).enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                onSuccessPsw(response);
            }
        });
    }

    private void loginSms(String codeKey) {
        String mobile = etPhonenumber.getText().toString().trim();
        HttpUtils.loginBySms(mobile, codeKey, imei, equipmentType, registrationId).enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {

                onSuccessPsw(response);
            }
        });
    }

    private void loginWithAlipay() {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                com.alipay.sdk.app.AuthTask authTask = new AuthTask(act);
                com.alipay.sdk.auth.AlipaySDK sdk = new AlipaySDK();
                Map<String, String> result = authTask.authV2(getAuthorInfo(), true);
                System.err.print("111111111111111111--->" + result.size());
                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };
        Thread td = new Thread(payRunnable);
        td.start();
    }


    private String getAuthorInfo() {
        Map<String, String> args = new HashMap<>();
        args.put("apiname", "com.alipay.account.auth");
        args.put("method", "alipay.open.auth.sdk.code.get");
        args.put("app_id", "2019060565460411");
        args.put("app_name", "娱乐互助");
        args.put("biz_type", "openservice");
        args.put("pid", "2088621904517300");
        args.put("product_id", "APP_FAST_LOGIN");
        args.put("scope", "kuaijie");
        args.put("target_id", SPUtils.getString(this, CommonConstant.MOBIL_IMEI, ""));

        args.put("auth_type", "AUTHACCOUNT");
        args.put("sign_type", "RSA2");

        args.put("sign", buildSign(args));

        String signature = "";
        for (Map.Entry<String, String> item : args.entrySet()) {
            if (!signature.isEmpty()) {
                signature += "&";
            }
            signature += item.getKey() + "=" + item.getValue();
        }
        return signature;
    }

    private String buildSign(Map<String, String> params) {
        String signature = "";
        String sign = "";
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.putAll(params);
        for (Map.Entry<String, String> item : treeMap.entrySet()) {
            if (!signature.isEmpty()) {
                signature += "&";
            }
            signature += item.getKey() + "=" + item.getValue();
        }
        sign = RSAUtils.rsaSign(signature, key, params.get("sign_type"), params.get("charset"));
        try {
            sign = URLEncoder.encode(sign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sign;
    }

    private void onSuccessPsw(BaseResult<UserBean> body) {
        if ((body.statusCode.startsWith("2"))) {
            AppContext.showNovice = true;
            AppContext.notice = true;
            UserBean data = body.data;
            SPUtils.saveString(NewLoginActivity.this, CommonConstant.USER_TOKEN, body.token);
            SPUtils.saveString(NewLoginActivity.this, CommonConstant.MOBIL_IMEI, data.imei);
            ToastUtils.show(body.message);
            finish();
        } else {
            ToastUtils.show(body.message);
        }
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

    public void wxLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        AppContext.api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCode.removeCountDown();
    }
}
