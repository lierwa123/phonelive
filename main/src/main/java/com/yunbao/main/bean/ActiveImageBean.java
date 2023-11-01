package com.yunbao.main.bean;

import java.io.File;

public class ActiveImageBean {
    private File mImageFile;

    public ActiveImageBean() {

    }

    public ActiveImageBean(File imageFile) {
        mImageFile = imageFile;
    }

    public File getImageFile() {
        return mImageFile;
    }

    public void setImageFile(File imageFile) {
        mImageFile = imageFile;
    }
}
