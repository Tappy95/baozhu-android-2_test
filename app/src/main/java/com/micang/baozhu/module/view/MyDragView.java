package com.micang.baozhu.module.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/9 16:02
 * @describe describe
 */
public class MyDragView extends RelativeLayout {

    private ViewDragHelper mDragger;

    private View mAutoBackView;

    public MyDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public int getViewHorizontalDragRange(View child) {
                //返回可拖动的子视图的水平运动范围(以像素为单位)的大小。
                //对于不能垂直移动的视图，此方法应该返回0。
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                //返回可拖动的子视图的竖直运动范围(以像素为单位)的大小。
                //对于不能垂直移动的视图，此方法应该返回0。
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //返回true表view示捕获当前touch到的
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > getWidth() - child.getMeasuredWidth()){
                    //超出左侧边界处理
                    left = getWidth() - child.getMeasuredWidth();
                } else if (left < 0) {
                    //超出右侧边界处理
                    left = 0;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top > getHeight() - child.getMeasuredHeight()){
                    //超出下边界处理
                    top = getHeight() - child.getMeasuredHeight();
                } else if (top < 0) {
                    //超出上边界处理
                    top = 0;
                }
                return top;
            }


        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //点击位置坐标
        int downX = (int) event.getX();
        int downY = (int) event.getY();
        //悬浮区域左上角坐标
        int x = (int) mAutoBackView.getX();
        int y = (int) mAutoBackView.getY();
        //悬浮区域宽高
        int width = mAutoBackView.getWidth();
        int height = mAutoBackView.getHeight();

        if ((downX >= x && downY >= y) && (downX <= (x+width) && downY <= (y+height)) ) {
            //点击在悬浮区域内部进行事件拦截，否则不拦截
            try {
                mDragger.processTouchEvent(event);
                return true;
            }catch (IllegalArgumentException e){
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * onFinishInflate 当View中所有的子控件均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAutoBackView = getChildAt(0);
    }

}
