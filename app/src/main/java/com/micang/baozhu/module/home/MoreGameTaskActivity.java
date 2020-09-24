package com.micang.baozhu.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.view.CommonPopupWindow;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.GameCompanyBean;
import com.micang.baozhu.http.bean.home.HasGameRightBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.GameCompanyAdapter;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.module.web.GeneralizeActivity;
import com.micang.baozhu.module.web.PcDDActivity;
import com.micang.baozhu.module.web.XWActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


public class MoreGameTaskActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private RecyclerView recycleview;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private int pageSize = 20;
    private int pageNum = 1;
    private GameCompanyAdapter adapter;
    private List<GameCompanyBean.ListBean> list = new ArrayList<>();
    private CommonPopupWindow showPopupWindow;
    private MoreGameTaskActivity instance;
    private LinearLayout ll;


    @Override
    public int layoutId() {
        return R.layout.activity_more_game_task;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        instance = this;
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        ll = findViewById(R.id.ll);
        recycleview = findViewById(R.id.recycleview);
        tvTitle.setText("更多任务");
        llBack.setOnClickListener(this);
        initData();

    }

    private void initData() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        emptyLayout = findViewById(R.id.empty_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new GameCompanyAdapter(R.layout.game_company_item, list);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GameCompanyBean.ListBean listBean = list.get(position);
                String shortName = listBean.shortName;
                String name = listBean.name;
                String id = listBean.id;
                checkLogin(shortName, name, id);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GameCompanyBean.ListBean listBean = list.get(position);
                String shortName = listBean.shortName;
                String name = listBean.name;
                String id = listBean.id;
                checkLogin(shortName, name, id);

            }
        });

        adapter.setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.load_more_view;
            }

            @Override
            protected int getLoadingViewId() {
                return R.id.load_more_loading_view;
            }

            @Override
            protected int getLoadFailViewId() {
                return R.id.load_more_load_fail_view;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.load_more_load_end_view;
            }
        });

        adapter.setOnLoadMoreListener((new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                if (list.size() >= pageSize) {
                    getList();
                }
            }
        }), recycleview);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        onRefreshData();
    }

    private void checkLogin(String shortName, String name, String id) {
        if (EmptyUtils.isTokenEmpty(MoreGameTaskActivity.this)) {
            toFirst();

        } else {
            hasGameRight(id, shortName, name);
        }
    }

    private void toPlay(String shortName, String name) {
        if ("PCDD".equals(shortName)) {
            Intent intent = new Intent(MoreGameTaskActivity.this, PcDDActivity.class);
            intent.putExtra("shortName", shortName);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MoreGameTaskActivity.this, XWActivity.class);
            intent.putExtra("shortName", shortName);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }

    private void hasGameRight(String id, final String shortName, final String name) {
        HttpUtils.hasGameRight(id).enqueue(new Observer<BaseResult<HasGameRightBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                HasGameRightBean data = (HasGameRightBean) response.data;
                if (2 == data.res) {
                    toPlay(shortName, name);
                }
            }
        });
    }

    private void onRefreshData() {
        pageNum = 1;
        list.clear();
        adapter.removeAllFooterView();
        if (null != adapter) {
            adapter.setEnableLoadMore(false);
            adapter.notifyDataSetChanged();
        }
        getList();
    }


    private void toFirst() {
        Intent intent = new Intent(MoreGameTaskActivity.this, NewLoginActivity.class);
        startActivity(intent);
    }

    private void getList() {
        HttpUtils.queryFList(pageSize, pageNum).enqueue(new Observer<BaseResult<GameCompanyBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                loadingLayout.setVisibility(View.GONE);
                GameCompanyBean gameCompanyBean = (GameCompanyBean) response.data;
                List<GameCompanyBean.ListBean> listBeans = gameCompanyBean.list;
                if (EmptyUtils.isNotEmpty(listBeans)) {
                    list.addAll(listBeans);
                    if (pageNum == 1) { //刷新
                        adapter.setNewData(list);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    if (list.size() < pageSize) {
                        adapter.loadMoreEnd();
                    } else {
                        adapter.loadMoreComplete();
                    }
                } else {
                    if (pageNum == 1) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
                refreshLayout.setRefreshing(false);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);

            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                refreshLayout.setRefreshing(false);
                pageNum--;
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                pageNum--;
                refreshLayout.setRefreshing(false);

                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
