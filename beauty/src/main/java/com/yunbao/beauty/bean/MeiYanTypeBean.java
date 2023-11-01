package com.yunbao.beauty.bean;

public class MeiYanTypeBean {

    private int mName;
    private boolean mChecked;


    public MeiYanTypeBean(int name) {
        mName = name;
    }

    public int getName() {
        return mName;
    }

    public void setName(int name) {
        mName = name;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
