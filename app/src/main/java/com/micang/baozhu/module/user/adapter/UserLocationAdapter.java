package com.micang.baozhu.module.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.user.AddressBean;

import java.util.List;

public class UserLocationAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {
    public UserLocationAdapter(int layoutResId, @Nullable List<AddressBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {

        if (item != null) {
            int isDefault = item.isDefault;
            String mobile = item.mobile;
            String receiver = item.receiver;
            String detailAddress = item.detailAddress;
            String fullName = item.fullName;
            int position = helper.getPosition();
            int size = getData().size();
            if (position == size-1) {
                helper.getView(R.id.view1).setVisibility(View.INVISIBLE);
            } else {
                helper.getView(R.id.view1).setVisibility(View.VISIBLE);
            }
            helper.setText(R.id.tv_name, receiver);
            helper.setText(R.id.tv_number, mobile);
            helper.setText(R.id.tv_address, fullName +"  "+ detailAddress);
            if (isDefault == 1) {
                helper.getView(R.id.iv_default_ads).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.iv_default_ads).setVisibility(View.INVISIBLE);
            }
            helper.addOnClickListener(R.id.tv_edit);
        }
    }
}
