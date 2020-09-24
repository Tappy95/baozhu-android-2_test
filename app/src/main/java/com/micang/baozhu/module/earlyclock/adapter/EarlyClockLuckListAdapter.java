package com.micang.baozhu.module.earlyclock.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockLuckBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.NumberUtils;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/23 19:29
 * @describe describe
 */
public class EarlyClockLuckListAdapter extends BaseQuickAdapter<EarlyClockLuckBean, BaseViewHolder> {

    public EarlyClockLuckListAdapter(int layoutResId, @Nullable List<EarlyClockLuckBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EarlyClockLuckBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            helper.setText(R.id.tv_phone, item.mobile);
            String position = helper.getPosition() + 1 + "";
            helper.setText(R.id.tv_id, position);
            helper.setText(R.id.tv_reward, NumberUtils.num2thousand(item.rewardCoin));
            ImageView imageView = helper.getView(R.id.iv_pic);
            String icon = item.img;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(imageView);
            }
        }
    }
}
