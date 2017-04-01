package com.android.lvxin.emoticon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.lvxin.R;
import com.android.lvxin.emoticon.utils.EmoticonsKeyboardBuilder;
import com.android.lvxin.emoticon.utils.Tools;
import com.android.lvxin.emoticon.view.i.IEmoticonsKeyboard;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
* @ClassName: EmoticonsToolBarView 
* @Description: TODO
* @author lvxin
* @date May 26, 2015 10:21:39 AM
 */
public class EmoticonsToolBarView extends RelativeLayout implements IEmoticonsKeyboard {
    
    private LayoutInflater inflater;
    private Context mContext;
    private HorizontalScrollView hsvToolbar;
    private LinearLayout lyTool;
    
    private ArrayList<ImageView> mToolBtnList = new ArrayList<ImageView>();
    private int mBtnWidth = 60;
    
    public EmoticonsToolBarView(Context context) {
        this(context, null);
    }
    
    public EmoticonsToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_emoticonstoolbar, this);
        this.mContext = context;
        findView();
    }
    
    private void findView() {
        hsvToolbar = (HorizontalScrollView)findViewById(R.id.hsv_toolbar);
        lyTool = (LinearLayout)findViewById(R.id.ly_tool);
    }
    
    private void scrollToBtnPosition(final int position) {
        int childCount = lyTool.getChildCount();
        if (position < childCount) {
            hsvToolbar.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = hsvToolbar.getScrollX();
                    
                    int childX = (int) ViewHelper.getX(lyTool.getChildAt(position));
                    
                    if (childX < mScrollX) {
                        hsvToolbar.scrollTo(childX, 0);
                        return;
                    }
                    
                    int childWidth = (int) lyTool.getChildAt(position).getWidth();
                    int hsvWidth = hsvToolbar.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;
                    
                    if (childRight > scrollRight) {
                        hsvToolbar.scrollTo(childRight - scrollRight, 0);
                        return;
                    }
                }
            });
        }
    }
    
    public void setToolBtnSelect(int select) {
        scrollToBtnPosition(select);
        for (int i = 0; i < mToolBtnList.size(); i++) {
            if (select == i) {
                mToolBtnList.get(i).setBackgroundColor(
                    getResources().getColor(R.color.toolbar_btn_select));
            } else {
                mToolBtnList.get(i).setBackgroundColor(
                    getResources().getColor(R.color.toolbar_btn_nomal));
            }
        }
    }

    /**
     *
     * @param width
     */
    public void setBtnWidth(int width) {
        mBtnWidth = width;
    }

    /**
     *
     * @param rec
     */
    public void addData(int rec) {
        if (lyTool != null) {
            LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toolBtnView = inflater.inflate(R.layout.item_toolbtn, null);
            ImageView ivIcon = (ImageView)toolBtnView.findViewById(R.id.iv_icon);
            ivIcon.setImageResource(rec);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(Tools.dip2px(
                mContext, mBtnWidth), LayoutParams.MATCH_PARENT);
            ivIcon.setLayoutParams(imgParams);
            lyTool.addView(toolBtnView);
            final int position = mToolBtnList.size();
            mToolBtnList.add(ivIcon);
            ivIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListeners != null && !mItemClickListeners.isEmpty()) {
                        for (OnToolBarItemClickListener listener : mItemClickListeners) {
                            listener.onToolBarItemClick(position);
                        }
                    }
                }
            });
        }
    }
    
    private int getIdValue() {
        int childCount = getChildCount();
        int id = 1;
        if (childCount == 0) {
            return id;
        }
        boolean isKeep = true;
        while (isKeep) {
            isKeep = false;
            Random random = new Random();
            id = random.nextInt(100);
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).getId() == id) {
                    isKeep = true;
                    break;
                }
            }
        }
        return id;
    }

    /**
     *
     * @param view
     * @param isRight
     */
    public void addFixedView(View view, boolean isRight) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams hsvParams = (RelativeLayout.LayoutParams) hsvToolbar
            .getLayoutParams();
        if (view.getId() <= 0) {
            view.setId(getIdValue());
        }
        if (isRight) {
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, view.getId());
        } else {
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF, view.getId());
        }
        addView(view, params);
        hsvToolbar.setLayoutParams(hsvParams);
    }
    
    @Override
    public void setBuilder(EmoticonsKeyboardBuilder builder) {
        /*
        mEmoticonSetBeanList = builder.builder == null ? null : builder.builder
            .getEmoticonSetBeanList();
        if (mEmoticonSetBeanList == null) {
            return;
        }
        
        int i = 0;
        for (EmoticonSetBean bean : mEmoticonSetBeanList) {
            LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toolBtnView = inflater.inflate(R.layout.item_toolbtn, null);
            View v_spit = (View)toolBtnView.findViewById(R.id.v_spit);
            ImageView iv_icon = (ImageView)toolBtnView.findViewById(R.id.iv_icon);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(Tools.dip2px(
                mContext, mBtnWidth), LayoutParams.MATCH_PARENT);
            iv_icon.setLayoutParams(imgParams);
            lyTool.addView(toolBtnView);
            
            int resId = mContext.getResources().getIdentifier(bean.getIconUri(), "drawable",
                mContext.getPackageName());
            iv_icon.setImageResource(resId);
            mToolBtnList.add(iv_icon);
            
            final int finalI = i;
            iv_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListeners != null && !mItemClickListeners.isEmpty()) {
                        for (OnToolBarItemClickListener listener : mItemClickListeners) {
                            listener.onToolBarItemClick(finalI);
                        }
                    }
                }
            });
            i++;
        }
        */
        setToolBtnSelect(0);
    }
    
    private List<OnToolBarItemClickListener> mItemClickListeners;

    /**
     *
     */
    public interface OnToolBarItemClickListener {
        /**
         *
         * @param position
         */
        void onToolBarItemClick(int position);
    }

    /**
     *
     * @param listener
     */
    public void addOnToolBarItemClickListener(OnToolBarItemClickListener listener) {
        if (mItemClickListeners == null) {
            mItemClickListeners = new ArrayList<OnToolBarItemClickListener>();
        }
        mItemClickListeners.add(listener);
    }
    
    public void setOnToolBarItemClickListener(OnToolBarItemClickListener listener) {
        addOnToolBarItemClickListener(listener);
    }
}
