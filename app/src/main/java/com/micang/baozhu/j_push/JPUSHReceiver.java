package com.micang.baozhu.j_push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;


public class JPUSHReceiver extends BroadcastReceiver {
    private static final String TAG = "JPUSH";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

//                ClipboardManager systemService = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                systemService.setPrimaryClip(ClipData.newPlainText("text", regId));
//                send the Registration Id to your server...
//                SharedPreferenceUtils.put(context, SpKey.JPUSH_ID, regId);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver]   ------------EXTRA_MESSAGE" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                Log.d(TAG, "[MyReceiver]   ------------EXTRA_TITLE" + bundle.getString(JPushInterface.EXTRA_TITLE));
                Log.d(TAG, "[MyReceiver]   ------------EXTRA_CONTENT_TYPE" + bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));
                Log.d(TAG, "[MyReceiver]   ------------EXTRA_EXTRA" + bundle.getString(JPushInterface.EXTRA_EXTRA));
                Log.d(TAG, "[MyReceiver]   ------------EXTRA_STATUS" + bundle.getString(JPushInterface.EXTRA_STATUS));
//                if (EmptyUtils.isNotEmpty(bundle.getString(JPushInterface.EXTRA_MESSAGE)) && !bundle.getString(JPushInterface.EXTRA_MESSAGE).equals("{}")) {
//                    ToastUtils.show("EXTRA_MESSAGE:+" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                }
                processCustomMessage(context, bundle);
//                JPushReceiverBean jPushReceiverBean = new Gson().fromJson(bundle.getString(JPushInterface.EXTRA_MESSAGE), JPushReceiverBean.class);
//                if (null != jPushReceiverBean) {
//
//                    //获取android bean
//
//                    NotificationManager mNotifyMgr =
//                            (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//                    //打开自定义的Activity 1 高街币  2 关注商品上新  3 订单提醒   过期和即将过期提醒',
//                    Intent mainIntent = null;
//                    String token = SPUtils.token(AppContext.getInstance());
//
//                    if (EmptyUtils.isEmpty(token)){ //如果退出登录直接点击推送直接跳转登录页面
//                        mainIntent=new Intent(context, LoginActivity.class);
//                    }else {
////                        switch (jPushReceiverBean.CONTENT_TYPE()) {
////                            case 1:
////                                mainIntent = new Intent(context, CoinsActivity.class);
////                                break;
////                            case 2:
////                                ActivityUtils.toMain();
////                                break;
////                            case 3:
////                                mainIntent = new Intent(context, MyOrderActivity.class); //3 订单提醒   过期和即将过期提醒
////                                break;
////                            default:
////                                mainIntent = new Intent(context, MainActivity.class);
////                        }
//                    }
//
//                    PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    //创建 Notification.Builder 对象
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            //点击通知后自动清除
//                            .setAutoCancel(true)
//                            .setContentTitle("宝猪乐园")
//                            .setContentText(jPushReceiverBean.MESSAGE)
//                            .setContentIntent(mainPendingIntent);
//                    //发送通知
//                    mNotifyMgr.notify(jPushReceiverBean.MSG_ID, builder.build());
//                }
////
//                //点亮屏幕
//                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
//                mWakelock.acquire();
//                mWakelock.release();

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//                receivingNotification(context, bundle);
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

//                if (EmptyUtils.isNotEmpty(extras) && !extras.equals("{}")){
//                    ToastUtils.show("EXTRA_EXTRA:+"+extras);
//                }
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                // openNotification(context, bundle);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
//                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nrelease.keystore:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nrelease.keystore:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nrelease.keystore:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
//                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nrelease.keystore:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AppContext.getInstance(), 0, intent, 0);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.e(TAG, "8.0处理了");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//定义通知id
//                int notificationId = (int) System.currentTimeMillis();//定义通知id
                String channelId = context.getPackageName();//通知渠道id
                String channelName = "消息通知";//"PUSH_NOTIFY_NAME";//通知渠道名
                int importance = NotificationManager.IMPORTANCE_HIGH;//通知级别
                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                channel.enableLights(true);//设置闪光灯
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);//设置通知出现震动
                channel.setShowBadge(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
                Log.i("123321", notificationId + "");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String s = bundle.getString(JPushInterface.EXTRA_EXTRA);

                builder.setContentTitle("宝猪会员")//设置通知栏标题
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                        .setChannelId(channelId)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                ;
                Notification notification = builder.build();

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                if (notificationManager != null) {
                    notificationManager.notify(notificationId, notification);
                }
            } else {

                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//定义通知id
//                int notificationId = (int) System.currentTimeMillis();//定义通知id
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                Log.i("123321", notificationId + "");
                notification.setAutoCancel(true)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("宝猪会员")
                        .setSmallIcon(R.mipmap.ic_launcher);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Log.i("12332155", extras);

                notificationManager.notify(notificationId, notification.build());

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "极光推送出错:" + e.getMessage());
        }
    }
}



