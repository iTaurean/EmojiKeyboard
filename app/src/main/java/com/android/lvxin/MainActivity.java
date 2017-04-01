package com.android.lvxin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.lvxin.emoticon.EmoticonsKeyBoardPopWindow;
import com.android.lvxin.emoticon.utils.EmoticonsUtils;
import com.android.lvxin.emoticon.view.EmoticonsEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mSwitchLayout;
    private EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;
    private EmoticonsEditText mContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentEditText = (EmoticonsEditText) findViewById(R.id.emoji_text);
        mContentEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (view == mContentEditText) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            mSwitchLayout.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                }
                return false;
            }
        });
        mSwitchLayout = (LinearLayout) findViewById(R.id.view_switch_bar);
        ImageView mSwitchBtn = (ImageView) findViewById(R.id.btn_switch);
        mSwitchBtn.setOnClickListener(this);
        initKeyBoardPopWindow();
    }

    private void initKeyBoardPopWindow() {
        mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(this);
        mKeyBoardPopWindow.setBuilder(EmoticonsUtils.getSimpleBuilder(this));
        mKeyBoardPopWindow.setEditText(mContentEditText);
        mKeyBoardPopWindow.setOnKeyBoardBarPopupListener(new EmoticonsKeyBoardPopWindow.KeyBoardBarPopupListener() {

            @Override
            public void onSwitchBtnClick() {
                mSwitchLayout.setVisibility(View.VISIBLE);
                mKeyBoardPopWindow.dismiss();
                EmoticonsUtils.openSoftKeyboard(mContentEditText);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_switch:
                mSwitchLayout.setVisibility(View.GONE);
                mKeyBoardPopWindow.showPopupWindow();
                break;
            default:
                break;
        }
    }
}
