package com.micang.baozhu.module.information;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.util.EmptyUtils;

import java.util.ArrayList;
import java.util.List;


public class NewsListAdapter extends BaseMultiItemQuickAdapter<HippoNewsBean, BaseViewHolder> {
    private final int TEXT = 0;
    private final int ONE_IMAGE = 1;
    private final int THREE_IMAGE = 2;
    private final int IS_ADD = 3;
    private int finalI;
    private List<String> idList = new ArrayList<>();//id

    public NewsListAdapter(@Nullable List<HippoNewsBean> data) {
        super(data);
        addItemType(TEXT, R.layout.adapter_news_list_text);
        addItemType(ONE_IMAGE, R.layout.adapter_news_list_img);
        addItemType(THREE_IMAGE, R.layout.adapter_news_list_imgs);
        addItemType(IS_ADD, R.layout.adapter_isads_list_imgs);
    }

    @Override
    protected void convert(BaseViewHolder helper, HippoNewsBean item) {
        String id = item.id;
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_time_info, item.source + "     " + item.update_time);
        switch (helper.getItemViewType()) {
            case TEXT:
                break;
            case ONE_IMAGE:
                ImageView iv1 = helper.getView(R.id.iv_1);
                List<HippoNewsBean.ImagesBean> covers = item.images;
                Glide.with(mContext).load(covers.get(0).url).placeholder(R.drawable.no_banner).error(R.drawable.no_banner)
                        .into(iv1);
                break;
            case IS_ADD:
                ImageView ivadd1 = helper.getView(R.id.iv_1);
                List<HippoNewsBean.AdBean> ad = item.ad;
                if (EmptyUtils.isNotEmpty(ad)) {
                    helper.setText(R.id.tv_time_info, "广告");
                    HippoNewsBean.AdBean adBean = ad.get(0);
                    if (EmptyUtils.isNotEmpty(adBean)) {
                        List<HippoNewsBean.AdBean.NativeadBean> nativead = adBean.nativead;
                        if (EmptyUtils.isNotEmpty(nativead)) {
                            if (EmptyUtils.isNotEmpty(nativead)) {
                                String title = nativead.get(0).title;
                                String desc = nativead.get(0).desc;
                                helper.setText(R.id.tv_title, title);
                                String url = nativead.get(0).img.get(0).url;
                                Glide.with(mContext).load(url).placeholder(R.drawable.no_banner).error(R.drawable.no_banner)
                                        .into(ivadd1);
                            }
                        }
                    }
                }
                break;
            case THREE_IMAGE:
                ImageView ivs1 = helper.getView(R.id.iv_1);
                ImageView ivs2 = helper.getView(R.id.iv_2);
                ImageView ivs3 = helper.getView(R.id.iv_3);
                List<HippoNewsBean.ImagesBean> covers3 = item.images;

                Glide.with(mContext).load(covers3.get(0).url).placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(ivs1);
                Glide.with(mContext).load(covers3.get(1).url).placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(ivs2);
                Glide.with(mContext).load(covers3.get(2).url).placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(ivs3);
                break;
            default:
        }
        if (!item.isshowed) {
            boolean is_ad = item.is_ad;
            if (is_ad) {
                List<String> imp_tracking = item.imp_tracking;
                if (EmptyUtils.isNotEmpty(imp_tracking)) {
                    volleyGet(imp_tracking);
                }
                List<HippoNewsBean.AdBean> ad = item.ad;
                if (EmptyUtils.isNotEmpty(ad)) {
                    for (int j = 0; j < ad.size(); j++) {
                        HippoNewsBean.AdBean adBean = ad.get(j);
                        List<String> imp_tracking1 = adBean.imp_tracking;
                        volleyGet(imp_tracking1);
                    }
                }
            } else {
                List<String> imp_tracking = item.imp_tracking;
                volleyGet(imp_tracking);
            }
            idList.add(id);
            item.isshowed = true;
        }

    }


    private void volleyGet(String url) {
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
}
