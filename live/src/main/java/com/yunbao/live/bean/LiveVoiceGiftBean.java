package com.yunbao.live.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class LiveVoiceGiftBean {
    private String mUid;
    private String mAvatar;
    private int mType;//-2全麦 ,-1主持 ,>=0其他上麦观众
    private boolean mChecked;
    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }


    public Drawable getCheckedDrawable() {
        return mCheckedDrawable;
    }

    public Drawable getUnCheckedDrawable() {
        return mUnCheckedDrawable;
    }


    public void setDrawable(Context context, int checkedRes, int unCheckedRes) {
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedRes);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, unCheckedRes);
    }
}
