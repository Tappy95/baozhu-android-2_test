package com.micang.baozhu.module.task.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.task.TaskDetailBean;
import com.umeng.socialize.media.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/28 20:59
 * @describe describe
 */
public class MakeMoneyStepAdapter extends BaseQuickAdapter<TaskDetailBean.TaskStepBean, BaseViewHolder> {

    private List<String> urlList = new ArrayList<>();

    public MakeMoneyStepAdapter(int layoutResId, @Nullable List<TaskDetailBean.TaskStepBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskDetailBean.TaskStepBean item) {
        helper.setText(R.id.tv_tips,item.content);
        RecyclerView rv = helper.getView(R.id.rv_qr_code);
        LinearLayout llSinglePic = helper.getView(R.id.ll_single_pic);
        LinearLayout llLink = helper.getView(R.id.ll_link);
        TextView tvLink = helper.getView(R.id.tv_link);
        ImageView iMSinglePic = helper.getView(R.id.im_single_pic);
        switch (item.stepType){
            case 1:
                break;
            case 2:     //二维码列表
                rv.setVisibility(View.VISIBLE);
                rv.setLayoutManager(new GridLayoutManager(mContext,3));
                String[] qrCodeUrl = item.url.split(",");
                if (urlList.size() > 0){
                    urlList.clear();
                }
                for (int i = 0; i < qrCodeUrl.length; i++) {
                    urlList.add(qrCodeUrl[i]);
                }
                rv.setAdapter(new TaskDetailItemAdapter(R.layout.item_task_detail_rv,urlList));
                break;
            case 3:     // 图片单双张
                if (item.isMust == 1){
                    llSinglePic.setVisibility(View.VISIBLE);
                    helper.addOnClickListener(R.id.im_single_pic);
                    Glide.with(mContext).load(item.url).into(iMSinglePic);
                }else {
                    rv.setVisibility(View.VISIBLE);
                    rv.setLayoutManager(new GridLayoutManager(mContext,3));
                    String[] picUrl = item.url.split(",");
                    if (urlList.size() > 0){
                        urlList.clear();
                    }
                    for (int i = 0; i < picUrl.length; i++) {
                        urlList.add(picUrl[i]);
                    }
                    rv.setAdapter(new TaskDetailItemAdapter(R.layout.item_task_detail_rv,urlList));
                }
                break;
            case 4:     //url链接
                llLink.setVisibility(View.VISIBLE);
                tvLink.setText(item.url);
                helper.addOnClickListener(R.id.tv_copy);
                break;
        }
    }
}
