package com.micang.baozhu.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.micang.baozhu.AppContext;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventWXBind;
import com.micang.baselibrary.event.EventWXPay;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    // 获取第一步的code后，请求以下链接获取access_token
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean intent = AppContext.api.handleIntent(getIntent(), this);
        if (!intent) {
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {

        if (EmptyUtils.isNotEmpty(((SendAuth.Resp) resp).code)) {
            String code = ((SendAuth.Resp) resp).code;
            EventBus.getDefault().postSticky(new EventWXBind<>(EventCode.wxcode, code));
        }
//        String result = "";
//        if (resp != null) {
//            resp = resp;
//        }
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                result = "发送成功";
////                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//                String code = ((SendAuth.Resp) resp).code;
//                Log.d("WXEntryActivity", "--------------------- " + code);
//
//                /*
//                 * 将你前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
//                 */
//                String get_access_token = getCodeRequest(code);
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    final Request request = new Request.Builder()
//                            .url(get_access_token)//请求接口。如果需要传参拼接到接口后面。
//                            .build();//创建Request 对象
//
//                    System.err.print(get_access_token);
//
//                    Call call = client.newCall(request);
//
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            if (response.isSuccessful()) {
//
//                                String res = response.body().string();
//                                Log.d("kwwl", "response.code()==" + response.code());
//                                Log.d("kwwl", "response.message()==" + response.message());
//                                Log.d("kwwl", "res==" + res);
//                                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
//
//                                System.err.print(response.body());
//                                Log.d("ttttttttttttttttttttttt", res);
//                                JSONObject jsonObject = null;
//                                try {
//                                    jsonObject = new JSONObject(res);
//                                    String access_token = jsonObject.getString("access_token");
//                                    String openid = jsonObject.getString("openid");
//                                    String get_user_info_url = getUserInfo(
//                                            access_token, openid);
//                                    getUserInfo(get_user_info_url);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//                } catch (Exception ex) {
//                    ToastUtils.show(ex.getMessage());
//                    ex.printStackTrace();
//                }
//
//
//                finish();
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = "发送取消";
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//                finish();
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = "发送被拒绝";
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//                finish();
//                break;
//            default:
//                result = "发送返回";
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//                finish();
//                break;
//        }
        finish();
    }

    /**
     * 通过拼接的用户信息url获取用户信息
     *
     * @param user_info_url
     */
    private void getUserInfo(String user_info_url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(user_info_url)//请求接口。如果需要传参拼接到接口后面。
                    .build();//创建Request 对象
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(result);
                            }
                        });

                        Log.d("kwwl", "response.code()==" + response.code());
                        Log.d("kwwl", "response.message()==" + response.message());
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                        if (!response.equals("")) {
                            String openid = null;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                openid = jsonObject.getString("openid");
                                String nickname = jsonObject.getString("nickname");
                                String headimgurl = jsonObject.getString("headimgurl");
                                System.err.print("openid:" + openid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });


        } catch (Exception ex) {

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AppContext.api.handleIntent(intent, this);
        finish();
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code 授权时，微信回调给的
     * @return URL
     */
    private String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID",
                urlEnodeUTF8(AppContext.WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET",
                urlEnodeUTF8(AppContext.WX_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;
    }

    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token 获取access_token时给的
     * @param openid       获取access_token时给的
     * @return URL
     */
    private String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
                urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }

    private String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
