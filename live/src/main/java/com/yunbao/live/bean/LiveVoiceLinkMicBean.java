package com.yunbao.live.bean;

import android.text.TextUtils;

public class LiveVoiceLinkMicBean {
    private String mUid;
    private String mUserName;
    private String mAvatar;
    private int mStatus;//麦位的状态 -1 关麦；  0无人； 1开麦 ； 2 禁麦；
    private int mFaceIndex = -1;
    private String mUserStream;//上麦观众的流名，主播混流用

    public LiveVoiceLinkMicBean() {
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getFaceIndex() {
        return mFaceIndex;
    }

    public void setFaceIndex(int faceIndex) {
        mFaceIndex = faceIndex;
    }


    public String getUserStream() {
        return mUserStream;
    }

    public void setUserStream(String userStream) {
        mUserStream = userStream;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mUid) || "0".equals(mUid);
    }
}
