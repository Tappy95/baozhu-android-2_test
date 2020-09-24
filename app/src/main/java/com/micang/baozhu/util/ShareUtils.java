package com.micang.baozhu.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.List;

/**
 * @author LiHongCheng
 * @version 1.0
 * @Package com.xlyongba.yb.Utils
 * @E-mail diosamolee2014@gmail.com
 * @time 2018/12/4 11:29
 * @describe 分享相关工具类
 * <p>
 * UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
 * <p>
 * //所有分享需要权限申请
 * if(Build.VERSION.SDK_INT>=23){
 * String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
 * ActivityCompat.requestPermissions(this,mPermissionList,123);
 * }
 */
public class ShareUtils {

    public static SHARE_MEDIA[] share_media = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
//        public static SHARE_MEDIA[] share_media = {SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ};

    //分享文本
    public static void shareText(Activity mActivity, String text, UMShareListener umShareListener) {
        new ShareAction(mActivity)
                .withText(text)
                .setDisplayList(share_media)
                .setCallback(umShareListener).open();
    }


    //分享图片
    public static void shareImage(Activity mActivity, String imageurl, UMShareListener umShareListener) {
        doShareImage(mActivity, new UMImage(mActivity, imageurl), umShareListener);
    }

    public static void shareImage(Activity mActivity, File file, UMShareListener umShareListener) {
        doShareImage(mActivity, new UMImage(mActivity, file), umShareListener);
    }

    public static void shareImage(Activity mActivity, Bitmap bitmap, UMShareListener umShareListener) {
        doShareImage(mActivity, new UMImage(mActivity, bitmap), umShareListener);
    }

    public static void shareImage(Activity mActivity, int res, UMShareListener umShareListener) {
        doShareImage(mActivity, new UMImage(mActivity, res), umShareListener);
    }

    public static void shareImage(Activity mActivity, Byte bitmap, UMShareListener umShareListener) {
        doShareImage(mActivity, new UMImage(mActivity, bitmap), umShareListener);
    }

    public static void doShareImage(Activity mActivity, UMImage image, UMShareListener umShareListener) {
        ShareAction share = new ShareAction(mActivity).setDisplayList(share_media).withText("宝猪乐园").withMedia(image);
        share.setCallback(umShareListener).open();
    }

    public static void ShareImage(Activity mActivity, Bitmap bitmap, SHARE_MEDIA share_media, UMShareListener umShareListener) {
        ShareAction share = new ShareAction(mActivity).setPlatform(share_media).withText("宝猪乐园").withMedia(new UMImage(mActivity, bitmap));
        share.setCallback(umShareListener).share();
    }

    //分享web
    public static void shareUrl(Activity mActivity, String imageurl, String url, String title, String description, UMShareListener umShareListener) {
        dpShareUrl(mActivity, new UMImage(mActivity, imageurl), url, title, description, umShareListener);
    }

    public static void shareUrl(Activity mActivity, File file, String url, String title, String description, UMShareListener umShareListener) {
        dpShareUrl(mActivity, new UMImage(mActivity, file), url, title, description, umShareListener);
    }

    public static void shareUrl(Activity mActivity, Bitmap bitmap, String url, String title, String description, UMShareListener umShareListener) {
        dpShareUrl(mActivity, new UMImage(mActivity, bitmap), url, title, description, umShareListener);
    }

    public static void shareUrl(Activity mActivity, int res, String url, String title, String description, UMShareListener umShareListener) {
        dpShareUrl(mActivity, new UMImage(mActivity, res), url, title, description, umShareListener);
    }

    public static void shareUrl(Activity mActivity, Byte bitmap, String url, String title, String description, UMShareListener umShareListener) {
        dpShareUrl(mActivity, new UMImage(mActivity, bitmap), url, title, description, umShareListener);
    }

    public static void dpShareUrl(Activity mActivity, UMImage image, String url, String title, String description, UMShareListener umShareListener) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        new ShareAction(mActivity).setDisplayList(share_media).withMedia(web).setCallback(umShareListener).open();
    }

    public static void share(SHARE_MEDIA share_media, Activity mActivity, int res, String url, String title, String description, UMShareListener umShareListener) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(mActivity, res));  //缩略图
        web.setDescription(description);//描述
        new ShareAction(mActivity).setPlatform(share_media).withMedia(web).setCallback(umShareListener).share();
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}
