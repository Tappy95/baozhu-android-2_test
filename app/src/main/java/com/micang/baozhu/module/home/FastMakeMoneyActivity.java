package com.micang.baozhu.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baozhu.module.web.XWActivity;
import com.micang.baozhu.module.web.XWGameDetailActivity;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.CrazyBoardAdapter;
import com.micang.baozhu.module.web.MYGameDetailsActivity;
import com.micang.baozhu.module.web.PCddGameDetailActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@BindEventBus
public class FastMakeMoneyActivity extends BaseActivity {
    private LinearLayout llBack;
    private TextView tvTitle;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycleview;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private ProgressBar loadingProgress;
    private CrazyBoardAdapter adapter;
    private List<GameBean.ListBean> list = new ArrayList<>();
    private int pageSize = 20;
    private int pageNum = 1;
    private UserBean data;
    private String mobile;

    @Override
    public int layoutId() {
        return R.layout.activity_fast_make_money;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("快速赚");
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        InitRecycleView();
    }

    private void InitRecycleView() {

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        recycleview = findViewById(R.id.recycleview);
        emptyLayout = findViewById(R.id.empty_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        loadingProgress = findViewById(R.id.loading_progress);
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CrazyBoardAdapter(R.layout.crazy_board_item, list);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final GameBean.ListBean listBean = list.get(position);
                String gameId = listBean.id;
                String interfaceName = listBean.interfaceName;
                if ("PCDD".equals(interfaceName)) {
                    HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response) {
                            String url = (String) response.data;
                            Intent intent = new Intent(FastMakeMoneyActivity.this, PCddGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
                if ("MY".equals(interfaceName)) {
                    HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response) {
                            String url = (String) response.data;
                            Intent intent = new Intent(FastMakeMoneyActivity.this, MYGameDetailsActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
                if ("xw-Android".equals(interfaceName)) {
                    HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response) {
                            String url = (String) response.data;
                            Intent intent = new Intent(FastMakeMoneyActivity.this, XWGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        //rv上啦加载视图
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
                    getGameList();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            mobile = data.mobile;
        }
    }

    private void onRefreshData() {
        pageNum = 1;
        list.clear();
        adapter.removeAllFooterView();
        if (null != adapter) {
            adapter.setEnableLoadMore(false);
            adapter.notifyDataSetChanged();
        }
        getGameList();
    }

    private void getGameList() {

        HttpUtils.queryGameList(pageSize, pageNum).enqueue(new Observer<BaseResult<GameBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                loadingLayout.setVisibility(View.GONE);
                GameBean gameBean = (GameBean) response.data;
                List<GameBean.ListBean> listBeans = gameBean.list;
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
                    if (pageNum == 1) { //刷新
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
}
