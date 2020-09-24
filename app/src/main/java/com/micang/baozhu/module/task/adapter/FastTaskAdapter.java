package com.micang.baozhu.module.task.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.task.TaskBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 16:33
 * @describe describe
 */
public class FastTaskAdapter extends BaseQuickAdapter<TaskBean.ListBean, BaseViewHolder> {
    public FastTaskAdapter(int layoutResId, @Nullable List<TaskBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskBean.ListBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            helper.setText(R.id.tv_task_name, item.name);
            helper.setText(R.id.tv_reward, item.reward + "元");
            helper.setText(R.id.tv_residue_task, "剩余" + item.surplusChannelTaskNumber + "份");
            ImageView logo = helper.getView(R.id.tv_1);
            String icon = item.logo;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(logo);
            }
            if (item.drReward > 0) {
                helper.setText(R.id.tv_task_drReward, "达人奖励" + item.drReward + "元");
                helper.getView(R.id.tv_task_drReward).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_task_drReward).setVisibility(View.GONE);
            }
            String[] split = item.label.split(",");
            if (EmptyUtils.isNotEmpty(split)) {
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        if (EmptyUtils.isNotEmpty(split[0])) {
                            helper.setText(R.id.tv_task_describe1, split[0]);
                            helper.getView(R.id.tv_task_describe1).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_task_describe2).setVisibility(View.GONE);
                            helper.getView(R.id.tv_task_describe3).setVisibility(View.GONE);
                        }
                    }
                    if (i == 1) {
                        if (EmptyUtils.isNotEmpty(split[1])) {
                            helper.setText(R.id.tv_task_describe2, split[1]);
                            helper.getView(R.id.tv_task_describe2).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_task_describe3).setVisibility(View.GONE);
                        }
                    }
                    if (i == 2) {
                        if (EmptyUtils.isNotEmpty(split[2])) {
                            helper.setText(R.id.tv_task_describe3, split[2]);
                            helper.getView(R.id.tv_task_describe3).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }
}
