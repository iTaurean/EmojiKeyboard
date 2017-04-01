package com.android.lvxin.emoticon.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import com.android.lvxin.R;
import com.android.lvxin.emoticon.bean.EmoticonBean;
import com.android.lvxin.emoticon.utils.DefEmoticons;
import com.android.lvxin.emoticon.utils.Tools;

import java.util.ArrayList;

/**
 * @author lvxin
 * @ClassName: EmoticonsEditText
 * @Description: TODO
 * @date May 26, 2015 10:20:45 AM
 */
public class EmoticonsEditText extends EditText {

    public static final int WRAP_DRAWABLE = -1;
    public static final int WRAP_FONT = -2;
    OnTextChangedInterface onTextChangedInterface;
    OnSizeChangedListener onSizeChangedListener;
    private Context mContext;
    private ArrayList<EmoticonBean> emoticonBeanList = null;
    private int mItemHeight;
    private int mItemWidth;
    private int mFontHeight;

    public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EmoticonsEditText(Context context) {
        super(context);
    }

    public EmoticonsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mFontHeight = getFontHeight();
        mItemHeight = mFontHeight;
        mItemWidth = mFontHeight;

        emoticonBeanList = DefEmoticons.EMOTIONS;
    }

    public void setEmoticonImageSpanSize(int width, int height) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > 0 && onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged();
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int after) {
        super.onTextChanged(text, start, lengthBefore, after);
        if (text.length() > 400) {
            Toast.makeText(mContext, "长度超过最大限制。", Toast.LENGTH_SHORT).show();
        } else {
            handleTextChanged(text, start, after);
        }
        if (onTextChangedInterface != null) {
            onTextChangedInterface.onTextChanged(text);
        }
    }

    private void handleTextChanged(CharSequence text, int start, int after) {
        if (after > 0) {
            int end = start + after;
            if (text.length() >= end) {
                String keyStr = text.toString().substring(start, end);
                boolean isEmoticonMatcher = replaceTextWithDrawable(keyStr, start, end);

                if (!isEmoticonMatcher) {
                    try {
                        handleUnmatchers(start, after);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }
    }

    private boolean replaceTextWithDrawable(String keyStr, int start, int end) {
        boolean isEmoticonMatcher = false;
        for (EmoticonBean bean : emoticonBeanList) {
            if (!Tools.isStrEmpty(bean.getContent()) && bean.getContent().equals(keyStr)) {
                int resID = mContext.getResources().getIdentifier(bean.getIconUri(), "drawable",
                        mContext.getPackageName());
                Drawable drawable = mContext.getResources().getDrawable((int) resID);

                if (drawable != null) {
                    int itemHeight;
                    if (mItemHeight == WRAP_DRAWABLE) {
                        itemHeight = drawable.getIntrinsicHeight();
                    } else if (mItemHeight == WRAP_FONT) {
                        itemHeight = mFontHeight;
                    } else {
                        itemHeight = mItemHeight;
                    }

                    int itemWidth;
                    if (mItemWidth == WRAP_DRAWABLE) {
                        itemWidth = drawable.getIntrinsicWidth();
                    } else if (mItemWidth == WRAP_FONT) {
                        itemWidth = mFontHeight;
                    } else {
                        itemWidth = mItemWidth;
                    }

                    drawable.setBounds(0, 0, itemHeight, itemWidth);
                    VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
                    getText().setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    isEmoticonMatcher = true;
                }
            }
        }
        return isEmoticonMatcher;
    }

    private void handleUnmatchers(int start, int after) {
        int end = start + after;
        ImageSpan[] oldSpans = getText().getSpans(start, end, ImageSpan.class);
        if (oldSpans != null) {
            for (int i = 0; i < oldSpans.length; i++) {
                int startOld = end;
                int endOld = after + getText().getSpanEnd(oldSpans[i]) - 1;
                if (startOld >= 0 && endOld > startOld) {
                    ImageSpan imageSpan = new ImageSpan(oldSpans[i].getDrawable(),
                            ImageSpan.ALIGN_BASELINE);
                    getText().removeSpan(oldSpans[i]);
                    getText().setSpan(imageSpan, startOld, endOld,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            super.setText(text, type);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(text.toString());
        }
    }

    private int getFontHeight() {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }

    public void setOnTextChangedInterface(OnTextChangedInterface i) {
        onTextChangedInterface = i;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener i) {
        onSizeChangedListener = i;
    }

    /**
     *
     */
    public interface OnTextChangedInterface {
        /**
         *
         * @param argo
         */
        void onTextChanged(CharSequence argo);
    }

    /**
     *
     */
    public interface OnSizeChangedListener {
        /**
         *
         */
        void onSizeChanged();
    }
}
