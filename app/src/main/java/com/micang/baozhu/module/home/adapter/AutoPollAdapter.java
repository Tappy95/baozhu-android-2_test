package com.micang.baozhu.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.home.SelectVipNewsBean;
import com.micang.baozhu.util.EmptyUtils;

import java.util.List;

public class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
    private final Context mContext;
    private final List<SelectVipNewsBean> mData;

    public AutoPollAdapter(Context context, List<SelectVipNewsBean> list) {
        this.mContext = context;
        this.mData = list;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auto_list_item, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int i) {

        if (EmptyUtils.isNotEmpty(mData)) {
            SelectVipNewsBean selectVipNewsBean = mData.get(i % mData.size());
            String content = selectVipNewsBean.content;
            int days = selectVipNewsBean.days;
            if (days == 0) {
                holder.days.setText("刚刚");
            } else {
                holder.days.setText(days + "天前");
            }
            SpannableString ss1 = new SpannableString(content);
            ss1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.FF3352)), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.content.setText(ss1);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView days;

        public BaseViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            days = itemView.findViewById(R.id.tv_day);
        }
    }
}
