package com.yunbao.common.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class InterceptFrameLayout extends FrameLayout {
    public InterceptFrameLayout(@NonNull Context context) {
        super(context);
    }

    public InterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean mIntercepted;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnInterceptListener != null) {
            mIntercepted = mOnInterceptListener.onInterceptCall();
            return mIntercepted;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIntercepted){
            return true;
        }
        return super.onTouchEvent(event);
    }

    private OnInterceptListener mOnInterceptListener;

    public void setOnInterceptListener(OnInterceptListener onInterceptListener) {
        mOnInterceptListener = onInterceptListener;
    }


    public interface OnInterceptListener {
        boolean onInterceptCall();
    }
}
