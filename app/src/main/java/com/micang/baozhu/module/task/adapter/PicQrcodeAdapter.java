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

import java.util.Arrays;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 16:33
 * @describe describe
 */
public class PicQrcodeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PicQrcodeAdapter(int layoutResId, @Nullable String[] data) {
        super(layoutResId, Arrays.asList(data));
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (EmptyUtils.isNotEmpty(item)) {
            ImageView view = helper.getView(R.id.iv_pic);
            Glide.with(mContext).load(item).into(view);
        }
    }
}
