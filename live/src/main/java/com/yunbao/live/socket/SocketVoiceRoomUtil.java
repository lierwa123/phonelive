package com.yunbao.live.socket;

import android.text.TextUtils;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;

/**
 * Created by cxf on 2018/11/16.
 * 语音聊天室
 */

public class SocketVoiceRoomUtil {

    /**
     * 用户发起上麦申请
     */
    public static void applyMicUp(SocketClient client) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_VOICE_ROOM)
                .param("action", 1));
    }


    /**
     * 主播同意或拒绝用户的上麦申请
     *
     * @param position 上麦的位置 从0开始 -1表示拒绝
     */
    public static void handleMicUpApply(SocketClient client, UserBean bean, int position) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_VOICE_ROOM)
                .param("action", 2)
                .param("touid", bean.getId())
                .param("toname", bean.getUserNiceName())
                .param("avatar", bean.getAvatar())
                .param("position", position)
        );
    }

    /**
     * 观众下麦
     *
     * @param uid 下麦的人的uid
     */
    public static void userDownMic(SocketClient client, String uid, int type) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_VOICE_ROOM)
                .param("action", 3)
                .param("uid", uid)
                .param("type", type)
        );
    }


    /**
     * 主播控制麦位 闭麦开麦禁麦等
     *
     * @param uid      被操作人的uid
     * @param position 麦位
     * @param status   麦位的状态 -1 关麦；  0无人； 1开麦 ； 2 禁麦；
     */
    public static void controlMicPosition(SocketClient client, String uid, int position, int status) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_VOICE_ROOM)
                .param("action", 4)
                .param("uid", TextUtils.isEmpty(uid) ? "0" : uid)
                .param("position", position)
                .param("status", status)
        );
    }

    /**
     * 观众上麦后推流成功，把自己的播放地址广播给所有人
     */
    public static void userPushSuccess(SocketClient client, String pull, String userStream) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_VOICE_ROOM)
                .param("action", 5)
                .param("uid", CommonAppConfig.getInstance().getUid())
                .param("pull", pull)
                .param("user_stream", userStream)
        );
    }

    /**
     * 发送表情
     *
     * @param faceIndex 表情标识
     */
    public static void voiceRoomSendFace(SocketClient client, int faceIndex) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_VOICE_ROOM)
                .param("action", 6)
                .param("uid", CommonAppConfig.getInstance().getUid())
                .param("face", faceIndex));
    }


}
