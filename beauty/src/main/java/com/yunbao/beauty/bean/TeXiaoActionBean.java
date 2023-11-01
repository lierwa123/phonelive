package com.yunbao.beauty.bean;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.yunbao.beauty.utils.MhDataManager;

public class TeXiaoActionBean {

    private int mName;
    private int mThumb0;
    private int mThumb1;
    private boolean mChecked;
    private Drawable mDrawable0;
    private Drawable mDrawable1;
    private String mUrl;
    private int mAction;
    private String stickerName;

    public TeXiaoActionBean(int name, int thumb0, int thumb1,String url,int action) {
        mName = name;
        mThumb0 = thumb0;
        mThumb1 = thumb1;
        mUrl = url;
        mAction = action;
    }

    public TeXiaoActionBean(int name, int thumb0, int thumb1, boolean checked) {
        this(name, thumb0, thumb1,"",0);
        mChecked = checked;
    }

    public int getName() {
        return mName;
    }

    public int getThumb0() {
        return mThumb0;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public Drawable getDrawable0() {
        if (mDrawable0 == null) {
            mDrawable0 = ContextCompat.getDrawable(MhDataManager.getInstance().getContext(), mThumb0);
        }
        return mDrawable0;
    }


    public Drawable getDrawable1() {
        if (mDrawable1 == null) {
            mDrawable1 = ContextCompat.getDrawable(MhDataManager.getInstance().getContext(), mThumb1);
        }
        return mDrawable1;
    }


    public String getUrl() {
        return mUrl;
    }

    public int getAction() {
        return mAction;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public String getStickerName() {
        return stickerName;
    }
}
