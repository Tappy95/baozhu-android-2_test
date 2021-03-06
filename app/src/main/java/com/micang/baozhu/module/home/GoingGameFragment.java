package com.micang.baozhu.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.GoingAdapter;
import com.micang.baozhu.module.web.MYGameDetailsActivity;
import com.micang.baozhu.module.web.PCddGameDetailActivity;
import com.micang.baozhu.module.web.SignMakeMYGameDetailActivity;
import com.micang.baozhu.module.web.SignMakePcddGameDetailActivity;
import com.micang.baozhu.module.web.SignMakeXWGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskMYGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskPcddGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskXWGameDetailActivity;
import com.micang.baozhu.module.web.XWGameDetailActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseLazyFragment;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/9/4 17:20
 * @describe describe
 */
@BindEventBus
public class GoingGameFragment extends BaseLazyFragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycleview;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;

    private List<GameBean.ListBean> list = new ArrayList<>();
    private GoingAdapter adapter;
    private UserBean data;
    private String moblie;
    private int pageSize = 20;
    private int pageNum = 1;

    @Override
    protected int layoutId() {
        return R.layout.activity_going;
    }

    @Override
    protected void init(View rootView) {
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recycleview = rootView.findViewById(R.id.recycleview);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        InitRecycleView();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            moblie = data.mobile;
        }
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    private void onRefreshData() {
        pageNum = 1;
        list.clear();
        if (null != adapter) {
            adapter.setEnableLoadMore(false);
            adapter.notifyDataSetChanged();
        }
        getGameList();
    }

    private void InitRecycleView() {
        recycleview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        recycleview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new GoingAdapter(R.layout.going_item, list);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                final GameBean.ListBean listBean = list.get(position);
                String gameId = listBean.id;
                String lid = listBean.lid;
                int tryTag = listBean.tryTag;
                int signinId = listBean.signinId;
                String interfaceName = listBean.interfaceName;
                switch (view.getId()) {
                    case R.id.constraint:
                    case R.id.bt_continue:
                        if (tryTag == 1) {
                            if ("PCDD".equals(interfaceName)) {
                                HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
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
                                HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
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
                                HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
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
                        } else if (tryTag == 2) {
                            int id = Integer.parseInt(gameId);
                            if ("PCDD".equals(interfaceName)) {
                                Intent intent = new Intent(activity, SignMakePcddGameDetailActivity.class);
                                intent.putExtra("gameid", id);
                                intent.putExtra("signid", signinId);
                                startActivity(intent);
                            }
                            if ("MY".equals(interfaceName)) {
                                Intent intent = new Intent(activity, SignMakeMYGameDetailActivity.class);
                                intent.putExtra("gameid", id);
                                intent.putExtra("signid", signinId);
                                startActivity(intent);
                            }
                            if ("xw-Android".equals(interfaceName)) {
                                Intent intent = new Intent(activity, SignMakeXWGameDetailActivity.class);
                                intent.putExtra("gameid", id);
                                intent.putExtra("signid", signinId);
                                startActivity(intent);
                            }

                        } else if (tryTag == 3) {
                            int id = Integer.parseInt(gameId);
                            if ("PCDD".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskPcddGameDetailActivity.class);
                                intent.putExtra("type", "1");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                            if ("MY".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskMYGameDetailActivity.class);
                                intent.putExtra("type", "1");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                            if ("xw-Android".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskXWGameDetailActivity.class);
                                intent.putExtra("type", "1");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                        } else if (tryTag == 4) {
                            int id = Integer.parseInt(gameId);
                            if ("PCDD".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskPcddGameDetailActivity.class);
                                intent.putExtra("type", "2");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                            if ("MY".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskMYGameDetailActivity.class);
                                intent.putExtra("type", "2");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                            if ("xw-Android".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskXWGameDetailActivity.class);
                                intent.putExtra("type", "2");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                        } else {
                            int id = Integer.parseInt(gameId);
                            if ("PCDD".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskPcddGameDetailActivity.class);
                                intent.putExtra("type", "3");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                            if ("MY".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskMYGameDetailActivity.class);
                                intent.putExtra("type", "3");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                            if ("xw-Android".equals(interfaceName)) {
                                Intent intent = new Intent(activity, VipTaskXWGameDetailActivity.class);
                                intent.putExtra("type", "3");
                                intent.putExtra("gameid", id);
                                intent.putExtra("vipId", signinId);
                                startActivity(intent);
                            }
                        }
                        break;
                    case R.id.btnDelete:
                        if (tryTag == 1) {
                            HttpUtils.remove(lid).enqueue(new Observer<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response) {
                                    if (position == 0 && list.size() == 1) {
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                        emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                                        recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
                                    } else if (position >= 0 && position < list.size()) {
                                        list.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                }
                            });
                        } else if (tryTag == 2) {
                            HttpUtils.removeQDZ(lid).enqueue(new Observer<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response) {
                                    if (position == 0 && list.size() == 1) {
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                        emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                                        recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
                                    } else if (position >= 0 && position < list.size()) {
                                        list.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                }
                            });
                        } else if (tryTag == 3) {
                            int id = Integer.parseInt(gameId);
                            HttpUtils.deleteActiveTask(id).enqueue(new Observer<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response) {
                                    if (position == 0 && list.size() == 1) {
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                        emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                                        recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
                                    } else if (position >= 0 && position < list.size()) {
                                        list.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                }
                            });
                        } else if (tryTag == 4) {
                            int id = Integer.parseInt(gameId);
                            HttpUtils.deleteActiveTask(id).enqueue(new Observer<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response) {
                                    if (position == 0 && list.size() == 1) {
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                        emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                                        recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
                                    } else if (position >= 0 && position < list.size()) {
                                        list.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                }
                            });
                        } else {
                            int id = Integer.parseInt(gameId);
                            HttpUtils.deleteActiveTask(id).enqueue(new Observer<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response) {
                                    if (position == 0 && list.size() == 1) {
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                        emptyLayout.setVisibility(EmptyUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
                                        recycleview.setVisibility(EmptyUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
                                    } else if (position >= 0 && position < list.size()) {
                                        list.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                }
                            });
                        }


                        break;
                }

            }
        });
        recycleview.setAdapter(adapter);
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

    private void getGameList() {
        HttpUtils.tryPlayList(pageSize, pageNum).enqueue(new Observer<BaseResult<GameBean>>() {
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
