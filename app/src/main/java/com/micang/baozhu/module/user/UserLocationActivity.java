package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.AddressBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.user.adapter.UserLocationAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class UserLocationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvSure;
    private RecyclerView recycleview;
    private UserLocationAdapter adapter;
    private List<AddressBean> list = new ArrayList<>();
    private LinearLayout emptyLayout;


    @Override
    public int layoutId() {
        return R.layout.activity_user_location;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvSure = findViewById(R.id.tv_sure);
        tvTitle.setText("管理收货地址");
        tvSure.setText("添加新地址");
        recycleview = findViewById(R.id.recycleview);
        emptyLayout = findViewById(R.id.empty_layout);
        llBack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        initData();
    }

    private void initData() {
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new UserLocationAdapter(R.layout.user_location_item, list);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String addressId = list.get(position).addressId;
                Intent intent = new Intent(UserLocationActivity.this, EditAddressActivity.class);
                intent.putExtra("ID", addressId);
                startActivityForResult(intent, 11);
            }
        });
        recycleview.setAdapter(adapter);
        getAddressList();
    }

    private void getAddressList() {
        HttpUtils.userAddressList().enqueue(new Observer<BaseResult<List<AddressBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {

                if (EmptyUtils.isNotEmpty(response.data)) {
                    recycleview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    List<AddressBean> data = (List<AddressBean>) response.data;
                    list.clear();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                } else {
                    recycleview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_sure:
                startActivityForResult(new Intent(this, AddNewLocationActivity.class), 10);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 10 || resultCode == 11) {
            getAddressList();
        }
    }
}
