package com.yunbao.main.event;

public class ActiveLikeEvent {
    private int mFrom;
    private String mActiveId;
    private int mIsLike;//是否点赞了
    private int mLikeNum;//点赞数


    public ActiveLikeEvent(int from, String activeId, int isLike, int likeNum) {
        mFrom = from;
        mActiveId = activeId;
        mIsLike = isLike;
        mLikeNum = likeNum;
    }


    public int getFrom() {
        return mFrom;
    }

    public String getActiveId() {
        return mActiveId;
    }

    public int getIsLike() {
        return mIsLike;
    }

    public int getLikeNum() {
        return mLikeNum;
    }
}

