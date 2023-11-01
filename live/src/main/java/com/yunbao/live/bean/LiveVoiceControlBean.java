package com.yunbao.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class LiveVoiceControlBean {

    private String mUid;
    private String mUserName;
    private String mAvatar;
    private int mSex;
    private int mLevel;
    private int mPosition;
    private int mStatus; //麦位的状态 -1 关麦；  0无人； 1开麦 ； 2 禁麦；

//    public static final int VOICE_CTRL_EMPTY = 0;//无人且麦位可用
//    public static final int VOICE_CTRL_BAN = 2;//无人且麦位不可用，被禁了
//    public static final int VOICE_CTRL_CLOSE = -1;//有人且被关麦，无法说话
//    public static final int VOICE_CTRL_OPEN = 1;//有人且正常说话


    @JSONField(name = "id")
    public String getUid() {
        return mUid;
    }
    @JSONField(name = "id")
    public void setUid(String uid) {
        mUid = uid;
    }

    @JSONField(name = "user_nicename")
    public String getUserName() {
        return mUserName;
    }

    @JSONField(name = "user_nicename")
    public void setUserName(String userName) {
        mUserName = userName;
    }

    @JSONField(name = "avatar")
    public String getAvatar() {
        return mAvatar;
    }

    @JSONField(name = "avatar")
    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    @JSONField(name = "sex")
    public int getSex() {
        return mSex;
    }

    @JSONField(name = "sex")
    public void setSex(int sex) {
        mSex = sex;
    }

    @JSONField(name = "level")
    public int getLevel() {
        return mLevel;
    }

    @JSONField(name = "level")
    public void setLevel(int level) {
        mLevel = level;
    }

    @JSONField(name = "position")
    public int getPosition() {
        return mPosition;
    }

    @JSONField(name = "position")
    public void setPosition(int position) {
        mPosition = position;
    }

    @JSONField(name = "mic_status")
    public int getStatus() {
        return mStatus;
    }

    @JSONField(name = "mic_status")
    public void setStatus(int status) {
        mStatus = status;
    }
}
