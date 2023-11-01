package com.yunbao.live.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.live.TXLivePusher;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.Constants;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveAudienceActivity;
import com.yunbao.live.adapter.LiveVoiceLinkMicAdapter;
import com.yunbao.live.bean.LiveVoiceGiftBean;
import com.yunbao.live.bean.LiveVoiceLinkMicBean;
import com.yunbao.live.interfaces.LivePushListener;

import java.util.ArrayList;
import java.util.List;

public class LiveVoiceLinkMicViewHolder extends AbsViewHolder implements ITXLivePushListener {

    private static final int USER_COUNT = 8;
    private List<LiveVoiceLinkMicBean> mList;
    private LiveVoiceLinkMicAdapter mAdapter;
    private TXLivePusher mLivePusher;
    private LivePushListener mLivePushListener;
    private boolean mStartPush;
    private TXLivePlayer[] mLivePlayerArr;
    private Handler mHandler;
    private boolean mPushMute;
    private boolean mPaused;


    public LiveVoiceLinkMicViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_voice_link_mic;
    }

    @Override
    public void init() {
        mList = new ArrayList<>();
        mLivePlayerArr = new TXLivePlayer[USER_COUNT];
        for (int i = 0; i < USER_COUNT; i++) {
            mList.add(new LiveVoiceLinkMicBean());
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mAdapter = new LiveVoiceLinkMicAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);
        if (mContext instanceof LiveAudienceActivity) {
            findViewById(R.id.voice_link_mic_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LiveAudienceActivity) mContext).light();
                }
            });
        }
    }


    /**
     * 用户上麦
     *
     * @param toUid    上麦人员的uid
     * @param toName   上麦人员的name
     * @param toAvatar 上麦人员的头像
     * @param position 上麦人员的位置
     */
    public void onUserUpMic(String toUid, String toName, String toAvatar, int position) {
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        LiveVoiceLinkMicBean bean = mList.get(position);
        bean.setUid(toUid);
        bean.setUserName(toName);
        bean.setAvatar(toAvatar);
        bean.setStatus(Constants.VOICE_CTRL_OPEN);
        bean.setFaceIndex(-1);
        bean.setUserStream(null);
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
        if (mHandler != null) {
            mHandler.removeMessages(position);
        }
    }


    /**
     * 用户下麦
     *
     * @param uid 下麦人员的uid
     */
    public void onUserDownMic(String uid) {
        onUserDownMic(getUserPosition(uid));
    }

    /**
     * 用户下麦
     *
     * @param position 下麦人员的position
     */
    public void onUserDownMic(int position) {
        if (position >= 0 && position < USER_COUNT) {
            LiveVoiceLinkMicBean bean = mList.get(position);
            bean.setUid(null);
            bean.setUserName(null);
            bean.setAvatar(null);
            bean.setStatus(Constants.VOICE_CTRL_EMPTY);
            bean.setFaceIndex(-1);
            bean.setUserStream(null);
            if (mAdapter != null) {
                mAdapter.notifyItemChanged(position);
            }
            if (mHandler != null) {
                mHandler.removeMessages(position);
            }
        }
    }


    /**
     * 语音聊天室--主播控制麦位 闭麦开麦禁麦等
     *
     * @param position 麦位
     * @param status   麦位的状态 -1 关麦；  0无人； 1开麦 ； 2 禁麦；
     */
    public void onControlMicPosition(int position, int status) {
        LiveVoiceLinkMicBean bean = mList.get(position);
        bean.setStatus(status);
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position, Constants.PAYLOAD);
        }
    }

    /**
     * 语音聊天室--收到上麦观众发送表情的消息
     *
     * @param uid       上麦观众的uid
     * @param faceIndex 表情标识
     */
    public void onVoiceRoomFace(String uid, int faceIndex) {
        int position = getUserPosition(uid);
        if (position >= 0 && position < USER_COUNT) {
            LiveVoiceLinkMicBean bean = mList.get(position);
            bean.setFaceIndex(faceIndex);
            if (mAdapter != null) {
                mAdapter.notifyItemChanged(position, Constants.VOICE_FACE);
            }
            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int pos = msg.what;
                        LiveVoiceLinkMicBean bean0 = mList.get(pos);
                        bean0.setFaceIndex(-1);
                        if (mAdapter != null) {
                            mAdapter.notifyItemChanged(pos, Constants.VOICE_FACE);
                        }
                    }
                };
            } else {
                mHandler.removeMessages(position);
            }
            mHandler.sendEmptyMessageDelayed(position, 5000);
        }
    }

    /**
     * 设置静音
     */
    public void setPushMute(boolean pushMute) {
        if (mPushMute != pushMute) {
            mPushMute = pushMute;
            if (mLivePusher != null) {
                mLivePusher.setMute(pushMute);
            }
        }
    }

    /**
     * 开始推流
     */
    public void startPush(String pushUrl, LivePushListener pushListener) {
        mLivePushListener = pushListener;
        if (mLivePusher == null) {
            mLivePusher = new TXLivePusher(mContext);
            TXLivePushConfig livePushConfig = new TXLivePushConfig();
            livePushConfig.enableAEC(true);//设置回声消除
//            livePushConfig.enableANS(true);//设置噪声抑制
            livePushConfig.setVolumeType(TXLiveConstants.AUDIO_VOLUME_TYPE_MEDIA);//表示通话音量类型
//            livePushConfig.enablePureAudioPush(true);//启动纯音频推流
            mLivePusher.setConfig(livePushConfig);
            mLivePusher.setPushListener(this);
            mLivePusher.startCameraPreview((TXCloudVideoView) findViewById(R.id.camera_preview));
        }
        mLivePusher.setMute(false);
        mLivePusher.startPusher(pushUrl);
    }

    @Override
    public void onPushEvent(int e, Bundle bundle) {
        if (e == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            ToastUtil.show(R.string.live_push_failed_2);
        } else if (e == TXLiveConstants.PUSH_ERR_NET_DISCONNECT || e == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS) {
//            L.e(mTag, "网络断开，推流失败------>");

        } else if (e == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {//推流成功
//            L.e(mTag, "mStearm--->推流成功");
            if (!mStartPush) {
                mStartPush = true;
                if (mLivePushListener != null) {
                    mLivePushListener.onPushStart();
                }
            }
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    /**
     * 停止推流
     */
    public void stopPush() {
        mStartPush = false;
        if (mLivePusher != null) {
            mLivePusher.stopPusher();
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay(String uid) {
        stopPlay(getUserPosition(uid));
    }


    /**
     * 停止播放
     */
    public void stopPlay(int position) {
        if (position >= 0 && position < USER_COUNT) {
            TXLivePlayer player = mLivePlayerArr[position];
            if (player != null && player.isPlaying()) {
                player.stopPlay(false);
            }
        }
    }

    /**
     * 停止所有播放
     */
    public void stopAllPlay() {
        for (TXLivePlayer player : mLivePlayerArr) {
            if (player != null && player.isPlaying()) {
                player.stopPlay(false);
            }
        }
    }


    /**
     * 语音聊天室--播放上麦观众的低延时流
     *
     * @param uid        上麦观众的uid
     * @param pull       上麦观众的低延时流地址
     * @param userStream 上麦观众的流名，主播混流用
     */
    public void playAccStream(String uid, String pull, String userStream) {
        int position = getUserPosition(uid);
        if (position >= 0 && position < USER_COUNT) {
            LiveVoiceLinkMicBean bean = mList.get(position);
            bean.setUserStream(userStream);
            TXLivePlayer player = mLivePlayerArr[position];
            if (player == null) {
                player = new TXLivePlayer(mContext);
                TXLivePlayConfig playConfig = new TXLivePlayConfig();
                playConfig.enableAEC(true);
                playConfig.setAutoAdjustCacheTime(true);
                playConfig.setMaxAutoAdjustCacheTime(1.0f);
                playConfig.setMinAutoAdjustCacheTime(1.0f);
                player.setConfig(playConfig);
                mLivePlayerArr[position] = player;
            }
            player.startPlay(pull, TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
        }

    }


    /**
     * 获取用户在麦上的位置
     */
    public int getUserPosition(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            for (int i = 0; i < USER_COUNT; i++) {
                LiveVoiceLinkMicBean bean = mList.get(i);
                if (uid.equals(bean.getUid())) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 获取用户
     */
    public LiveVoiceLinkMicBean getUserBean(int position) {
        if (position >= 0 && position < USER_COUNT) {
            return mList.get(position);
        }
        return null;
    }


    /**
     * 获取用户
     */
    public LiveVoiceLinkMicBean getUserBean(String toUid) {
        return getUserBean(getUserPosition(toUid));
    }


    /**
     * 主播混流时候获取上麦用户的Stream
     */
    public List<String> getUserStreamForMix() {
        List<String> list = null;
        for (int i = 0; i < USER_COUNT; i++) {
            String userStream = mList.get(i).getUserStream();
            if (!TextUtils.isEmpty(userStream)) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(userStream);
            }
        }
        return list;
    }

    /**
     * 显示房间用户数据
     */
    public void showUserList(JSONArray arr) {
        for (int i = 0; i < USER_COUNT; i++) {
            LiveVoiceLinkMicBean bean = mList.get(i);
            JSONObject obj = arr.getJSONObject(i);
            bean.setUid(obj.getString("id"));
            bean.setUserName(obj.getString("user_nicename"));
            bean.setAvatar(obj.getString("avatar"));
            bean.setStatus(obj.getIntValue("status"));
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    public List<LiveVoiceGiftBean> getVoiceGiftUserList() {
        List<LiveVoiceGiftBean> list = null;
        for (int i = 0; i < USER_COUNT; i++) {
            LiveVoiceLinkMicBean bean = mList.get(i);
            if (!bean.isEmpty()) {
                LiveVoiceGiftBean giftUserBean = new LiveVoiceGiftBean();
                giftUserBean.setUid(bean.getUid());
                giftUserBean.setAvatar(bean.getAvatar());
                giftUserBean.setType(i);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(giftUserBean);
            }
        }
        return list;
    }


    @Override
    public void release() {
        stopAllPlay();
        if (mLivePusher != null) {
            mLivePusher.stopPusher();
            mLivePusher.stopScreenCapture();
            mLivePusher.stopCameraPreview(false);
            mLivePusher.setPushListener(null);
        }
        mLivePusher = null;
        mLivePushListener = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        super.release();
    }

    @Override
    public void onPause() {
        if (!mPushMute && mLivePusher != null) {
            mLivePusher.setMute(true);
        }
        for (TXLivePlayer player : mLivePlayerArr) {
            if (player != null) {
                player.setMute(true);
            }
        }
        mPaused = true;
    }

    @Override
    public void onResume() {
        if (mPaused) {
            if (!mPushMute && mLivePusher != null) {
                mLivePusher.setMute(false);
            }
        }
        for (TXLivePlayer player : mLivePlayerArr) {
            if (player != null) {
                player.setMute(false);
            }
        }
        mPaused = false;
    }
}
