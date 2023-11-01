package com.yunbao.live.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.event.TieZhiCancelEvent;
import com.yunbao.beauty.interfaces.IBeautyEffectListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.L;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveStickerBean;
import com.yunbao.live.interfaces.ILivePushViewHolder;
import com.yunbao.live.interfaces.LivePushListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by cxf on 2018/12/22.
 */

public abstract class AbsLivePushViewHolder extends AbsViewHolder implements ILivePushViewHolder {

    protected final String TAG = getClass().getSimpleName();
    protected LivePushListener mLivePushListener;
    protected boolean mCameraFront;//是否是前置摄像头
    protected boolean mFlashOpen;//闪光灯是否开启了
    protected boolean mPaused;
    protected boolean mStartPush;
    protected ViewGroup mBigContainer;
    protected ViewGroup mSmallContainer;
    protected ViewGroup mLeftContainer;
    protected ViewGroup mRightContainer;
    protected ViewGroup mPkContainer;
    protected View mPreView;
    protected boolean mOpenCamera;//是否选择了相机

    //倒计时
    protected TextView mCountDownText;
    protected int mCountDownCount = 3;
    private boolean mStartCountDown;

    //道具礼物
    private Handler mHandler;
    private List<LiveStickerBean> mStickerList;
    private ConcurrentLinkedQueue<LiveStickerBean> mQueue;
    protected boolean mIsPlayGiftSticker;//主播是否在播放道具礼物


    public AbsLivePushViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public AbsLivePushViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    public void init() {
        mBigContainer = (ViewGroup) findViewById(R.id.big_container);
        mSmallContainer = (ViewGroup) findViewById(R.id.small_container);
        mLeftContainer = (ViewGroup) findViewById(R.id.left_container);
        mRightContainer = (ViewGroup) findViewById(R.id.right_container);
        mPkContainer = (ViewGroup) findViewById(R.id.pk_container);
        mCameraFront = true;
        getStickerList();

    }


    /**
     * 开播的时候 3 2 1倒计时
     */
    protected void startCountDown() {
        if (mStartCountDown) {
            return;
        }
        mStartCountDown = true;
        ViewGroup parent = (ViewGroup) mContentView;
        mCountDownText = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_count_down, parent, false);
        parent.addView(mCountDownText);
        mCountDownText.setText(String.valueOf(mCountDownCount));
        ScaleAnimation animation = new ScaleAnimation(3, 1, 3, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(2);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mCountDownText != null) {
                    ViewGroup parent = (ViewGroup) mCountDownText.getParent();
                    if (parent != null) {
                        parent.removeView(mCountDownText);
                        mCountDownText = null;
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mCountDownText != null) {
                    mCountDownCount--;
                    if (mCountDownCount > 0) {
                        mCountDownText.setText(String.valueOf(mCountDownCount));
                    } else {
                        mCountDownText.setText(null);
                    }
                }
            }
        });
        mCountDownText.startAnimation(animation);
    }


    @Override
    public ViewGroup getSmallContainer() {
        return mSmallContainer;
    }


    @Override
    public ViewGroup getRightContainer() {
        return mRightContainer;
    }

    @Override
    public ViewGroup getPkContainer() {
        return mPkContainer;
    }


    @Override
    public void setOpenCamera(boolean openCamera) {
        mOpenCamera = openCamera;
    }

    @Override
    public void setLivePushListener(LivePushListener livePushListener) {
        mLivePushListener = livePushListener;
    }


    protected abstract void onCameraRestart();


    public abstract void togglePushMirror();

    @Override
    public void release() {
        if (mCountDownText != null) {
            mCountDownText.clearAnimation();
        }
        mLivePushListener = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        if (mQueue != null) {
            mQueue.clear();
        }
        mQueue = null;
    }

    @Override
    public void onReStart() {
        if (mOpenCamera) {
            mOpenCamera = false;
            onCameraRestart();
        }
    }

    @Override
    public void onDestroy() {

        L.e(TAG, "LifeCycle------>onDestroy");
    }

    public boolean isFlashOpen() {
        return mFlashOpen;
    }

    public IBeautyEffectListener getMeiYanChangedListener() {
        return null;
    }


    /**
     * 获取道具列表
     */
    private void getStickerList() {
        if (CommonAppConfig.getInstance().isMhBeautyEnable()) {
            MHSDK.getStickerGiftList(new MHSDK.TieZhiListCallback() {
                @Override
                public void getTieZhiList(String data) {
                    mStickerList = JSON.parseArray(data, LiveStickerBean.class);
                }
            });
        }
    }


    private LiveStickerBean findStickerBean(String stickerId) {
        if (TextUtils.isEmpty(stickerId)) {
            return null;
        }
        if (mStickerList == null || mStickerList.size() == 0) {
            return null;
        }
        for (LiveStickerBean bean : mStickerList) {
            if (stickerId.equals(bean.getId())) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 道具礼物贴纸
     */
    public void setLiveStickerGift(String stickerGiftId, long playTime) {
        LiveStickerBean bean = findStickerBean(stickerGiftId);
        if (bean == null) {
            return;
        }
        bean.setPlayTime(playTime);
        if (mIsPlayGiftSticker) {
            if (mQueue == null) {
                mQueue = new ConcurrentLinkedQueue<>();
            }
            mQueue.offer(bean);
        } else {
            showSticker(bean.getName(), playTime);
        }
    }

    /**
     * 道具礼物贴纸
     */
    private void showSticker(final String stickerName, final long playTime) {
        mIsPlayGiftSticker = true;
        boolean fileExist = MhDataManager.isTieZhiDownloaded(stickerName);
        if (fileExist) {
            playSticker(stickerName, playTime);
        } else {
            MhDataManager.downloadTieZhi(stickerName, new CommonCallback<Boolean>() {
                @Override
                public void callback(Boolean success) {
                    if (success) {
                        playSticker(stickerName, playTime);
                    } else {
                        getNextStickerGift();
                    }
                }
            });
        }
    }


    /**
     * 道具礼物贴纸
     */
    private void playSticker(String stickerName, long playTime) {
        EventBus.getDefault().post(new TieZhiCancelEvent());
        MhDataManager.getInstance().setTieZhi(stickerName);
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1010) {
                        getNextStickerGift();
                    }
                }
            };
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(1010, playTime);
    }


    /**
     * 获取下一个道具礼物贴纸
     */
    private void getNextStickerGift() {
        if (mQueue != null) {
            LiveStickerBean nextGiftBean = mQueue.poll();
            if (nextGiftBean == null) {
                endSticker();
            } else {
                showSticker(nextGiftBean.getName(), nextGiftBean.getPlayTime());
            }
        } else {
            endSticker();
        }
    }


    private void endSticker() {
        if (mIsPlayGiftSticker) {
            mIsPlayGiftSticker = false;
            MhDataManager.getInstance().setTieZhi(null);
        }
    }

}
