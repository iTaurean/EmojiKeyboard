package com.android.lvxin.emoticon.utils;

import com.android.lvxin.emoticon.bean.EmoticonSetBean;

import java.util.ArrayList;

/**
 * @author lvxin
 * @ClassName: EmoticonsKeyboardBuilder
 * @Description: TODO
 * @date May 26, 2015 10:18:31 AM
 */
public class EmoticonsKeyboardBuilder {

    public Builder builder;

    public EmoticonsKeyboardBuilder(Builder builder) {
        this.builder = builder;
    }

    /**
     *
     */
    public static class Builder {

        ArrayList<EmoticonSetBean> mEmoticonSetBeanList = new ArrayList<EmoticonSetBean>();

        public Builder() {
        }

        public ArrayList<EmoticonSetBean> getEmoticonSetBeanList() {
            return mEmoticonSetBeanList;
        }

        public Builder setEmoticonSetBeanList(ArrayList<EmoticonSetBean> mEmoticonSetBeanList) {
            this.mEmoticonSetBeanList = mEmoticonSetBeanList;
            return this;
        }

        /**
         * @return
         */
        public EmoticonsKeyboardBuilder build() {
            return new EmoticonsKeyboardBuilder(this);
        }
    }
}
