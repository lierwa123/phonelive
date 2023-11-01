package com.yunbao.common.utils;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;


public class KeyBoardUtil {

    private static final String TAG = "KeyBoardUtil";
    private View mRootView;
    private Handler mHandler;
    private Rect mRect;
    private int mOriginHeight;//初始高度
    private int mLastHeight;//上一次的高度
    private KeyBoardHeightListener mKeyBoardHeightListener;


    public KeyBoardUtil(View rootView, KeyBoardHeightListener listener) {
        mRootView = rootView;
        mKeyBoardHeightListener = listener;
        mRect = new Rect();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mRootView == null || mRect == null) {
                    return;
                }
                mRootView.getWindowVisibleDisplayFrame(mRect);
                int curHeight = mRect.height();
                if (mLastHeight != curHeight) {
                    int keyboardHeight = mOriginHeight - curHeight;
//                    L.e(TAG, "---可视区高度--->" + curHeight + " -----键盘高度-----> " + keyboardHeight);
                    mLastHeight = curHeight;
                    if (mKeyBoardHeightListener != null) {
                        mKeyBoardHeightListener.onKeyBoardHeightChanged(keyboardHeight);
                    }
                }
                next();
            }
        };
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRootView == null || mRect == null) {
                    return;
                }
                mRootView.getWindowVisibleDisplayFrame(mRect);
                mOriginHeight = mRect.height();
                mLastHeight = mOriginHeight;
//                L.e(TAG, "---初始高度--->" + mOriginHeight);
                next();
            }
        }, 200);
    }

    private void next() {
        if (mHandler != null) {
            long now = SystemClock.uptimeMillis();
            long next = now + (200 - now % 200);
            mHandler.sendEmptyMessageAtTime(0, next);
        }
    }


    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        mRootView = null;
        mRect = null;
        mKeyBoardHeightListener = null;
        L.e(TAG, "---release--->");
    }


    public interface KeyBoardHeightListener {
        void onKeyBoardHeightChanged(int keyboardHeight);
    }
}
