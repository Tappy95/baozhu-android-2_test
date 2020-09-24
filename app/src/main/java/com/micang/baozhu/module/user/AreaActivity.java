package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.EventAddress;
import com.micang.baselibrary.event.EventCode;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.AreaBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.user.adapter.AreaAdapter;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 区
 */
public class AreaActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private RecyclerView recycleview;
    private String level = "3";
    private String parentId = "";
    private List<AreaBean> list = new ArrayList<>();
    private AreaAdapter adapter;

    @Override
    public int layoutId() {
        return R.layout.activity_area;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("选择地址");
        recycleview = findViewById(R.id.recycleview);
        llBack.setOnClickListener(this);
        Intent intent = getIntent();
        String areaparentId = intent.getStringExtra("PARENTID");
        if (EmptyUtils.isNotEmpty(areaparentId)) {
            parentId = areaparentId;
        }
        initData();
    }

    private void initData() {
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new AreaAdapter(R.layout.area_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AreaBean areaBean = list.get(position);
                EventBus.getDefault().postSticky(new EventAddress<>(EventCode.useAddress, areaBean));
                ProvinceAreaActivity.instance.finish();
                CityAreaActivity.instance.finish();
                finish();
            }
        });
        recycleview.setAdapter(adapter);
        getAreaList();
    }

    private void getAreaList() {
        HttpUtils.areaList(level, parentId).enqueue(new Observer<BaseResult<List<AreaBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                List<AreaBean> data = (List<AreaBean>) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    list.clear();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
