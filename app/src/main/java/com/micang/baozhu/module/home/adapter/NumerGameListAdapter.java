package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

public class NumerGameListAdapter extends BaseQuickAdapter<GameBean.ListBean, BaseViewHolder> {

    public NumerGameListAdapter(int layoutResId, @Nullable List<GameBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean.ListBean item) {

        if (item != null) {

            String gameGold = item.gameGold;
            String gameTitle = item.gameTitle;
            helper.setText(R.id.tv_game_name, gameTitle);
            helper.setText(R.id.tv_money, "+" + gameGold);
            ImageView imageView = helper.getView(R.id.tv_1);
            String icon = item.icon;
            if (EmptyUtils.isNotEmpty(icon)) {
                Glide.with(mContext).load(icon).into(imageView);
            }

        }
    }
}
