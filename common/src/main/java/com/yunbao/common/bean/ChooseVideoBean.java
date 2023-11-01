package com.yunbao.common.bean;

import java.io.File;

/**
 * Created by cxf on 2018/6/20.
 * 选择视频的实体类
 */

public class ChooseVideoBean {

    public static final int CAMERA = 0;
    public static final int FILE = 1;

    private File videoFile;
    private long duration;
    private String durationString;
    private int mType;

    public ChooseVideoBean(int type) {
        mType = type;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }


    public int getType(){
        return mType;
    }
}
