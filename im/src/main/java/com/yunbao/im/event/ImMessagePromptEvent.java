package com.yunbao.im.event;

public class ImMessagePromptEvent {

    private String mToUid;
    private int mMsgId;
    private boolean mSelf;//是否是自己撤回的

    public ImMessagePromptEvent(String toUid, int msgId, boolean self) {
        mToUid = toUid;
        mMsgId = msgId;
        mSelf = self;
    }

    public String getToUid() {
        return mToUid;
    }

    public void setToUid(String toUid) {
        mToUid = toUid;
    }

    public int getMsgId() {
        return mMsgId;
    }

    public void setMsgId(int msgId) {
        mMsgId = msgId;
    }

    public boolean isSelf() {
        return mSelf;
    }

    public void setSelf(boolean self) {
        mSelf = self;
    }
}
