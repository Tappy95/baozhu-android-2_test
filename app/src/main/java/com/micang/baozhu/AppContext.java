package com.micang.baozhu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.cmcm.cmgame.CmGameSdk;
import com.cmcm.cmgame.gamedata.CmGameAppInfo;
import com.component.dly.xzzq_ywsdk.YwSDK;
import com.iBookStar.views.YmConfig;
import com.liulishuo.filedownloader.FileDownloader;
import com.micang.baozhu.config.TTAdManagerHolder;
import com.micang.baozhu.http.bean.DisposeBean;
import com.micang.baozhu.util.CmGameImageLoader;
import com.micang.baselibrary.util.TLog;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;

public class AppContext extends Application {

    private static AppContext instance;
    public static boolean isfirst = true;
    public static boolean notice = true;
    //福利荟
    public static String fuLiHui = "";
    public static boolean fuLiHuiisfirst = true;
    //新手任务开关
    public static String noviceTask = "2";
    //    推荐游戏id
    public static String recommendGame = "";
    //更新
    public static DisposeBean.PVersionBean bean;
    //新手引导
    public static boolean showNovice = true;
    private static RequestQueue queues;

    public static String WX_APP_ID = "";
    public static String WX_APP_SECRET = "";
    public static IWXAPI api;
    private static final String APP_ID = "";        //免费小说APPID

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            JLibrary.InitEntry(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initAutoSize();
        fontSize();
        UMConfigure.init(this, "", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMShareAPI.get(this);

        UMConfigure.setLogEnabled(true);
        //微信
        PlatformConfig.setWeixin("", "");

        PlatformConfig.setQQZone("", "");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "518639d538", true);

        FileDownloader.setup(this);
        queues = Volley.newRequestQueue(getApplicationContext());
        registerToWX();
        YmConfig.initNovel(this, APP_ID);
        //穿山甲SDK初始化
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdManagerHolder.init(this);
       // initBaoqu();//游戏SDK初始化
        CallFromReflect();//小
    }

    private void initBaoqu() {
        // 注意：如果有多个进程，请务必确保这个初始化逻辑只会在一个进程里运行
        CmGameAppInfo cmGameAppInfo = new CmGameAppInfo();
        cmGameAppInfo.setAppId("");                             // GameSdkID，向我方申请
        cmGameAppInfo.setAppHost("");   // 游戏host地址，向我方申请

        // 【可选功能】游戏退出时，增加游戏推荐弹窗，提高游戏的点击个数，注释此行可去掉此功能
        cmGameAppInfo.setQuitGameConfirmFlag(true);

        // 【必须功能】设置游戏的广告id
        CmGameAppInfo.TTInfo ttInfo = new CmGameAppInfo.TTInfo();
        ttInfo.setRewardVideoId("");   // 激励视频
        ttInfo.setFullVideoId("");     // 全屏视频
        ttInfo.setExpressBannerId(""); // Banner广告，模板渲染，尺寸：600*150
        ttInfo.setExpressInteractionId(""); // 插屏广告，模板渲染，尺寸比例 2：3

        // 游戏加载时的插屏广告，广告样式为：插屏广告-原生-自渲染-大图
        // 在2019-7-17后，穿山甲只针对部分媒体开放申请，如后台无法申请到这个广告位，则无需调用代码
//        ttInfo.setLoadingNativeId("901121435");zz
        cmGameAppInfo.setTtInfo(ttInfo);

        CmGameSdk.initCmGameSdk(this, cmGameAppInfo, new CmGameImageLoader(), BuildConfig.DEBUG);
        TLog.d("cmgamesdk", "current sdk version : " + CmGameSdk.getVersion());

    }





    private void  CallFromReflect(){
        int res=MdidSdkHelper.InitSdk(this,true,new YuWan(this));
        Log.e("注册失败","--"+res);
    }

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        // 将该app注册到微信
        api.registerApp(WX_APP_ID);
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }

    private void initAutoSize() {
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setCustomFragment(true)
                .setExcludeFontScale(false)
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {

                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {

                    }
                });
    }


    private void fontSize() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    public static Context getInstance() {
        if (null == instance) {
            instance = new AppContext();
        }
        return instance;
    }
}
