package com.yunbao.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class MyRelativeLayout1 extends RelativeLayout {
    public MyRelativeLayout1(Context context) {
        super(context);
    }

    public MyRelativeLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
