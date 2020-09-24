package com.micang.baozhu.module.task;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.task.TaskBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.NewVipActivity;
import com.micang.baozhu.module.task.adapter.FastTaskAdapter;
import com.micang.baozhu.module.view.CommonDialog;
import com.micang.baozhu.module.web.TaskActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseFragment;
import com.micang.baselibrary.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 14:43
 * @describe 全部
 */
public class AllTaskFragment extends BaseFragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycleview;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private List<TaskBean.ListBean> list = new ArrayList<>();
    private FastTaskAdapter adapter;
    private int pageSize = 20;
    private int pageNum = 1;
    private String type = "";
    private String isupper = "1";
    private CommonDialog commonDialog;

    @Override
    protected int layoutId() {
        return R.layout.fragment_all_task;
    }

    @Override
    protected void init(View rootView) {
        TLog.d("fragment", "AllTaskFragment");
        recycleview = rootView.findViewById(R.id.recycleview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        recycleview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new FastTaskAdapter(R.layout.item_fast_task, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(activity, TaskDetailActivity.class);
//                intent.putExtra("id", list.get(position).id);
//                startActivity(intent);
                String id = String.valueOf(list.get(position).id);
                HttpUtils.buildUrl(id).enqueue(new Observer<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response) {
                        String url = (String) response.data;
                        if ("HighVipFirst".equals(url)) {
                            showVip();
                        } else {
                            Intent intent = new Intent(activity, TaskActivity.class);
                            intent.putExtra("URLS", url);
                            startActivity(intent);
                        }
                    }
                });
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

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        if (!hidden) {
//            onRefreshData();
//        }
//
//    }

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

    private void showVip() {
        if (activity.isFinishing()) {
            return;
        }
        if (commonDialog != null && commonDialog.isShowing()) {
            return;
        }
        commonDialog = new CommonDialog(activity, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_notice_highvip, null);
        commonDialog.setContentView(contentView);

        commonDialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NewVipActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                commonDialog.dismiss();
            }
        });

        commonDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

}
