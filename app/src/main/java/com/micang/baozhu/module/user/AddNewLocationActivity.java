package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventAddress;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.AreaBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class AddNewLocationActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvSure;
    private EditText etName;
    private EditText etMoblie;
    private TextView tvArea;
    private EditText etDetailsAds;

    private ImageView ivDefaultAds;
    private boolean isDefault = false;
    private String id = "";//选择地区的id

    @Override
    public int layoutId() {
        return R.layout.activity_add_new_location;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvSure = findViewById(R.id.tv_sure);
        tvTitle.setText("添加收货地址");
        tvSure.setText("保存");
        etName = findViewById(R.id.et_name);

        etMoblie = findViewById(R.id.et_moblie);
        tvArea = findViewById(R.id.tv_area);
        etDetailsAds = findViewById(R.id.et_details_ads);

        ivDefaultAds = findViewById(R.id.iv_default_ads);
        llBack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvArea.setOnClickListener(this);
        ivDefaultAds.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventAddress<AreaBean> event) {
        if (event.code == EventCode.useAddress) {
            AreaBean data = event.data;
            String name = data.fullName;
            tvArea.setText(name);
            id = data.id;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_sure:

                String name = etName.getText().toString().trim();
                String moblie = etMoblie.getText().toString().trim();
                String detailsAds = etDetailsAds.getText().toString().trim();
                if (EmptyUtils.isEmpty(name)) {
                    ToastUtils.show("请输入收货人姓名");
                    return;
                }
                if (EmptyUtils.isEmpty(moblie)) {
                    ToastUtils.show("请输入收货人手机号");
                    return;
                }
                if (EmptyUtils.isEmpty(detailsAds)) {
                    ToastUtils.show("请输入详细地址");
                    return;
                }
                if (EmptyUtils.isEmpty(id)) {
                    ToastUtils.show("请选择地区");
                    return;
                }
                addAddress(name, moblie, detailsAds, id);
                break;
            case R.id.tv_area:
                startActivity(new Intent(this, ProvinceAreaActivity.class));
                break;
            case R.id.iv_default_ads:
                if (isDefault) {
                    ivDefaultAds.setImageDrawable(getResources().getDrawable(R.drawable.icon_default_ads_press));
                    isDefault = false;
                } else {
                    ivDefaultAds.setImageDrawable(getResources().getDrawable(R.drawable.icon_default_ads));
                    isDefault = true;
                }
                break;

            default:
                break;
        }
    }

    private void addAddress(String name, String moblie, String detailsAds, String id) {
        String defaultAds;
        if (isDefault) {
            defaultAds = "2";
            TLog.d("MSG", "2");
        } else {
            defaultAds = "1";
            TLog.d("MSG", "1");
        }
        HttpUtils.addAddress(name, moblie, id, detailsAds, defaultAds).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                Intent intent = getIntent();
                setResult(10, intent);
                ToastUtils.show("添加成功");
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(EventAddress.class);
    }
}
