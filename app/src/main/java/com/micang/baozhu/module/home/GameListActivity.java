package com.micang.baozhu.module.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.BannerBean;
import com.micang.baozhu.http.bean.MainPositionBean;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.home.GameTypeBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.GameListAdapter;
import com.micang.baozhu.module.home.adapter.GameTypeAdapter;
import com.micang.baozhu.module.task.NewTaskActivity;
import com.micang.baozhu.module.web.*;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventToHome;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.DensityUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@BindEventBus
public class GameListActivity extends BaseActivity {
    private RelativeLayout head;
    private LinearLayout llBack;
    private TextView tvTitle;
    private Banner banner;
    private RecyclerView recycleviewType;
    private TextView tvGametype;
    private RecyclerView recycleview;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LinearLayout loadingLayout;
    private ImageView ivNotice;
    private List<BannerBean> bannerBeans;
    private LinearLayout llSearch;
    private List<String> images = new ArrayList<>();
    private List<GameTypeBean> typeList = new ArrayList<>();
    private List<GameBean.ListBean> list = new ArrayList<>();
    private GameTypeAdapter typeAdapter;
    private String typeName;
    private int selectId = -1;
    private int pageSize = 20;
    private int pageNum = 1;
    private int typeid;
    private GameListAdapter gameListAdapter;
    private UserBean data;
    private String mobile;
    private GyrosensorUtils gyrosensor;

    @Override
    public int layoutId() {
        return R.layout.activity_game_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);

        head = findViewById(R.id.head);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        banner = findViewById(R.id.banner);
        tvTitle.setText("充值高返");
        recycleviewType = findViewById(R.id.recycleview_type);
        tvGametype = findViewById(R.id.tv_gametype);
        recycleview = findViewById(R.id.recycleview);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        emptyLayout = findViewById(R.id.empty_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        ivNotice = findViewById(R.id.iv_notice);
        gyrosensor = GyrosensorUtils.getInstance(this);
        llSearch = findViewById(R.id.ll_search);
//        getBanner();
        InitRecycleView();
        getGameType();
        initClick();
    }

    private void initClick() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameListActivity.this, MoreGameTaskActivity.class));
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameListActivity.this, SearchActivity.class));
            }
        });
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
    protected void onResume() {
        super.onResume();
        addGyro();
    }

    private void InitRecycleView() {

        recycleviewType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        typeAdapter = new GameTypeAdapter(R.layout.item_game_type, typeList);
        typeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (selectId != position) {
                    typeList.get(selectId).setSelected(false);
                    adapter.notifyItemChanged(selectId);
                    selectId = position;
                    typeList.get(selectId).setSelected(true);
                    adapter.notifyItemChanged(selectId);
                    typeName = typeList.get(position).typeName;
                    typeid = typeList.get(position).id;
                    tvGametype.setText(typeName);
                    onRefreshData();
                }
            }
        });
        recycleviewType.setAdapter(typeAdapter);
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gameListAdapter = new GameListAdapter(R.layout.item_game_list, list);
        gameListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
                            Intent intent = new Intent(GameListActivity.this, PCddGameDetailActivity.class);
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
                            Intent intent = new Intent(GameListActivity.this, MYGameDetailsActivity.class);
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
                            Intent intent = new Intent(GameListActivity.this, XWGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
                if ("xhm_api".equals(interfaceName)) {
                    HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response) {
                            String imei = getIMEI();
//                            TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                            if (telephonyMgr != null) {
//                                imei = telephonyMgr.getDeviceId();
//                            }
                            String url = (String) response.data + "&deviceId=" + imei;
//                            String url = (String) response.data;
                            Intent intent = new Intent(GameListActivity.this, RedhatGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
                if ("ABX_Api".equals(interfaceName)) {
                    HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response) {
                            String url = (String) response.data;
                            Intent intent = new Intent(GameListActivity.this, MYGameDetailsActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        gameListAdapter.setLoadMoreView(new LoadMoreView() {
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

        gameListAdapter.setOnLoadMoreListener((new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                if (list.size() >= 20) {
                    getGameList(typeid);
                }
            }
        }), recycleview);

        recycleview.setAdapter(gameListAdapter);
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
        if (null != gameListAdapter) {
            gameListAdapter.setEnableLoadMore(false);
            gameListAdapter.notifyDataSetChanged();
        }
        getGameList(typeid);
    }

    @SuppressLint("MissingPermission")
    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        if (EmptyUtils.isEmpty(imei)) {
            //由于Android 10系统限制，无法获取IMEI等标识，如果为空再去获取Android ID作为标识进行传递
            String deviceId = Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            imei = deviceId;
        }
        return imei;
    }

    private void getGameType() {
        HttpUtils.tpGameType().enqueue(new Observer<BaseResult<List<GameTypeBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<GameTypeBean> data = (List<GameTypeBean>) response.data;
                    typeList.clear();
                    typeList.addAll(data);
                    typeName = typeList.get(0).typeName;
                    typeid = typeList.get(0).id;
                    tvGametype.setText(typeName);
                    typeList.get(0).setSelected(true);
                    for (int i = 0; i < typeList.size(); i++) {
                        if (typeList.get(i).isSelected) {
                            selectId = i;
                        }
                    }
                    typeAdapter.notifyDataSetChanged();
                    getGameList(typeid);
                }
            }
        });
    }

    private void addGyro() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        CoordinatesBean coordinates = gyrosensor.getCoordinates();
        String anglex = String.format("%.2f", coordinates.anglex);
        String angley = String.format("%.2f", coordinates.angley);
        String anglez = String.format("%.2f", coordinates.anglez);
        HttpUtils.addGyro(anglex, angley, anglez, 2).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });

    }

    private void getGameList(int selectId) {
        HttpUtils.tpGameList(pageSize, pageNum, selectId).enqueue(new Observer<BaseResult<GameBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                loadingLayout.setVisibility(View.GONE);
                GameBean gameBean = (GameBean) response.data;
                List<GameBean.ListBean> listBeans = gameBean.list;
                if (EmptyUtils.isNotEmpty(listBeans)) {
                    list.addAll(listBeans);
                    if (pageNum == 1) { //刷新
                        gameListAdapter.setNewData(list);
                    } else {
                        gameListAdapter.notifyDataSetChanged();
                    }
                    if (list.size() < pageSize) {
                        gameListAdapter.loadMoreEnd();
                    } else {
                        gameListAdapter.loadMoreComplete();
                    }
                } else {
                    if (pageNum == 1) { //刷新
                        gameListAdapter.notifyDataSetChanged();
                    } else {
                        gameListAdapter.loadMoreEnd();
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

    private void getBanner() {
        HttpUtils.queryBanner("3").enqueue(new Observer<BaseResult<List<BannerBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                bannerBeans = (List<BannerBean>) response.data;
                if (EmptyUtils.isNotEmpty(bannerBeans)) {
                    images.clear();
                    for (int i = 0; i < bannerBeans.size(); i++) {
                        images.add(bannerBeans.get(i).imageUrl);
                    }
                    startBanner();
                }
            }
        });
    }

    private void startBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        if (this.isFinishing()) {
            return;
        }
        banner.setImageLoader(new GlideImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String linkUrl = bannerBeans.get(position).linkUrl;
                if (EmptyUtils.isTokenEmpty(GameListActivity.this)) {
                    return;
                }
                if (linkUrl.startsWith("http")) {
                    startActivity(new Intent(GameListActivity.this, WebActivity.class).putExtra("url", linkUrl));
                }

                if (linkUrl.equals("快速赚")) {
                    startActivity(new Intent(GameListActivity.this, NewTaskActivity.class));
                }
                if (linkUrl.equals("邀请赚")) {
                    startActivity(new Intent(GameListActivity.this, GeneralizeActivity.class));
                }
                if (linkUrl.equals("签到赚")) {
                    EventBus.getDefault().postSticky(new EventToHome<>(10099, new MainPositionBean(1)));
                    finish();
                }
                if (linkUrl.equals("会员")) {
                    startActivity(new Intent(GameListActivity.this, NewVipActivity.class));
                }

            }
        });
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(DensityUtil.dip2px(context, 6));
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            //Glide 加载图片简单用法
            Glide.with(context).load(path).apply(options).into(imageView);
        }
    }
}
