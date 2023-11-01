package com.yunbao.beauty.bean;

public class TieZhiTypeBean {
    private int mId;
    private String mName;
    private boolean mAdvance;
    private boolean mChecked;


    public TieZhiTypeBean(int id, String name) {
        mId = id;
        mName = name;
    }

    public TieZhiTypeBean(int id, String name, boolean advance) {
        this(id, name);
        mAdvance = advance;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isAdvance() {
        return mAdvance;
    }

    public void setAdvance(boolean advance) {
        mAdvance = advance;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
