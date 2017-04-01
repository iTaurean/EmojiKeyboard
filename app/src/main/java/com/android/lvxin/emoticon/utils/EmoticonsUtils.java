package com.android.lvxin.emoticon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.lvxin.emoticon.bean.EmoticonSetBean;

import java.util.ArrayList;

/**
 * @author lvxin
 * @ClassName: EmoticonsUtils
 * @Description: TODO
 * @date May 26, 2015 10:17:25 AM
 */
public class EmoticonsUtils {

    private static final String EXTRA_DEF_KEYBOARDHEIGHT = "DEF_KEYBOARDHEIGHT";
    public static int sDefRow = 7;
    public static int sDefLine = 3;
    /**
     * 键盘默认高度 (dp)
     */
    private static int sDefKeyboardHeight = 250;
    /**
     * 屏幕宽度
     */
    private static int displayWidthPixels = 0;

    public static int getDefKeyboardHeight(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int height = settings.getInt(EXTRA_DEF_KEYBOARDHEIGHT, 0);
        if (height > 0 && sDefKeyboardHeight != height) {
            setDefKeyboardHeight(context, height);
        }
        return sDefKeyboardHeight;
    }

    public static void setDefKeyboardHeight(Context context, int height) {
        if (sDefKeyboardHeight != height) {
            final SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(context);
            settings.edit().putInt(EXTRA_DEF_KEYBOARDHEIGHT, height).commit();
        }
        sDefKeyboardHeight = height;
    }

    private static void getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidthPixels = dm.widthPixels;// 宽度
    }

    public static int getDisplayWidthPixels(Context context) {
        if (context == null) {
            return -1;
        }
        if (displayWidthPixels == 0) {
            getDisplayMetrics(context);
        }
        return displayWidthPixels;
    }

    /**
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 开启软键盘
     */
    public static void openSoftKeyboard(EditText et) {
        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static final ArrayList<EmoticonSetBean> getEmotionSets(final Context context) {
        getDisplayMetrics(context);
        ArrayList<EmoticonSetBean> emoticonSetBeanList = new ArrayList<EmoticonSetBean>();

        int columns = displayWidthPixels / (dip2px(context, 50));
        int lines = getDefKeyboardHeight(context) / 63;
        EmoticonSetBean emoticonSetBean = new EmoticonSetBean("emoji", lines, columns);
        emoticonSetBean.setIconUri("icon_emoji");
        emoticonSetBean.setItemPadding(0);
        emoticonSetBean.setVerticalSpacing(0);
        emoticonSetBean.setShowDelBtn(true);
        emoticonSetBean.setEmoticonList(DefEmoticons.getEmoticons());
        emoticonSetBeanList.add(emoticonSetBean);

        return emoticonSetBeanList;
    }

    public static EmoticonsKeyboardBuilder getSimpleBuilder(Context context) {

        return new EmoticonsKeyboardBuilder.Builder().setEmoticonSetBeanList(
                getEmotionSets(context)).build();
    }

    public static Drawable getEmoticonDrawable(Context context, String iconUri, float textSize,
                                               float lineSpacing) {
        int resID = context.getResources().getIdentifier(iconUri, "drawable",
                context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(resID);
        int mFontHeight = getFontHeight(textSize);
        drawable.setBounds(0, 0, mFontHeight, mFontHeight);
        return drawable;
    }

    public static int getFontHeight(float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.bottom - fontMetrics.top);
    }
}
