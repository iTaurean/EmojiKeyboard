package com.android.lvxin.emoticon.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;

public class Tools {
    private static long lastClickTime;

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStrEmpty(String str) {
        if (null == str || "".equals(str.trim()) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 将dip/dp值转换为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue（DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 隐藏系统软键盘
     *
     * @param activity
     */
    @SuppressLint("NewApi")
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * 数字转换成字符创，小于10前面添加0
     *
     * @param value
     * @return
     */
    public static String intToString(int value) {
        return 10 > value ? ("0" + value) : String.valueOf(value);
    }

    /**
     * 判断当前网络是否为wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context mContext) {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 判断连续点击方法
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static int getFileSize(File file) throws Exception {
        int size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        }
        return size;
    }

    /**
     * 设置View背景drawable
     *
     * @param view
     * @param drawable
     */
    public static void setDrawableToBkg(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

    }

    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }

    public static int[] getDisplayMetric(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayWidth = wm.getDefaultDisplay().getWidth();
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return new int[]{displayWidth, displayHeight};
    }

    /**
     * 在Activity中获取状态栏高度
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static int getStatusHeight(Context activity) {
        Rect frame = new Rect();
        ((Activity) activity).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 将字符串中的Emoji表情替换为*
     *
     * @param source
     * @return
     */

    public static String replaceEmojiToStar(String source) {
        String res = "";
        if (Tools.isStrEmpty(source)) {
            return res;
        }
        if (!containsEmoji(source)) {
            return source;
        } else {
            int len = source.length();
            char[] dest = new char[len];
            for (int i = 0; i < len; i++) {
                char codePoint = source.charAt(i);
                if (isNotEmojiCharacter(codePoint)) {
                    dest[i] = codePoint;
                } else {
                    dest[i] = '*';
                }
            }
            res = String.valueOf(dest);
        }
        return res;

    }

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (Tools.isStrEmpty(source)) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isNotEmojiCharacter(codePoint)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否是非Emoji字符之外的正常字符，正常字符返回true
     *
     * @param codePoint
     * @return
     */

    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD));
    }

    /**
     * 正则表达式验证昵称
     *
     * @param nickName
     * @return
     */
    public static boolean rexCheckNickName(String nickName) {
        // 昵称格式：限16个字符，支持中英文、数字、减号或下划线
        String regStr = "^[\\u4e00-\\u9fa5_a-zA-Z0-9-]{1,16}$";
        return nickName.matches(regStr);
    }

    /**
     * 正则表达式验证密码
     *
     * @param input
     * @return
     */
    public static boolean rexCheckPassword(String input) {
        // 6-20 位，字母、数字、字符
        //String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        //        String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,20}$";
        String regStr = "(?!^[\\d]+$)(?!^[a-z]+$)(?!^[A-Z]+$)(?!^[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$)[a-zA-Z0-9`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]{8,20}";
        return input.matches(regStr);
    }

    /**
     * 手机号码中间改为****
     *
     * @param mobile
     * @return
     */
    public static String getSafeMobileFormat(String mobile) {
        String newMobile = mobile;
        if (!Tools.isStrEmpty(mobile) && mobile.length() >= 11) {
            mobile = mobile.trim();
            newMobile = mobile.substring(0, 3) + "****" + mobile.subSequence(7, mobile.length());
        }
        return newMobile;
    }

    /**
     * 清空指定文件夹
     *
     * @param dirString
     */
    public static void clearFolder(String dirString) {
        File dir = new File(dirString);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                new File(dir, child).delete();
            }
        }
    }

    public static Animation repeatChangeAnimation() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

}
