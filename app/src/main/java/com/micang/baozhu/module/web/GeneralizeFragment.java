package com.micang.baozhu.module.web;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.COOBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.home.MainActivity;
import com.micang.baozhu.module.user.PayCOOActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseFragment;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLEncoder;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/20 18:19
 * @describe describe
 */

@BindEventBus
public class GeneralizeFragment extends BaseFragment {
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private String outTradeNo;
    private UserBean data;
    private String apprentice;
    private String reward;
    private String qrCode;

    @Override
    protected int layoutId() {
        return R.layout.activity_generalize;
    }

    @Override
    protected void init(View rootView) {
        layout = rootView.findViewById(R.id.layout);

    }

    @Override
    protected void initData() throws NullPointerException {
        initAgentWeb();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            apprentice = data.apprentice;
            reward = data.reward;
            qrCode = data.qrCode;
        }
    }
    private void initAgentWeb() {
        String token = SPUtils.token(activity);
        String imei = SPUtils.imei(activity);
        int progressbarColor = ContextCompat.getColor(activity, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/#/enlighteningToMakeMoney?token=" + token + "&imei=" + imei);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, ((MainActivity) activity)));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);
    }

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, MainActivity activity) {

        }
        @JavascriptInterface
        public void toPay(String price, String descripte) {
            createOrder(price, descripte);
        }

        @JavascriptInterface
        public String inviteFriend(String s) {
            volleyGet();
            return s;
        }
    }
    private void createOrder(String price, String descripte) {
        if ("3".equals(descripte)) {
            descripte = "续费运营总监";

        } else {
            descripte = "升级为运营总监";
        }
        HttpUtils.tradeTame(descripte, price, "1").enqueue(new Observer<BaseResult<COOBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                COOBean data = (COOBean) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    Intent intent = new Intent(activity, PayCOOActivity.class);
                    intent.putExtra("COOBean", data);
                    outTradeNo = data.outTradeNo;
                    startActivity(intent);
                }
            }
        });
    }
    private void volleyGet() {
        String encode = URLEncoder.encode(qrCode);
        String url = "http://bzlyplay.info/setUrl?url=" + encode;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {//s为请求返回的字符串数据
                        TLog.d("volley", s);
                        BaseResult baseResult = new Gson().fromJson(s, BaseResult.class);
                        String url = baseResult.data.toString();
                        share(url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        share(qrCode);
                        TLog.d("volley", volleyError.toString());
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testGet");
        //将请求加入全局队列中
        AppContext.getHttpQueues().add(request);
    }
    private void share(String url) {
        ShareUtils.shareUrl(activity, R.drawable.icon_share_ic, url, "一亿人都想玩的赚钱APP", "动动手指边玩游戏边赚钱", new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                TLog.d("SHARE", "开始了");
            }


            @Override
            public void onResult(SHARE_MEDIA platform) {
                ToastUtils.show(activity, "分享成功");
            }


            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                TLog.d("SHARE", t.getMessage());
                ToastUtils.show(activity, t.getMessage());
            }


            @Override
            public void onCancel(SHARE_MEDIA platform) {
//                    ToastUtils.show(InviteApprenticeActivity.this, "取消了分享");
            }
        });
    }
}
