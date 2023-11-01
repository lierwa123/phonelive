package com.yunbao.common.custom;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

/**
 * Created by cxf on 2018/9/27.
 */

public class MyFrameLayout3 extends FrameLayout {

    private int mWidth;

    public MyFrameLayout3(@NonNull Context context) {
        this(context, null);
    }

    public MyFrameLayout3(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout3(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int dp16 = (int) (dm.density * 16 + 0.5f);
        mWidth = (dm.widthPixels - dp16) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
