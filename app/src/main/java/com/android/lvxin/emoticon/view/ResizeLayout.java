package com.android.lvxin.emoticon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * @author lvxin
 * @ClassName: ResizeLayout
 * @Description: TODO
 * @date May 26, 2015 10:22:30 AM
 */
public class ResizeLayout extends RelativeLayout {

    private int mMaxParentHeight = 0;
    private ArrayList<Integer> heightList = new ArrayList<Integer>();
    private OnResizeListener mListener;

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureHeight = measureHeight(heightMeasureSpec);
        heightList.add(measureHeight);
        if (mMaxParentHeight != 0) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (heightList.size() >= 2) {
            int oldh = heightList.get(0);
            int newh = heightList.get(heightList.size() - 1);
            int softHeight = mMaxParentHeight - newh;

            if (oldh == mMaxParentHeight) {
                if (mListener != null) {
                    mListener.onSoftPop(softHeight);
                }
            } else if (newh == mMaxParentHeight) {
                if (mListener != null) {
                    mListener.onSoftClose(softHeight);
                }
            } else {
                if (mListener != null) {
                    mListener.onSoftChanegHeight(softHeight);
                }
            }
            heightList.clear();
        } else {
            heightList.clear();
        }
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;

            default:
                break;

        }
        return result;
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

    /**
     *
     */
    public interface OnResizeListener {

        /**
         * 软键盘弹起
         */
        void onSoftPop(int height);

        /**
         * 软键盘关闭
         */
        void onSoftClose(int height);

        /**
         * 软键盘高度改变
         */
        void onSoftChanegHeight(int height);
    }
}