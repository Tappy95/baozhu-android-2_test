package com.micang.baselibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PopView extends PopupWindow implements View.OnClickListener {

    int[] ids;
    View touchView;
    int selectColor,defaultColor;
    private View view;
    private Activity context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(View view);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }




    public PopView(Activity context,int layoutId) {
        super(context);
        this.context=context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(layoutId,null);
        this.setContentView(view);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        List<View> children=getAllChildViews(view);
        for (View vi:children){
            vi.setOnClickListener(this);
        }

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                touchView.setSelected(false);
                setSelectColor(touchView,defaultColor);
            }
        });
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(!isDismiss(id)){
            mListener.onItemClickListener(v);
        }
        this.dismiss();
    }
    private boolean isDismiss(int id){
        for(int i:ids){
            if(i==id){
                this.dismiss();

                return true;
            }
        }
        return false;
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

    public void setShowView(View v, int selectColor,int defaultColor){
        this.defaultColor=defaultColor;
        this.selectColor=selectColor;
        this.touchView=v;
        this.showAsDropDown(v,0,2);
        setSelectColor(v,selectColor);
    }
    public void setDismiss(int... id){
        ids=id;
    }

    private void setSelectColor(View v,int id){
        try {
            List<View> list=getAllChildViews(v);
            for(View item:list){
                if(item instanceof TextView){
                    Method method=null;
                    Method[] methods=item.getClass().getMethods();
                    for (Method m:methods){
                        if(m.getName().equals("setTextColor")){
                            method=m;
                            break;
                        }
                    }
                    method.setAccessible(true);
                    int identifier=getId(context,id);
                    if(identifier>0)
                        method.invoke(item,identifier);//context.getResources().getColor(id));
//                    else method.invoke(item,id);
                    break;
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static int getId(Context context, int id) {
        Resources res=context.getResources();
        if(res==null){
            return 0;
        }
        return res.getColor(id);
    }
}