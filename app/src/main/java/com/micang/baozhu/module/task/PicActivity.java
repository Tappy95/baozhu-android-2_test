package com.micang.baozhu.module.task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PicActivity extends BaseActivity {


    private FrameLayout flayout;
    private ImageView pic;
    private Button save;

    @Override
    public int layoutId() {
        return R.layout.activity_pic;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
        Intent intent = getIntent();
        String picurl = intent.getStringExtra("pic");
        flayout = findViewById(R.id.framlayout);
        pic = findViewById(R.id.iv_pic);
        save = findViewById(R.id.btn_ok);
        Glide.with(this).load(picurl).into(pic);
        overridePendingTransition(R.anim.scale_in_500, R.anim.scale_out_500);
        flayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.scale_in_500, R.anim.scale_out_500);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = viewConversionBitmap(pic);
                int i = saveImageToGallery(bitmap);
                if (i == 2) {
                    ToastUtils.show(PicActivity.this, "保存成功");
                    overridePendingTransition(R.anim.scale_in_500, R.anim.scale_out_500);
                } else {
                    ToastUtils.show(PicActivity.this, "保存失败,请重试");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.scale_in_500, R.anim.scale_out_500);
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

    public int saveImageToGallery(Bitmap bmp) {
        //生成路径
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String dirName = "erweima16";
//        File appDir = new File(root, dirName);
//        if (!appDir.exists()) {
//            appDir.mkdirs();
//        }

//        //文件名为时间
        String timeStamp = System.currentTimeMillis() + ".jpg";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sd = sdf.format(new Date(timeStamp));
//        String fileName = sd;
        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pceggs" + File.separator + timeStamp;

        //获取文件
        File file = new File(downloadPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            PicActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
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
}
