package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.EverydayRedListBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/22 15:51
 * @describe describe
 */
public class EverydayRedListAdapter extends BaseQuickAdapter<EverydayRedListBean, BaseViewHolder> {


    public EverydayRedListAdapter(int layoutResId, @Nullable List<EverydayRedListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EverydayRedListBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            helper.setText(R.id.tv_coin, "+" + item.score);
        }
    }
}
