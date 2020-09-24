package com.micang.baozhu.module.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.flyco.tablayout.SlidingTabLayout;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.MarqueeviewMessageBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.task.adapter.CheckProgressFragmentAdapter;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskProgressActivity extends BaseActivity {
    private LinearLayout llBack;
    private LinearLayout llService;
    private ViewFlipper viewFlipper;
    private SlidingTabLayout commonTablayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> mFragments;

    private List<String> channelName = new ArrayList<>();

    @Override
    public int layoutId() {
        return R.layout.activity_task_progress;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        commonTablayout = findViewById(R.id.slidingTablayout);
        viewPager = findViewById(R.id.viewPager);
        llBack = findViewById(R.id.ll_back);
        llService = findViewById(R.id.ll_service);
        viewFlipper = findViewById(R.id.view_flipper);
        initTitle();
        if (viewPager.getAdapter() == null) {
            CheckProgressFragmentAdapter adapter = new CheckProgressFragmentAdapter(getSupportFragmentManager(), mFragments, channelName);
            viewPager.setAdapter(adapter);
        }
        viewPager.setOffscreenPageLimit(3);
        commonTablayout.setViewPager(viewPager);
        getMarqueeviewMessage();
        Intent intent = getIntent();
        int state = intent.getIntExtra("state", 0);
        if (EmptyUtils.isNotEmpty(state)) {
            commonTablayout.setCurrentTab(state);
        }
        llBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });
    }

    private void initTitle() {
        mFragments = new ArrayList<>();
        channelName.add("已领取");
        channelName.add("审核中");
        channelName.add("已审核");
        channelName.add("已预约");
        mFragments.add(new CheckProgress1Fragment());
        mFragments.add(new CheckProgress2Fragment());
        mFragments.add(new CheckProgress3Fragment());
        mFragments.add(new CheckProgress4Fragment());
    }

    private void getMarqueeviewMessage() {
        HttpUtils.kszList().enqueue(new Observer<BaseResult<List<MarqueeviewMessageBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                final List<MarqueeviewMessageBean> data = (List<MarqueeviewMessageBean>) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    for (int i = 0; i < data.size(); i++) {
                        String mobile = data.get(i).mobile;
                        String content = data.get(i).content;
                        View view = getLayoutInflater().inflate(R.layout.item_flipper, null);
                        TextView but = view.findViewById(R.id.btn);
                        TextView textView = view.findViewById(R.id.tv);
                        SpannableString ss1 = new SpannableString(mobile + content);
                        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_ff2454)), mobile.length(), ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textView.setText(ss1);
                        final int finalI = i;
                        but.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.show("点击了条目" + data.get(finalI).id);
                            }
                        });
                        viewFlipper.addView(view);
                    }
                    viewFlipper.setFlipInterval(2000);
                    viewFlipper.startFlipping();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewFlipper != null) {
            viewFlipper.startFlipping();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewFlipper.stopFlipping();
    }
}
