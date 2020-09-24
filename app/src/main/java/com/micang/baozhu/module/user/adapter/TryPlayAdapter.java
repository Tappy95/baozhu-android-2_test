package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;
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

public class TryPlayAdapter extends BaseQuickAdapter<GameBean.ListBean, BaseViewHolder> {
    public TryPlayAdapter(int layoutResId, @Nullable List<GameBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean.ListBean item) {

        if (item != null) {
            String gameTitle = item.gameTitle;
            String introduce = item.introduce;
            int tryTag = item.tryTag;
            if (tryTag == 1) {
                helper.getView(R.id.iv_sign_game).setVisibility(View.GONE);
            } else if (tryTag == 2) {
                helper.setText(R.id.iv_sign_game, "签到赚");
                helper.getView(R.id.iv_sign_game).setVisibility(View.VISIBLE);
            } else {
                helper.setText(R.id.iv_sign_game, "VIP任务");
                helper.getView(R.id.iv_sign_game).setVisibility(View.VISIBLE);
            }
            long enddate = TimeUtils.getMillis(item.enddate);
            String residueDays = TimeUtils.formatDuringDays(enddate);
            Random rand = new Random();
            int randm = rand.nextInt(1500) + 501;
            helper.setText(R.id.tv_game_name, gameTitle);
            helper.setText(R.id.tv_try_details, introduce);
            if (EmptyUtils.isNotEmpty(residueDays)) {
                helper.setText(R.id.tv_try_lastdata, "还剩" + residueDays + "天");
            }
            helper.setText(R.id.tv_try_number, randm + "人在玩");
            ImageView imageView = helper.getView(R.id.tv_1);
            String icon = item.icon;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(imageView);
            }
        }
    }
}
