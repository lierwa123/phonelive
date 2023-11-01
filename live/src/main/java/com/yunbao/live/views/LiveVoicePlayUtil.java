package com.yunbao.live.views;

import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.interfaces.AppLifecycleUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.floatwindow.FloatWindowUtil;

public class LiveVoicePlayUtil implements AppLifecycleUtil.LifecycleCallback {

    private static LiveVoicePlayUtil sInstance;
    private TXLivePlayer mPlayer;
    private boolean mKeepAlive;

    private LiveVoicePlayUtil() {
        AppLifecycleUtil.addLifecycleCallback(this);
    }

    public void setKeepAlive(boolean keepAlive) {
        mKeepAlive = keepAlive;
    }

    public static LiveVoicePlayUtil getInstance() {
        if (sInstance == null) {
            synchronized (LiveVoicePlayUtil.class) {
                if (sInstance == null) {
                    sInstance = new LiveVoicePlayUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 开始播放
     *
     * @param url   要播放的流地址
     * @param isAcc 是否是加速流
     */
    public void startPlay(String url, boolean isAcc) {
        if (mKeepAlive) {
            mKeepAlive = false;
            return;
        }
        if (mPlayer == null) {
            mPlayer = new TXLivePlayer(CommonAppContext.getInstance());
            TXLivePlayConfig playConfig = new TXLivePlayConfig();
            playConfig.setAutoAdjustCacheTime(true);
            playConfig.setMaxAutoAdjustCacheTime(1.0f);
            playConfig.setMinAutoAdjustCacheTime(1.0f);
            playConfig.setHeaders(CommonAppConfig.HEADER);
            mPlayer.setConfig(playConfig);
            mPlayer.setPlayListener(new ITXLivePlayListener() {
                @Override
                public void onPlayEvent(int e, Bundle bundle) {
                    switch (e) {//播放失败，断开连接
                        case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                        case TXLiveConstants.PLAY_EVT_PLAY_END:
                        case TXLiveConstants.PLAY_WARNING_DNS_FAIL:
                        case TXLiveConstants.PLAY_WARNING_SEVER_CONN_FAIL:
                        case TXLiveConstants.PLAY_WARNING_SHAKE_FAIL:
                            FloatWindowUtil.getInstance().release();
                            break;
                    }
                }

                @Override
                public void onNetStatus(Bundle bundle) {

                }
            });
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            int playType = -1;
            if (isAcc) {
                playType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC;
            } else {
                if (url.startsWith("rtmp://")) {
                    playType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
                } else if (url.contains(".flv")) {
                    playType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
                } else if (url.contains(".m3u8")) {
                    playType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
                } else if (url.contains(".mp4")) {
                    playType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
                }
            }
            if (playType == -1) {
                ToastUtil.show(R.string.live_play_error_2);
                return;
            }
            if (mPlayer != null) {
                mPlayer.stopPlay(false);
                mPlayer.startPlay(url, playType);
            }
//            L.e("LiveVoicePlayUtil", "play----url--->" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopPlay() {
        if (mKeepAlive) {
            return;
        }
        if (mPlayer != null) {
            try {
                mPlayer.stopPlay(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void release() {
        if (mKeepAlive) {
            return;
        }
        if (mPlayer != null) {
            try {
                mPlayer.stopPlay(false);
                mPlayer.setPlayListener(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mPlayer = null;
        mKeepAlive = false;
    }

    private void setMute(boolean mute) {
//        if (mKeepAlive) {
//            return;
//        }
        if (mPlayer != null) {
            try {
                mPlayer.setMute(mute);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onAppFrontGround() {
        setMute(false);
    }

    @Override
    public void onAppBackGround() {
        setMute(true);
    }
}
