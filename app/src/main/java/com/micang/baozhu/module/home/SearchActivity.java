package com.micang.baozhu.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.SearchGameListAdapter;
import com.micang.baozhu.module.home.adapter.SearchHistoryAdapter;
import com.micang.baozhu.module.view.AutoLineFeedLayoutManager;
import com.micang.baozhu.module.web.MYGameDetailsActivity;
import com.micang.baozhu.module.web.PCddGameDetailActivity;
import com.micang.baozhu.module.web.XWGameDetailActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@BindEventBus
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private EditText etGametitle;
    private TextView tvSearch;
    private RecyclerView recycleview;
    private LinearLayout emptyLayout;
    private ImageView tvDelete;
    private RecyclerView rvHistory;
    private RecyclerView rvRecommend;

    private List<GameBean.ListBean> list = new ArrayList<>();
    private List<String> historylist = new ArrayList<>();
    private List<String> tjlist = new ArrayList<>();
    private SearchGameListAdapter gameListAdapter;
    private String mobile;
    private SearchHistoryAdapter historyAdapter;
    private SearchHistoryAdapter recommendAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);

        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("宝猪游戏");
        etGametitle = findViewById(R.id.et_gametitle);
        tvSearch = findViewById(R.id.tv_search);
        recycleview = findViewById(R.id.recycleview);
        emptyLayout = findViewById(R.id.empty_layout);
        tvDelete = findViewById(R.id.tv_delete);
        rvHistory = findViewById(R.id.rv_history);
        rvRecommend = findViewById(R.id.rv_recommend);
        initRecycleView();
        initClick();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        history();
        recommend();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            UserBean data = event.data;
            if (EmptyUtils.isNotEmpty(data)) {
                mobile = data.mobile;
            }
        }
    }

    private void initRecycleView() {
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gameListAdapter = new SearchGameListAdapter(R.layout.item_search_game_list, list);
        recycleview.setAdapter(gameListAdapter);
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
                            Intent intent = new Intent(SearchActivity.this, PCddGameDetailActivity.class);
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
                            Intent intent = new Intent(SearchActivity.this, MYGameDetailsActivity.class);
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
                            Intent intent = new Intent(SearchActivity.this, XWGameDetailActivity.class);
                            intent.putExtra("URLS", url);
                            intent.putExtra("bean", listBean);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        rvHistory.setLayoutManager(new AutoLineFeedLayoutManager());
        historyAdapter = new SearchHistoryAdapter(R.layout.item_search_string, historylist);
        rvHistory.setAdapter(historyAdapter);
        historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String s = historylist.get(position);
                etGametitle.setText(s);
                etGametitle.setSelection(s.length());
                search();
            }
        });

        rvRecommend.setLayoutManager(new AutoLineFeedLayoutManager());
        recommendAdapter = new SearchHistoryAdapter(R.layout.item_search_string, tjlist);
        rvRecommend.setAdapter(recommendAdapter);
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String s = tjlist.get(position);
                etGametitle.setText(s);
                etGametitle.setSelection(s.length());
                search();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_search:
                search();
                break;
            case R.id.tv_delete:
                removeAll();
                break;
            default:
                break;
        }
    }


    private void search() {
        String game = etGametitle.getText().toString().trim();
        if (EmptyUtils.isEmpty(game)) {
            ToastUtils.show("请输入内容");
            return;
        }
        HttpUtils.lenovoList(game, 1, 30).enqueue(new Observer<BaseResult<GameBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                GameBean gameBean = (GameBean) response.data;
                List<GameBean.ListBean> listBeans = gameBean.list;
                if (EmptyUtils.isNotEmpty(listBeans)) {
                    list.clear();
                    list.addAll(listBeans);
                    gameListAdapter.notifyDataSetChanged();
                    recycleview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                } else {
                    list.clear();
                    recycleview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    private void history() {
        HttpUtils.userSearchLogs().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    historylist.clear();
                    List<String> data = (List<String>) response.data;
                    historylist.addAll(data);
                    historyAdapter.notifyDataSetChanged();
                } else {
                    historylist.clear();
                    historyAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void recommend() {
        HttpUtils.tjList().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<String> data = (List<String>) response.data;
                    tjlist.clear();
                    tjlist.addAll(data);
                    recommendAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void removeAll() {
        HttpUtils.removeAll().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                history();
            }
        });
    }
}
