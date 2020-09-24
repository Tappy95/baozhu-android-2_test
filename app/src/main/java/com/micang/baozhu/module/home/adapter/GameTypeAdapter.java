package com.micang.baozhu.module.home.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.GameTypeBean;
import com.micang.baozhu.http.bean.user.UserWithDrawBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

public class GameTypeAdapter extends BaseQuickAdapter<GameTypeBean, BaseViewHolder> {

    public GameTypeAdapter(int layoutResId, @Nullable List<GameTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameTypeBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            helper.setText(R.id.tv_game_type, item.typeName);
            int position = helper.getPosition();
            int size = getData().size();
            if (position == size - 1) {
                helper.getView(R.id.view).setVisibility(View.INVISIBLE);
            } else {
                helper.getView(R.id.view).setVisibility(View.VISIBLE);
            }
            if (item.isSelected) {
                helper.setTextColor(R.id.tv_game_type, ContextCompat.getColor(mContext, R.color.color_3c3e5b));
                helper.setBackgroundColor(R.id.rl_bg, ContextCompat.getColor(mContext, R.color.color_ffffff));

            } else {
                helper.setTextColor(R.id.tv_game_type, ContextCompat.getColor(mContext, R.color.color_9ea9bc));
                helper.setBackgroundColor(R.id.rl_bg, ContextCompat.getColor(mContext, R.color.color_f2f4f7));
            }
        }
    }
}
