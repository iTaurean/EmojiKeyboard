package com.android.lvxin.emoticon.bean;

/**
 * @author lvxin
 * @ClassName: EmoticonBean
 * @Description: TODO
 * @date May 26, 2015 10:17:48 AM
 */
public class EmoticonBean {

    public final static int FACE_TYPE_NOMAL = 0;
    public final static int FACE_TYPE_DEL = 1;
    public final static int FACE_TYPE_USERDEF = 2;

    /**
     * 点击处理事件类型
     */
    private long eventType;
    /**
     * 表情图标
     */
    private String iconUri;
    /**
     * 内容
     */
    private String content;

    private String code;

    public EmoticonBean(String iconUri, String content, String code) {
        this(0, iconUri, content, code);
    }

    public EmoticonBean(long eventType, String iconUri, String content, String code) {
        this.eventType = eventType;
        this.iconUri = iconUri;
        this.content = content;
    }

    public EmoticonBean() {
    }

    /**
     * @param chars
     * @return
     */
    public static String fromChars(String chars) {
        return chars;
    }

    /**
     * @param ch
     * @return
     */
    public static String fromChar(char ch) {
        return Character.toString(ch);
    }

    /**
     * @param codePoint
     * @return
     */
    public static String fromCodePoint(int codePoint) {
        return newString(codePoint);
    }

    /**
     * @param codePoint
     * @return
     */
    public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getEventType() {
        return eventType;
    }

    public void setEventType(long eventType) {
        this.eventType = eventType;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
