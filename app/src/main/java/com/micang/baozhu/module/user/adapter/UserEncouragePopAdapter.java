package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class UserEncouragePopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public UserEncouragePopAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        if (item != null) {

        }
    }
}
