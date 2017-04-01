package com.android.lvxin.emoticon.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.android.lvxin.emoticon.bean.EmoticonBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lvxin
 * @ClassName: SpannableStringParser
 * @Description: TODO
 * @date May 26, 2015 10:19:30 AM
 */
public class SpannableStringParser {

    //    private static final PATTERN_STRING = "(/:[^ ]+)";
    private static final String PATTERN_STRING = "\\[(.*?)\\]";
    private final Pattern emotionPattern;
    private float textSize;
    private float lineSpacing;

    public SpannableStringParser(float textSize) {
        emotionPattern = Pattern.compile(PATTERN_STRING);
        this.textSize = textSize;
    }

    public SpannableStringParser(float textSize, float lineSpacing) {
        this(textSize);
        this.lineSpacing = lineSpacing;
    }

    /**
     * @param value
     * @return
     */
    public CharSequence parseSpan(Context context, SpannableString value) {
        return parseEmotion(context, value);
    }

    /**
     * @param text
     * @return
     */
    public CharSequence parseSpan(Context context, String text) {
        if (Tools.isStrEmpty(text)) {
            return text;
        }
        SpannableString result = parseEmotion(context, text);
        return null == result ? null : result;
    }

    /**
     * @param text
     * @return
     */
    public SpannableString parseEmotion(Context context, String text) {
        SpannableString spannableString = SpannableString.valueOf(text);
        List<Tag> tags = getTags(text, emotionPattern);
        if (null == tags || tags.isEmpty()) {
            return spannableString;
        }

        ImageSpan imageSpan = null;
        for (Tag tag : tags) {
            imageSpan = getEmotionSpan(context, tag.getContent());
            if (null != imageSpan) {
                spannableString.setSpan(imageSpan, tag.getStart(), tag.getEnd(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * @param spannableString
     * @return
     */
    public SpannableString parseEmotion(Context context, SpannableString spannableString) {
//        SpannableString spannableString = SpannableString.valueOf(text);
        List<Tag> tags = getTags(spannableString.toString(), emotionPattern);
        if (null == tags || tags.isEmpty()) {
            return spannableString;
        }

        ImageSpan imageSpan = null;
        for (Tag tag : tags) {
            imageSpan = getEmotionSpan(context, tag.getContent());
            if (null != imageSpan) {
                spannableString.setSpan(imageSpan, tag.getStart(), tag.getEnd(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    private ImageSpan getEmotionSpan(Context context, String content) {
        EmoticonBean emoticonBean = DefEmoticons.getEmoticonBean(content);
        if (null != emoticonBean) {
            Drawable drawable = EmoticonsUtils.getEmoticonDrawable(context,
                    emoticonBean.getIconUri(), textSize, lineSpacing);
            if (null != drawable) {
                return new ImageSpan(drawable);
            }
        }
        return null;
    }

    private List<Tag> getTags(String text, Pattern pattern) {
        List<Tag> tags = new ArrayList<Tag>();
        if (Tools.isStrEmpty(text)) {
            return null;
        }

        Matcher matcher = pattern.matcher(text);
        int start;
        int end;
        Tag tag;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            tag = new Tag();
            tag.setContent(text.substring(start, end));
            tag.setStart(start);
            tag.setEnd(end);

            tags.add(tag);
        }

        return tags;
    }

    private static class Tag {
        int start;
        int end;
        String content;

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}