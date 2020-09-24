package com.micang.baozhu.module.task.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.pay.WithdrawBalanceBean;
import com.micang.baozhu.http.bean.task.SelectReasonBean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/7/1 17:45
 * @describe describe
 */
public class SelectReasonAdapter extends BaseQuickAdapter<SelectReasonBean, BaseViewHolder> {
    public SelectReasonAdapter(int layoutResId, @Nullable List<SelectReasonBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectReasonBean item) {
        if (item != null) {
            helper.setText(R.id.tvreason, item.getReason());
            if (item.isSelected()) {
                helper.setBackgroundRes(R.id.tvreason, R.drawable.shape_bg_radius12_stroke_ff2b49)
                        .setTextColor(R.id.tvreason, ContextCompat.getColor(mContext, R.color.ff2b49));
            } else {
                helper.setBackgroundRes(R.id.tvreason, R.drawable.shape_bg_radius12_stroke_a2abbc)
                        .setTextColor(R.id.tvreason, ContextCompat.getColor(mContext, R.color.A2ABBB));
            }
        }
    }
}
