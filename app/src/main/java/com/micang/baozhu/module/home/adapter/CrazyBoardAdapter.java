package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;
import java.util.Random;

public class CrazyBoardAdapter extends BaseQuickAdapter<GameBean.ListBean, BaseViewHolder> {

    public CrazyBoardAdapter(int layoutResId, @Nullable List<GameBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean.ListBean item) {

        if (item != null) {

            String gameGold = item.gameGold;
            String gameTitle = item.gameTitle;
            String introduce = item.introduce;
            long enddate = TimeUtils.getMillis(item.enddate);
            String residueDays = TimeUtils.formatDuringDays(enddate);
            Random rand = new Random();
            int randm = rand.nextInt(1500) + 501;
            helper.setText(R.id.tv_try_number, randm + "人在玩");
            helper.setText(R.id.tv_reward, "+" + gameGold + "元");
            helper.setText(R.id.tv_game_name, gameTitle);
            helper.setText(R.id.tv_details, introduce);
            if (EmptyUtils.isNotEmpty(residueDays)) {
                helper.setText(R.id.tv_try_lastdata, "还剩"+residueDays+"天");
            }
            ImageView imageView = helper.getView(R.id.tv_1);
            String icon = item.icon;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(imageView);
            }

        }
    }
}
