package com.yunbao.live.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.activity.LiveAudienceActivity;

/**
 * Created by cxf on 2018/10/9.
 * 观众直播间逻辑
 */

public class LiveVoiceAudienceViewHolder extends AbsLiveViewHolder {

    private ImageView mBtnFunction;
//    private Drawable mDrawable0;
//    private Drawable mDrawable1;
    private ImageView mBtnJoin;
    private ImageView mBtnMic;
    private Drawable mMicUp;//上麦图标
    private Drawable mMicDown;//下麦图标
    private View mGroupMic;
    private Drawable mDrawableMicOpen;//开麦
    private Drawable mDrawableMicClose;//关麦

    public LiveVoiceAudienceViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_audience_voice;
    }

    @Override
    public void init() {
        super.init();
//        mDrawable0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_func_0);
//        mDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_func_1);
        mMicUp = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_voice_join_1);
        mMicDown = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_voice_join_0);
        mDrawableMicOpen = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_mic_open);
        mDrawableMicClose = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_mic_close);
        mBtnFunction = (ImageView) findViewById(R.id.btn_function);
//        mBtnFunction.setImageDrawable(mDrawable0);
        mBtnFunction.setOnClickListener(this);
        mBtnJoin = findViewById(R.id.btn_join);
        mBtnMic = findViewById(R.id.btn_mic);
        mBtnJoin.setOnClickListener(this);
        mBtnMic.setOnClickListener(this);
        findViewById(R.id.btn_gift).setOnClickListener(this);
        findViewById(R.id.btn_face).setOnClickListener(this);
        mGroupMic = findViewById(R.id.group_mic);
    }


    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_join) {
            ((LiveAudienceActivity) mContext).clickVoiceUpMic();
        } else if (i == R.id.btn_mic) {
            ((LiveActivity) mContext).changeVoiceMicOpen(CommonAppConfig.getInstance().getUid());
        } else if (i == R.id.btn_face) {
            ((LiveAudienceActivity) mContext).openVoiceRoomFace();
        } else if (i == R.id.btn_gift) {
            ((LiveActivity) mContext).openGiftWindow();

        } else if (i == R.id.btn_function) {
            showFunctionDialog();
        }
    }

    /**
     * 显示功能弹窗
     */
    private void showFunctionDialog() {
//        if (mBtnFunction != null) {
//            mBtnFunction.setImageDrawable(mDrawable1);
//        }
        ((LiveAudienceActivity) mContext).showFunctionDialog();
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
     * 改变上麦下麦状态
     */
    public void changeMicUp(boolean isUpMic) {
        setVoiceMicClose(false);
        if (mBtnJoin != null) {
            mBtnJoin.setImageDrawable(isUpMic ? mMicDown : mMicUp);
        }
        if (mGroupMic != null) {
            if (isUpMic) {
                if (mGroupMic.getVisibility() != View.VISIBLE) {
                    mGroupMic.setVisibility(View.VISIBLE);
                }
            } else {
                if (mGroupMic.getVisibility() != View.GONE) {
                    mGroupMic.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 设置是否被关麦
     *
     * @param micClose
     */
    public void setVoiceMicClose(boolean micClose) {
        if (mBtnMic != null) {
            mBtnMic.setImageDrawable(micClose ? mDrawableMicClose : mDrawableMicOpen);
        }
    }


}
