package com.android.lvxin.emoticon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.android.lvxin.R;
import com.android.lvxin.emoticon.bean.EmoticonBean;
import com.android.lvxin.emoticon.utils.EmoticonsKeyboardBuilder;
import com.android.lvxin.emoticon.utils.EmoticonsUtils;
import com.android.lvxin.emoticon.utils.Tools;
import com.android.lvxin.emoticon.view.EmoticonsEditText;
import com.android.lvxin.emoticon.view.EmoticonsIndicatorView;
import com.android.lvxin.emoticon.view.EmoticonsPageView;
import com.android.lvxin.emoticon.view.EmoticonsToolBarView;
import com.android.lvxin.emoticon.view.i.IView;

/**
 * @author lvxin
 * @ClassName: EmoticonsKeyBoardPopWindow
 * @Description: TODO
 * @date May 26, 2015 10:03:40 AM
 */
@SuppressLint("InflateParams")
public class EmoticonsKeyBoardPopWindow extends PopupWindow {

    KeyBoardBarPopupListener mKeyBoardBarPopupListener;
    private Context mContext;
    private ImageView mSwitchBtn;
    private EmoticonsPageView mEmoticonsPageView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;
    private EditText mEditText;

    public EmoticonsKeyBoardPopWindow(Context context) {
        super(context, null);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mConentView = inflater.inflate(R.layout.view_keyboardpopwindow, null);
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;

        this.setContentView(mConentView);
        this.setWidth(w);
        this.setHeight(Tools.dip2px(context, EmoticonsUtils.getDefKeyboardHeight(mContext)));
        this.setAnimationStyle(R.style.PopupAnimation);
        this.update();
        this.setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

        updateView(mConentView);
    }

    /**
     * @param view
     */
    public void updateView(View view) {
        mSwitchBtn = (ImageView) view.findViewById(R.id.btn_switch);
        mSwitchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mKeyBoardBarPopupListener) {
                    mKeyBoardBarPopupListener.onSwitchBtnClick();
                }
            }
        });
        mEmoticonsPageView = (EmoticonsPageView) view.findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) view.findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) view.findViewById(R.id.view_etv);

        mEmoticonsPageView
                .setOnIndicatorListener(new EmoticonsPageView.OnEmoticonsPageViewListener() {
                    @Override
                    public void emoticonsPageViewInitFinish(int count) {
                        mEmoticonsIndicatorView.init(count);
                    }

                    @Override
                    public void emoticonsPageViewCountChanged(int count) {
                        mEmoticonsIndicatorView.setIndicatorCount(count);
                    }

                    @Override
                    public void playTo(int position) {
                        mEmoticonsIndicatorView.playTo(position);
                    }

                    @Override
                    public void playBy(int oldPosition, int newPosition) {
                        mEmoticonsIndicatorView.playBy(oldPosition, newPosition);
                    }
                });

        mEmoticonsPageView.setIViewListener(new IView() {
            @Override
            public void onItemClick(EmoticonBean bean) {
                if (mEditText != null) {
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);
                    mEditText.requestFocus();

                    if (bean.getEventType() == EmoticonBean.FACE_TYPE_DEL) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        mEditText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        return;
                    }

                    int index = mEditText.getSelectionStart();
                    Editable editable = mEditText.getEditableText();
                    if (index < 0) {
                        editable.append(bean.getContent());
                    } else {
                        editable.insert(index, bean.getContent());
                    }
                }
            }

            @Override
            public void onItemDisplay(EmoticonBean bean) {
            }

            @Override
            public void onPageChangeTo(int position) {
                mEmoticonsToolBarView.setToolBtnSelect(position);
            }
        });

        mEmoticonsToolBarView
                .setOnToolBarItemClickListener(new EmoticonsToolBarView.OnToolBarItemClickListener() {
                    @Override
                    public void onToolBarItemClick(int position) {
                        mEmoticonsPageView.setPageSelect(position);
                    }
                });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    public void setBuilder(EmoticonsKeyboardBuilder builder) {
        if (mEmoticonsPageView != null) {
            mEmoticonsPageView.setBuilder(builder);
        }
        if (mEmoticonsToolBarView != null) {
            mEmoticonsToolBarView.setBuilder(builder);
        }
    }

    public void setEditText(EmoticonsEditText edittext) {
        mEditText = edittext;
    }

    /**
     *
     */
    public void showPopupWindow() {
        View rootView = EmoticonsUtils.getRootView((Activity) mContext);
        if (this.isShowing()) {
            this.dismiss();
        } else {
            EmoticonsUtils.closeSoftKeyboard(mContext);
            this.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    public void setOnKeyBoardBarPopupListener(KeyBoardBarPopupListener l) {
        this.mKeyBoardBarPopupListener = l;
    }

    /**
     *
     */
    public interface KeyBoardBarPopupListener {
        /**
         *
         */
        public void onSwitchBtnClick();
    }
}
