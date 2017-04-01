package com.android.lvxin.emoticon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.android.lvxin.emoticon.adapter.EmoticonsAdapter;
import com.android.lvxin.emoticon.bean.EmoticonBean;
import com.android.lvxin.emoticon.bean.EmoticonSetBean;
import com.android.lvxin.emoticon.utils.EmoticonsKeyboardBuilder;
import com.android.lvxin.emoticon.view.i.IEmoticonsKeyboard;
import com.android.lvxin.emoticon.view.i.IView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lvxin
 * @ClassName: EmoticonsPageView
 * @Description: TODO
 * @date May 26, 2015 10:21:19 AM
 */
@SuppressLint("NewApi")
public class EmoticonsPageView extends ViewPager implements IEmoticonsKeyboard, IView {

    public int mOldPagePosition = -1;
    private Context mContext;
    private int mMaxEmoticonSetPageCount = 0;
    private List<EmoticonSetBean> mEmoticonSetBeanList;
    private EmoticonsViewPagerAdapter mEmoticonsViewPagerAdapter;
    private ArrayList<View> mEmoticonPageViews = new ArrayList<View>();
    private List<IView> mIViewListeners;
    private OnEmoticonsPageViewListener mOnEmoticonsPageViewListener;

    public EmoticonsPageView(Context context) {
        this(context, null);
    }

    public EmoticonsPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        EmoticonsPageView.this.post(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }

    private void updateView() {
        if (mEmoticonSetBeanList == null) {
            return;
        }

        if (mEmoticonsViewPagerAdapter == null) {
            mEmoticonsViewPagerAdapter = new EmoticonsViewPagerAdapter();
            setAdapter(mEmoticonsViewPagerAdapter);
            setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (mOldPagePosition < 0) {
                        mOldPagePosition = 0;
                    }
                    int end = 0;
                    int pagerPosition = 0;
                    for (EmoticonSetBean emoticonSetBean : mEmoticonSetBeanList) {

                        int size = getPageCount(emoticonSetBean);

                        if (end + size > position) {
                            if (mOnEmoticonsPageViewListener != null) {
                                mOnEmoticonsPageViewListener.emoticonsPageViewCountChanged(size);
                            }
                            // 上一页
                            if (mOldPagePosition - end >= size) {
                                if (position - end >= 0) {
                                    if (mOnEmoticonsPageViewListener != null) {
                                        mOnEmoticonsPageViewListener.playTo(position - end);
                                    }
                                }
                                if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
                                    for (IView listener : mIViewListeners) {
                                        listener.onPageChangeTo(pagerPosition);
                                    }
                                }
                                break;
                            }
                            // 下一页
                            if (mOldPagePosition - end < 0) {
                                if (mOnEmoticonsPageViewListener != null) {
                                    mOnEmoticonsPageViewListener.playTo(0);
                                }
                                if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
                                    for (IView listener : mIViewListeners) {
                                        listener.onPageChangeTo(pagerPosition);
                                    }
                                }
                                break;
                            }
                            // 本页切换
                            if (mOnEmoticonsPageViewListener != null) {
                                mOnEmoticonsPageViewListener.playBy(mOldPagePosition - end,
                                        position - end);
                            }
                            break;
                        }
                        pagerPosition++;
                        end += size;
                    }
                    mOldPagePosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        mEmoticonPageViews.clear();
        mEmoticonsViewPagerAdapter.notifyDataSetChanged();

        for (EmoticonSetBean bean : mEmoticonSetBeanList) {
            ArrayList<EmoticonBean> emoticonList = bean.getEmoticonList();
            if (emoticonList != null) {
                int emoticonSetSum = emoticonList.size();
                int row = bean.getRow();
                int line = bean.getLine();

                int del = bean.isShowDelBtn() ? 1 : 0;
                int everyPageMaxSum = row * line - del;
                int pageCount = getPageCount(bean);

                mMaxEmoticonSetPageCount = Math.max(mMaxEmoticonSetPageCount, pageCount);

                int start = 0;
                int end = everyPageMaxSum > emoticonSetSum ? emoticonSetSum : everyPageMaxSum;

                RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                gridParams.addRule(ResizeLayout.CENTER_VERTICAL);

                for (int i = 0; i < pageCount; i++) {
                    RelativeLayout rl = new RelativeLayout(mContext);
                    GridView gridView = new GridView(mContext);
                    gridView.setMotionEventSplittingEnabled(false);
                    gridView.setNumColumns(bean.getRow());
                    gridView.setBackgroundColor(Color.TRANSPARENT);
                    gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                    gridView.setCacheColorHint(0);
                    gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    gridView.setGravity(Gravity.CENTER);
                    gridView.setVerticalScrollBarEnabled(false);

                    List<EmoticonBean> list = new ArrayList<EmoticonBean>();
                    for (int j = start; j < end; j++) {
                        list.add(emoticonList.get(j));
                    }

                    // 删除按钮
                    if (bean.isShowDelBtn()) {
                        int count = bean.getLine() * bean.getRow();
                        while (list.size() < count - 1) {
                            list.add(null);
                        }
                        list.add(new EmoticonBean(EmoticonBean.FACE_TYPE_DEL, "icon_emotion_del",
                                null, null));
                    } else {
                        int count = bean.getLine() * bean.getRow();
                        while (list.size() < count) {
                            list.add(null);
                        }
                    }

                    EmoticonsAdapter adapter = new EmoticonsAdapter(mContext, list);
                    gridView.setAdapter(adapter);
                    rl.addView(gridView, gridParams);
                    mEmoticonPageViews.add(rl);
                    adapter.setOnItemListener(this);

                    start = everyPageMaxSum + i * everyPageMaxSum;
                    end = everyPageMaxSum + (i + 1) * everyPageMaxSum;
                    if (end >= emoticonSetSum) {
                        end = emoticonSetSum;
                    }
                }
            }
        }
        mEmoticonsViewPagerAdapter.notifyDataSetChanged();

        if (mOnEmoticonsPageViewListener != null) {
            mOnEmoticonsPageViewListener.emoticonsPageViewInitFinish(mMaxEmoticonSetPageCount);
        }
    }

    public void setPageSelect(int position) {
        if (getAdapter() != null && position >= 0 && position < mEmoticonSetBeanList.size()) {
            int count = 0;
            for (int i = 0; i < position; i++) {
                count += getPageCount(mEmoticonSetBeanList.get(i));
            }
            setCurrentItem(count);
        }
    }

    public int getPageCount(EmoticonSetBean emoticonSetBean) {
        int pageCount = 0;
        if (emoticonSetBean != null && emoticonSetBean.getEmoticonList() != null) {
            int del = emoticonSetBean.isShowDelBtn() ? 1 : 0;
            int everyPageMaxSum = emoticonSetBean.getRow() * emoticonSetBean.getLine() - del;
            pageCount = (int) Math.ceil((double) emoticonSetBean.getEmoticonList().size()
                    / everyPageMaxSum);
        }
        return pageCount;
    }

    @Override
    public void setBuilder(EmoticonsKeyboardBuilder builder) {
        mEmoticonSetBeanList = builder.builder.getEmoticonSetBeanList();
    }

    @Override
    public void onItemClick(EmoticonBean bean) {
        if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
            for (IView listener : mIViewListeners) {
                listener.onItemClick(bean);
            }
        }
    }

    @Override
    public void onItemDisplay(EmoticonBean bean) {

    }

    @Override
    public void onPageChangeTo(int position) {

    }

    /**
     * @param listener
     */
    public void addIViewListener(IView listener) {
        if (mIViewListeners == null) {
            mIViewListeners = new ArrayList<IView>();
        }
        mIViewListeners.add(listener);
    }

    public void setIViewListener(IView listener) {
        addIViewListener(listener);
    }

    public void setOnIndicatorListener(OnEmoticonsPageViewListener listener) {
        mOnEmoticonsPageViewListener = listener;
    }

    /**
     *
     */
    public interface OnEmoticonsPageViewListener {
        /**
         * @param count
         */
        void emoticonsPageViewInitFinish(int count);

        /**
         * @param count
         */
        void emoticonsPageViewCountChanged(int count);

        /**
         * @param position
         */
        void playTo(int position);

        /**
         * @param oldPosition
         * @param newPosition
         */
        void playBy(int oldPosition, int newPosition);
    }

    private class EmoticonsViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mEmoticonPageViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mEmoticonPageViews.get(arg1));
            return mEmoticonPageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
