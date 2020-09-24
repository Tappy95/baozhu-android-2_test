package com.micang.baselibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.micang.baselibrary.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class PopMenu extends PopupWindow implements View.OnClickListener {

    int[] ids;
    View touchView;
    int selectColor, defaultColor;
    private View view;
    private Activity context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(View view);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }


    public PopMenu(Activity context, int layoutId) {
        super(context);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(layoutId, null);
        this.setContentView(view);
        int anchWidth=view.getWidth();
        int anchHeight=view.getHeight();
        this.setWidth(DensityUtil.dip2px(context, 144));
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击


        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        List<View> children = getAllChildViews(view);
        for (View vi : children) {
            if(vi instanceof LinearLayout)
            vi.setOnClickListener(this);
        }
    }

    //只做了针对下拉，靠右支持
    private void InitPopView(){
        View view=getContentView();
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)view.getLayoutParams();

        if(params!=null){
            params.setMargins(0,0,DensityUtil.dip2px(context, 10),0);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null)
            mListener.onItemClickListener(v);
        this.dismiss();
    }
    @Override
    public void showAsDropDown(View anchor) {
//        super.showAsDropDown(anchor);
        int marginLeft = DensityUtil.dip2px(context, 200);
        int marginTop = DensityUtil.dip2px(context, 10);
        this.showAsDropDown(anchor, marginLeft, marginTop,Gravity.RIGHT);
        bgAlpha(0.5f);
        InitPopView();

    }
    @Override
    public void dismiss(){
        super.dismiss();
        bgAlpha(1.0f);
    }
    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        ((Activity)context).getWindow().setAttributes(lp);
    }

    private List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }



    public static int getId(Context context, int id) {
        Resources res = context.getResources();
        if (res == null) {
            return 0;
        }
        return res.getColor(id);
    }
}