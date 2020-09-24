package com.micang.baozhu.module.web;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.task.TaskProgressActivity;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.LQRPhotoSelectUtils;
import com.micang.baselibrary.util.PermissionUtil;
import com.micang.baselibrary.util.TLog;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@BindEventBus
public class TaskActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private TextView tvShare;
    private Configuration config;
    private UploadManager uploadManager;
    private String qiniuToken;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private static final int CAMERA_CODE = 10086;
    private BottomDialog mBottomDialogP;
    private String urls;
    private UserBean data;
    private String qrCode;
    private int select = 0;

    @Override
    public int layoutId() {
        return R.layout.activity_task;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        tvShare = findViewById(R.id.tv_share);
        Intent intent = getIntent();
        urls = intent.getStringExtra("URLS");
        initAgentWeb();
        config = new Configuration.Builder()
                .connectTimeout(10)
                .useHttps(true)
                .responseTimeout(20)
                .zone(FixedZone.zone0)
                .build();
        uploadManager = new UploadManager(config);
        getUploadToken();
        initViewAndEvent();
        tvShare.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            qrCode = data.qrCode;
        }
    }

    private void getUploadToken() {
        HttpUtils.getUploadTokenload().enqueue(new Observer<BaseResult<String>>() {
            @Override
            public void onSuccess(BaseResult response) {
                qiniuToken = (String) response.data;
            }
        });
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.clearWebCache();
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initAgentWeb() {

        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(urls);
//                .go("http://192.168.1.24:8080/#/taskDetails?userId=10171&userChannel=baozhu&taskId=153&sign=f92a424b1e02bcf14f27454a87497b11");
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!agentWeb.back()) {
                finish();
            }
        }
    };

    @Override
    public void onClick(View view) {
        volleyGet();
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
        ShareUtils.shareUrl(TaskActivity.this, R.drawable.icon_share_ic, url, "199元任务奖励,快来和他一起赚钱吧!——快速赚", "下载宝猪乐园APP,进入快速赚,完成任务赏金就属于你", new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                TLog.d("SHARE", "开始了");
            }


            @Override
            public void onResult(SHARE_MEDIA platform) {
                ToastUtils.show(TaskActivity.this, "分享成功");
            }


            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                TLog.d("SHARE", t.getMessage());
                ToastUtils.show(TaskActivity.this, t.getMessage());
            }


            @Override
            public void onCancel(SHARE_MEDIA platform) {
//                    ToastUtils.show(InviteApprenticeActivity.this, "取消了分享");
            }
        });

    }

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, TaskActivity taskActivity) {

        }

        @JavascriptInterface
        public void selectPic() {
            showPhotoPicker();
        }

        @JavascriptInterface
        public void savePic(String url) {
            svPic(url);
        }

        @JavascriptInterface
        public void toTaskProgress(int state) {
            Intent intent = new Intent(TaskActivity.this, TaskProgressActivity.class);
            intent.putExtra("state", state);//1已提交,5已预约
            startActivity(intent);
            finish();
        }
    }

    public void initViewAndEvent() {

        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(final File outputFile, Uri outputUri) {
                Luban.with(TaskActivity.this)
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
                        // 这里进行图片上传，用服务器返回的数据加载图片，不要用本地的
                        String key = file.getName();
                        if (EmptyUtils.isNotEmpty(qiniuToken) && EmptyUtils.isNotEmpty(key)) {
                            uploadManager.put(file, key, qiniuToken, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {
                                        Log.i("qiniu", "Upload Success");
                                        final String url = CommonConstant.PICURL + key;

                                        agentWeb.getJsAccessEntrace().quickCallJs("getPic", url);
                                    } else {
                                        Log.i("qiniu", "Upload Fail");
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                    }
                                    Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
                                }

                            }, null);
                        } else {
                            getUploadToken();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
            }
        }, false);
    }

    private void showPhotoPicker() {
        mBottomDialogP = new BottomDialog(this, true, true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_choose_photo, null);
        mBottomDialogP.setContentView(contentView);

        mBottomDialogP.findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(1);//1 选择图片
                select = 1;
                mBottomDialogP.dismiss();
            }
        });

        mBottomDialogP.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PermissionsManager.applyPermissions(this, PermissionsManager.PERMISSIONS_CAMERA, PermissionsManager.CAMERA);
                //申请权限
                requestPermissions(2);//2拍照
                select = 2;
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

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void requestPermissions(int i) {
        if (!PermissionUtil.isPermissionGranted(this, Manifest.permission.CAMERA)
                || !PermissionUtil.isPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                PermissionGen.needPermission(this, CAMERA_CODE, perms);
            }
        } else {
            if (i == 1) {
                mLqrPhotoSelectUtils.selectPhoto();
            } else {
                mLqrPhotoSelectUtils.takePhoto();
            }
        }
    }

    @PermissionSuccess(requestCode = CAMERA_CODE)
    public void permissionSuccess() {
        if (select == 1) {
            mLqrPhotoSelectUtils.selectPhoto();
        } else {
            mLqrPhotoSelectUtils.takePhoto();
        }
    }

    @PermissionFail(requestCode = CAMERA_CODE)
    public void permissionFailed() {

        ToastUtils.show(this, "未授权权限，部分功能不能使用");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void svPic(String urls) {

        String timeStamp = System.currentTimeMillis() + ".jpg";
        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pceggs" + File.separator + timeStamp;
        FileDownloader.getImpl().create(urls)
                .setPath(downloadPath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        task.getPath();
                        task.getTargetFilePath();
                        //通知系统相册刷新
                        TaskActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(task.getPath()))));
                        ToastUtils.show(TaskActivity.this, "保存成功");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();

    }
}
