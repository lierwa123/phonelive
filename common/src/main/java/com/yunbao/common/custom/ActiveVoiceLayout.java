package com.yunbao.common.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.R;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.StringUtil;

import java.io.File;

public class ActiveVoiceLayout extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    private float mScale;
    private ImageView mImageView;
    private TextView mTextView;
    private Drawable[] mDrawables;
    private boolean mPlaying;
    private Handler mHandler;
    private int mSeconds;
    private int mSecondsMax;
    private int mImageIndex;
    private String mVoiceUrl;
    private ActionListener mActionListener;


    public ActiveVoiceLayout(Context context) {
        this(context, null);
    }

    public ActiveVoiceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActiveVoiceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        mDrawables = new Drawable[3];
        mDrawables[0] = ContextCompat.getDrawable(context, R.mipmap.icon_active_voice_anim_0);
        mDrawables[1] = ContextCompat.getDrawable(context, R.mipmap.icon_active_voice_anim_1);
        mDrawables[2] = ContextCompat.getDrawable(context, R.mipmap.icon_active_voice_anim_2);
        setOnClickListener(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImageView = new ImageView(mContext);
        LayoutParams params1 = new LayoutParams(dp2px(120), dp2px(22));
        params1.gravity = Gravity.CENTER_VERTICAL;
        params1.leftMargin = dp2px(15);
        mImageView.setLayoutParams(params1);
        mImageView.setImageDrawable(mDrawables[0]);
        addView(mImageView);

        mTextView = new TextView(mContext);
        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        params2.rightMargin = dp2px(13);
        mTextView.setLayoutParams(params2);
        mTextView.setTextColor(0xffffffff);
        mTextView.setTextSize(12);
        addView(mTextView);
    }


    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if (mPlaying) {
            stopPlay();
        } else {
            starPlay();
        }
    }

    private long getNextTime(int time) {
        long now = SystemClock.uptimeMillis();
        if (time < 1000) {
            return now + time;
        }
        return now + time + -now % 1000;
    }

    private void changeAnim() {
        mImageIndex++;
        if (mImageIndex >= 3) {
            mImageIndex = 0;
        }
        if (mImageView != null) {
            mImageView.setImageDrawable(mDrawables[mImageIndex]);
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageAtTime(0, getNextTime(200));
        }
    }


    private void changeSeconds() {
        mSeconds--;
        if (mTextView != null) {
            mTextView.setText(mSeconds + "s");
        }
        if (mSeconds > 0 && mHandler != null) {
            mHandler.sendEmptyMessageAtTime(1, getNextTime(1000));
        }
    }

    public void starPlay() {
        File voiceFile = null;
        if (TextUtils.isEmpty(mVoiceUrl)) {
            if (mActionListener != null) {
                voiceFile = mActionListener.getLocalFile();
            }
            if (voiceFile == null || !voiceFile.exists()) {
                return;
            }
        } else {
            voiceFile = new File(StringUtil.contact(CommonAppConfig.MUSIC_PATH, MD5Util.getMD5(mVoiceUrl)));
            if (!voiceFile.exists()) {
                if (mActionListener != null) {
                    mActionListener.onNeedDownload(this, mVoiceUrl);
                }
                return;
            }
        }
        mPlaying = true;
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            changeAnim();
                            break;
                        case 1:
                            changeSeconds();
                            break;
                    }
                }
            };
        }
        mSeconds = mSecondsMax;
        mHandler.sendEmptyMessageAtTime(0, getNextTime(200));
        mHandler.sendEmptyMessageAtTime(1, getNextTime(1000));
        if (mActionListener != null) {
            mActionListener.onPlayStart(this, voiceFile);
        }
    }


    public void stopPlay() {
        mPlaying = false;
        mImageIndex = 0;
        mSeconds = mSecondsMax;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mTextView != null) {
            mTextView.setText(mSecondsMax + "s");
        }
        if (mImageView != null) {
            mImageView.setImageDrawable(mDrawables[mImageIndex]);
        }
        if (mActionListener != null) {
            mActionListener.onPlayStop();
        }
    }

    public boolean isPlaying() {
        return mPlaying;
    }


    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mImageView != null) {
            mImageView.setImageDrawable(null);
        }
        mActionListener = null;
    }


    public void setSecondsMax(int secondsMax) {
        mSecondsMax = secondsMax;
        if (mTextView != null) {
            mTextView.setText(mSecondsMax + "s");
        }
    }


    public void setVoiceUrl(String voiceUrl) {
        mVoiceUrl = voiceUrl;
    }

    public String getVoiceUrl() {
        return mVoiceUrl;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {

        void onPlayStart(ActiveVoiceLayout voiceLayout, File voiceFile);

        void onPlayStop();

        void onNeedDownload(ActiveVoiceLayout voiceLayout, String url);

        File getLocalFile();
    }

}
