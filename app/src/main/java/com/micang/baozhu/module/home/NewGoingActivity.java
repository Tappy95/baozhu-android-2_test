package com.micang.baozhu.module.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.module.task.CheckProgress1Fragment;
import com.micang.baozhu.module.task.CheckProgress2Fragment;
import com.micang.baozhu.module.task.CheckProgress3Fragment;
import com.micang.baozhu.module.task.CheckProgress4Fragment;
import com.micang.baozhu.module.task.adapter.CheckProgressFragmentAdapter;
import com.micang.baozhu.module.view.SwipDeleViewPager;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baselibrary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class NewGoingActivity extends BaseActivity {
    private LinearLayout llBack;
    private SlidingTabLayout commonTablayout;
    private SwipDeleViewPager viewPager;


    private ArrayList<Fragment> mFragments;
    private List<String> channelName = new ArrayList<>();

    @Override
    public int layoutId() {
        return R.layout.activity_new_going;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);

        llBack = findViewById(R.id.ll_back);
        commonTablayout = findViewById(R.id.slidingTablayout);
        viewPager = findViewById(R.id.viewPager);
        initTitle();
        if (viewPager.getAdapter() == null) {
            CheckProgressFragmentAdapter adapter = new CheckProgressFragmentAdapter(getSupportFragmentManager(), mFragments, channelName);
            viewPager.setAdapter(adapter);
        }
        viewPager.setOffscreenPageLimit(2);
        commonTablayout.setViewPager(viewPager);
        llBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });
    }

    private void initTitle() {
        mFragments = new ArrayList<>();
        channelName.add("游戏试玩");
        channelName.add("高额赚");
        mFragments.add(new GoingGameFragment());
        mFragments.add(new CheckProgress1Fragment());
    }
}
