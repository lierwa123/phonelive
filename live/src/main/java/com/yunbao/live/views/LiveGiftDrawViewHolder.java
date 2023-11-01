package com.yunbao.live.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.LevelBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveReceiveGiftBean;
import com.yunbao.live.utils.LiveTextRender;

/**
 * 手绘礼物
 */

public class LiveGiftDrawViewHolder extends AbsViewHolder {

    private View mRoot;
    private View mBg;
    private View mStar;
    private ImageView mAvatar;
    private TextView mName;
    private TextView mContent;
    private TextView mGiftGroupCount;
    private int mDp214;
    private ObjectAnimator mAnimator;
    private boolean mShowed;//展示礼物的控件是否显示出来了
    private Handler mHandler;


    public LiveGiftDrawViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_gift_draw;
    }

    @Override
    public void init() {
        mRoot = findViewById(R.id.root);
        mBg = findViewById(R.id.bg);
        mStar = findViewById(R.id.star);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mContent = (TextView) findViewById(R.id.content);
        mGiftGroupCount = (TextView) findViewById(R.id.gift_group_count);
        mDp214 = DpUtil.dp2px(214);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mBg != null) {
                    mBg.setTranslationX(-mDp214);
                }
                if (mStar != null && mStar.getVisibility() == View.VISIBLE) {
                    mStar.setVisibility(View.INVISIBLE);
                }
            }
        };
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        mAnimator = ObjectAnimator.ofFloat(mBg, "translationX", 0);
        mAnimator.setDuration(300);
        mAnimator.setInterpolator(interpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mStar != null && mStar.getVisibility() != View.VISIBLE) {
                    mStar.setVisibility(View.VISIBLE);
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(0, 200);
                }
            }
        });
    }


    /**
     * 显示礼物动画
     */
    public void show(LiveReceiveGiftBean bean) {
        if (!mShowed) {
            mShowed = true;
            if (mRoot != null && mRoot.getVisibility() != View.VISIBLE) {
                mRoot.setVisibility(View.VISIBLE);
            }
        }
        ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
        LevelBean levelBean = CommonAppConfig.getInstance().getLevel(bean.getLevel());
        if (levelBean != null) {
            mName.setTextColor(Color.parseColor(levelBean.getColor()));
        } else {
            mName.setTextColor(0xffffffff);
        }
        mName.setText(bean.getUserNiceName());
        mContent.setText(LiveTextRender.renderGiftInfo(bean.getGiftName(), bean.getToName()));
        mGiftGroupCount.setText("x" + bean.getGiftCount());
        if (mAnimator != null) {
            mAnimator.start();
        }
    }


    public void hide() {
        if (mBg != null) {
            mBg.setTranslationX(-mDp214);
        }
        if (mRoot != null && mRoot.getVisibility() == View.VISIBLE) {
            mRoot.setVisibility(View.INVISIBLE);
        }
        mAvatar.setImageDrawable(null);
        mShowed = false;

    }

    /**
     * 是否是空闲的
     */
    public boolean isIdle() {
        return false;
    }



    public void cancelAnimAndHide() {
        cancelAnim();
        hide();
    }

    private void cancelAnim() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }

    }

    public void release() {
        cancelAnim();
        mContext = null;
        mParentView = null;
        mHandler = null;

    }
}
