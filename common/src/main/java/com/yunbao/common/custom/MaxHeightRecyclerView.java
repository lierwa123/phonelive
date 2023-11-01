package com.yunbao.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yunbao.common.R;

public class MaxHeightRecyclerView extends RecyclerView {

    private int mMaxHeight;

    public MaxHeightRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MaxHeightRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
        mMaxHeight = (int)ta.getDimension(R.styleable.MaxHeightRecyclerView_mrv_max_height, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mMaxHeight > 0) {
            int height = MeasureSpec.getSize(heightSpec);
            if (height > mMaxHeight) {
                int mode = MeasureSpec.getMode(heightSpec);
                heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, mode);
            }
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
