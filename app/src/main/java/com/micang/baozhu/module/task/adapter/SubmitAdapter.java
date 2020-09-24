package com.micang.baozhu.module.task.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.micang.baozhu.R;
import com.micang.baozhu.http.bean.task.TaskDetailBean;
import com.micang.baozhu.http.bean.user.PictureBean;
import com.micang.baozhu.module.information.HippoNewsBean;
import com.micang.baozhu.module.user.adapter.SelectPicAdapter;
import com.micang.baozhu.util.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/26 17:44
 * @describe describe
 */
public class SubmitAdapter extends BaseMultiItemQuickAdapter<TaskDetailBean.TaskSubmitBean, BaseViewHolder> {
    private final int INPUT = 1;
    private final int INPUTIMAGE = 2;
    private List<PictureBean> list = new ArrayList<>();
    private SelectPicAdapter adapter;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SubmitAdapter(List<TaskDetailBean.TaskSubmitBean> data) {
        super(data);
        addItemType(INPUT, R.layout.ll_input1);
        addItemType(INPUTIMAGE, R.layout.ll_input2);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskDetailBean.TaskSubmitBean item) {
        if (EmptyUtils.isNotEmpty(item)) {
            int type = item.type;
            switch (type) {
                case INPUT:
                    EditText etInput = helper.getView(R.id.et_input);
                    etInput.setHint(item.name);
                    break;
                case INPUTIMAGE:
                    RecyclerView recyclerView =   helper.getView(R.id.pic_recycle);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    adapter = new SelectPicAdapter(list);
                    break;
            }
        }
    }
}
