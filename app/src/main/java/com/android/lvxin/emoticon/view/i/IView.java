package com.android.lvxin.emoticon.view.i;

import com.android.lvxin.emoticon.bean.EmoticonBean;

/**
 * @author lvxin
 * @ClassName: IView
 * @Description: TODO
 * @date May 26, 2015 10:19:56 AM
 */
public interface IView {
    /**
     * @param bean
     */
    void onItemClick(EmoticonBean bean);

    /**
     * @param bean
     */
    void onItemDisplay(EmoticonBean bean);

    /**
     * @param position
     */
    void onPageChangeTo(int position);
}
