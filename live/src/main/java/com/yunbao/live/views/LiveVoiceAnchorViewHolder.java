package com.yunbao.live.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.activity.LiveAnchorActivity;

/**
 * Created by cxf on 2018/10/9.
 * 主播直播间逻辑btn_function
 */

public class LiveVoiceAnchorViewHolder extends AbsLiveViewHolder {

    private ImageView mBtnFunction;
//    private Drawable mDrawable0;
//    private Drawable mDrawable1;
    private View mApplyUpMicTip;


    public LiveVoiceAnchorViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_anchor_voice;
    }

    @Override
    public void init() {
        super.init();
//        mDrawable0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_func_0);
//        mDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_func_1);
        mBtnFunction = (ImageView) findViewById(R.id.btn_function);
//        mBtnFunction.setImageDrawable(mDrawable0);
        mBtnFunction.setOnClickListener(this);
        findViewById(R.id.btn_gift).setOnClickListener(this);
        findViewById(R.id.btn_mic_apply).setOnClickListener(this);
        findViewById(R.id.btn_mic_control).setOnClickListener(this);
        mApplyUpMicTip = findViewById(R.id.tip_mic_apply);
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_function) {
            showFunctionDialog();
        } else if (i == R.id.btn_gift) {
            ((LiveActivity) mContext).openGiftWindow();
        } else if (i == R.id.btn_mic_apply) {
            ((LiveActivity) mContext).voiceApplyUpMic();
            setApplyUpMicTipShow(false);
        } else if (i == R.id.btn_mic_control) {
            ((LiveAnchorActivity) mContext).controlMic();
        }


    }


    /**
     * 显示功能弹窗
     */
    private void showFunctionDialog() {
//        if (mBtnFunction != null) {
//            mBtnFunction.setImageDrawable(mDrawable1);
//        }
        ((LiveAnchorActivity) mContext).showFunctionDialog();
    }

    /**
     * 设置功能按钮变暗
     */
    public void setBtnFunctionDark() {
//        if (mBtnFunction != null) {
//            mBtnFunction.setImageDrawable(mDrawable0);
//        }
    }

    /**
     * 显示或隐藏 上麦申请的红点提示
     */
    public void setApplyUpMicTipShow(boolean show) {
        if (mApplyUpMicTip != null) {
            if (show) {
                if (mApplyUpMicTip.getVisibility() != View.VISIBLE) {
                    mApplyUpMicTip.setVisibility(View.VISIBLE);
                }
            } else {
                if (mApplyUpMicTip.getVisibility() == View.VISIBLE) {
                    mApplyUpMicTip.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}
