package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.SignMakeBean;
import com.micang.baozhu.http.bean.home.GameCompanyBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

public class SignMakeAdapter extends BaseQuickAdapter<SignMakeBean.SignBean, BaseViewHolder> {
    private int days;

    public void setDays(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public SignMakeAdapter(int layoutResId, @Nullable List<SignMakeBean.SignBean> data, int days) {
        super(layoutResId, data);
        this.days = days;
    }

    @Override
    protected void convert(BaseViewHolder helper, SignMakeBean.SignBean item) {
        if (item != null) {
            TextView tvState = helper.getView(R.id.tv_state);
            int gameCount = item.gameCount;
            int reward = item.reward;
            int signDay = item.signDay;
            int status = item.status;
            if (signDay == days) {
                helper.setText(R.id.tv_day, "今天");
            } else {
                helper.setText(R.id.tv_day, "第" + signDay + "天");
            }
            helper.setText(R.id.tv_money, reward + "元");
            if (status == -1) {
                helper.getView(R.id.ll_state).setVisibility(View.INVISIBLE);
            }
            if (status == 1) {
                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_state).setBackgroundResource(R.drawable.icon_item_click);
                tvState.setTextColor(mContext.getResources().getColor(R.color.fa2e41));
                helper.setText(R.id.tv_state, "待补签");
            }
            if (status == 2) {
                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_state).setBackgroundResource(R.drawable.icon_item_click);
                helper.getView(R.id.ll_bg).setBackgroundResource(R.drawable.icon_item_money);
                tvState.setTextColor(mContext.getResources().getColor(R.color.fa2e41));
                helper.setText(R.id.tv_state, "点我签到");
            } else {
                helper.getView(R.id.ll_bg).setBackgroundResource(R.drawable.icon_item_money_no);
            }
            if (status == 3) {
                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_state).setBackgroundResource(R.drawable.icon_item_click);
                helper.getView(R.id.ll_bg).setBackgroundResource(R.drawable.icon_item_money);
                tvState.setTextColor(mContext.getResources().getColor(R.color.fa2e41));
                helper.setText(R.id.tv_state, "领红包");
            }
            if (status == 4) {
                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_state).setBackgroundResource(R.drawable.icon_item_over_click);
                helper.setText(R.id.tv_state, "已签到");
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));
            }
            helper.addOnClickListener(R.id.ll_bg);
            helper.addOnClickListener(R.id.tv_state);
        }
    }
}
