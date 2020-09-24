package com.micang.baozhu.module.home;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.VipInfoBean;
import com.micang.baozhu.http.bean.home.VipListBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseFragment;
import com.micang.baselibrary.base.BaseLazyFragment;
import com.micang.baselibrary.util.DensityUtil;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/7 19:03
 * @describe describe
 */

public class VIPFragment extends BaseFragment {
    protected static final String VIP_BEAN = "vip_bean";
    private VipListBean vipListBean;
    private ImageView ivpic;
    private TextView tvVipName;
    private TextView tvVipState;
    private TextView tvVipPrice;
    private int id;
    private boolean hidden1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vipListBean = (VipListBean) getArguments().getSerializable(VIP_BEAN);
            id = vipListBean.id;
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_vip_card;
    }

    public static VIPFragment newInstance(VipListBean newsId) {
        VIPFragment fragment = new VIPFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VIP_BEAN, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void init(View rootView) {
        ivpic = rootView.findViewById(R.id.iv_pic);
        tvVipName = rootView.findViewById(R.id.tv_vip_name);
        tvVipState = rootView.findViewById(R.id.tv_vip_state);
        tvVipPrice = rootView.findViewById(R.id.tv_vip_price);
        id = vipListBean.id;
        if (vipListBean.isBuy == 1) {
            tvVipState.setText("会员已开通");
            tvVipState.setTextColor(activity.getResources().getColor(R.color.color_9ea9bc));
        } else {
            tvVipState.setText("开通会员");
            tvVipState.setTextColor(activity.getResources().getColor(R.color.color_3C3E59));
        }
        tvVipName.setText(vipListBean.name);

        if (activity == null || activity.isFinishing()) {
            return;
        }
        setBalance(vipListBean.price);
        Glide.with(activity).load(vipListBean.backgroundImg).into(ivpic);
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden1) {
            getVipIn();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        hidden1 = isHidden();
        if (!hidden1) {
            getVipIn();
        }

    }

    private void getVipIn() {
        HttpUtils.vipinfo(id).enqueue(new Observer<BaseResult<VipInfoBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    VipInfoBean data = (VipInfoBean) response.data;
                    VipInfoBean.MVipInfoBean mVipInfo = data.mVipInfo;
                    if (EmptyUtils.isNotEmpty(mVipInfo)) {

                        if (mVipInfo.isBuy == 0) {
                            tvVipState.setText("开通会员");
                            tvVipState.setTextColor(activity.getResources().getColor(R.color.color_3C3E59));
                        } else {
                            tvVipState.setText("会员已开通");
                            tvVipState.setTextColor(activity.getResources().getColor(R.color.color_9ea9bc));
                        }
                        setBalance(mVipInfo.price);
                    }
                }
            }
        });
    }

    private void setBalance(String balance) {
        String text = "¥ " + balance;
        SpannableString spannableString = new SpannableString(text);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(DensityUtil.sp2px(activity, 14), false);
        spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvVipPrice.setText(spannableString);
    }
}
