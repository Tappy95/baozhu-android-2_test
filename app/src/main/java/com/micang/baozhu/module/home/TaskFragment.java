package com.micang.baozhu.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baozhu.module.web.XWGameDetailActivity;
import com.micang.baselibrary.base.BaseFragment;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


@BindEventBus
public class TaskFragment extends BaseFragment {

    private RecyclerView recycleview;

    private CrazyBoardAdapter adapter;
    private List<GameBean.ListBean> list = new ArrayList<>();
    private int pageSize = 20;
    private int pageNum = 1;
    private UserBean data;
    private String mobile;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;

    @Override
    protected int layoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void init(View rootView) {

        recycleview = rootView.findViewById(R.id.recycleview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        recycleview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new CrazyBoardAdapter(R.layout.crazy_board_item, list);
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
                            Intent intent = new Intent(activity, PCddGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
                if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
                    HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response) {
                            String url = (String) response.data;
                            Intent intent = new Intent(activity, MYGameDetailsActivity.class);
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
                            Intent intent = new Intent(activity, XWGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }

            }
        });

        recycleview.setAdapter(adapter);

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
                if (list.size() >= 20) {
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
//        onRefreshData();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            mobile = data.mobile;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            onRefreshData();
        }

    }

    @Override
    protected void initData() throws NullPointerException {

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
