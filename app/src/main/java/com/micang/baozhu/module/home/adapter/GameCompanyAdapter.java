package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameCompanyBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

public class GameCompanyAdapter extends BaseQuickAdapter<GameCompanyBean.ListBean, BaseViewHolder> {

    public GameCompanyAdapter(int layoutResId, @Nullable List<GameCompanyBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameCompanyBean.ListBean item) {

        if (item != null) {
            String name = item.name;
            String remark = item.remark;
            String imageUrl = item.imageUrl;
            helper.setText(R.id.tv_gamecompany_name, name);
            helper.setText(R.id.tv_details, remark);
            helper.addOnClickListener(R.id.bt_begin);
            ImageView imageView = helper.getView(R.id.iv_gamecompany_pic);
            if (EmptyUtils.isNotEmpty(imageUrl)) {
                Glide.with(mContext).load(imageUrl).into(imageView);
            }
        }
    }
}
