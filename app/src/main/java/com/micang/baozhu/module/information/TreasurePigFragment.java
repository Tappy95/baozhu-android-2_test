package com.micang.baozhu.module.information;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseFragment;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.InformationRewardCoinBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TreasurePigFragment extends BaseFragment {
    private SlidingTabLayout commonTablayout;
    private ViewPager viewPager;
    private ArrayList<NewsListFragment> mFragments = new ArrayList<>();

    private List<String> channelName;
    private TextView tvCoin;
    private ImageView ivClose;
    private boolean hidden1;
    private RelativeLayout rlBg;
    private String type = "13";
    private String content = "";


    @Override
    protected int layoutId() {
        return R.layout.fragment_treasure_pig;
    }

    @Override
    protected void init(View rootView) {
        commonTablayout = rootView.findViewById(R.id.slidingTablayout);
        viewPager = rootView.findViewById(R.id.viewPager);
        tvCoin = rootView.findViewById(R.id.tv_Coin);
        ivClose = rootView.findViewById(R.id.iv_close);
        rlBg = rootView.findViewById(R.id.rl_bg);

        initTitle();
        if (viewPager.getAdapter() == null) {

            BaseFragmentAdapter adapter = new BaseFragmentAdapter(getFragmentManager(),
                    mFragments, channelName);
            viewPager.setAdapter(adapter);
        } else {
            final BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
            adapter.updateFragments(mFragments, channelName);
        }
        viewPager.setOffscreenPageLimit(3);
        commonTablayout.setViewPager(viewPager);
        ivClose.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int count = SPUtils.getInt(activity, "COUNT", 0);
                count++;
                SPUtils.saveInt(activity, "COUNT", count);
                String stringDate = TimeUtils.getStringDate();
                String lastTime = stringDate + " 23:59:59";
                long lastTimeMillis = TimeUtils.formatDateMillis(lastTime);
                SPUtils.saveLong(activity, "TIME", lastTimeMillis);
                TLog.d("DATA", lastTime + "-------------------" + lastTimeMillis + "----coutn" + count);
                rlBg.setVisibility(View.GONE);

            }
        });

    }

    private void initTitle() {

        ArrayList<NewsChannelTable> newsChannelTables = new ArrayList<>();
        channelName = Arrays.asList(AppContext.getInstance().getResources()
                .getStringArray(R.array.hippo_news_channel));
        List<String> channelId = Arrays.asList(AppContext.getInstance().getResources()
                .getStringArray(R.array.hippo_news_channel_id));
        for (int i = 0; i < channelName.size(); i++) {
            NewsChannelTable table = new NewsChannelTable(channelName.get(i), channelId.get(i));
            newsChannelTables.add(table);
            mFragments.add(NewsListFragment.newInstance(channelId.get(i)));
        }
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden1) {
            if (!EmptyUtils.isTokenEmpty(activity)) {
                getReward();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        hidden1 = isHidden();
        if (!hidden1) {
            if (!EmptyUtils.isTokenEmpty(activity)) {
                getReward();
            }
            int count = SPUtils.getInt(activity, "COUNT", 0);
            long lastTimeMillis = SPUtils.getLong(activity, "TIME", 1392515067621L);
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis > lastTimeMillis) {
                if (count < 3) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            rlBg.setVisibility(View.VISIBLE);
                        }
                    }, 15000);

                } else {
                    rlBg.setVisibility(View.GONE);
                }
            } else {
                rlBg.setVisibility(View.GONE);
            }
        }
    }

    private void getReward() {
        HttpUtils.getRewardConifg(type, content).enqueue(new Observer<BaseResult<InformationRewardCoinBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                InformationRewardCoinBean data = (InformationRewardCoinBean) response.data;
                String has_get = data.has_get;
                tvCoin.setText(has_get);
            }
        });
    }
}
