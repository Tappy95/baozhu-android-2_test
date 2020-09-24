package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.util.TimeUtils;

import java.util.List;

public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchHistoryAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        if (item != null) {
            helper.setText(R.id.tv_name, item);
        }
    }
}
