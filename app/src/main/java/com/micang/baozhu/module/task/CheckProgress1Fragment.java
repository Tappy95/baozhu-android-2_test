package com.micang.baozhu.module.task;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.task.SelectReasonBean;
import com.micang.baozhu.http.bean.task.TaskProgressBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.task.adapter.CheckProgress1Adapter;
import com.micang.baozhu.module.task.adapter.SelectReasonAdapter;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.TaskActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseLazyFragment;
import com.micang.baselibrary.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/24 14:43
 * @describe 已领取
 */
public class CheckProgress1Fragment extends BaseLazyFragment {

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private RecyclerView recyclerView;
    private List<TaskProgressBean.ListBean> list = new ArrayList<>();
    private CheckProgress1Adapter adapter;
    private int pageSize = 20;
    private int pageNum = 1;
    private String status = "1";
    private NewCommonDialog cancelTask;
    private List<SelectReasonBean> reasonlist = new ArrayList<>();
    private int mSelectedPos = -1;
    private String reason;
    private EditText etReason;

    @Override
    protected int layoutId() {
        return R.layout.fragment_task1;
    }

    @Override
    protected void init(View rootView) {
        TLog.d("fragment", "CheckProgress1Fragment");
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new CheckProgress1Adapter(R.layout.item_checkprogress1, list, 1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(activity, TaskDetailActivity.class);
//                intent.putExtra("id", list.get(position).tpTaskId);
//                startActivity(intent);
                String id = String.valueOf(list.get(position).tpTaskId);
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
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_submit:
//                        Intent intent = new Intent(activity, TaskDetailActivity.class);
//                        intent.putExtra("id", list.get(position).tpTaskId);
//                        startActivity(intent);
                        String id = String.valueOf(list.get(position).tpTaskId);
                        HttpUtils.buildUrl(id).enqueue(new Observer<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response) {
                                String url = (String) response.data;
                                Intent intent = new Intent(activity, TaskActivity.class);
                                intent.putExtra("URLS", url);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.tv_cancel:
                        showNotice(list.get(position).id);
//                        cancelTask(list.get(position).id);
                        break;
                }

            }
        });
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

    private void showNotice(final int id) {
        cancelTask = new NewCommonDialog(activity, false, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_show_cance_task, null);
        RecyclerView recycleview = contentView.findViewById(R.id.recycleview);
        final LinearLayout llet = contentView.findViewById(R.id.ll_et);
        etReason = contentView.findViewById(R.id.et_reason);
        setData();
        recycleview.setLayoutManager(new GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false));
        SelectReasonAdapter adapter = new SelectReasonAdapter(R.layout.item_select_reason, reasonlist);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mSelectedPos != position) {

                    reasonlist.get(mSelectedPos).setSelected(false);
                    adapter.notifyItemChanged(mSelectedPos);
                    mSelectedPos = position;
                    reasonlist.get(mSelectedPos).setSelected(true);
                    adapter.notifyItemChanged(mSelectedPos);
                    reason = reasonlist.get(position).getReason();
                    if (mSelectedPos == 4) {
                        llet.setVisibility(View.VISIBLE);
                    } else {
                        llet.setVisibility(View.GONE);
                    }
                }
            }
        });
        recycleview.setAdapter(adapter);
        contentView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cancelTask != null && cancelTask.isShowing()) {
                    cancelTask.dismiss();
                }
            }
        });
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelTask(id);
            }
        });
        cancelTask.setContentView(contentView);

        cancelTask.show();
    }

    private void cancelTask(int id) {
        if (mSelectedPos == 4) {
            reason = etReason.getText().toString().trim();
            if (EmptyUtils.isEmpty(reason)) {
                ToastUtils.show("请输入放弃原因");
                return;
            }
        }
        if (EmptyUtils.isEmpty(reason)) {
            ToastUtils.show("请选择放弃原因");
            return;
        }
        HttpUtils.givein(id, reason).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("操作成功");
                onRefreshData();
            }
        });
        if (cancelTask != null && cancelTask.isShowing()) {
            cancelTask.dismiss();
        }
    }

    private void setData() {
        reasonlist.clear();
        reasonlist.add(new SelectReasonBean("无法下载", true));
        reasonlist.add(new SelectReasonBean("有难度", false));
        reasonlist.add(new SelectReasonBean("先看看别的", false));
        reasonlist.add(new SelectReasonBean("没有WIFI", false));
        reasonlist.add(new SelectReasonBean("其他", false));
        reason = reasonlist.get(0).getReason();
        for (int i = 0; i < reasonlist.size(); i++) {
            if (reasonlist.get(i).isSelected()) {
                mSelectedPos = i;
            }
        }
    }
}
