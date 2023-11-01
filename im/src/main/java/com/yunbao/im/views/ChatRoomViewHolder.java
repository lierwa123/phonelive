package com.yunbao.im.views;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.custom.InterceptFrameLayout;
import com.yunbao.common.event.FollowEvent;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.DateFormatUtil;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.common.views.InputViewHolder;
import com.yunbao.im.R;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.adapter.ImRoomAdapter;
import com.yunbao.im.bean.ImMessageBean;
import com.yunbao.im.custom.MyImageView;
import com.yunbao.im.dialog.ChatImageDialog;
import com.yunbao.im.event.ImMessagePromptEvent;
import com.yunbao.im.http.ImHttpUtil;
import com.yunbao.im.interfaces.ChatRoomActionListener;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.im.utils.MediaRecordUtil;
import com.yunbao.im.utils.VoiceMediaPlayerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;


/**
 * Created by cxf on 2018/10/24.
 */

public class ChatRoomViewHolder extends AbsViewHolder implements View.OnClickListener, ImRoomAdapter.ActionListener {

    private RecyclerView mRecyclerView;
    private ImRoomAdapter mAdapter;
    private TextView mTitleView;
    private UserBean mUserBean;
    private String mToUid;
    private ChatRoomActionListener mActionListener;
    private ImMessageBean mCurMessageBean;
    private long mLastSendTime;//上一次发消息的时间
    private HttpCallback mCheckBlackCallback;
    private ChatImageDialog mChatImageDialog;//图片预览弹窗
    private boolean mFollowing;
    private View mFollowGroup;
    private MediaRecordUtil mMediaRecordUtil;
    private File mRecordVoiceFile;//录音文件
    private long mRecordVoiceDuration;//录音时长
    private Handler mHandler;
    private VoiceMediaPlayerUtil mVoiceMediaPlayerUtil;
    private View mGroupVoiceTip;

    private InputViewHolder mInputViewHolder;

    public ChatRoomViewHolder(Context context, ViewGroup parentView, UserBean userBean, boolean following) {
        super(context, parentView, userBean, following);
    }

    @Override
    protected void processArguments(Object... args) {
        mUserBean = (UserBean) args[0];
        mFollowing = (boolean) args[1];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_chat_room;
    }

    @Override
    public void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTitleView = (TextView) findViewById(R.id.titleView);
        mFollowGroup = findViewById(R.id.btn_follow_group);
        if (!mFollowing) {
            mFollowGroup.setVisibility(View.VISIBLE);
            mFollowGroup.findViewById(R.id.btn_close_follow).setOnClickListener(this);
            mFollowGroup.findViewById(R.id.btn_follow).setOnClickListener(this);
        }
        findViewById(R.id.btn_back).setOnClickListener(this);
        mCheckBlackCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                processCheckBlackData(code, msg, info);
            }
        };
        mGroupVoiceTip = findViewById(R.id.group_voice_record_tip);
        EventBus.getDefault().register(this);
        mHandler = new Handler();
        findViewById(R.id.btn_user_home).setOnClickListener(this);

        mInputViewHolder = new InputViewHolder(mContext,
                (ViewGroup) findViewById(R.id.input_container),
                false, R.layout.view_input_top_msg, R.layout.view_input_face
        );
        mInputViewHolder.addToParent();
        mInputViewHolder.subscribeActivityLifeCycle();
        View moreView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_more, null, false);
        moreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(120)));
        moreView.findViewById(R.id.btn_img).setOnClickListener(this);
        moreView.findViewById(R.id.btn_camera).setOnClickListener(this);
        moreView.findViewById(R.id.btn_voice).setOnClickListener(this);
        moreView.findViewById(R.id.btn_location).setOnClickListener(this);
        mInputViewHolder.addMoreView(moreView);
        mInputViewHolder.setActionListener(new InputViewHolder.ActionListener() {
            @Override
            public void onSendClick(String text) {
                sendText(text);
            }

            @Override
            public void onVoiceRecordStart() {
                startRecordVoice();
            }

            @Override
            public void onVoiceRecordEnd() {
                stopRecordVoice();
            }

            @Override
            public void onVoiceRecordCancel() {
                cancelRecordVoice();
            }
        });
        InterceptFrameLayout groupIntercept = findViewById(R.id.group_intercept);
        groupIntercept.setOnInterceptListener(new InterceptFrameLayout.OnInterceptListener() {
            @Override
            public boolean onInterceptCall() {
                if (mInputViewHolder != null) {
                    return mInputViewHolder.hideKeyBoardFaceMore();
                }
                return false;
            }
        });
    }


    public void loadData() {
        if (mUserBean == null) {
            return;
        }
        mToUid = mUserBean.getId();
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        mTitleView.setText(mUserBean.getUserNiceName());
        mAdapter = new ImRoomAdapter(mContext, mToUid, mUserBean);
        mAdapter.setActionListener(this);
        mRecyclerView.setAdapter(mAdapter);
        ImMessageUtil.getInstance().getChatMessageList(mToUid, new CommonCallback<List<ImMessageBean>>() {
            @Override
            public void callback(List<ImMessageBean> list) {
                mAdapter.setList(list);
                mAdapter.scrollToBottom();
            }
        });
    }


    public void setActionListener(ChatRoomActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void onKeyBoardHeightChanged(int keyboardHeight) {
        if (mAdapter != null) {
            mAdapter.scrollToBottom();
        }
        if (mInputViewHolder != null) {
            mInputViewHolder.onKeyBoardHeightChanged(keyboardHeight);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_back) {
            back();

        } else if (i == R.id.btn_img) {
            if (mActionListener != null) {
                mActionListener.onChooseImageClick();
            }

        } else if (i == R.id.btn_camera) {
            if (mActionListener != null) {
                mActionListener.onCameraClick();
            }

        } else if (i == R.id.btn_voice) {
            if (mActionListener != null) {
                mActionListener.onVoiceInputClick();
            }

        } else if (i == R.id.btn_location) {
            if (mActionListener != null) {
                mActionListener.onLocationClick();
            }

        } else if (i == R.id.btn_close_follow) {
            closeFollow();

        } else if (i == R.id.btn_follow) {
            follow();

        } else if (i == R.id.btn_user_home) {
            if (mContext instanceof ChatRoomActivity && ((ChatRoomActivity) mContext).isFromUserHome()) {
                ((ChatRoomActivity) mContext).superBackPressed();
            } else {
                RouteUtil.forwardUserHome(mContext, mToUid);
            }
        }
    }

    /**
     * 关闭关注提示
     */
    private void closeFollow() {
        if (mFollowGroup != null && mFollowGroup.getVisibility() == View.VISIBLE) {
            mFollowGroup.setVisibility(View.GONE);
        }
    }

    /**
     * 关注
     */
    private void follow() {
        CommonHttpUtil.setAttention(mToUid, null);
    }

    /**
     * 返回
     */
    public void back() {
        if (mInputViewHolder != null && mInputViewHolder.hideKeyBoardFaceMore()) {
            return;
        }
        if (mActionListener != null) {
            mActionListener.onCloseClick();
        }
    }


    /**
     * 释放资源
     */
    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        if (mMediaRecordUtil != null) {
            mMediaRecordUtil.release();
        }
        mMediaRecordUtil = null;
        if (mVoiceMediaPlayerUtil != null) {
            mVoiceMediaPlayerUtil.destroy();
        }
        mVoiceMediaPlayerUtil = null;
        if (mAdapter != null) {
            mAdapter.release();
        }
        ImMessageUtil.getInstance().refreshAllUnReadMsgCount();
        EventBus.getDefault().unregister(this);
        mActionListener = null;
        if (mChatImageDialog != null) {
            mChatImageDialog.dismiss();
        }
        mChatImageDialog = null;
    }

    /**
     * 点击图片的回调，显示图片
     */
    @Override
    public void onImageClick(MyImageView imageView, int x, int y) {
        if (mAdapter == null || imageView == null) {
            return;
        }
        File imageFile = imageView.getFile();
        ImMessageBean imMessageBean = imageView.getImMessageBean();
        if (imageFile != null && imMessageBean != null) {
            mChatImageDialog = new ChatImageDialog();
            mChatImageDialog.setImageInfo(mAdapter.getChatImageBean(imMessageBean), x, y, imageView.getWidth(), imageView.getHeight(), imageView.getDrawable());
            mChatImageDialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "ChatImageDialog2");
        }
    }


    /**
     * 点击语音消息的回调，播放语音
     */
    @Override
    public void onVoiceStartPlay(File voiceFile) {
        if (mVoiceMediaPlayerUtil == null) {
            mVoiceMediaPlayerUtil = new VoiceMediaPlayerUtil(mContext);
            mVoiceMediaPlayerUtil.setActionListener(new VoiceMediaPlayerUtil.ActionListener() {
                @Override
                public void onPlayEnd() {
                    if (mAdapter != null) {
                        mAdapter.stopVoiceAnim();
                    }
                }
            });
        }
        mVoiceMediaPlayerUtil.startPlay(voiceFile.getAbsolutePath());
    }

    /**
     * 点击语音消息的回调，停止播放语音
     */
    @Override
    public void onVoiceStopPlay() {
        if (mVoiceMediaPlayerUtil != null) {
            mVoiceMediaPlayerUtil.stopPlay();
        }
    }


    /**
     * 开始录音
     */
    private void startRecordVoice() {
        if (mGroupVoiceTip != null && mGroupVoiceTip.getVisibility() != View.VISIBLE) {
            mGroupVoiceTip.setVisibility(View.VISIBLE);
        }
        if (mMediaRecordUtil == null) {
            mMediaRecordUtil = new MediaRecordUtil();
        }
        File dir = new File(CommonAppConfig.MUSIC_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mRecordVoiceFile = new File(dir, DateFormatUtil.getCurTimeString() + ".m4a");
        mMediaRecordUtil.startRecord(mRecordVoiceFile.getAbsolutePath());
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopRecordVoice();
                }
            }, 60000);
        }
    }

    /**
     * 结束录音
     */
    private void stopRecordVoice() {
        if (mGroupVoiceTip != null && mGroupVoiceTip.getVisibility() == View.VISIBLE) {
            mGroupVoiceTip.setVisibility(View.INVISIBLE);
        }
        mRecordVoiceDuration = mMediaRecordUtil.stopRecord();
        if (mRecordVoiceDuration < 2000) {
            ToastUtil.show(WordUtil.getString(R.string.im_record_audio_too_short));
            deleteVoiceFile();
        } else {
            ImMessageBean msgBean = ImMessageUtil.getInstance().createVoiceMessage(mToUid, mRecordVoiceFile, mRecordVoiceDuration);
            if (msgBean != null) {
                mCurMessageBean = msgBean;
                sendMessage();
            } else {
                ToastUtil.show(R.string.im_msg_send_failed);
                deleteVoiceFile();
            }
        }
    }


    /**
     * 取消录音
     */
    private void cancelRecordVoice() {
        if (mGroupVoiceTip != null && mGroupVoiceTip.getVisibility() == View.VISIBLE) {
            mGroupVoiceTip.setVisibility(View.INVISIBLE);
        }
        if (mMediaRecordUtil != null) {
            mMediaRecordUtil.stopRecord();
        }
        deleteVoiceFile();
    }

    /**
     * 删除录音文件
     */
    private void deleteVoiceFile() {
        if (mRecordVoiceFile != null && mRecordVoiceFile.exists()) {
            mRecordVoiceFile.delete();
        }
        mRecordVoiceFile = null;
        mRecordVoiceDuration = 0;
    }

    /**************************************************************************************************/
    /*********************************以上是处理界面逻辑，以下是处理消息逻辑***********************************/
    /**************************************************************************************************/

    /**
     * 刷新最后一条聊天数据
     */
    public void refreshLastMessage() {
        if (mAdapter != null) {
            ImMessageBean bean = mAdapter.getLastMessage();
            if (bean != null) {
                ImMessageUtil.getInstance().refreshLastMessage(mToUid, bean);
            }
        }
    }


    /**
     * 收到消息的回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImMessageBean(ImMessageBean bean) {
        if (!bean.getUid().equals(mToUid)) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.insertItem(bean);
            ImMessageUtil.getInstance().markAllMessagesAsRead(mToUid, false);
        }
    }

    /**
     * 撤回消息的回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImMessagePromptEvent(ImMessagePromptEvent e) {
        String toUid = e.getToUid();
        if (TextUtils.isEmpty(toUid) || !toUid.equals(mToUid)) {
            return;
        }
        if (mChatImageDialog != null) {
            mChatImageDialog.dismiss();
        }
        mChatImageDialog = null;
        if (mAdapter != null) {
            mAdapter.onPromptMessage(e.getMsgId());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (e.getToUid().equals(mToUid)) {
            if (mFollowGroup != null) {
                if (e.getIsAttention() == 1) {
                    if (mFollowGroup.getVisibility() == View.VISIBLE) {
                        mFollowGroup.setVisibility(View.GONE);
                    }
                    ToastUtil.show(R.string.im_follow_tip_2);
                } else {
                    if (mFollowGroup.getVisibility() != View.VISIBLE) {
                        mFollowGroup.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


    /**
     * 检查是否能够发送消息
     */
    private boolean isCanSendMsg() {
        if (!CommonAppConfig.getInstance().isLoginIM()) {
            ToastUtil.show("IM暂未接入，无法使用");
            return false;
        }
        long curTime = System.currentTimeMillis();
        if (curTime - mLastSendTime < 1500) {
            ToastUtil.show(R.string.im_send_too_fast);
            return false;
        }
        mLastSendTime = curTime;
        return true;
    }

    /**
     * 发送文本信息
     */
    public void sendText(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(R.string.content_empty);
            return;
        }
        ImMessageBean messageBean = ImMessageUtil.getInstance().createTextMessage(mToUid, content);
        if (messageBean == null) {
            ToastUtil.show(R.string.im_msg_send_failed);
            return;
        }
        mCurMessageBean = messageBean;
        sendMessage();
    }


    /**
     * 发送图片消息
     */
    public void sendImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ImMessageBean messageBean = ImMessageUtil.getInstance().createImageMessage(mToUid, path);
        if (messageBean == null) {
            ToastUtil.show(R.string.im_msg_send_failed);
            return;
        }
        mCurMessageBean = messageBean;
        sendMessage();
    }

    /**
     * 发送位置消息
     */
    public void sendLocation(double lat, double lng, int scale, String address) {
        ImMessageBean messageBean = ImMessageUtil.getInstance().createLocationMessage(mToUid, lat, lng, scale, address);
        if (messageBean == null) {
            ToastUtil.show(R.string.im_msg_send_failed);
            return;
        }
        mCurMessageBean = messageBean;
        sendMessage();
    }


    /**
     * 发送消息
     */
    private void sendMessage() {
        if (!isCanSendMsg()) {
            return;
        }
        if (mCurMessageBean != null) {
            ImHttpUtil.checkBlack(mToUid, mCheckBlackCallback);
        } else {
            ToastUtil.show(R.string.im_msg_send_failed);
        }
    }

    /**
     * 处理拉黑接口返回的数据
     */
    private void processCheckBlackData(int code, String msg, String[] info) {
        if (code == 0) {
            if (info.length > 0) {
                JSONObject obj = JSON.parseObject(info[0]);
                int t2u = obj.getIntValue("t2u");
                if (1 == t2u) {//被拉黑
                    ToastUtil.show(R.string.im_you_are_blacked);
                    if (mCurMessageBean != null) {
                        ImMessageUtil.getInstance().removeMessage(mToUid, mCurMessageBean);
                    }
                } else {
                    if (mCurMessageBean != null) {
                        if (mCurMessageBean.getType() == ImMessageBean.TYPE_TEXT) {
                            if (mInputViewHolder != null) {
                                mInputViewHolder.clearEditText();
                            }
                        }
                        if (mAdapter != null) {
                            mAdapter.insertSelfItem(mCurMessageBean);
                        }
                    } else {
                        ToastUtil.show(WordUtil.getString(R.string.im_msg_send_failed));
                    }
                }
            }
        } else {
            ToastUtil.show(msg);
        }
    }


    public void onPause() {
        if (mVoiceMediaPlayerUtil != null) {
            mVoiceMediaPlayerUtil.pausePlay();
        }
    }

    public void onResume() {
        if (mVoiceMediaPlayerUtil != null) {
            mVoiceMediaPlayerUtil.resumePlay();
        }
    }


}
