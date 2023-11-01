package com.yunbao.beauty.bean;

public class MeiYanFilterBean {
    private int mName;
    private int mThumb;
    private int mFilterRes;
    private boolean mChecked;

    public MeiYanFilterBean(int name, int thumb, int filterRes) {
        mName = name;
        mThumb = thumb;
        mFilterRes = filterRes;
    }

    public MeiYanFilterBean(int name, int thumb, int filterRes, boolean checked) {
        this(name, thumb, filterRes);
        mChecked = checked;
    }


    public int getName() {
        return mName;
    }

    public int getThumb() {
        return mThumb;
    }

    public int getFilterRes() {
        return mFilterRes;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public boolean isChecked() {
        return mChecked;
    }


}
