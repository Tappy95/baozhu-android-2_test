package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.MyCardBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.EveryDayTaskActivity;
import com.micang.baozhu.module.home.GameListActivity;
import com.micang.baozhu.module.home.NewVipActivity;
import com.micang.baozhu.module.user.adapter.MyCardAdapter;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class MyCardActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout llNoCard;
    private LinearLayout llHavaCard;
    private RecyclerView recycleview;
    private List<MyCardBean.ListBean> cardList = new ArrayList<>();
    private String pageSize = "30";
    private String pageNum = "1";
    private MyCardAdapter adapter;
    private View footView;

    @Override
    public int layoutId() {
        return R.layout.activity_my_card;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        llNoCard = findViewById(R.id.ll_no_card);
        llHavaCard = findViewById(R.id.ll_hava_card);
        recycleview = findViewById(R.id.recycleview);
        tvTitle.setText("我的卡券");
        llBack.setOnClickListener(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initData() {
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyCardAdapter(R.layout.my_card_item, cardList);
        footView = getLayoutInflater().inflate(R.layout.layout_end_card, (ViewGroup) recycleview.getParent(), false);
        recycleview.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int type = cardList.get(position).passbookType;
                switch (type) {
                    case 1:
                        startActivity(new Intent(MyCardActivity.this, EveryDayTaskActivity.class));
                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(MyCardActivity.this, NewVipActivity.class));
                        finish();
                        break;
                    case 3:
                        startActivity(new Intent(MyCardActivity.this, GameListActivity.class));
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
        getCardList();
    }

    private void getCardList() {

        HttpUtils.getPassbookList(pageSize, pageNum).enqueue(new Observer<BaseResult<MyCardBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                MyCardBean data = (MyCardBean) response.data;
                List<MyCardBean.ListBean> list = data.list;
                if (EmptyUtils.isNotEmpty(list)) {
                    adapter.addFooterView(footView);
                    llNoCard.setVisibility(View.INVISIBLE);
                    llHavaCard.setVisibility(View.VISIBLE);
                    cardList.clear();
                    cardList.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    llNoCard.setVisibility(View.VISIBLE);
                    llHavaCard.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
