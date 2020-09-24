package com.micang.baozhu.module.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.http.BaseResult;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.util.EmptyUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.jaeger.library.StatusBarUtil;
import com.micang.baselibrary.util.TLog;

import java.net.URLEncoder;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;

import static cn.bingoogolapple.qrcode.zxing.QRCodeEncoder.HINTS;


public class QRcodeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private ImageView ivQr;
    private String qrcode;
    private TextView tvCode;

    @Override
    public int layoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("我的二维码");
        ivQr = findViewById(R.id.iv_qr);
        llBack.setOnClickListener(this);
        tvCode = findViewById(R.id.tv_code);
        Intent intent = getIntent();
        qrcode = intent.getStringExtra("QRCODE");
        if (EmptyUtils.isNotEmpty(qrcode)) {
            //String[] split = qrcode.split("qrCode=");
            if (EmptyUtils.isNotEmpty(qrcode)) {
                tvCode.setText(qrcode);
            }
            createQrcode(qrcode);
        } else {
            createQrcode("二维码");
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
        Bitmap bitmap = syncEncodeQRCode(qrcode, BGAQRCodeUtil.dp2px(QRcodeActivity.this, 300), Color.BLACK, Color.WHITE,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        ivQr.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, int backgroundColor, Bitmap logo) {
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


    private static Bitmap addLogoToQRCode(Bitmap src, Bitmap logo) {
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
}
