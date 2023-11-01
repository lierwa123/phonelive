package com.yunbao.live.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.yunbao.common.CommonAppContext;
import com.yunbao.common.utils.ScreenDimenUtil;

public class FloatWindow implements View.OnTouchListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;
    private int mWidth;
    private int mHeight;
    private float mLastX;
    private float mLastY;
    private int mLastVisibleX;
    private boolean mMove;

    public FloatWindow() {
        mWindowManager = (WindowManager) CommonAppContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.TOP | Gravity.START;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mLayoutParams.windowAnimations = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
    }


    public void setX(int x) {
        if (mLayoutParams != null) {
            mLayoutParams.x = x;
        }
    }

    public void setY(int y) {
        if (mLayoutParams != null) {
            mLayoutParams.y = y;
        }
    }

    public void setWidth(int width) {
        mWidth = width;
        if (mLayoutParams != null) {
            mLayoutParams.width = width;
        }
    }

    public void setHeight(int height) {
        mHeight = height;
        if (mLayoutParams != null) {
            mLayoutParams.height = height;
        }
    }

    public void setView(View view) {
        view.setOnTouchListener(this);
        mView = view;
    }

    public boolean show() {
        mLastVisibleX = 0;
        try {
            if (mWindowManager != null && mLayoutParams != null) {
                mWindowManager.addView(mView, mLayoutParams);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void dismiss() {
        try {
            if (mWindowManager != null && mView != null) {
                mView.setOnTouchListener(null);
                mWindowManager.removeView(mView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mView = null;
        mLastVisibleX = 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        float x = e.getRawX();
        float y = e.getRawY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMove = false;
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (dx != 0) {
                    mMove = true;
                    float targetX = mLayoutParams.x + dx;
                    if (targetX < 0) {
                        targetX = 0;
                    }
                    int rightLimit = ScreenDimenUtil.getInstance().getScreenWdith() - mWidth;
                    if (targetX > rightLimit) {
                        targetX = rightLimit;
                    }
                    mLayoutParams.x = (int) targetX;
                }
                if (dy != 0) {
                    mMove = true;
                    float targetY = mLayoutParams.y + dy;
                    if (targetY < 0) {
                        targetY = 0;
                    }
                    int bottomLimit = ScreenDimenUtil.getInstance().getScreenHeight() - mHeight;
                    if (targetY > bottomLimit) {
                        targetY = bottomLimit;
                    }
                    mLayoutParams.y = (int) targetY;
                }
                mWindowManager.updateViewLayout(mView, mLayoutParams);
        }
        mLastX = x;
        mLastY = y;
        return mMove;
    }


    public void setVisible(boolean visible) {
        if (mWindowManager != null && mLayoutParams != null && mView != null) {
            if (visible) {
                mLayoutParams.x = mLastVisibleX == 0 ? (ScreenDimenUtil.getInstance().getScreenWdith() - mWidth) : mLastVisibleX;
            } else {
                mLastVisibleX = mLayoutParams.x;
                mLayoutParams.x = ScreenDimenUtil.getInstance().getScreenWdith();
            }
            mWindowManager.updateViewLayout(mView, mLayoutParams);
        }
    }


}
