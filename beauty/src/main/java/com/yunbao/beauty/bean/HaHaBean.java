package com.yunbao.beauty.bean;

public class HaHaBean {
    private int mId;
    private int mName;
    private int mThumb;
    private boolean mChecked;

    public HaHaBean(int id, int name, int thumb) {
        mId = id;
        mName = name;
        mThumb = thumb;
    }

    public HaHaBean(int id, int name, int thumb, boolean checked) {
        this(id, name, thumb);
        mChecked = checked;
    }


    public int getId() {
        return mId;
    }


    public int getName() {
        return mName;
    }


    public int getThumb() {
        return mThumb;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
