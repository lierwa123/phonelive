package com.yunbao.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yunbao.common.R;

public class LineProgress extends View {

    private int mHeight;
    private int mFgColor;
    private int mBgColor;
    private int mMaxProgress;
    private int mCurProgress;
    private Paint mBgPaint;
    private Paint mFgPaint;
    private float mStrokeWidth;
    private float[] mLineX;

    public LineProgress(Context context) {
        this(context, null);
    }

    public LineProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineProgress);
        mFgColor = ta.getColor(R.styleable.LineProgress_lp_fg_color, 0);
        mBgColor = ta.getColor(R.styleable.LineProgress_lp_bg_color, 0);
        mMaxProgress = ta.getInt(R.styleable.LineProgress_lp_max_progress, 2);
        mCurProgress = ta.getInt(R.styleable.LineProgress_lp_cur_progress, 0);
        mStrokeWidth = ta.getDimension(R.styleable.LineProgress_lp_strokeWidth, 0);
        mLineX = new float[mMaxProgress];
        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mStrokeWidth);
        mFgPaint = new Paint();
        mFgPaint.setAntiAlias(true);
        mFgPaint.setDither(true);
        mFgPaint.setColor(mFgColor);
        mFgPaint.setStyle(Paint.Style.STROKE);
        mFgPaint.setStrokeWidth(mStrokeWidth);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mHeight = h;
        if (mStrokeWidth > 0 && mMaxProgress > 1) {
            float space = (w - mStrokeWidth * mMaxProgress) / (mMaxProgress - 1);
            float halfStrokeWidth = mStrokeWidth / 2f;
            for (int i = 0; i < mMaxProgress; i++) {
                mLineX[i] = i * (mStrokeWidth + space) + halfStrokeWidth;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mMaxProgress; i++) {
            if (i <= mCurProgress - 1) {
                canvas.drawLine(mLineX[i], 0, mLineX[i], mHeight, mFgPaint);
            } else {
                canvas.drawLine(mLineX[i], 0, mLineX[i], mHeight, mBgPaint);
            }
        }

    }

    public void setProgress(int progress) {
        mCurProgress = progress;
        if (mCurProgress > mMaxProgress) {
            mCurProgress = mMaxProgress;
        }
        invalidate();
    }
}
