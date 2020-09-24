package com.micang.baozhu.module.web;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.ChangeBean;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class MYGameDetailsActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout webView;
    private AgentWeb agentWeb;
    private String urls;
    private GameBean.ListBean bean;
    private MyCommonPopupWindow backPop;
    ImageView ivGamePic;
    TextView tvDetails;
    TextView tvMoney;
    Button btBegin;
    private int count = 0;
    private int go = 0;
    private String gameId;
    private String interfaceName;
    private UserBean data;
    private String moblie;
    private ChangeBean gameBean;
    private GyrosensorUtils gyrosensor;

    @Override
    public int layoutId() {
        return R.layout.activity_game_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        Intent intent = getIntent();
        urls = intent.getStringExtra("URLS");
        bean = (GameBean.ListBean) intent.getSerializableExtra("bean");
        webView = findViewById(R.id.layout);
        tvTitle.setText("游戏详情");
        llBack.setOnClickListener(listener);
        initAgentWeb();
        gyrosensor = GyrosensorUtils.getInstance(this);
    }

    @Override
    protected void onResume() {
        if (agentWeb != null) {
            agentWeb.getWebCreator().getWebView().reload();
        }
        agentWeb.getWebLifeCycle().onResume();
        addGyro();
        super.onResume();
    }

    private void addGyro() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        CoordinatesBean coordinates = gyrosensor.getCoordinates();
        String anglex = String.format("%.2f", coordinates.anglex);
        String angley = String.format("%.2f", coordinates.angley);
        String anglez = String.format("%.2f", coordinates.anglez);
        HttpUtils.addGyro(anglex, angley, anglez, 3).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            moblie = data.mobile;
        }
    }

    private void initAgentWeb() {
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(urls);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new MYGameDetailsActivity.AndroidInterface(agentWeb, this));
//        agentWeb.getJsAccessEntrace().quickCallJs("OpenApp_Return", "0");
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (count == 0) {
                initbackPopWindow();
            } else {
                finish();
            }

        }
    };

    private void initbackPopWindow() {
        if (this.isFinishing()) {
            return;
        }
        count++;
        backPop = new MyCommonPopupWindow(this, R.layout.game_back_pop_new, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();

                LinearLayout llChangeGame;
                LinearLayout llbacklist;
                ImageView flClose;
                ImageView ivwelfare;

                ivwelfare = view.findViewById(R.id.iv_welfare);
                llbacklist = view.findViewById(R.id.ll_back_list);
                ivGamePic = view.findViewById(R.id.tv_1);
                tvDetails = view.findViewById(R.id.tv_details);
                tvMoney = view.findViewById(R.id.tv_money);
                llChangeGame = view.findViewById(R.id.ll_change_game);
                btBegin = view.findViewById(R.id.bt_begin);
                flClose = view.findViewById(R.id.fl_close);
                tvDetails.setText(bean.gameTitle);
                Glide.with(MYGameDetailsActivity.this).load(R.drawable.icon_bg_changegame).into(ivwelfare);
                String str = "当前任务最高可奖励 <font color='#FF3C2E'>" + bean.gameGold + "</font>" + " 元";
                tvMoney.setText(Html.fromHtml(str));
                Glide.with(MYGameDetailsActivity.this).load(bean.icon).into(ivGamePic);
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeGame();

                    }
                });
                flClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getPopupWindow().dismiss2();

                    }
                });

                ivwelfare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();
                        String fuLiHui = AppContext.fuLiHui;
                        if (EmptyUtils.isEmpty(fuLiHui)) {
                            ToastUtils.show("功能待开发");
                        } else {
                            Intent intent = new Intent(MYGameDetailsActivity.this, WelfareActivity.class);
                            intent.putExtra("url", fuLiHui);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                llbacklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getPopupWindow().dismiss2();
                        finish();
                    }
                });
                btBegin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断赚钱还是跳别的界面
                        if (go == 0) {
                            getPopupWindow().dismiss2();
                        } else {
                            getUrl();
                        }
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                NewPopWindow instance = getPopupWindow();
                instance.canDismiss = false;
                instance.setOnDismissListener(new NewPopWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        NewPopWindow popupWindow = backPop.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        backPop.getPopupWindow().showAtLocation(llBack, Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void getUrl() {
        if ("PCDD".equals(interfaceName)) {
            HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(MYGameDetailsActivity.this, NextPCddGameDetailActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    backPop.getPopupWindow().dismiss2();
                    startActivity(intent);
                    finish();
                }
            });
        }
        if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
            HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(MYGameDetailsActivity.this, NextMYGameDetailsActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    backPop.getPopupWindow().dismiss2();
                    startActivity(intent);
                    finish();
                }
            });
        }
        if ("xw-Android".equals(interfaceName)) {
            HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(MYGameDetailsActivity.this, NextXWGameDetailActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    backPop.getPopupWindow().dismiss2();
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (count == 0) {
                initbackPopWindow();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeGame() {
        HttpUtils.recommendGame().enqueue(new Observer<BaseResult<ChangeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                gameBean = (ChangeBean) response.data;
                if (EmptyUtils.isNotEmpty(gameBean)) {
                    if (MYGameDetailsActivity.this.isFinishing()) {
                        return;
                    }

                    Glide.with(MYGameDetailsActivity.this).load(gameBean.icon).into(ivGamePic);
                    tvDetails.setText(gameBean.gameTitle);
                    String str = "当前任务最高可奖励 <font color='#FF3C2E'>" + gameBean.gameGold + "</font>" + " 元";
                    tvMoney.setText(Html.fromHtml(str));
                    btBegin.setText("开始赚钱");
                    interfaceName = gameBean.interfaceName;
                    gameId = gameBean.id;
                    go++;
                }
            }
        });
    }

    public class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, MYGameDetailsActivity gameDetailsActivity) {
        }

        @JavascriptInterface
        public String CheckInstall(String s) {
            if (EmptyUtils.isNotEmpty(s)) {
                boolean appInstalled = AppUtils.isAppInstalled(s);
                if (appInstalled) {
                    s = "1";
                    agentWeb.getJsAccessEntrace().quickCallJs("CheckInstall_Return", "1");
                } else {
                    s = "0";
                    agentWeb.getJsAccessEntrace().quickCallJs("CheckInstall_Return", "0");
                }
            }
            return s;
        }

        @JavascriptInterface
        public String OpenApp(String s) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(s);
            if (intent != null) {
                intent.putExtra("type", "110");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            return s;
        }

        @JavascriptInterface
        public String InstallAPP(String urls) {
            openBrowser(MYGameDetailsActivity.this, urls);
            return urls;
        }
    }


    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url)); // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名 // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么 L.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }


}
