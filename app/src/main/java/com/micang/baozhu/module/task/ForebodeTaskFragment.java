package com.micang.baozhu.module.task;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.task.TaskBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.task.adapter.ForebodetaskAdapter;
import com.micang.baozhu.module.web.TaskActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 10:17
 * @describe describe
 */
public class ForebodeTaskFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycleview;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private ProgressBar loadingProgress;
    private List<TaskBean.ListBean> list = new ArrayList<>();
    private ForebodetaskAdapter adapter;
    private int pageSize = 20;
    private int pageNum = 1;
    private String type = "";
    private String isupper = "3";

    @Override
    protected int layoutId() {
        return R.layout.fragment_forebode_task;
    }

    @Override
    protected void init(View rootView) {

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        recycleview = rootView.findViewById(R.id.recycleview);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        loadingProgress = rootView.findViewById(R.id.loading_progress);
        recycleview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new ForebodetaskAdapter(R.layout.item_forebode_task, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(activity, TaskDetailActivity.class);
//                //已预约,可预约
////                if () {
////                }
//                intent.putExtra("id", list.get(position).id);
//                startActivity(intent);
                String id = String.valueOf(list.get(position).id);
                HttpUtils.buildUrl(id).enqueue(new Observer<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response) {
                        String url = (String) response.data;
                        Intent intent = new Intent(activity, TaskActivity.class);
                        intent.putExtra("URLS", url);
                        startActivity(intent);
                    }
                });
            }
        });
        recycleview.setAdapter(adapter);
        adapter.setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.load_more_task_view;
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
        }), recycleview);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        onRefreshData();
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

    private void getList() {
        HttpUtils.getFList(type, isupper, pageNum, pageSize).enqueue(new Observer<BaseResult<TaskBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                loadingLayout.setVisibility(View.GONE);
                if (EmptyUtils.isNotEmpty(response.data)) {
                    TaskBean taskBean = (TaskBean) response.data;
                    List<TaskBean.ListBean> listBeans = taskBean.list;
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
