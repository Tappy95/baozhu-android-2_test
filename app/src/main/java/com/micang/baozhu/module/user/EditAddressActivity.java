package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.micang.baozhu.http.bean.user.UserAddressBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.view.CommonDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvSure;
    private EditText etName;
    private EditText etMoblie;
    private TextView tvArea;
    private EditText etDetailsAds;
    private ImageView ivDefaultAds;
    private TextView tvDeleteAds;
    private boolean isDefault = false;//false1,true2
    private String areaId = "";
    private String addressId;
    private CommonDialog commonDialog;

    @Override
    public int layoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvSure = findViewById(R.id.tv_sure);
        tvTitle.setText("修改收货地址");
        tvSure.setText("保存");
        etName = findViewById(R.id.et_name);

        etMoblie = findViewById(R.id.et_moblie);
        tvArea = findViewById(R.id.tv_area);
        etDetailsAds = findViewById(R.id.et_details_ads);

        ivDefaultAds = findViewById(R.id.iv_default_ads);
        tvDeleteAds = findViewById(R.id.tv_delete_ads);
        llBack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvArea.setOnClickListener(this);
        ivDefaultAds.setOnClickListener(this);
        tvDeleteAds.setOnClickListener(this);
        Intent intent = getIntent();
        addressId = intent.getStringExtra("ID");
        initData();
    }

    private void initData() {
        HttpUtils.getAddress(addressId).enqueue(new Observer<BaseResult<UserAddressBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                UserAddressBean data = (UserAddressBean) response.data;
                String detailAddress = data.detailAddress;
                areaId = data.areaId;
                String fullName = data.fullName;
                String mobile = data.mobile;
                String receiver = data.receiver;
                int inDefault = data.isDefault;
                etName.setText(receiver);
                etMoblie.setText(mobile);
                tvArea.setText(fullName);
                etDetailsAds.setText(detailAddress);
                if (inDefault == 1) {
                    isDefault = false;
                    ivDefaultAds.setImageDrawable(getResources().getDrawable(R.drawable.icon_default_ads_press));
                } else {
                    isDefault = true;
                    ivDefaultAds.setImageDrawable(getResources().getDrawable(R.drawable.icon_default_ads));
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventAddress<AreaBean> event) {
        if (event.code == EventCode.useAddress) {
            AreaBean data = event.data;
            String name = data.fullName;
            tvArea.setText(name);
            areaId = data.id;
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
                if (EmptyUtils.isEmpty(areaId)) {
                    ToastUtils.show("请选择地区");
                    return;
                }
                editAddress(name, moblie, detailsAds, areaId);
                break;
            case R.id.tv_area:
                startActivity(new Intent(this, ProvinceAreaActivity.class));
                break;
            case R.id.tv_delete_ads:
                showDelete();
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

    private void showDelete() {
        commonDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_remove_address, null);
        commonDialog.setContentView(contentView);

        commonDialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonDialog.dismiss();

            }
        });

        commonDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            //确定
            @Override
            public void onClick(View v) {
                removeAddress();
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    private void removeAddress() {
        HttpUtils.removeAddress(addressId).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("删除成功");
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });
    }

    private void editAddress(String name, String moblie, String detailsAds, String id) {
        String defaultAds;
        if (isDefault) {
            defaultAds = "2";
            TLog.d("MSG", "2");
        } else {
            defaultAds = "1";
            TLog.d("MSG", "1");
        }
        HttpUtils.editList(addressId, name, moblie, id, detailsAds, defaultAds).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                Intent intent = getIntent();
                setResult(11, intent);
                ToastUtils.show("修改成功");
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
