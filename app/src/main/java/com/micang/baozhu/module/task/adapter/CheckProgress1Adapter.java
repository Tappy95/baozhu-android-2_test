package com.micang.baozhu.module.task.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.task.TaskProgressBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.util.TimeUtils;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 16:33
 * @describe describe
 */
public class CheckProgress1Adapter extends BaseQuickAdapter<TaskProgressBean.ListBean, BaseViewHolder> {
    private int i;
    private List<TaskProgressBean.ListBean> data;

    public CheckProgress1Adapter(int layoutResId, @Nullable List<TaskProgressBean.ListBean> data, int i) {
        super(layoutResId, data);
        this.i = i;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskProgressBean.ListBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            helper.setText(R.id.tv_task_name, item.name);
            helper.setText(R.id.tv_reward, item.reward + "元");
            ImageView logo = helper.getView(R.id.tv_1);
            String icon = item.logo;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(logo);
            }
            String time = TimeUtils.formatDate(item.expireTime);
            String[] split = item.label.split(",");
            if (EmptyUtils.isNotEmpty(split)) {
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        if (EmptyUtils.isNotEmpty(split[0])) {
                            helper.setText(R.id.tv_task_describe1, split[0]);
                            helper.getView(R.id.tv_task_describe1).setVisibility(View.VISIBLE);
                        }
                    }
                    if (i == 1) {
                        if (EmptyUtils.isNotEmpty(split[1])) {
                            helper.getView(R.id.tv_task_describe2).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_task_describe2, split[1]);
                        }
                    }
                    if (i == 2) {
                        if (EmptyUtils.isNotEmpty(split[2])) {
                            helper.getView(R.id.tv_task_describe3).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_task_describe3, split[2]);
                        }
                    }
                }
            }
            switch (i) {
                case 1:
                    helper.addOnClickListener(R.id.tv_cancel);
                    helper.addOnClickListener(R.id.tv_submit);
                    helper.setText(R.id.tv_time, time);
                    break;
                case 2:
                    helper.setText(R.id.tv_time, time);
                    break;
                case 3:
                    if (item.status == 3) {
                        helper.setText(R.id.tv_state, "奖励已领取").setTextColor(R.id.tv_state, ContextCompat.getColor(mContext, R.color.A2ABBB));
                        helper.getView(R.id.tv_remark).setVisibility(View.GONE);
                        helper.getView(R.id.view).setVisibility(View.INVISIBLE);
                    } else {
                        helper.setText(R.id.tv_state, "审核不通过").setTextColor(R.id.tv_state, ContextCompat.getColor(mContext, R.color.ff2b49));
                        helper.setText(R.id.tv_remark, item.remark);
                        helper.getView(R.id.tv_remark).setVisibility(View.VISIBLE);
                        helper.getView(R.id.view).setVisibility(View.VISIBLE);
                    }
                    break;
                case 5:
                    String orderTime = TimeUtils.formatDate(item.orderTime);
                    helper.setText(R.id.tv_time, orderTime);
                    break;

            }

        }
    }
}
