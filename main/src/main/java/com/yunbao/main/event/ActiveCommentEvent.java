package com.yunbao.main.event;

public class ActiveCommentEvent {
    private String mActiveId;
    private int mCommentNum;

    public ActiveCommentEvent(String activeId, int commentNum) {
        mActiveId = activeId;
        mCommentNum = commentNum;
    }

    public String getActiveId() {
        return mActiveId;
    }

    public void setActiveId(String videoId) {
        mActiveId = videoId;
    }

    public int getCommentNum() {
        return mCommentNum;
    }

    public void setCommentNum(int commentNum) {
        mCommentNum = commentNum;
    }
}
