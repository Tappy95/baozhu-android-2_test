package com.micang.baozhu.module.earlyclock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.module.home.ShareQrcodeActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.TLog;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;

import static cn.bingoogolapple.qrcode.zxing.QRCodeEncoder.HINTS;

@BindEventBus
public class EarlyClockInviteActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "EarlyClockInviteActivity";
    private RelativeLayout head;
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout llPic;
    private ImageView ivQr;
    private LinearLayout llShare;
    private String qrcode;


    @Override
    public int layoutId() {
        return R.layout.activity_early_clock_invite;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        head = findViewById(R.id.head);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        llPic = findViewById(R.id.ll_pic);
        ivQr = findViewById(R.id.iv_qr);
        llShare = findViewById(R.id.ll_share);
        tvTitle.setText("邀请好友");
        llShare = findViewById(R.id.ll_share);
        initClick();
        if (EmptyUtils.isNotEmpty(qrcode)) {
            createQrcode(qrcode);
        } else {
            createQrcode("二维码");
        }
    }

    private void initClick() {
        llShare.setOnClickListener(this);
        llBack.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            UserBean data = event.data;
            if (EmptyUtils.isNotEmpty(data)) {
                qrcode = data.qrCode;
                if (EmptyUtils.isNotEmpty(qrcode)) {
                    createQrcode(qrcode);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_share:
                Bitmap bitmap = viewConversionBitmap(llPic);
//                int i = saveImageToGallery(bitmap);
//                if (i == 2) {
//                    ToastUtils.show(EarlyClockInviteActivity.this, "保存成功");
//                } else {
//                    ToastUtils.show(EarlyClockInviteActivity.this, "保存失败,请重试");
//                }
                ShareUtils.shareImage(EarlyClockInviteActivity.this, bitmap, new UMShareListener() {

                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        TLog.d(TAG, "开始了");
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
//                        ToastUtils.show("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        TLog.d(TAG, t.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
//                    ToastUtils.show(InviteApprenticeActivity.this, "取消了分享");
                    }
                });

                break;
            default:
                break;
        }
    }

    private void volleyGet(final String qrcode) {
        String encode = URLEncoder.encode(qrcode);
        String url = "http://bzlyplay.info/setUrl?url=" + encode;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {//s为请求返回的字符串数据
                        TLog.d("volley", s);
                        BaseResult baseResult = new Gson().fromJson(s, BaseResult.class);
                        String url = baseResult.data.toString();
                        createQrcode(url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        createQrcode(qrcode);
                        TLog.d("volley", volleyError.toString());
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testGet");
        //将请求加入全局队列中
        AppContext.getHttpQueues().add(request);
    }

    private void createQrcode(String qrcode) {
        Bitmap bitmap = syncEncodeQRCode(qrcode, BGAQRCodeUtil.dp2px(EarlyClockInviteActivity.this, 94), Color.BLACK, Color.WHITE,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        ivQr.setImageBitmap(bitmap);
    }

    private Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, int backgroundColor, Bitmap logo) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, HINTS);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * size + x] = foregroundColor;
                    } else {
                        pixels[y * size + x] = backgroundColor;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            if (null != logo) {
                return addLogoToQRCode(bitmap, logo);
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * view转bitmap
     */
    public Bitmap viewConversionBitmap(View view) {
        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        view.draw(c);
        return bmp;
    }

    private Bitmap addLogoToQRCode(Bitmap src, Bitmap logo) {
        if (src == null || logo == null) {
            return src;
        }
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    public int saveImageToGallery(Bitmap bmp) {

//        //文件名为时间
        String timeStamp = System.currentTimeMillis() + ".jpg";

        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pceggs" + File.separator + timeStamp;

        //获取文件
        File file = new File(downloadPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            EarlyClockInviteActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            return 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
