package com.micang.baozhu.module.user;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.LQRPhotoSelectUtils;
import com.micang.baselibrary.util.PermissionUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.PictureBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.user.adapter.SelectPicAdapter;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class NewFeedBackActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private FrameLayout flSelectType;
    private TextView tvType;
    private EditText etDetails;
    private RecyclerView recycleview;
    private EditText etEmail;
    private Button btnSubmit;
    private BottomDialog mBottomDialog;
    String opinionType;
    private SelectPicAdapter adapter;
    private List<PictureBean> list = new ArrayList<>();
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private BottomDialog mBottomDialogP;
    private static final int CAMERA_CODE = 10086;
    private Configuration config;
    private UploadManager uploadManager;
    private String data;
    private boolean canSubmit = false;
    String url = "";
    int num = 0;

    @Override
    public int layoutId() {
        return R.layout.activity_new_feed_back;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("用户反馈");
        flSelectType = findViewById(R.id.fl_select_type);
        tvType = findViewById(R.id.tv_type);
        etDetails = findViewById(R.id.et_details);
        recycleview = findViewById(R.id.recycleview);
        etEmail = findViewById(R.id.et_email);
        btnSubmit = findViewById(R.id.btn_submit);
        initClick();
        initRecycleView();
        initData();
        initViewAndEvent();
        config = new Configuration.Builder()

                .connectTimeout(10)
                .useHttps(true)
                .responseTimeout(20)


                .zone(FixedZone.zone0)
                .build();
        uploadManager = new UploadManager(config);
    }

    private void initRecycleView() {
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new SelectPicAdapter(list);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_add) {
                    if (list.size() >= 4) {
                        ToastUtils.show("最多上传三张图片");
                    } else {
                        showPhotoPicker();
                    }
                } else {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        recycleview.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EmptyUtils.isEmpty(data)) {
            getUploadToken();
        }
    }

    private void initData() {
        PictureBean pictureBean = new PictureBean();
        pictureBean.setCode(0);
        list.add(pictureBean);
        adapter.notifyDataSetChanged();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        flSelectType.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.fl_select_type:
                showTypePicker();
                break;
            case R.id.btn_submit:
                if (EmptyUtils.isEmpty(opinionType)) {
                    ToastUtils.show("请选择意见类型");
                    return;
                }
                String detail = etDetails.getText().toString().trim();
                if (EmptyUtils.isEmpty(detail)) {
                    ToastUtils.show("请输入您的问题");
                    return;
                }
                String email = etEmail.getText().toString().trim();
                if (EmptyUtils.isEmpty(email)) {
                    ToastUtils.show("请输入您的邮箱");
                    return;
                }
                checkPic(detail, email);
                break;

            default:
                break;
        }
    }

    private void feedback(String detail, String email) {
        HttpUtils.feedback(opinionType, detail, url, email).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

                ToastUtils.show("提交成功");
                finish();
            }
        });

    }

    private void checkPic(String detail, String email) {
        if (list.size() < 2) {
            feedback(detail, email);
        } else {
            for (int i = 1; i < list.size(); i++) {
                PictureBean pictureBean = list.get(i);
                File file = pictureBean.path;
                String key = file.getPath();
                upload(file, key, detail, email);
            }
        }
    }

    private void upload(File file, final String key1, final String detail, final String email) {
        uploadManager.put(file, key1, data, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    num++;
                    Log.i("qiniu", "Upload Success");
                    //todo 上传
                    final String s = CommonConstant.PICURL + key1;
                    url = url + "," + s;
                    if (num == (list.size() - 1)) {
                        feedback(detail, email);
                    }
                } else {
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    ToastUtils.show("提交失败请重试");
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
            }

        }, null);

    }

    private void getUploadToken() {
        HttpUtils.getUploadTokenload().enqueue(new Observer<BaseResult<String>>() {
            @Override
            public void onSuccess(BaseResult response) {
                data = (String) response.data;
            }
        });
    }

    private void showTypePicker() {
        mBottomDialog = new BottomDialog(this, true, true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_choose_type, null);
        mBottomDialog.setContentView(contentView);

        final TextView tv1 = mBottomDialog.findViewById(R.id.tv_1);
        final TextView tv2 = mBottomDialog.findViewById(R.id.tv_2);
        final TextView tv3 = mBottomDialog.findViewById(R.id.tv_3);
        final TextView tv4 = mBottomDialog.findViewById(R.id.tv_4);
        final TextView tv5 = mBottomDialog.findViewById(R.id.tv_5);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tv1.getText().toString();
                tvType.setText(s1);
                opinionType = "1";
                mBottomDialog.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tv2.getText().toString();
                tvType.setText(s1);
                opinionType = "2";
                mBottomDialog.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tv3.getText().toString();
                tvType.setText(s1);
                opinionType = "3";
                mBottomDialog.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tv4.getText().toString();
                tvType.setText(s1);
                opinionType = "4";
                mBottomDialog.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tv5.getText().toString();
                tvType.setText(s1);
                opinionType = "5";
                mBottomDialog.dismiss();
            }
        });
        mBottomDialog.show();
    }

    public void initViewAndEvent() {
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(final File outputFile, Uri outputUri) {
                Luban.with(NewFeedBackActivity.this)
                        .load(outputFile)
                        .ignoreBy(100)
                        .setTargetDir(getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        }).setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(final File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        PictureBean pictureBean = new PictureBean();
                        pictureBean.setCode(1);
                        pictureBean.setPath(file);
                        list.add(pictureBean);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
            }
        }, false);
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    private void showPhotoPicker() {
        mBottomDialogP = new BottomDialog(this, true, true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_choose_photo, null);
        mBottomDialogP.setContentView(contentView);

        mBottomDialogP.findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                mBottomDialogP.dismiss();
            }
        });

        mBottomDialogP.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PermissionsManager.applyPermissions(this, PermissionsManager.PERMISSIONS_CAMERA, PermissionsManager.CAMERA);
                //申请权限
                requestPermissions();
                mBottomDialogP.dismiss();
            }
        });

        mBottomDialogP.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialogP.dismiss();
            }
        });

        mBottomDialogP.show();
    }

    private void checkPermission() {

        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //申请的权限全部允许
                    //Toast.makeText(LoginActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                    mLqrPhotoSelectUtils.selectPhoto();
                } else {
                    //只要有一个权限被拒绝，就会执行
                    ToastUtils.show("未授权权限，部分功能不能使用");
                }
            }

        });
    }

    private void requestPermissions() {
        if (!PermissionUtil.isPermissionGranted(this, Manifest.permission.CAMERA)
                || !PermissionUtil.isPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                PermissionGen.needPermission(this, CAMERA_CODE, perms);
            }
        } else {
            mLqrPhotoSelectUtils.takePhoto();
        }
    }

    @PermissionSuccess(requestCode = CAMERA_CODE)
    public void permissionSuccess() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionFail(requestCode = CAMERA_CODE)
    public void permissionFailed() {

        ToastUtils.show(this, "未授权权限，部分功能不能使用");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
