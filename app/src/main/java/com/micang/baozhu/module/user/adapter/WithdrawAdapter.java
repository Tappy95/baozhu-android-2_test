package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.user.UserWithDrawBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

public class WithdrawAdapter extends BaseQuickAdapter<UserWithDrawBean, BaseViewHolder> {

    public WithdrawAdapter(int layoutResId, @Nullable List<UserWithDrawBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserWithDrawBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            int parentPosition = helper.getAdapterPosition();
            if (parentPosition == 8) {
                helper.setText(R.id.tv_balance, item.price);
            } else {
                helper.setText(R.id.tv_balance, item.price + "元");
            }
            if (item.isSelected) {
                helper.setBackgroundRes(R.id.rl_bg, R.drawable.bg_stroke_ff3140_radius_4);
                helper.setTextColor(R.id.tv_balance, ContextCompat.getColor(mContext, R.color.color_ff3140));
            } else {
                helper.setBackgroundRes(R.id.rl_bg, R.drawable.bg_stroke_ffffff_radius_4);
                helper.setTextColor(R.id.tv_balance, ContextCompat.getColor(mContext, R.color.color_3c3e5b));
            }
            if (item.isTask == 1) {
                helper.getView(R.id.iv_task).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_num).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.iv_task).setVisibility(View.INVISIBLE);
                helper.getView(R.id.tv_num).setVisibility(View.INVISIBLE);
            }
        }
    }
}
