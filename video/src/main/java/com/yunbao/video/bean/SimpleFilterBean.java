package com.yunbao.video.bean;

/**
 * Created by cxf on 2018/8/4.
 */

public class SimpleFilterBean {

    private int mImgSrc;
    private int mFilterSrc;
    private boolean mChecked;

    public SimpleFilterBean() {
    }


    public SimpleFilterBean(int imgSrc, int filterSrc) {
        mImgSrc = imgSrc;
        mFilterSrc = filterSrc;
    }


    public SimpleFilterBean(int imgSrc, int filterSrc, boolean checked) {
        this(imgSrc, filterSrc);
        mChecked = checked;
    }

    public int getImgSrc() {
        return mImgSrc;
    }

    public void setImgSrc(int imgSrc) {
        this.mImgSrc = imgSrc;
    }

    public int getFilterSrc() {
        return mFilterSrc;
    }

    public void setFilterSrc(int filterSrc) {
        mFilterSrc = filterSrc;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

}
