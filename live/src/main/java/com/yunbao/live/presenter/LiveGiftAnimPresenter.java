package com.yunbao.live.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.opensource.svgaplayer.utils.SVGARect;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.GifCacheUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.ScreenDimenUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.GlobalGiftBean;
import com.yunbao.live.bean.LiveGiftPrizePoolWinBean;
import com.yunbao.live.bean.LiveLuckGiftWinBean;
import com.yunbao.live.bean.LiveReceiveGiftBean;
import com.yunbao.live.views.LiveGiftDrawViewHolder;
import com.yunbao.live.views.LiveGiftLuckTopViewHolder;
import com.yunbao.live.views.LiveGiftPrizePoolViewHolder;
import com.yunbao.live.views.LiveGiftViewHolder;
import com.yunbao.live.views.LiveTitleAnimViewHolder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by cxf on 2018/10/13.
 * 产品让改礼物效果
 */

public class LiveGiftAnimPresenter {

    private Context mContext;
    private ViewGroup mParent2;
    private ViewGroup mDrawParent;
    private SVGAImageView mSVGAImageView;
    private GifImageView mGifImageView;
    private GifDrawable mGifDrawable;
    private View mGifGiftTipGroup;
    private TextView mGifGiftTip;
    private ObjectAnimator mGifGiftTipShowAnimator;
    private ObjectAnimator mGifGiftTipHideAnimator;

    private View mGlobalGiftGroup;
    private TextView[] mGlobalGiftTips;
    private ObjectAnimator mGlobalGiftShowAnimator;//全站礼物
    private ValueAnimator mGlobalGiftMoveAnimator;//全站礼物
    private ObjectAnimator mGlobalGiftHideAnimator;//全站礼物
    private int mMoveGlobalSpace;
    private LiveGiftDrawViewHolder mGiftDrawViewHolder;
    private LiveGiftViewHolder[] mLiveGiftViewHolders;
    private ConcurrentLinkedQueue<LiveReceiveGiftBean> mQueue;
    private ConcurrentLinkedQueue<LiveReceiveGiftBean> mGifQueue;
    private ConcurrentLinkedQueue<GlobalGiftBean> mGlobalGiftQueue;
    private ConcurrentLinkedQueue<LiveReceiveGiftBean> mDrawGifQueue;
    private ConcurrentLinkedQueue<String> mGoodsFloatQueue;
    private Map<String, LiveReceiveGiftBean> mMap;
    private Handler mHandler;
    private MediaController mMediaController;//koral--/android-gif-drawable 这个库用来播放gif动画的
    private static final int WHAT_GIF = -1;
    private static final int WHAT_ANIM = -2;
    private static final int WHAT_GLOBAL = -3;
    private static final int WHAT_GLOBAL_2 = -4;
    private static final int WHAT_DRAW = -5;
    private static final int WHAT_DRAW_FINISH = -6;
    private static final int WHAT_DRAW_END = -7;
    private boolean mShowGif;
    private boolean mShowGlobal;
    private boolean mShowDrawGif;
    private boolean mShowGoodsFloat;
    private CommonCallback<File> mDownloadGifCallback;
    private int mDp10;
    private int mDp20;
    private int mDp50;
    private int mDp500;
    private LiveReceiveGiftBean mTempGifGiftBean;
    private String mSendString;
    private SVGAParser mSVGAParser;
    private SVGAParser.ParseCompletion mParseCompletionCallback;
    private long mSvgaPlayTime;
    private Map<String, SoftReference<SVGAVideoEntity>> mSVGAMap;
    private ViewGroup mTopLuckContainer;
    private LiveGiftLuckTopViewHolder mLiveGiftLuckTopViewHolder;
    private LiveGiftPrizePoolViewHolder mPrizePoolViewHolder;
    private ViewGroup mLiveGiftPrizePoolContainer;
//    private TextView mPrizePoolLevel;
//    private View mPrizePoolGuang;
    private Animation mPrizePoolLevelAnim;
    private Animation mPrizePoolGuangAnim;
    private ViewGroup mTitleContainer;
    private LiveTitleAnimViewHolder mTitleAnimViewHolder;


    private FrameLayout mDrawGiftContainer;
    private List<ImageView> mDrawImgList;
    private Drawable mDrawGiftDrawable;
    private float mDrawGiftOffsetX;
    private float mDrawGiftOffsetY;
    private List<PointF> mDrawGiftPointList;
    private int mDrawCount;
    private int mDrawIndex;
    private ScaleAnimation mDrawImgAnim;
    private ScaleAnimation mDrawEndAnim;
    private String mGoodsFloatString;
    private TextView mTvGoodsFloat;
    private ObjectAnimator mGoodsFloatShowAnimator;
    private ObjectAnimator mGoodsFloatShowAnimator2;
    private ObjectAnimator mGoodsFloatHideAnimator;

    public LiveGiftAnimPresenter(Context context, View v, GifImageView gifImageView, SVGAImageView svgaImageView, ViewGroup liveGiftPrizePoolContainer) {
        mContext = context;
        mParent2 = (ViewGroup) v.findViewById(R.id.gift_group_1);
        mDrawParent = (ViewGroup) v.findViewById(R.id.gift_group_draw);
        mTopLuckContainer = v.findViewById(R.id.luck_container);
        mGifImageView = gifImageView;
        mSVGAImageView = svgaImageView;
        mLiveGiftPrizePoolContainer = liveGiftPrizePoolContainer;
//        mPrizePoolLevel = prizePoolLevel;
//        mPrizePoolGuang = prizePoolGuang;
        mTitleContainer = v.findViewById(R.id.title_container);
        mSVGAImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                long diffTime = 4000 - (System.currentTimeMillis() - mSvgaPlayTime);
                if (diffTime < 0) {
                    diffTime = 0;
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_GIF, diffTime);
                }
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });


        mGifGiftTipGroup = v.findViewById(R.id.gif_gift_tip_group);
        mGifGiftTip = (TextView) v.findViewById(R.id.gif_gift_tip);
        mDrawGiftContainer = v.findViewById(R.id.draw_gift_container);
        mDrawGiftOffsetX = ScreenDimenUtil.getInstance().getScreenWdith() / 20f;
        mDrawImgAnim = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDrawImgAnim.setDuration(100);
        mDrawEndAnim = new ScaleAnimation(1f, 2.5f, 1f, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDrawEndAnim.setDuration(300);
        mTvGoodsFloat = v.findViewById(R.id.goods_float);
        mGifGiftTipGroup = v.findViewById(R.id.gif_gift_tip_group);
        mGifGiftTip = (TextView) v.findViewById(R.id.gif_gift_tip);
        mDp500 = DpUtil.dp2px(500);
        mGifGiftTipShowAnimator = ObjectAnimator.ofFloat(mGifGiftTipGroup, "translationX", mDp500, 0);
        mGifGiftTipShowAnimator.setDuration(1000);
        mGifGiftTipShowAnimator.setInterpolator(new LinearInterpolator());
        mGifGiftTipShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_ANIM, 2000);
                }
            }
        });
        mDp10 = DpUtil.dp2px(10);
        mDp20 = DpUtil.dp2px(20);
        mGifGiftTipHideAnimator = ObjectAnimator.ofFloat(mGifGiftTipGroup, "translationX", 0);
        mGifGiftTipHideAnimator.setDuration(800);
        mGifGiftTipHideAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mGifGiftTipHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGifGiftTipGroup.setAlpha(1 - animation.getAnimatedFraction());
            }
        });
        mSendString = WordUtil.getString(R.string.live_send_gift_3);
        mGoodsFloatString = WordUtil.getString(R.string.mall_392);
        mDp50 = DpUtil.dp2px(50);
        mGlobalGiftGroup = v.findViewById(R.id.global_gift_tip_group);
        mGlobalGiftTips = new TextView[3];
        mGlobalGiftTips[0] = (TextView) v.findViewById(R.id.global_gift_tip_0);
        mGlobalGiftTips[1] = (TextView) v.findViewById(R.id.global_gift_tip_1);
        mGlobalGiftTips[2] = (TextView) v.findViewById(R.id.global_gift_tip_2);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mGlobalGiftShowAnimator = ObjectAnimator.ofFloat(mGlobalGiftGroup, "translationX", mDp500, 0);
        mGlobalGiftShowAnimator.setDuration(1000);
        mGlobalGiftShowAnimator.setInterpolator(linearInterpolator);
        mGlobalGiftShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_GLOBAL, 1200);
                }
            }
        });

        mGlobalGiftMoveAnimator = ValueAnimator.ofFloat(0, 2);
        mGlobalGiftMoveAnimator.setInterpolator(linearInterpolator);
        mGlobalGiftMoveAnimator.setDuration(4500);
        mGlobalGiftMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = ((float) animation.getAnimatedValue()) * mMoveGlobalSpace;
                mGlobalGiftTips[0].setTranslationX(-x);
                mGlobalGiftTips[1].setTranslationX(mMoveGlobalSpace - x);
                mGlobalGiftTips[2].setTranslationX(mMoveGlobalSpace * 2 - x);
            }
        });
        mGlobalGiftMoveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_GLOBAL_2, 1200);
                }
            }
        });
        mGlobalGiftHideAnimator = ObjectAnimator.ofFloat(mGlobalGiftGroup, "alpha", 1, 0);
        mGlobalGiftHideAnimator.setDuration(500);
        mGlobalGiftHideAnimator.setRepeatCount(3);
        mGlobalGiftHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mGlobalGiftGroup.setTranslationX(mDp500);
                mShowGlobal = false;
                if (mGlobalGiftQueue != null) {
                    GlobalGiftBean bean = mGlobalGiftQueue.poll();
                    if (bean != null) {
                        showGlobalGift(bean);
                    }
                }
            }
        });


        mLiveGiftViewHolders = new LiveGiftViewHolder[2];
        mLiveGiftViewHolders[0] = new LiveGiftViewHolder(context, (ViewGroup) v.findViewById(R.id.gift_group_2));
        mLiveGiftViewHolders[0].addToParent();
        mQueue = new ConcurrentLinkedQueue<>();
        mGifQueue = new ConcurrentLinkedQueue<>();
        mGlobalGiftQueue = new ConcurrentLinkedQueue<>();
        mDrawGifQueue = new ConcurrentLinkedQueue<>();
        mGoodsFloatQueue = new ConcurrentLinkedQueue<>();
        mMap = new HashMap<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_GIF) {
                    mShowGif = false;
                    if (mGifImageView != null) {
                        mGifImageView.setImageDrawable(null);
                    }
                    if (mGifDrawable != null && !mGifDrawable.isRecycled()) {
                        mGifDrawable.stop();
                        mGifDrawable.recycle();
                    }
                    LiveReceiveGiftBean bean = mGifQueue.poll();
                    if (bean != null) {
                        showGifGift(bean);
                    }
                } else if (msg.what == WHAT_ANIM) {
                    mGifGiftTipHideAnimator.setFloatValues(0, -mDp10 - mGifGiftTipGroup.getWidth());
                    mGifGiftTipHideAnimator.start();
                } else if (msg.what == WHAT_GLOBAL) {
                    mGlobalGiftMoveAnimator.start();
                } else if (msg.what == WHAT_GLOBAL_2) {
                    mGlobalGiftHideAnimator.start();
                } else if (msg.what == WHAT_DRAW) {
                    nextDrawGift();
                } else if (msg.what == WHAT_DRAW_FINISH) {
                    if (mDrawGiftContainer != null) {
                        mDrawGiftContainer.startAnimation(mDrawEndAnim);
                    }
                    if (mGiftDrawViewHolder != null) {
                        mGiftDrawViewHolder.hide();
                    }
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(WHAT_DRAW_END, 500);
                    }
                } else if (msg.what == WHAT_DRAW_END) {
                    clearDrawGift();
                    mShowDrawGif = false;
                    if (mDrawGifQueue != null) {
                        LiveReceiveGiftBean bean = mDrawGifQueue.poll();
                        if (bean != null) {
                            showDrawGift(bean);
                        }
                    }
                } else {
                    LiveGiftViewHolder vh = mLiveGiftViewHolders[msg.what];
                    if (vh != null) {
                        LiveReceiveGiftBean bean = mQueue.poll();
                        if (bean != null) {
                            mMap.remove(bean.getKey());
                            vh.show(bean, false);
                            resetTimeCountDown(msg.what);
                        } else {
                            vh.hide();
                        }
                    }
                }
            }
        };
        mDownloadGifCallback = new CommonCallback<File>() {
            @Override
            public void callback(File file) {
                if (file != null) {
                    playHaoHuaGift(file);
                } else {
                    mShowGif = false;
                }
            }
        };
    }

    public void showGiftAnim(LiveReceiveGiftBean bean) {
        if (bean.getType() == 1) {////豪华礼物
            showGifGift(bean);
        } else if (bean.getType() == 3) {//手绘礼物
            showDrawGift(bean);
        } else {
            showNormalGift(bean);//普通礼物
        }
    }

    /**
     * 显示gif礼物
     */
    private void showGifGift(LiveReceiveGiftBean bean) {
        String url = bean.getGifUrl();
        L.e("gif礼物----->" + bean.getGiftName() + "----->" + url);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mShowGif) {
            if (mGifQueue != null) {
                mGifQueue.offer(bean);
            }
        } else {
            mShowGif = true;
            mTempGifGiftBean = bean;
            if (!url.endsWith(".gif") && !url.endsWith(".svga")) {
                ImgLoader.displayDrawable(mContext, url, new ImgLoader.DrawableCallback() {
                    @Override
                    public void onLoadSuccess(Drawable drawable) {
                        resizeGifImageView(drawable);
                        mGifImageView.setImageDrawable(drawable);
                        showHaoHuaGiftTip();
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(WHAT_GIF, 4000);
                        }
                    }

                    @Override
                    public void onLoadFailed() {
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(WHAT_GIF);
                        }
                    }
                });
            } else {
                GifCacheUtil.getFile(MD5Util.getMD5(url), url, mDownloadGifCallback);
            }
        }
    }


    private void showHaoHuaGiftTip() {
        if (mTempGifGiftBean != null && mTempGifGiftBean.getIsGlobal() == 0) {
            mGifGiftTip.setText(String.format(mSendString, mTempGifGiftBean.getUserNiceName(), mTempGifGiftBean.getGiftName()));
            mGifGiftTipGroup.setAlpha(1f);
            mGifGiftTipShowAnimator.start();
        }
    }

    /**
     * 调整mGifImageView的大小
     */
    private void resizeGifImageView(Drawable drawable) {
        float w = drawable.getIntrinsicWidth();
        float h = drawable.getIntrinsicHeight();
        ViewGroup.LayoutParams params = mGifImageView.getLayoutParams();
        params.height = (int) (mGifImageView.getWidth() * h / w);
        mGifImageView.setLayoutParams(params);
    }

    /**
     * 调整mSVGAImageView的大小
     */
    private void resizeSvgaImageView(double w, double h) {
        ViewGroup.LayoutParams params = mSVGAImageView.getLayoutParams();
        params.height = (int) (mSVGAImageView.getWidth() * h / w);
        mSVGAImageView.setLayoutParams(params);
    }

    /**
     * 播放豪华礼物
     */
    private void playHaoHuaGift(File file) {
        if (mTempGifGiftBean.getGifType() == 0) {//豪华礼物类型 0是gif  1是svga
            showHaoHuaGiftTip();
            playGift(file);
        } else {
            SVGAVideoEntity svgaVideoEntity = null;
            if (mSVGAMap != null) {
                SoftReference<SVGAVideoEntity> reference = mSVGAMap.get(mTempGifGiftBean.getGiftId());
                if (reference != null) {
                    svgaVideoEntity = reference.get();
                }
            }
            if (svgaVideoEntity != null) {
                playSVGA(svgaVideoEntity);
            } else {
                decodeSvga(file);
            }
        }
    }

    /**
     * 播放gif
     */
    private void playGift(File file) {
        try {
            mGifDrawable = new GifDrawable(file);
            mGifDrawable.setLoopCount(1);
            resizeGifImageView(mGifDrawable);
            mGifImageView.setImageDrawable(mGifDrawable);
            if (mMediaController == null) {
                mMediaController = new MediaController(mContext);
                mMediaController.setVisibility(View.GONE);
            }
            mMediaController.setMediaPlayer((GifDrawable) mGifImageView.getDrawable());
            mMediaController.setAnchorView(mGifImageView);
            int duration = mGifDrawable.getDuration();
            mMediaController.show(duration);
            if (duration < 4000) {
                duration = 4000;
            }
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(WHAT_GIF, duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mShowGif = false;
        }
    }

    /**
     * 播放svga
     */
    private void playSVGA(SVGAVideoEntity svgaVideoEntity) {
        if (mSVGAImageView != null) {
            SVGARect rect = svgaVideoEntity.getVideoSize();
            resizeSvgaImageView(rect.getWidth(), rect.getHeight());
            //SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
            //mSVGAImageView.setImageDrawable(drawable);
            mSVGAImageView.setVideoItem(svgaVideoEntity);
            mSvgaPlayTime = System.currentTimeMillis();
            mSVGAImageView.startAnimation();
            showHaoHuaGiftTip();
        }
    }

    /**
     * 播放svga
     */
    private void decodeSvga(File file) {
        if (mSVGAParser == null) {
            mSVGAParser = new SVGAParser(mContext);
        }
        if (mParseCompletionCallback == null) {
            mParseCompletionCallback = new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    if (mSVGAMap == null) {
                        mSVGAMap = new HashMap<>();
                    }
                    if (mTempGifGiftBean != null) {
                        mSVGAMap.put(mTempGifGiftBean.getGiftId(), new SoftReference<>(svgaVideoEntity));
                    }
                    playSVGA(svgaVideoEntity);
                }

                @Override
                public void onError() {
                    mShowGif = false;
                }
            };
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            mSVGAParser.decodeFromInputStream(bis, file.getAbsolutePath(), mParseCompletionCallback, true);
        } catch (Exception e) {
            e.printStackTrace();
            mShowGif = false;
        }
    }

    /**
     * 显示普通礼物
     */
    private void showNormalGift(LiveReceiveGiftBean bean) {
        if (mLiveGiftViewHolders[0].isIdle()) {
            if (mLiveGiftViewHolders[1] != null && mLiveGiftViewHolders[1].isSameGift(bean)) {
                mLiveGiftViewHolders[1].show(bean, true);
                resetTimeCountDown(1);
                return;
            }
            mLiveGiftViewHolders[0].show(bean, false);
            resetTimeCountDown(0);
            return;
        }
        if (mLiveGiftViewHolders[0].isSameGift(bean)) {
            mLiveGiftViewHolders[0].show(bean, true);
            resetTimeCountDown(0);
            return;
        }
        if (mLiveGiftViewHolders[1] == null) {
            mLiveGiftViewHolders[1] = new LiveGiftViewHolder(mContext, mParent2);
            mLiveGiftViewHolders[1].addToParent();
        }
        if (mLiveGiftViewHolders[1].isIdle()) {
            mLiveGiftViewHolders[1].show(bean, false);
            resetTimeCountDown(1);
            return;
        }
        if (mLiveGiftViewHolders[1].isSameGift(bean)) {
            mLiveGiftViewHolders[1].show(bean, true);
            resetTimeCountDown(1);
            return;
        }
        String key = bean.getKey();
        if (!mMap.containsKey(key)) {
            mMap.put(key, bean);
            mQueue.offer(bean);
        } else {
            LiveReceiveGiftBean bean1 = mMap.get(key);
            bean1.setLianCount(bean1.getLianCount() + 1);
        }
    }

    private void resetTimeCountDown(int index) {
        if (mHandler != null) {
            mHandler.removeMessages(index);
            mHandler.sendEmptyMessageDelayed(index, 5000);
        }
    }


    public void cancelAllAnim() {
        clearAnim();
        cancelNormalGiftAnim();
        if (mGifGiftTipGroup != null && mGifGiftTipGroup.getTranslationX() != mDp500) {
            mGifGiftTipGroup.setTranslationX(mDp500);
        }
        if (mGlobalGiftGroup != null && mGlobalGiftGroup.getTranslationX() != mDp500) {
            mGlobalGiftGroup.setTranslationX(mDp500);
        }
    }

    private void cancelNormalGiftAnim() {
        if (mLiveGiftViewHolders[0] != null) {
            mLiveGiftViewHolders[0].cancelAnimAndHide();
        }
        if (mLiveGiftViewHolders[1] != null) {
            mLiveGiftViewHolders[1].cancelAnimAndHide();
        }
        if (mGiftDrawViewHolder != null) {
            mGiftDrawViewHolder.cancelAnimAndHide();
        }
    }


    private void clearAnim() {
        mShowGif = false;
        mShowGlobal = false;
        mShowDrawGif = false;
        mShowGoodsFloat = false;
        CommonHttpUtil.cancel(CommonHttpConsts.DOWNLOAD_GIF);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mGifGiftTipShowAnimator != null) {
            mGifGiftTipShowAnimator.cancel();
        }
        if (mGifGiftTipHideAnimator != null) {
            mGifGiftTipHideAnimator.cancel();
        }
        if (mGlobalGiftShowAnimator != null) {
            mGlobalGiftShowAnimator.cancel();
        }
        if (mGlobalGiftMoveAnimator != null) {
            mGlobalGiftMoveAnimator.cancel();
        }
        if (mGlobalGiftHideAnimator != null) {
            mGlobalGiftHideAnimator.cancel();
        }
        if (mQueue != null) {
            mQueue.clear();
        }
        if (mGifQueue != null) {
            mGifQueue.clear();
        }
        if (mGlobalGiftQueue != null) {
            mGlobalGiftQueue.clear();
        }
        if (mDrawGifQueue != null) {
            mDrawGifQueue.clear();
        }
        if (mGoodsFloatQueue != null) {
            mGoodsFloatQueue.clear();
        }

        if (mMap != null) {
            mMap.clear();
        }
        if (mMediaController != null) {
            mMediaController.hide();
            mMediaController.setAnchorView(null);
        }
        if (mGifImageView != null) {
            mGifImageView.setImageDrawable(null);
        }
        if (mGifDrawable != null && !mGifDrawable.isRecycled()) {
            mGifDrawable.stop();
            mGifDrawable.recycle();
            mGifDrawable = null;
        }
        if (mSVGAImageView != null) {
            mSVGAImageView.stopAnimation(true);
        }
        if (mSVGAMap != null) {
            mSVGAMap.clear();
        }
        if (mTitleAnimViewHolder != null) {
            mTitleAnimViewHolder.clearAnim();
        }
        if (mLiveGiftLuckTopViewHolder != null) {
            mLiveGiftLuckTopViewHolder.clearAnim();
        }
        if (mPrizePoolViewHolder != null) {
            mPrizePoolViewHolder.clearAnim();
        }
//        if (mPrizePoolGuang != null) {
//            mPrizePoolGuang.clearAnimation();
//        }
//        if (mPrizePoolLevel != null) {
//            mPrizePoolLevel.clearAnimation();
//        }
        if (mGoodsFloatShowAnimator != null) {
            mGoodsFloatShowAnimator.cancel();
        }
        if (mGoodsFloatShowAnimator2 != null) {
            mGoodsFloatShowAnimator2.cancel();
        }
        if (mGoodsFloatHideAnimator != null) {
            mGoodsFloatHideAnimator.cancel();
        }
        if (mTvGoodsFloat != null) {
            mTvGoodsFloat.setTranslationX(mDp500);
        }
        clearDrawGift();
    }

    public void release() {
        clearAnim();
        if (mGifGiftTipShowAnimator != null) {
            mGifGiftTipShowAnimator.removeAllListeners();
            mGifGiftTipShowAnimator.removeAllUpdateListeners();
        }
        if (mGifGiftTipHideAnimator != null) {
            mGifGiftTipHideAnimator.removeAllListeners();
            mGifGiftTipHideAnimator.removeAllUpdateListeners();
        }
        if (mGlobalGiftShowAnimator != null) {
            mGlobalGiftShowAnimator.removeAllListeners();
            mGlobalGiftShowAnimator.removeAllUpdateListeners();
        }
        if (mGlobalGiftMoveAnimator != null) {
            mGlobalGiftMoveAnimator.removeAllListeners();
            mGlobalGiftMoveAnimator.removeAllUpdateListeners();
        }
        if (mGlobalGiftHideAnimator != null) {
            mGlobalGiftHideAnimator.removeAllListeners();
            mGlobalGiftHideAnimator.removeAllUpdateListeners();
        }
        if (mLiveGiftViewHolders[0] != null) {
            mLiveGiftViewHolders[0].release();
        }
        if (mLiveGiftViewHolders[1] != null) {
            mLiveGiftViewHolders[1].release();
        }
        if (mSVGAImageView != null) {
            mSVGAImageView.setCallback(null);
        }
        if (mTitleAnimViewHolder != null) {
            mTitleAnimViewHolder.release();
        }
        if (mLiveGiftLuckTopViewHolder != null) {
            mLiveGiftLuckTopViewHolder.release();
        }
        if (mPrizePoolViewHolder != null) {
            mPrizePoolViewHolder.release();
        }
        if (mPrizePoolGuangAnim != null) {
            mPrizePoolGuangAnim.cancel();
            mPrizePoolGuangAnim.setAnimationListener(null);
        }
        if (mPrizePoolLevelAnim != null) {
            mPrizePoolLevelAnim.cancel();
            mPrizePoolLevelAnim.setAnimationListener(null);
        }
        if (mGiftDrawViewHolder != null) {
            mGiftDrawViewHolder.release();
        }
        if (mGoodsFloatShowAnimator != null) {
            mGoodsFloatShowAnimator.cancel();
            mGoodsFloatShowAnimator.removeAllListeners();
            mGoodsFloatShowAnimator.removeAllUpdateListeners();
        }
        if (mGoodsFloatShowAnimator2 != null) {
            mGoodsFloatShowAnimator2.cancel();
            mGoodsFloatShowAnimator2.removeAllListeners();
            mGoodsFloatShowAnimator2.removeAllUpdateListeners();
        }
        if (mGoodsFloatHideAnimator != null) {
            mGoodsFloatHideAnimator.cancel();
            mGoodsFloatHideAnimator.removeAllListeners();
            mGoodsFloatHideAnimator.removeAllUpdateListeners();
        }

        mSVGAImageView = null;
        mDownloadGifCallback = null;
        mHandler = null;
        mTitleAnimViewHolder = null;
        mLiveGiftLuckTopViewHolder = null;
        mPrizePoolViewHolder = null;
        mGoodsFloatShowAnimator = null;
        mGoodsFloatShowAnimator2 = null;
        mGoodsFloatHideAnimator = null;
    }


    /**
     * 幸运礼物中奖
     */
    public void showLuckGiftWinAnim(LiveLuckGiftWinBean bean) {
        if (mTopLuckContainer == null || bean == null) {
            return;
        }
        if (mLiveGiftLuckTopViewHolder == null) {
            mLiveGiftLuckTopViewHolder = new LiveGiftLuckTopViewHolder(mContext, mTopLuckContainer);
            mLiveGiftLuckTopViewHolder.addToParent();
        }
        mLiveGiftLuckTopViewHolder.show(bean);
    }


    /**
     * 奖池中奖
     */
    public void showPrizePoolWinAnim(LiveGiftPrizePoolWinBean bean) {
        if (mLiveGiftPrizePoolContainer == null) {
            return;
        }
        if (mPrizePoolViewHolder == null) {
            mPrizePoolViewHolder = new LiveGiftPrizePoolViewHolder(mContext, mLiveGiftPrizePoolContainer);
            mPrizePoolViewHolder.addToParent();
        }
        mPrizePoolViewHolder.show(bean);
    }

//    public void setPrizePoolView(TextView prizePoolLevel, View prizePoolGuang) {
//        mPrizePoolLevel = prizePoolLevel;
//        mPrizePoolGuang = prizePoolGuang;
//    }

    /**
     * 奖池升级
     */
    public void showPrizePoolUp(String level) {
//        if (mPrizePoolLevel == null || mPrizePoolGuang == null) {
//            return;
//        }
//        if (mPrizePoolGuangAnim == null) {
//            mPrizePoolGuangAnim = new TranslateAnimation(-DpUtil.dp2px(48), DpUtil.dp2px(100), 0, 0);
//            mPrizePoolGuangAnim.setDuration(1000);
//            mPrizePoolGuangAnim.setRepeatCount(1);
//            mPrizePoolGuangAnim.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    if (mPrizePoolLevel != null) {
//                        if (mPrizePoolLevelAnim == null) {
//                            mPrizePoolLevelAnim = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            mPrizePoolLevelAnim.setDuration(400);
//                            mPrizePoolLevelAnim.setRepeatCount(1);
//                        }
//                        mPrizePoolLevel.startAnimation(mPrizePoolLevelAnim);
//                    }
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//
//        mPrizePoolLevel.setText(String.format(WordUtil.getString(R.string.live_gift_prize_pool_3), level));
//        mPrizePoolGuang.startAnimation(mPrizePoolGuangAnim);

    }

    /**
     * 直播间标题动画
     */
    public void showLiveTitleAnim(String title) {
        if (mTitleContainer == null || mHandler == null) {
            return;
        }
        if (mTitleAnimViewHolder == null) {
            mTitleAnimViewHolder = new LiveTitleAnimViewHolder(mContext, mTitleContainer, mHandler);
            mTitleAnimViewHolder.addToParent();
        }
        mTitleAnimViewHolder.show(title);
    }


    /**
     * 全站礼物
     */
    public void showGlobalGift(GlobalGiftBean bean) {
        if (mShowGlobal) {
            if (mGlobalGiftQueue != null) {
                mGlobalGiftQueue.offer(bean);
            }
            return;
        }
        mShowGlobal = true;
        String s = String.format(WordUtil.getString(R.string.global_gift), bean.getUserName(), bean.getLiveName(), bean.getGiftName());
        mGlobalGiftTips[0].setText(s);
        mGlobalGiftTips[1].setText(s);
        mGlobalGiftTips[2].setText(s);
        mGlobalGiftTips[0].measure(0, 0);
        int width = mGlobalGiftTips[0].getMeasuredWidth();
//        L.e("showGlobalGift----width-----> " + width);
        mMoveGlobalSpace = mDp50 + width;
        mGlobalGiftTips[0].setTranslationX(0);
        mGlobalGiftTips[1].setTranslationX(mMoveGlobalSpace);
        mGlobalGiftTips[2].setTranslationX(mMoveGlobalSpace * 2);
        mGlobalGiftGroup.setAlpha(1f);
        mGlobalGiftShowAnimator.start();
    }


    private void showDrawGift(LiveReceiveGiftBean bean) {
        if (bean == null) {
            return;
        }
        if (mShowDrawGif) {
            if (mDrawGifQueue != null) {
                mDrawGifQueue.offer(bean);
            }
            return;
        }
        mShowDrawGif = true;
        mDrawGiftPointList = bean.getPointList();
        if (mDrawGiftPointList == null || mDrawGiftPointList.size() <= 0) {
            return;
        }
        if (mGiftDrawViewHolder == null) {
            mGiftDrawViewHolder = new LiveGiftDrawViewHolder(mContext, mDrawParent);
            mGiftDrawViewHolder.addToParent();
        }
        mGiftDrawViewHolder.show(bean);
        float pw = bean.getDrawWidth();
        float ph = bean.getDrawHeight();
        if (pw <= 0 || ph <= 0) {
            return;
        }
        final float rate = ScreenDimenUtil.getInstance().getScreenWdith() / pw;
        if (mDrawGiftContainer != null) {
            ViewGroup.LayoutParams lp = mDrawGiftContainer.getLayoutParams();
            lp.width = ScreenDimenUtil.getInstance().getScreenWdith();
            lp.height = (int) (ph * rate);
            mDrawGiftContainer.requestLayout();
        }
        ImgLoader.displayDrawable(mContext, bean.getGiftIcon(), new ImgLoader.DrawableCallback() {

            @Override
            public void onLoadFailed() {

            }

            @Override
            public void onLoadSuccess(Drawable drawable) {
                if (drawable == null) {
                    return;
                }
                mDrawGiftDrawable = drawable;
                if (mDrawImgList == null) {
                    mDrawImgList = new ArrayList<>();
                }
                mDrawCount = mDrawGiftPointList.size();
                int cha = mDrawCount - mDrawImgList.size();
                if (cha > 0) {
                    for (int i = 0; i < cha; i++) {
                        ImageView imageView = new ImageView(mContext);
                        mDrawGiftContainer.addView(imageView);
                        mDrawImgList.add(imageView);
                    }
                }
                mDrawGiftOffsetY = mDrawGiftOffsetX * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                for (int i = 0; i < mDrawCount; i++) {
                    ImageView imageView = mDrawImgList.get(i);
                    PointF pointF = mDrawGiftPointList.get(i);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                    if (layoutParams == null) {
                        layoutParams = new FrameLayout.LayoutParams((int) (mDrawGiftOffsetX * 2), (int) (mDrawGiftOffsetY * 2));
                        layoutParams.leftMargin = (int) (pointF.x * rate - mDrawGiftOffsetX);
                        layoutParams.topMargin = (int) (pointF.y * rate - mDrawGiftOffsetY);
                        imageView.setLayoutParams(layoutParams);
                    } else {
                        layoutParams.width = (int) (mDrawGiftOffsetX * 2);
                        layoutParams.height = (int) (mDrawGiftOffsetY * 2);
                        layoutParams.leftMargin = (int) (pointF.x * rate - mDrawGiftOffsetX);
                        layoutParams.topMargin = (int) (pointF.y * rate - mDrawGiftOffsetY);
                        imageView.requestLayout();
                    }
                }
                mDrawIndex = 0;
                nextDrawGift();
            }
        });
    }

    private void nextDrawGift() {
        if (mDrawIndex < mDrawCount) {
            ImageView imageView = mDrawImgList.get(mDrawIndex);
            imageView.setImageDrawable(mDrawGiftDrawable);
            imageView.startAnimation(mDrawImgAnim);
            mDrawIndex++;
            if (mDrawIndex < mDrawCount) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_DRAW, 200);
                }
            } else {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_DRAW_FINISH, 500);
                }
            }
        }
    }

    private void clearDrawGift() {
        if (mDrawImgList != null && mDrawImgList.size() > 0) {
            for (ImageView imageView : mDrawImgList) {
                imageView.clearAnimation();
                imageView.setImageDrawable(null);
            }
        }
    }

    /**
     * 直播间购物飘屏
     */
    public void onLiveGoodsFloat(String userName) {
        if (TextUtils.isEmpty(userName) || mTvGoodsFloat == null) {
            return;
        }
        if (mShowGoodsFloat) {
            if (mGoodsFloatQueue != null) {
                mGoodsFloatQueue.offer(userName);
            }
            return;
        }
        mShowGoodsFloat = true;
        if (userName.length() > 0) {
            userName = StringUtil.contact(userName.substring(0, 1), "***", mGoodsFloatString);
        }
        mTvGoodsFloat.setText(userName);
        if (mGoodsFloatHideAnimator == null) {
            mGoodsFloatHideAnimator = ObjectAnimator.ofFloat(mTvGoodsFloat, "translationX", mDp20);
            mGoodsFloatHideAnimator.setDuration(1000);
            mGoodsFloatHideAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mGoodsFloatHideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mShowGoodsFloat = false;
                    if (mGoodsFloatQueue != null) {
                        String name = mGoodsFloatQueue.poll();
                        if (name != null) {
                            onLiveGoodsFloat(name);
                        }
                    }
                }
            });
        }
        if (mGoodsFloatShowAnimator == null) {
            mGoodsFloatShowAnimator = ObjectAnimator.ofFloat(mTvGoodsFloat, "translationX", mDp500, mDp20);
            mGoodsFloatShowAnimator.setDuration(1000);
            mGoodsFloatShowAnimator.setInterpolator(new LinearInterpolator());
            mGoodsFloatShowAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mGoodsFloatShowAnimator2 == null) {
                        mGoodsFloatShowAnimator2 = ObjectAnimator.ofFloat(mTvGoodsFloat, "translationX", mDp20, 0);
                        mGoodsFloatShowAnimator2.setDuration(1500);
                        mGoodsFloatShowAnimator2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (mGoodsFloatHideAnimator != null && mTvGoodsFloat != null) {
                                    mGoodsFloatHideAnimator.setFloatValues(0, -mDp10 - mTvGoodsFloat.getWidth());
                                    mGoodsFloatHideAnimator.start();
                                }
                            }
                        });
                    }
                    mGoodsFloatShowAnimator2.start();
                }
            });
        }
        mGoodsFloatShowAnimator.start();


    }

}
