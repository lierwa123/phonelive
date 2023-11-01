package com.yunbao.live.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.yunbao.common.utils.L;
import com.yunbao.live.R;

/**
 * Created by cxf on 2018/10/10.
 * 直播间播放器  腾讯播放器
 */

public class LiveVoicePlayTxViewHolder extends LiveRoomPlayViewHolder {

    private static final String TAG = "LiveTxPlayViewHolder";

    private boolean mPaused;//是否切后台了
    private ViewGroup mContainer;
    private String mAnchorOriginUrl;//主播的最开始的播放地址

    public LiveVoicePlayTxViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_play_tx_voice;
    }

    @Override
    public void init() {
        mContainer = findViewById(R.id.voice_play_container);
    }

    public ViewGroup getContainer() {
        return mContainer;
    }


    @Override
    public void hideCover() {
    }

    @Override
    public void setCover(String coverUrl) {
    }


    /**
     * 暂停播放
     */
    @Override
    public void pausePlay() {

    }

    /**
     * 暂停播放后恢复
     */
    @Override
    public void resumePlay() {

    }

    /**
     * 开始播放
     *
     * @param url 流地址
     */
    @Override
    public void play(String url) {
        mAnchorOriginUrl = url;
        LiveVoicePlayUtil.getInstance().startPlay(url, false);
    }


    /**
     * 播放主播低延时流地址
     *
     * @param accPullUrl 低延时流地址，为空切回到普通流
     */
    public void changeAccStream(String accPullUrl) {
        if (TextUtils.isEmpty(accPullUrl)) {
            LiveVoicePlayUtil.getInstance().startPlay(mAnchorOriginUrl, false);
        } else {
            LiveVoicePlayUtil.getInstance().startPlay(accPullUrl, true);
        }
    }

    @Override
    public void stopPlay() {
        stopPlay2();
    }

    @Override
    public void stopPlay2() {
        LiveVoicePlayUtil.getInstance().stopPlay();
    }

    @Override
    public void release() {
        LiveVoicePlayUtil.getInstance().release();
        L.e(TAG, "release------->");
    }


    @Override
    public ViewGroup getSmallContainer() {
        return null;
    }


    @Override
    public ViewGroup getRightContainer() {
        return null;
    }

    @Override
    public ViewGroup getPkContainer() {
        return null;
    }

    @Override
    public void changeToLeft() {

    }

    @Override
    public void changeToBig() {

    }

    @Override
    public void onResume() {
//        if (mPaused) {
//            LiveVoicePlayUtil.getInstance().setMute(false);
//        }
//        mPaused = false;
    }

    @Override
    public void onPause() {
//        LiveVoicePlayUtil.getInstance().setMute(true);
//        mPaused = true;
    }

    @Override
    public void onDestroy() {
        release();
    }


}
