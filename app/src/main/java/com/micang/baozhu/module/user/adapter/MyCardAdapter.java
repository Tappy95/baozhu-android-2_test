package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.user.MyCardBean;

import java.util.List;

public class MyCardAdapter extends BaseQuickAdapter<MyCardBean.ListBean, BaseViewHolder> {
    public MyCardAdapter(int layoutResId, @Nullable List<MyCardBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCardBean.ListBean item) {

        if (item != null) {
            String passbookName = item.passbookName;
            int type = item.passbookType;
            int id = item.id;
            String expirationTime = item.expirationTime;
            String typeName = item.typeName;
            int passbookValue = item.passbookValue;
            helper.setText(R.id.tv_last_data, item.expirationTime);
            switch (type) {
                case 1:
                    helper.getView(R.id.iv_card_type).setBackgroundResource(R.drawable.icon_user_card_double);
                    helper.setText(R.id.tv_card_type_describe, "[翻倍券]");
                    helper.setText(R.id.tv_card_detail, "收益翻" + passbookValue + "倍");
                    helper.setText(R.id.tv_apply_detail, typeName);
                    helper.setText(R.id.tv_economize, "");
                    break;
                case 2:
                    helper.getView(R.id.iv_card_type).setBackgroundResource(R.drawable.icon_user_card_discount);
                    helper.setText(R.id.tv_card_type_describe, "[折扣券]");
                    helper.setText(R.id.tv_card_detail, passbookValue + "%折扣");
                    helper.setText(R.id.tv_apply_detail, typeName);
                    helper.setText(R.id.tv_economize, "");
                    break;
                case 3:
                    helper.getView(R.id.iv_card_type).setBackgroundResource(R.drawable.icon_user_card_addition);
                    helper.setText(R.id.tv_card_type_describe, "[加成券]");
                    helper.setText(R.id.tv_card_detail, passbookValue + "%加成");
                    helper.setText(R.id.tv_apply_detail, typeName);
                    helper.setText(R.id.tv_economize, "");
                    break;
                default:
                    break;
            }
            helper.addOnClickListener(R.id.bt_use);
        }
    }
}
