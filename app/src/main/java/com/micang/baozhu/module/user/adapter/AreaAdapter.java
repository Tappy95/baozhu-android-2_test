package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.user.AreaBean;

import java.util.List;

public class AreaAdapter extends BaseQuickAdapter<AreaBean, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable List<AreaBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, AreaBean item) {

        if (item != null) {
            int position = helper.getPosition();
            int size = getData().size();
            if (position == size - 1) {
                helper.getView(R.id.view1).setVisibility(View.INVISIBLE);
            } else {
                helper.getView(R.id.view1).setVisibility(View.VISIBLE);
            }
            String name = item.name;
            helper.setText(R.id.tv_area, name);
        }
    }
}
