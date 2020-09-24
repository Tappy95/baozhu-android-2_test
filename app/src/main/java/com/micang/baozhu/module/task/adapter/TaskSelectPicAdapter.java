package com.micang.baozhu.module.task.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.task.TaskPictureBean;
import com.micang.baozhu.http.bean.user.PictureBean;

import java.io.File;
import java.util.List;

public class TaskSelectPicAdapter extends BaseMultiItemQuickAdapter<TaskPictureBean, BaseViewHolder> {
    private final int ADD = 0;
    private final int NOADD = 1;

    public TaskSelectPicAdapter(@Nullable List<TaskPictureBean> data) {
        super(data);
        addItemType(ADD, R.layout.select_taskpic_list_item);
        addItemType(NOADD, R.layout.select_taskpic_list_item1);
    }


    @Override
    protected void convert(BaseViewHolder helper, TaskPictureBean item) {

        if (item != null) {
            switch (helper.getItemViewType()) {
                case ADD:
                    helper.addOnClickListener(R.id.iv_add);
                    break;
                case NOADD:
                    String path = item.path;
                    ImageView image = helper.getView(R.id.iv_pic);
                    Glide.with(mContext).load(path).into(image);
                    helper.addOnClickListener(R.id.iv_delete);
                    break;
                default:
                    break;
            }
        }
    }


}
