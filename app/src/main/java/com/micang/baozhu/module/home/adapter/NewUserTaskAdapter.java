package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.home.NewUserTaskBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.util.TimeUtils;

import java.util.List;

public class NewUserTaskAdapter extends BaseQuickAdapter<NewUserTaskBean, BaseViewHolder> {

    public NewUserTaskAdapter(int layoutResId, @Nullable List<NewUserTaskBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewUserTaskBean item) {

        if (item != null) {
            int position = helper.getPosition();
            int size = getData().size();
            if (position == size - 1) {
                helper.getView(R.id.view).setVisibility(View.INVISIBLE);
            } else {
                helper.getView(R.id.view).setVisibility(View.VISIBLE);
            }
            helper.setText(R.id.tv_task_name, item.task_name);

            ImageView imageView = helper.getView(R.id.iv_task_pic);
            String icon = item.task_img;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(imageView);
            }
            switch (item.reward_unit) {
                case 1:
                    helper.setText(R.id.tv_reward, "+" + item.reward);
                    helper.getView(R.id.iv_coin).setBackgroundResource(R.drawable.icon_coin);
                    helper.getView(R.id.iv_coin).setVisibility(View.VISIBLE);
                    if (item.isComplete == 0) {
                        helper.getView(R.id.iv_coin).setBackgroundResource(R.drawable.icon_coin);
                    } else {
                        helper.getView(R.id.iv_coin).setBackgroundResource(R.drawable.icon_gray_coin);
                    }
                    break;
                case 3:
                    helper.setText(R.id.tv_reward, "+" + item.reward + "%金币奖励");
                    helper.getView(R.id.iv_coin).setVisibility(View.GONE);
                    break;
            }
            if (item.isComplete == 0) {
                helper.setText(R.id.bt_go, "去完成");
                helper.setTextColor(R.id.tv_reward, ContextCompat.getColor(mContext, R.color.ff2b49));
                helper.getView(R.id.bt_go).setBackgroundResource(R.drawable.bg_radius_22_color_ff3e2b_ff1370);
                helper.getView(R.id.bt_go).setEnabled(true);
            } else {
                helper.setText(R.id.bt_go, "已完成");
                helper.setTextColor(R.id.tv_reward, ContextCompat.getColor(mContext, R.color.color_9ea9bc));
                helper.getView(R.id.bt_go).setBackgroundResource(R.drawable.shape_bg_radius_24_cecece);
                helper.getView(R.id.bt_go).setEnabled(false);
            }
            helper.addOnClickListener(R.id.bt_go);
        }
    }
}
