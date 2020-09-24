package com.micang.baozhu.module.task;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.task.TaskProgressBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.task.adapter.CheckProgress1Adapter;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseFragment;
import com.micang.baselibrary.base.BaseLazyFragment;
import com.micang.baselibrary.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 14:43
 * @describe 审核中
 */
public class CheckProgress2Fragment extends BaseLazyFragment {

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private RecyclerView recyclerView;
    private List<TaskProgressBean.ListBean> list = new ArrayList<>();
    private CheckProgress1Adapter adapter;
    private int pageSize = 20;
    private int pageNum = 1;
    private String status = "2";

    @Override
    protected int layoutId() {
        return R.layout.fragment_task1;
    }

    @Override
    protected void init(View rootView) {
        TLog.d("fragment", "CheckProgress2Fragment");


        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new CheckProgress1Adapter(R.layout.item_checkprogress2, list, 2);

        recyclerView.setAdapter(adapter);
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
                    getList();
                }
            }
        }), recyclerView);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
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

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    protected void delayload() {
        super.delayload();
        onRefreshData();
    }

    private void getList() {
        HttpUtils.queryFlist(status, pageNum, pageSize).enqueue(new Observer<BaseResult<TaskProgressBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                loadingLayout.setVisibility(View.GONE);
                if (EmptyUtils.isNotEmpty(response.data)) {
                    TaskProgressBean data = ((TaskProgressBean) response.data);
                    List<TaskProgressBean.ListBean> listBeans = data.list;
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
                    recyclerView.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
                }
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                refreshLayout.setRefreshing(false);
                pageNum--;
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                pageNum--;
                refreshLayout.setRefreshing(false);

                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
            }
        });
    }
}
