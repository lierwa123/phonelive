package com.yunbao.live.bean;

public class LiveVoiceFaceBean {
    private int mIndex;
    private int mImageRes;

    public LiveVoiceFaceBean(int index, int imageRes) {
        mIndex = index;
        mImageRes = imageRes;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public int getImageRes() {
        return mImageRes;
    }

    public void setImageRes(int imageRes) {
        mImageRes = imageRes;
    }
}
