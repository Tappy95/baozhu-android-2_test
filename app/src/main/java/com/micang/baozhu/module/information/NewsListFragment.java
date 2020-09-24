package com.micang.baozhu.module.information;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.micang.baselibrary.base.BaseLazyFragment;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.LocationUtils;
import com.micang.baozhu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class NewsListFragment extends BaseLazyFragment {
    protected static final String NEWS_ID = "news_id";
    protected String mNewsId;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<HippoNewsBean> arrayList = new ArrayList<>();
    private NewsListAdapter newsListAdapter;
    private int pageOffset = 0;
    private int refreshpage = 0;
    private int pageSize = 10;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private String page = "1";
    private BottomDialog mBottomDialog;
    private int finalI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_ID);
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_newlist;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init(View rootView) {
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        recyclerView = rootView.findViewById(R.id.recyclerView);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
    }

    public static NewsListFragment newInstance(String newsId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_ID, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() throws NullPointerException {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        newsListAdapter = new NewsListAdapter(arrayList);
        recyclerView.setAdapter(newsListAdapter);

        //rv上啦加载视图
        newsListAdapter.setLoadMoreView(new LoadMoreView() {
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

        newsListAdapter.setOnLoadMoreListener((new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                int i = pageOffset += 1;
                page = String.valueOf(i);
                getListData();
            }
        }), recyclerView);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        newsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (EmptyUtils.isNotEmpty(adapter.getData())) {
                    HippoNewsBean data = (HippoNewsBean) adapter.getData().get(position);
                    if (EmptyUtils.isEmpty(data)) {
                        ToastUtils.show("抱歉暂不支持该新闻浏览");
                        return;
                    } else {
                        boolean tokenEmpty = EmptyUtils.isTokenEmpty(activity);
                        if (tokenEmpty) {
                            Intent intent = new Intent(activity, NewLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        if (data.is_ad) {
                            String url = data.clk_url;
                            volleyGet(url);
                            List<HippoNewsBean.AdBean> ad = data.ad;
                            if (EmptyUtils.isNotEmpty(ad)) {
                                HippoNewsBean.AdBean adBean = ad.get(0);
                                if (EmptyUtils.isNotEmpty(adBean)) {
                                    List<String> clk_tracking = adBean.clk_tracking;
                                    volleyGet(clk_tracking);
                                    List<HippoNewsBean.AdBean.NativeadBean> nativead = adBean.nativead;
                                    if (EmptyUtils.isNotEmpty(nativead)) {
                                        String ldp = nativead.get(0).ldp;
                                        Intent intent = new Intent(activity, NewsDetailsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("URLS", ldp);
                                        intent.putExtra("isAdd", 0);
                                        intent.putExtra("content", nativead.get(0).title);
                                        intent.putExtra("title", nativead.get(0).title);
                                        startActivity(intent);
                                    }
                                }
                            }
                        } else {
                            String url = data.clk_url;
                            String title = data.title;
                            String id = data.id;
                            Intent intent = new Intent(activity, NewsDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("URLS", url);
                            intent.putExtra("isAdd", 1);
                            intent.putExtra("title", title);
                            intent.putExtra("content", id);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void delayload() {
        super.delayload();
        int i = pageOffset += 1;
        page = String.valueOf(i);
        getListData();
    }

    private void onRefreshData() {
        int i = refreshpage -= 1;
        page = String.valueOf(i);
        arrayList.clear();
        if (null != newsListAdapter) {
            newsListAdapter.setEnableLoadMore(true);
            newsListAdapter.notifyDataSetChanged();
        }
        getListData();
    }

    private void getListData() {
        double longitude = 0;
        double latitude = 0;
        try {
            Location location = LocationUtils.getInstance(activity).showLocation();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkGps();
        HttpUtils.getHIPPONews(mNewsId, page, longitude + "", latitude + "").enqueue(new Observer<BaseResult<List<HippoNewsBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                loadingLayout.setVisibility(View.GONE);
                List<HippoNewsBean> listBean = (List<HippoNewsBean>) response.data;
                if (EmptyUtils.isNotEmpty(listBean)) {
                    arrayList.addAll(listBean);
                    if (pageOffset == 0) { //刷新
                        newsListAdapter.setNewData(arrayList);
                    } else {
                        newsListAdapter.notifyDataSetChanged();
                    }
                    if (arrayList.size() < pageSize) {
                        newsListAdapter.loadMoreEnd();
                    } else {
                        newsListAdapter.loadMoreComplete();
                    }
                } else {
                    if (pageOffset == 0) { //刷新
                        newsListAdapter.notifyDataSetChanged();
                    } else {
                        newsListAdapter.loadMoreEnd();
                    }
                }
                refreshLayout.setRefreshing(false);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(arrayList) ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(EmptyUtils.isEmpty(arrayList) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                refreshLayout.setRefreshing(false);
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(arrayList) ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(EmptyUtils.isEmpty(arrayList) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                refreshLayout.setRefreshing(false);
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(EmptyUtils.isEmpty(arrayList) ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(EmptyUtils.isEmpty(arrayList) ? View.GONE : View.VISIBLE);
            }

        });

    }

    private void volleyGet(List<String> url) {
        if (EmptyUtils.isEmpty(url)) {
            return;
        } else {
            for (int i = 0; i < url.size(); i++) {
                finalI++;
                StringRequest request = new StringRequest(Request.Method.GET, url.get(i),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {//s为请求返回的字符串数据
//                            ToastUtils.show(s);
                                TLog.d("volley", s + finalI);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
//                            ToastUtils.show(volleyError.toString());
                                TLog.d("volley", volleyError.toString());
                            }
                        });
                //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
                request.setTag("testGet");
                //将请求加入全局队列中
                AppContext.getHttpQueues().add(request);
            }
        }

    }

    private void volleyGet(String url) {
        if (EmptyUtils.isEmpty(url)) {
            return;
        } else {
            finalI++;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {//s为请求返回的字符串数据
//                            ToastUtils.show(s);
                            TLog.d("volley", s + finalI);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
//                            ToastUtils.show(volleyError.toString());
                            TLog.d("volley", volleyError.toString());
                        }
                    });
            //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
            request.setTag("testGet");
            //将请求加入全局队列中
            AppContext.getHttpQueues().add(request);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocationUtils.getInstance(activity).removeLocationUpdatesListener();
    }

    public void checkGps() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        boolean ok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!ok) {
            showOpenGPS();
        }
    }

    private void showOpenGPS() {
        mBottomDialog = new BottomDialog(activity, true, true);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.bottom_dialog_open_gps, null);
        mBottomDialog.setContentView(contentView);

        mBottomDialog.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialog.dismiss();
            }
        });
        mBottomDialog.show();
    }
}
