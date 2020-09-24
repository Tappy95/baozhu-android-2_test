package com.micang.baozhu.module.task.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/28 21:40
 * @describe describe
 */
public class TaskDetailItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TaskDetailItemAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.im_url);
        Glide.with(mContext).load(item).into(imageView);
    }
}
