package com.yunbao.main.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ActiveLikeImage extends AppCompatImageView {

    private Drawable[] mDrawables;
    private boolean mIsLike;
    private Runnable mAnimRunnable;
    private int mIndex;

    public ActiveLikeImage(Context context) {
        super(context);
    }

    public ActiveLikeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActiveLikeImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawables(Drawable[] drawables) {
        mDrawables = drawables;
    }

    public void setLike(boolean isLike, boolean showAnim) {
        if (mIsLike == isLike) {
            return;
        }
        mIsLike = isLike;
        if (mDrawables != null && mDrawables.length > 0) {
            if (isLike) {
                if (showAnim) {
                    startAnim();
                } else {
                    setImageDrawable(mDrawables[mDrawables.length - 1]);
                }
            } else {
                setImageDrawable(mDrawables[0]);
            }
        }

    }


    private void startAnim() {
        if (mDrawables != null) {
            mIndex = 0;
            if (mAnimRunnable == null) {
                mAnimRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mIndex++;
                        if (mIndex < mDrawables.length) {
                            setImageDrawable(mDrawables[mIndex]);
                            postDelayed(mAnimRunnable, 70);
                        }
                    }
                };
            }
            mAnimRunnable.run();
        }
    }


}
