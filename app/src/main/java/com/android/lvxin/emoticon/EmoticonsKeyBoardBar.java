package com.android.lvxin.emoticon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.lvxin.R;
import com.android.lvxin.emoticon.bean.EmoticonBean;
import com.android.lvxin.emoticon.utils.EmoticonsKeyboardBuilder;
import com.android.lvxin.emoticon.utils.EmoticonsUtils;
import com.android.lvxin.emoticon.utils.Tools;
import com.android.lvxin.emoticon.view.AutoHeightLayout;
import com.android.lvxin.emoticon.view.EmoticonsEditText;
import com.android.lvxin.emoticon.view.EmoticonsIndicatorView;
import com.android.lvxin.emoticon.view.EmoticonsPageView;
import com.android.lvxin.emoticon.view.EmoticonsToolBarView;
import com.android.lvxin.emoticon.view.i.IEmoticonsKeyboard;
import com.android.lvxin.emoticon.view.i.IView;

/**
 *
 */
public class EmoticonsKeyBoardBar extends AutoHeightLayout implements IEmoticonsKeyboard,
        View.OnClickListener, EmoticonsToolBarView.OnToolBarItemClickListener {

    public int mChildViewPosition = -1;
    KeyBoardBarViewListener mKeyBoardBarViewListener;
    private EmoticonsPageView mEmoticonsPageView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;
    private EmoticonsEditText mContentEdit;
    private LinearLayout mInputLayout;
    private LinearLayout mFootEmoticonsLayout;
    private ImageView mFaceBtn;
    private Button mSendBtn;

    public EmoticonsKeyBoardBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_keyboardbar, this);
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mEmoticonsPageView = (EmoticonsPageView) findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) findViewById(R.id.view_etv);

        mInputLayout = (LinearLayout) findViewById(R.id.layout_input);
        mFootEmoticonsLayout = (LinearLayout) findViewById(R.id.ly_foot_func);
        mSendBtn = (Button) mInputLayout.findViewById(R.id.moment_detail_comment_pulish);
        mSendBtn.setClickable(false);
        mFaceBtn = (ImageView) mInputLayout.findViewById(R.id.moment_detail_comment_emotion);
        mContentEdit = (EmoticonsEditText) mInputLayout
                .findViewById(R.id.moment_detail_comment_content);
        setAutoHeightLayoutView(mFootEmoticonsLayout);
        mFaceBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);

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
                if (mContentEdit != null) {
                    changeEditTextFocus(true);
                    // 删除
                    if (bean.getEventType() == EmoticonBean.FACE_TYPE_DEL) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        mContentEdit.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        return;
                    }

                    int index = mContentEdit.getSelectionStart();
                    Editable editable = mContentEdit.getEditableText();
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

        mContentEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        changeEditTextFocus(true);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mContentEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            }
        });
        mContentEdit.setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mKeyBoardBarViewListener != null) {
                            mKeyBoardBarViewListener.onKeyBoardStateChange(mKeyboardState, -1);
                        }
                    }
                });
            }
        });
        mContentEdit.setOnTextChangedInterface(new EmoticonsEditText.OnTextChangedInterface() {
            @Override
            public void onTextChanged(CharSequence arg0) {
                String str = arg0.toString();
                int resId = R.drawable.comment_send_unclickable_selector;
                boolean clickable = false;
                if (!Tools.isStrEmpty(str)) {
                    resId = R.drawable.comment_send_selector;
                    clickable = true;
                }
                mSendBtn.setBackgroundResource(resId);
                mSendBtn.setClickable(clickable);
            }
        });
    }

    /**
     * @param isShow
     */
    public void showOrHideInputLayout(boolean isShow) {
        mInputLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * @param focusable
     */
    public void changeEditTextFocus(boolean focusable) {
        mContentEdit.requestFocus();
        mContentEdit.setFocusable(focusable);
        mContentEdit.setFocusableInTouchMode(focusable);
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return mEmoticonsToolBarView;
    }

    public EmoticonsPageView getEmoticonsPageView() {
        return mEmoticonsPageView;
    }

    public EmoticonsEditText getContentEdit() {
        return mContentEdit;
    }

    public String getContent() {
        return getContentEdit().getText().toString();
    }

    /**
     * @param resid
     * @param value
     */
    public void setTextHint(int resid, String value) {
        if (Tools.isStrEmpty(value)) {
            value = " ";
        }
        mContentEdit.setHint(mContext.getResources().getString(resid, value));
    }

    /**
     * @param icon
     */
    public void addToolView(int icon) {
        if (mEmoticonsToolBarView != null && icon > 0) {
            // mEmoticonsToolBarView.addData(icon);
        }
    }

    /**
     * @param view
     * @param isRight
     */
    public void addFixedView(View view, boolean isRight) {
        if (mEmoticonsToolBarView != null) {
            mEmoticonsToolBarView.addFixedView(view, isRight);
        }
    }

    /**
     *
     */
    public void clearEditText() {
        if (mContentEdit != null) {
            mContentEdit.setText("");
        }
    }

    /**
     *
     */
    public void resetFaceBtn() {
        mFaceBtn.setImageResource(R.drawable.icon_face_normal);
    }

    /**
     *
     */
    public void del() {
        if (mContentEdit != null) {
            int action = KeyEvent.ACTION_DOWN;
            int code = KeyEvent.KEYCODE_DEL;
            KeyEvent event = new KeyEvent(action, code);
            mContentEdit.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        }
    }

    @Override
    public void setBuilder(EmoticonsKeyboardBuilder builder) {
        mEmoticonsPageView.setBuilder(builder);
        mEmoticonsToolBarView.setBuilder(builder);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mFootEmoticonsLayout != null && mFootEmoticonsLayout.isShown()) {
                    hideAutoView();
                    mFaceBtn.setImageResource(R.drawable.icon_face_normal);
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.moment_detail_comment_emotion) {
            switch (mKeyboardState) {
                case KEYBOARD_STATE_NONE:
                case KEYBOARD_STATE_BOTH:
                    show();
                    mFaceBtn.setImageResource(R.drawable.icon_face_pop);
                    showAutoView();
                    EmoticonsUtils.closeSoftKeyboard(mContext);
                    break;
                case KEYBOARD_STATE_FUNC:
                    if (mChildViewPosition == 0) {
                        mFaceBtn.setImageResource(R.drawable.icon_face_normal);
                        EmoticonsUtils.openSoftKeyboard(mContentEdit);
                    } else {
                        show();
                        mFaceBtn.setImageResource(R.drawable.icon_face_pop);
                    }
                    break;

                default:
                    break;

            }
        } else if (id == R.id.moment_detail_comment_pulish) {
            String content = mContentEdit.getText().toString();

            if (mKeyBoardBarViewListener != null && !Tools.isStrEmpty(content)) {
                mKeyBoardBarViewListener.onSendBtnClick(content);
            }
        }
    }

    /**
     * @param view
     */
    public void add(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFootEmoticonsLayout.addView(view, params);
    }

    /**
     *
     */
    public void show() {
        int childCount = mFootEmoticonsLayout.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                if (i == 0) {
                    mFootEmoticonsLayout.getChildAt(i).setVisibility(VISIBLE);
                    mChildViewPosition = i;
                } else {
                    mFootEmoticonsLayout.getChildAt(i).setVisibility(GONE);
                }
            }
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.onKeyBoardStateChange(mKeyboardState, -1);
                }
            }
        });
    }

    @Override
    public void onSoftPop(final int height) {
        super.onSoftPop(height);
        post(new Runnable() {
            @Override
            public void run() {
                mFaceBtn.setImageResource(R.drawable.icon_face_normal);
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.onKeyBoardStateChange(mKeyboardState, height);
                }
            }
        });
    }

    @Override
    public void onSoftClose(int height) {
        super.onSoftClose(height);
        if (mKeyBoardBarViewListener != null) {
            mKeyBoardBarViewListener.onKeyBoardStateChange(mKeyboardState, height);
        }
    }

    @Override
    public void onSoftChanegHeight(int height) {
        super.onSoftChanegHeight(height);
        if (mKeyBoardBarViewListener != null) {
            mKeyBoardBarViewListener.onKeyBoardStateChange(mKeyboardState, height);
        }
    }

    public void setOnKeyBoardBarViewListener(KeyBoardBarViewListener l) {
        this.mKeyBoardBarViewListener = l;
    }

    @Override
    public void onToolBarItemClick(int position) {

    }

    /**
     *
     */
    public interface KeyBoardBarViewListener {
        /**
         * @param state
         * @param height
         */
        public void onKeyBoardStateChange(int state, int height);

        /**
         * @param msg
         */
        public void onSendBtnClick(String msg);
    }
}
