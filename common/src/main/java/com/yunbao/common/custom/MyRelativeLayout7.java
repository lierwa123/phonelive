package com.yunbao.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

/**
 * Created by cxf on 2018/9/26.
 */

public class MyRelativeLayout7 extends RelativeLayout {

    private int mWidth;

    public MyRelativeLayout7(Context context) {
        this(context, null);
    }

    public MyRelativeLayout7(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout7(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int space = (int) (dm.density * 15 + 0.5f);
        mWidth = (dm.widthPixels - space) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
