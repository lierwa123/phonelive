package com.yunbao.common.bean;

import java.io.File;

public class ChooseImageBean {
    public static final int CAMERA = 0;
    public static final int FILE = 1;

    private File mImageFile;
    private int mType;
    private boolean mChecked;

    public ChooseImageBean(int type) {
        mType = type;
    }

    public ChooseImageBean(int type, File file) {
        mType = type;
        mImageFile = file;
    }

    public int getType() {
        return mType;
    }

    public File getImageFile() {
        return mImageFile;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

}
