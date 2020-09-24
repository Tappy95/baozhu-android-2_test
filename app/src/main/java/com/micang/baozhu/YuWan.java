package com.micang.baozhu;

import android.app.Application;

import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;
import com.component.dly.xzzq_ywsdk.YwSDK;

public class YuWan implements IIdentifierListener {
    private Application application;

    public YuWan(Application application) {
        this.application = application;
    }

    @Override
    public void OnSupport(boolean b, IdSupplier idSupplier) {
        if(idSupplier==null) {
            initXiaoYu(application,"失败");
            return;
        }
        String oaid=idSupplier.getOAID();
        String vaid=idSupplier.getVAID();
        String aaid=idSupplier.getAAID();
        initXiaoYu(application,oaid);
    }
    public static void initXiaoYu(Application application, String oaid){
        //YwSDK.setDebugMode();
        String appId="1483";
        String appSecret="yzkp444d9syun0cmdjodqv8av9ibqxaz";
        String mediaUserId="1483";
        YwSDK.Companion.init(application,appSecret,appId,mediaUserId,"1",oaid);
        YwSDK.Companion.refreshMediaUserId(mediaUserId);
        YwSDK.Companion.refreshAppSecret(appSecret,appId);
    }
}
