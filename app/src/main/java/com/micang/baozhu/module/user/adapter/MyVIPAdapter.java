package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.VipBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;


public class MyVIPAdapter extends BaseQuickAdapter<VipBean, BaseViewHolder> {
    public MyVIPAdapter(int layoutResId, @Nullable List<VipBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            String logo = item.logo;
            if (EmptyUtils.isNotEmpty(logo)) {
                ImageView view = (ImageView) helper.getView(R.id.iv_vip);
                Glide.with(mContext).load(logo).into(view);
            }
        }
    }
}
