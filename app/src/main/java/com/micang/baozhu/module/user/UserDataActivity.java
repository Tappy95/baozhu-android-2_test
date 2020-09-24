package com.micang.baozhu.module.user;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.VipBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.module.user.adapter.MyVIPAdapter;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.module.view.CircleImageView;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.LQRPhotoSelectUtils;
import com.micang.baselibrary.util.PermissionUtil;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.StringUtil;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class UserDataActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private FrameLayout flChangePic;
    private CircleImageView ivPic;
    private FrameLayout flChangeName;
    private TextView tvName;
    private FrameLayout flTwoCode;
    private FrameLayout flVipNmb;
    private FrameLayout flChangeMale;
    private TextView tvMale;
    private FrameLayout flChangeBirthday;
    private TextView tvBirthday;
    private TimePickerView pvCustomLunar;
    private FrameLayout flChangeQQ;
    private TextView tvQq;
    private OptionsPickerView genderPicker;
    private List<String> gender = new ArrayList<>();
    private BottomDialog mBottomDialog;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private String aliasName;
    private static final int CAMERA_CODE = 10086;
    private String male;
    private int sex;
    private String time;
    private String qrCode;
    private String data;
    private Configuration config;
    private UploadManager uploadManager;
    private RequestOptions requestOptions = RequestOptions.circleCropTransform();
    private RecyclerView recycleVippic;
    private MyVIPAdapter adapter;
    private List<VipBean> vipBeanList = new ArrayList<>();

    @Override
    public int layoutId() {
        return R.layout.activity_user_data;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.user_data);
        flChangePic = findViewById(R.id.fl_change_pic);
        ivPic = findViewById(R.id.iv_pic);
        flChangeName = findViewById(R.id.fl_change_name);
        tvName = findViewById(R.id.tv_name);
        flTwoCode = findViewById(R.id.fl_two_code);
        flVipNmb = findViewById(R.id.fl_vip_nmb);
        flChangeMale = findViewById(R.id.fl_change_male);
        tvMale = findViewById(R.id.tv_male);
        flChangeBirthday = findViewById(R.id.fl_change_birthday);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvQq = findViewById(R.id.tv_qq);
        flChangeQQ = findViewById(R.id.fl_change_QQ);
        recycleVippic = findViewById(R.id.recycle_vippic);
        recycleVippic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MyVIPAdapter(R.layout.my_vip_item, vipBeanList);
        recycleVippic.setAdapter(adapter);
        initClick();
        initLunarPicker();
        initViewAndEvent();
        initMale();
        config = new Configuration.Builder()

                .connectTimeout(10)
                .useHttps(true)
                .responseTimeout(20)


                .zone(FixedZone.zone0)
                .build();
        uploadManager = new UploadManager(config);

    }

    private void queryMyVips() {
        String token = SPUtils.token(UserDataActivity.this);
        String imei = SPUtils.imei(UserDataActivity.this);
        if (EmptyUtils.isEmpty(token) || EmptyUtils.isEmpty(imei)) {
            return;
        }
        HttpUtils.queryMyVips().enqueue(new Observer<BaseResult<List<VipBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                List<VipBean> data = (List<VipBean>) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    vipBeanList.clear();
                    vipBeanList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        queryMyVips();
    }

    private void getUserInfo() {

        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        HttpUtils.getUserInfo().enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (UserDataActivity.this == null || UserDataActivity.this.isFinishing()) {
                    return;
                }
                if (EmptyUtils.isEmpty(response.data)) {
                    return;
                }
                UserBean data = (UserBean) response.data;
                aliasName = data.aliasName;
                qrCode = data.qrCode;
                String birthday = data.birthday;
                sex = data.sex;
                if (sex == 2) {
                    tvMale.setText("女");
                } else {
                    tvMale.setText("男");
                }
                String profile = data.profile;//头像
                tvName.setText(aliasName);
                tvQq.setText(data.qqNum);
                if (EmptyUtils.isNotEmpty(birthday)) {
                    tvBirthday.setText(birthday);
                }
                if (EmptyUtils.isNotEmpty(profile)) {
                    Glide.with(UserDataActivity.this).load(profile).apply(requestOptions).into(ivPic);
                } else {
                    Glide.with(UserDataActivity.this).load("").placeholder(R.drawable.ic_user_pic).error(R.drawable.ic_user_pic).apply(requestOptions).into(ivPic);

                }
            }
        });
    }


    private void getUploadToken() {
        HttpUtils.getUploadTokenload().enqueue(new Observer<BaseResult<String>>() {
            @Override
            public void onSuccess(BaseResult response) {
                data = (String) response.data;
            }
        });
    }


    public void initViewAndEvent() {
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(final File outputFile, Uri outputUri) {
                Luban.with(UserDataActivity.this)
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
                        if (EmptyUtils.isNotEmpty(data) && EmptyUtils.isNotEmpty(key)) {
                            uploadManager.put(file, key, data, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {
                                        Log.i("qiniu", "Upload Success");
                                        //todo 上传头像
                                        final String url = CommonConstant.PICURL + key;
                                        changePic(url);
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
        }, true);
    }

    private void changePic(final String url) {
        HttpUtils.changePic(url).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                Glide.with(UserDataActivity.this).load(url).apply(requestOptions).into(ivPic);
            }
        });
    }


    private void initMale() {// 不联动的多级选项
        gender.add("男");
        gender.add("女");
        genderPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                male = gender.get(options1);
            }
        })
                .setLayoutRes(R.layout.gender_picker, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                genderPicker.returnData();
                                genderPicker.dismiss();
                                changeMale(male);
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                genderPicker.dismiss();
                            }
                        });
                    }
                })
                .setLineSpacingMultiplier(2.2f)
                .setDividerColor(ContextCompat.getColor(this, R.color.color_f7f7f7))
                .setContentTextSize(18)
                .setOutSideCancelable(false)
                .build();
        genderPicker.setPicker(gender);
    }


    private void initLunarPicker() {
        int WhiteTextColor = ContextCompat.getColor(this, R.color.color_f7f7f7);
        Calendar calendar = StringUtil.getCalendar("2018-02-23");
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                time = getTime(date);

            }
        })
                .setDate(calendar)//设置显示时间
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_data, new CustomListener() {

                    @Override
                    public void customLayout(final View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.returnData();
                                pvCustomLunar.dismiss();
                                changeBirthday(time);
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(WhiteTextColor)
                .build();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        flChangePic.setOnClickListener(this);
        flChangeName.setOnClickListener(this);
        flTwoCode.setOnClickListener(this);
        flVipNmb.setOnClickListener(this);
        flChangeMale.setOnClickListener(this);
        flChangeBirthday.setOnClickListener(this);
        flChangeQQ.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.fl_change_pic:
                if (EmptyUtils.isEmpty(data)) {
                    getUploadToken();
                }
                showPhotoPicker();
                break;
            case R.id.fl_change_name:
                String s = tvName.getText().toString();
                Intent intent = new Intent(UserDataActivity.this, ChangeNameActivity.class);
                intent.putExtra("NAME", s);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.fl_two_code:
                Intent intentQrcode = new Intent(UserDataActivity.this, QRcodeActivity.class);
                intentQrcode.putExtra("QRCODE", qrCode);
                startActivity(intentQrcode);
                break;
            case R.id.fl_change_male:
                genderPicker.show();
                break;
            case R.id.fl_change_QQ:
                String s1 = tvQq.getText().toString();
                Intent intent1 = new Intent(UserDataActivity.this, ChangeNameActivity.class);
                intent1.putExtra("NAME", s1);
                intent1.putExtra("type", "2");
                startActivity(intent1);
                break;
            case R.id.fl_change_birthday:
                String birthday = tvBirthday.getText().toString();
                Calendar calendar = StringUtil.getCalendar(birthday);
                pvCustomLunar.setDate(calendar);
                pvCustomLunar.show();

                break;
            default:
                break;
        }
    }

    private void changeBirthday(final String time) {

        HttpUtils.changebirthday(time).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                tvBirthday.setText(time);
                ToastUtils.show(UserDataActivity.this, "修改成功");
            }
        });

    }

    private void changeMale(String male) {
        if (male.equals("女")) {
            sex = 2;
        } else {
            sex = 1;
        }
        String s = String.valueOf(sex);
        changeMaleNet(s);

    }

    private void changeMaleNet(String s) {
        HttpUtils.changesex(s).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                tvMale.setText(male);
                ToastUtils.show(UserDataActivity.this, "修改成功");
            }
        });
    }


    private void showPhotoPicker() {
        mBottomDialog = new BottomDialog(this, true, true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_choose_photo, null);
        mBottomDialog.setContentView(contentView);

        mBottomDialog.findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PermissionsManager.applyPermissions(this, PermissionsManager.PERMISSIONS_CAMERA, PermissionsManager.CAMERA);
                //申请权限
                requestPermissions();
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.show();
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
