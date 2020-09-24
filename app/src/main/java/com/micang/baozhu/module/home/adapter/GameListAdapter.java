package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.util.TimeUtils;

import java.util.List;
import java.util.Random;

public class GameListAdapter extends BaseQuickAdapter<GameBean.ListBean, BaseViewHolder> {

    public GameListAdapter(int layoutResId, @Nullable List<GameBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean.ListBean item) {

        if (item != null) {
            int position = helper.getPosition();
            int size = getData().size();
            if (position == size - 1) {
                helper.getView(R.id.view).setVisibility(View.INVISIBLE);
            } else {
                helper.getView(R.id.view).setVisibility(View.VISIBLE);
            }
            String gameGold = item.gameGold;
            String gameTitle = item.gameTitle;
            String introduce = item.introduce;
            long enddate = TimeUtils.getMillis(item.enddate);
            String residueDays = TimeUtils.formatDuringDays(enddate);
            helper.setText(R.id.tv_reward, "+" + gameGold + "元");
            helper.setText(R.id.tv_game_name, gameTitle);
            helper.setText(R.id.tv_details, introduce);
            if (EmptyUtils.isNotEmpty(residueDays)) {
                helper.setText(R.id.tv_try_lastdata, "还剩" + residueDays + "天");
            }
            ImageView imageView = helper.getView(R.id.iv_game_pic);
            String icon = item.icon;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(imageView);
            }
            String[] split = item.labelStr.split(",");
            if (EmptyUtils.isNotEmpty(item.shortIntro)) {
                helper.setText(R.id.tv_shortIntro, item.shortIntro);
                helper.getView(R.id.tv_shortIntro).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_shortIntro).setVisibility(View.GONE);
            }
            if (EmptyUtils.isNotEmpty(split)) {
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        if (EmptyUtils.isNotEmpty(split[0])) {
                            helper.setText(R.id.tv_labelStr1, split[0]);
                            helper.getView(R.id.tv_labelStr1).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_labelStr2).setVisibility(View.GONE);
                        }
                    }
                    if (i == 1) {
                        if (EmptyUtils.isNotEmpty(split[1])) {
                            helper.getView(R.id.tv_labelStr2).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_labelStr2, split[1]);
                        }
                    }
                }
            }
        }
    }
}
