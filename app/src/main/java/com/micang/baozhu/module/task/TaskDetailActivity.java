package com.micang.baozhu.module.task;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.task.GetTaskSuccessBean;
import com.micang.baozhu.http.bean.task.SubmitaskBean;
import com.micang.baozhu.http.bean.task.TaskDetailBean;
import com.micang.baozhu.http.bean.task.TaskPictureBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.task.adapter.PicQrcodeAdapter;
import com.micang.baozhu.module.task.adapter.TaskSelectPicAdapter;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.LQRPhotoSelectUtils;
import com.micang.baselibrary.util.PermissionUtil;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.util.WindowUtils;
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
import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@BindEventBus
public class TaskDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int CAMERA_CODE = 10086;
    private LayoutInflater mLayountInflater;
    private LinearLayout taskexplainLayout;
    private LinearLayout linearLayout;
    private RelativeLayout makeMoneyStep;
    private RelativeLayout llSubmitStep;
    private NestedScrollView srcrollview;
    private LinearLayout llBack;
    private TextView tvShare;
    private ImageView ivGamePic;
    private TextView tvTaskName;
    private TextView tvTime;
    private TextView tvReward;
    private TextView tvNotice;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private List<TaskPictureBean> list = new ArrayList<>();
    private BottomDialog mBottomDialogP;
    private TaskSelectPicAdapter adapter = new TaskSelectPicAdapter(list);
    private String qiniuToken;
    private List<TaskDetailBean.TaskSubmitBean> taskSubmit = new ArrayList<>();
    private List<SubmitaskBean> submitaskBeanList = new ArrayList<>();
    private LinearLayout llSubmitTask;
    private LinearLayout taskLoadingLayout;
    private int id;
    private CountdownView countdown;
    private LinearLayout llState;
    private TextView tvState;
    private LinearLayout llShare;
    private LinearLayout llGetTask;
    private LinearLayout llAfterSubmit;
    private LinearLayout llSubscribe;
    private LinearLayout llallstate;
    private int taskId;
    private int lTpTaskId;
    private UploadManager uploadManager;
    private Configuration config;
    private UserBean data;
    private String qrCode;
    private NewCommonDialog commonDialog;
    private LinearLayout llSubscribeOver;
    private int state = 0;
    private NewCommonDialog subscribeDialog;
    private String logo;
    private long orderTime;
    private String reward;

    @Override
    public int layoutId() {
        return R.layout.activity_task_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        WindowUtils.setPicTranslucentToStatus(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        initView();
        config = new Configuration.Builder()
                .connectTimeout(10)
                .useHttps(true)
                .responseTimeout(20)
                .zone(FixedZone.zone0)
                .build();
        uploadManager = new UploadManager(config);
        initclick();
        getUploadToken();
        getTaskDetail(id);
        initViewAndEvent();
    }

    private void initView() {
        linearLayout = findViewById(R.id.linearLayout);
        llallstate = findViewById(R.id.ll_all_state);
        taskLoadingLayout = findViewById(R.id.task_loading_layout);
        mLayountInflater = LayoutInflater.from(this);
        srcrollview = findViewById(R.id.srcrollview);
        llBack = findViewById(R.id.ll_back);
        tvShare = findViewById(R.id.tv_share);
        ivGamePic = findViewById(R.id.tv_1);
        tvTaskName = findViewById(R.id.tv_task_name);
        tvTime = findViewById(R.id.tv_time);
        tvReward = findViewById(R.id.tv_reward);
        tvNotice = findViewById(R.id.tv_notice);
        llSubmitTask = findViewById(R.id.ll_submit_task);
        countdown = findViewById(R.id.countdown);

        llAfterSubmit = findViewById(R.id.ll_after_submit);
        llSubscribe = findViewById(R.id.ll_subscribe);
        llSubscribeOver = findViewById(R.id.ll_subscribe_over);
        llState = findViewById(R.id.ll_state);
        tvState = findViewById(R.id.tv_state);
        llShare = findViewById(R.id.ll_share);
        llGetTask = findViewById(R.id.ll_getTask);

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

    private void initclick() {
        llBack.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llState.setOnClickListener(this);

        llGetTask.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                getTask();
            }
        });

        llSubscribe.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //立即预约
                showSubscribe();
            }
        });
        llSubscribeOver.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //查看进度
                Intent intent = new Intent(TaskDetailActivity.this, TaskProgressActivity.class);
                intent.putExtra("state", 5);
                startActivity(intent);
                finish();
            }
        });
        llSubmitTask.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                for (int i = 0; i < submitaskBeanList.size(); i++) {
                    if (EmptyUtils.isEmpty(submitaskBeanList.get(i).getSubmitValue())) {
                        ToastUtils.show("请按提示录入信息");
                        return;
                    }
                }
                for (int i = 0; i < taskSubmit.size(); i++) {
                    if (taskSubmit.get(i).type == 2) {
                        SubmitaskBean putTaskBean = new SubmitaskBean();
                        putTaskBean.setSubmitId(taskSubmit.get(i).id);
                        putTaskBean.setSubmitName(taskSubmit.get(i).name);
                        putTaskBean.setType("2");
                        if (list.size() < 2) {
                            ToastUtils.show("请选择图片上传");
                            return;
                        }
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int j = 1; j < list.size(); j++) {
                            String key = list.get(j).getPath();
                            stringBuffer.append(key + ",");
                        }
                        putTaskBean.setSubmitValue(stringBuffer.toString());
                        submitaskBeanList.add(putTaskBean);
                    }

                }
                Gson gson = new Gson();
                String s = gson.toJson(submitaskBeanList);
//                ToastUtils.show(s);
                TLog.d("sss", s);
                submitTask();

            }
        });
    }

    private void goSubscribe() {
        HttpUtils.addYY(id).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.toastProcess(TaskDetailActivity.this, "预约成功");
                llGetTask.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.GONE);
                llSubscribeOver.setVisibility(View.VISIBLE);
                llAfterSubmit.setVisibility(View.GONE);
                llSubscribe.setVisibility(View.GONE);
            }
        });
    }

    private void submitTask() {
        HttpUtils.userTptaskSubmit(taskId, lTpTaskId, submitaskBeanList).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.toastProcess(TaskDetailActivity.this, "提交成功,审核中");
                Intent intent = new Intent(TaskDetailActivity.this, TaskProgressActivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                for (int i = 0; i < submitaskBeanList.size(); i++) {
                    if (submitaskBeanList.get(i).getType().equals("2")) {
                        submitaskBeanList.remove(i);
                    }
                }
                ToastUtils.show(msg);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                for (int i = 0; i < submitaskBeanList.size(); i++) {
                    if (submitaskBeanList.get(i).getType().equals("2")) {
                        submitaskBeanList.remove(i);
                    }
                }
            }
        });
    }

    private void getUploadToken() {
        HttpUtils.getUploadTokenload().enqueue(new Observer<BaseResult<String>>() {
            @Override
            public void onSuccess(BaseResult response) {
                qiniuToken = (String) response.data;
            }
        });
    }

    private void getTaskDetail(int id) {
        HttpUtils.tptaskInfo(id).enqueue(new Observer<BaseResult<TaskDetailBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    llallstate.setVisibility(View.VISIBLE);
                    taskLoadingLayout.setVisibility(View.GONE);
                    srcrollview.setVisibility(View.VISIBLE);
                    TaskDetailBean data = (TaskDetailBean) response.data;
                    addView(data);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                taskLoadingLayout.setVisibility(View.GONE);
                srcrollview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                taskLoadingLayout.setVisibility(View.GONE);
                srcrollview.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addView(TaskDetailBean data) {
        TaskDetailBean.TaskInfoBean taskInfo = data.taskInfo;

        if (EmptyUtils.isNotEmpty(taskInfo)) {
            if (this.isFinishing()) {
                return;
            }
            logo = taskInfo.logo;
            reward = taskInfo.reward;
            orderTime = taskInfo.orderTime;
            if (taskInfo.isUpper == 3) {
                llGetTask.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.GONE);
                llSubscribeOver.setVisibility(View.GONE);
                llAfterSubmit.setVisibility(View.GONE);
                llSubscribe.setVisibility(View.VISIBLE);
            }
            Glide.with(this).load(logo).into(ivGamePic);
            tvTaskName.setText(taskInfo.name);
            tvNotice.setText(taskInfo.remind);
            tvReward.setText("+" + reward + "元");
            String time = null;
            if (taskInfo.timeUnit == 1) {
                time = "天";
            }
            if (taskInfo.timeUnit == 2) {
                time = "小时";
            }
            if (taskInfo.timeUnit == 3) {
                time = "分钟";
            }
            tvTime.setText(taskInfo.fulfilTime + time);
            taskexplainLayout = (LinearLayout) mLayountInflater.inflate(R.layout.ll_task_explain_detail, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.leftMargin = DensityUtil.dip2px(this, 8);
//            params.rightMargin = DensityUtil.dip2px(this, 8);
            params.topMargin = DensityUtil.dip2px(this, 8);
            linearLayout.addView(taskexplainLayout, params);
            TextView taskExplain = taskexplainLayout.findViewById(R.id.tv_task_explain);
            taskExplain.setText(taskInfo.explains);
        }

        if (EmptyUtils.isNotEmpty(data.lUserTptask)) { //没有关系,开始任务
            TaskDetailBean.LUserTptaskBean lUserTptask = data.lUserTptask;
            taskId = lUserTptask.tpTaskId;
            lTpTaskId = lUserTptask.id;
            checkState(lUserTptask.status, lUserTptask.expireTime);
        }

        List<TaskDetailBean.TaskStepBean> taskStep = data.taskStep;
        if (EmptyUtils.isNotEmpty(taskStep)) {
            makeMoneyStep = ((RelativeLayout) mLayountInflater.inflate(R.layout.ll_makemoney, null));
            RelativeLayout.LayoutParams makeMoneyStepparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(makeMoneyStep, makeMoneyStepparams);
            for (int i = 0; i < taskStep.size(); i++) {
                TaskDetailBean.TaskStepBean taskStepBean = taskStep.get(i);
                addStep(taskStepBean);
            }
        }

        taskSubmit = data.taskSubmit;
        if (EmptyUtils.isNotEmpty(taskSubmit)) {
            llSubmitStep = ((RelativeLayout) mLayountInflater.inflate(R.layout.ll_submitstep, null));
            RelativeLayout.LayoutParams llSubmitStepparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(llSubmitStep, llSubmitStepparams);
            addSubmit(taskSubmit);
        }
    }

    private void checkState(int lUserTptask, long expireTime) {
        switch (lUserTptask) {
            case -2://-2-已放弃

                break;
            case -1:    //-1-已过期
                llAfterSubmit.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.GONE);
                llGetTask.setVisibility(View.VISIBLE);
                break;
            case 1:     //1-待提交
                llAfterSubmit.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.VISIBLE);
                llGetTask.setVisibility(View.GONE);

                break;
            case 2:     //2-已提交，待审核
                tvState.setText("已提交");
                llAfterSubmit.setVisibility(View.VISIBLE);
                llSubmitTask.setVisibility(View.GONE);
                llGetTask.setVisibility(View.GONE);
                break;
            case 3:     //3-审核通过
                llAfterSubmit.setVisibility(View.VISIBLE);
                tvState.setText("审核通过");
                llSubmitTask.setVisibility(View.GONE);
                llGetTask.setVisibility(View.GONE);
                break;
            case 4:     //4-审核失败
                tvState.setText("审核通过");
                llAfterSubmit.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.GONE);
                llGetTask.setVisibility(View.VISIBLE);
                break;
            case 5:     //5已预约
                llGetTask.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.GONE);
                llSubscribeOver.setVisibility(View.VISIBLE);
                llAfterSubmit.setVisibility(View.GONE);
                llSubscribe.setVisibility(View.GONE);
                break;
        }
        countdown.start(expireTime);
        countdown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                //倒计时结束回调
                llAfterSubmit.setVisibility(View.GONE);
                llSubmitTask.setVisibility(View.GONE);
                llGetTask.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addSubmit(List<TaskDetailBean.TaskSubmitBean> taskSubmit) {
        LinearLayout llInput = ((LinearLayout) mLayountInflater.inflate(R.layout.ll_input_data, null));
        LinearLayout.LayoutParams llInputparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llInputparams.leftMargin = DensityUtil.dip2px(this, 8);
//        llInputparams.rightMargin = DensityUtil.dip2px(this, 8);
        llInputparams.topMargin = DensityUtil.dip2px(this, 8);
        llInputparams.bottomMargin = DensityUtil.dip2px(this, 71);
        linearLayout.addView(llInput, llInputparams);
        RecyclerView recyclerView = llInput.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SubmitAdapter submitAdapter = new SubmitAdapter(taskSubmit);
        recyclerView.setAdapter(submitAdapter);
        submitAdapter.notifyDataSetChanged();

    }

    private void addStep(final TaskDetailBean.TaskStepBean taskStepBean) {
        int step = taskStepBean.step;
        String content = taskStepBean.content;
        int isMust = taskStepBean.isMust;
        switch (taskStepBean.stepType) {
            case 1:
                LinearLayout lltyp1 = ((LinearLayout) mLayountInflater.inflate(R.layout.ll_type1, null));
                LinearLayout.LayoutParams lltyp1params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lltyp1params.leftMargin = DensityUtil.dip2px(this, 8);
//                lltyp1params.rightMargin = DensityUtil.dip2px(this, 8);
                lltyp1params.topMargin = DensityUtil.dip2px(this, 8);
                linearLayout.addView(lltyp1, lltyp1params);
                TextView tvstep = lltyp1.findViewById(R.id.tv_step);
                TextView tvstepsxplain = lltyp1.findViewById(R.id.tv_step_explain);
                tvstep.setText(step + ".");
                tvstepsxplain.setText(content);
                break;
            case 2:
                LinearLayout lltyp2 = ((LinearLayout) mLayountInflater.inflate(R.layout.ll_type2, null));
                LinearLayout.LayoutParams lltyp2params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lltyp2params.leftMargin = DensityUtil.dip2px(this, 8);
//                lltyp2params.rightMargin = DensityUtil.dip2px(this, 8);
                lltyp2params.topMargin = DensityUtil.dip2px(this, 8);
                linearLayout.addView(lltyp2, lltyp2params);
                TextView tvstep2 = lltyp2.findViewById(R.id.tv_step);
                TextView tvstepsxplain2 = lltyp2.findViewById(R.id.tv_step_explain);
                tvstep2.setText(step + ".");
                tvstepsxplain2.setText(content);
                RecyclerView picRecycle = lltyp2.findViewById(R.id.pic_recycle);
                final String[] splitpic = taskStepBean.url.split(",");
                picRecycle.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
                PicQrcodeAdapter picQrcodeAdapter = new PicQrcodeAdapter(R.layout.item_qrcode, splitpic);
                picQrcodeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        scalePic(splitpic[position]);
                        if (EmptyUtils.isNotEmpty(splitpic[position])) {
                            Intent intent = new Intent(TaskDetailActivity.this, PicActivity.class);
                            intent.putExtra("pic", splitpic[position]);
                            startActivity(intent);
                        }
                    }
                });
                picRecycle.setAdapter(picQrcodeAdapter);
                picQrcodeAdapter.notifyDataSetChanged();
                break;
            case 3:
//                if (isMust == 1) {
                LinearLayout lltyp3Must = ((LinearLayout) mLayountInflater.inflate(R.layout.ll_type3_must, null));
                LinearLayout.LayoutParams lltyp3Mustparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lltyp3Mustparams.leftMargin = DensityUtil.dip2px(this, 8);
//                lltyp3Mustparams.rightMargin = DensityUtil.dip2px(this, 8);
                lltyp3Mustparams.topMargin = DensityUtil.dip2px(this, 8);
                linearLayout.addView(lltyp3Must, lltyp3Mustparams);
                TextView tvstep3Must = lltyp3Must.findViewById(R.id.tv_step);
                TextView tvstepsxplain3must = lltyp3Must.findViewById(R.id.tv_step_explain);
                ImageView ivpic = lltyp3Must.findViewById(R.id.iv_pic);
                ImageView ivMustPic = lltyp3Must.findViewById(R.id.iv_must_pic);
                if (isMust == 1) {
                    ivMustPic.setVisibility(View.VISIBLE);
                } else {
                    ivMustPic.setVisibility(View.GONE);
                }
                Glide.with(this).load(taskStepBean.url).into(ivpic);
                tvstep3Must.setText(step + ".");
                tvstepsxplain3must.setText(content);
                ivpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TaskDetailActivity.this, PicActivity.class);
                        intent.putExtra("pic", taskStepBean.url);
                        startActivity(intent);
                    }
                });

//                }
//                if (isMust == 2) {
//                    LinearLayout lltyp3 = ((LinearLayout) mLayountInflater.inflate(R.layout.ll_type3, null));
//                    LinearLayout.LayoutParams lltyp3params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    lltyp3params.leftMargin = DensityUtil.dip2px(this, 8);
//                    lltyp3params.rightMargin = DensityUtil.dip2px(this, 8);
//                    lltyp3params.topMargin = DensityUtil.dip2px(this, 8);
//                    linearLayout.addView(lltyp3, lltyp3params);
//                    TextView tvstep3 = lltyp3.findViewById(R.id.tv_step);
//                    TextView tvstepsxplain3 = lltyp3.findViewById(R.id.tv_step_explain);
//                    tvstep3.setText(step + "、");
//                    tvstepsxplain3.setText(content);
//                }
                break;
            case 4:
                LinearLayout lltyp4 = ((LinearLayout) mLayountInflater.inflate(R.layout.ll_type4, null));
                LinearLayout.LayoutParams lltyp4params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lltyp4params.leftMargin = DensityUtil.dip2px(this, 8);
//                lltyp4params.rightMargin = DensityUtil.dip2px(this, 8);
                lltyp4params.topMargin = DensityUtil.dip2px(this, 8);
                linearLayout.addView(lltyp4, lltyp4params);
                TextView tvstep4 = lltyp4.findViewById(R.id.tv_step);
                TextView tvstepsxplain4 = lltyp4.findViewById(R.id.tv_step_explain);
                tvstep4.setText(step + ".");
                tvstepsxplain4.setText(content);
                final TextView link = lltyp4.findViewById(R.id.tv_link);
                link.setText(taskStepBean.url);
                TextView copy = lltyp4.findViewById(R.id.tv_copy);
                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = link.getText().toString();
                        CopyToClipboard(s);
                    }
                });
                break;
        }
    }

    /**
     * 复制到剪切板
     */
    public void CopyToClipboard(String text) {
        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", text);
        clip.setPrimaryClip(myClip);
        ToastUtils.show("复制成功");
    }

    class SubmitAdapter extends BaseMultiItemQuickAdapter<TaskDetailBean.TaskSubmitBean, BaseViewHolder> {
        private final int INPUT = 1;
        private final int INPUTIMAGE = 2;

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        public SubmitAdapter(List<TaskDetailBean.TaskSubmitBean> data) {
            super(data);
            addItemType(INPUT, R.layout.ll_input1);
            addItemType(INPUTIMAGE, R.layout.ll_input2);
        }

        @Override
        protected void convert(BaseViewHolder helper, TaskDetailBean.TaskSubmitBean item) {
            if (EmptyUtils.isNotEmpty(item)) {
                int type = item.type;
                switch (type) {
                    case INPUT:
                        EditText etInput = helper.getView(R.id.et_input);
                        etInput.setHint(item.name);
                        //通过tag判断当前editText是否已经设置监听，有监听的话，移除监听再给editText赋值
                        if (etInput.getTag() instanceof TextWatcher) {
                            etInput.removeTextChangedListener((TextWatcher) etInput.getTag());
                        }
                        final SubmitaskBean bean = new SubmitaskBean();
                        bean.setSubmitValue("");
                        bean.setType("1");
                        //给item中的editText设置监听
                        TextWatcher watcherMobile = new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (!TextUtils.isEmpty(s)) {
                                    String result = String.valueOf(s);
                                    bean.setSubmitValue(result);
                                    Log.d("sss", result);
                                }
                            }
                        };
                        etInput.addTextChangedListener(watcherMobile);
                        //给editText设置tag，以便于判断当前editText是否已经设置监听
                        etInput.setTag(watcherMobile);//给item中的editText设置监听
                        bean.setSubmitName(item.name);
                        bean.setSubmitId(String.valueOf(item.id));
                        submitaskBeanList.add(bean);
                        break;
                    case INPUTIMAGE:
                        RecyclerView recyclerView = helper.getView(R.id.pic_recycle);
                        TextView tvInputExplain = helper.getView(R.id.tv_input_explain);
                        tvInputExplain.setText(item.name);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TaskDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        TaskPictureBean pictureBean = new TaskPictureBean();
                        pictureBean.setCode(0);
                        list.add(pictureBean);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                if (view.getId() == R.id.iv_add) {
                                    if (list.size() >= 10) {
                                        ToastUtils.show("最多上传九张图片");
                                    } else {
                                        showPhotoPicker();
                                    }
                                } else {
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);

//                        StringBuffer stringBuffer = new StringBuffer();
//                        for (int i = 1; i < list.size(); i++) {
//                            String release.keystore = list.get(i).getPath().getName();
//                            stringBuffer.append(release.keystore + ",");
//                        }
//                        submitaskBeanList.add(new PutTaskBean(item.name, String.valueOf(item.id), stringBuffer.toString()));
                        break;
                }
            }
        }
    }

    public void initViewAndEvent() {

        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(final File outputFile, Uri outputUri) {
                Luban.with(TaskDetailActivity.this)
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

                                        TaskPictureBean pictureBean = new TaskPictureBean();
                                        pictureBean.setCode(1);
                                        pictureBean.setPath(url);
                                        list.add(pictureBean);
                                        adapter.notifyDataSetChanged();
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
                mLqrPhotoSelectUtils.selectPhoto();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_share:
            case R.id.ll_share:
                share(qrCode);
                break;
            case R.id.ll_state:
                Intent intent = new Intent(TaskDetailActivity.this, TaskProgressActivity.class);
                intent.putExtra("state", state);
                startActivity(intent);
                break;
        }
    }

    private void share(String url) {
        ShareUtils.shareUrl(TaskDetailActivity.this, R.drawable.icon_share_ic, url, "199元任务奖励,快来和他一起赚钱吧!——快速赚", "下载宝猪乐园APP,进入快速赚,完成任务赏金就属于你", new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                TLog.d("SHARE", "开始了");
            }


            @Override
            public void onResult(SHARE_MEDIA platform) {
                ToastUtils.show(TaskDetailActivity.this, "分享成功");
            }


            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                TLog.d("SHARE", t.getMessage());
                ToastUtils.show(TaskDetailActivity.this, t.getMessage());
            }


            @Override
            public void onCancel(SHARE_MEDIA platform) {
//                    ToastUtils.show(InviteApprenticeActivity.this, "取消了分享");
            }
        });

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

    private void getTask() {
        HttpUtils.userTptask(id).enqueue(new Observer<BaseResult<GetTaskSuccessBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    GetTaskSuccessBean data = ((GetTaskSuccessBean) response.data);
                    taskId = data.tpTaskId;
                    lTpTaskId = data.id;
                    checkState(data.status, data.expireTime);
                }
            }

            @Override
            public void onFailed(String code, String msg) {
                //弹窗
                if ("9001".equals(code)) {
                    showNotice();
                } else {
                    super.onFailed(code, msg);
                }
            }
        });
    }

    private void showNotice() {
        if (this.isFinishing()) {
            return;
        }
        commonDialog = new NewCommonDialog(this, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_notice, null);
        final TextView tvTime = contentView.findViewById(R.id.tv_time);
        commonDialog.setContentView(contentView);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "S");
            }

            @Override
            public void onFinish() {
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
            }
        }.start();
        commonDialog.show();
    }

    private void showSubscribe() {
        if (this.isFinishing()) {
            return;
        }
        subscribeDialog = new NewCommonDialog(this, false, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_subscribe, null);
        ImageView ivGamePic = contentView.findViewById(R.id.tv_1);
        TextView tvTime = contentView.findViewById(R.id.tv_time);
        Glide.with(this).load(logo).into(ivGamePic);
        tvTime.setText(TimeUtils.formatDate(orderTime));
        contentView.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subscribeDialog != null && subscribeDialog.isShowing()) {
                    subscribeDialog.dismiss();
                }
                goSubscribe();
            }
        });
        contentView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subscribeDialog != null && subscribeDialog.isShowing()) {
                    subscribeDialog.dismiss();
                }
            }
        });
        subscribeDialog.setContentView(contentView);

        subscribeDialog.show();
    }

}
