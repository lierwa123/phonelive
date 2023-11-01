package com.yunbao.beauty.bean;

import android.graphics.Bitmap;

public class MeiYanDataBean {

    private String tieZhiName;
    private int tieZhiAction;
    private boolean tieZhiShow;
    private MeiYanValueBean meiYanValueBean;
    private int filterId;
    private int teXiaoId;
    private Bitmap waterBitmap;
    private int waterRes;
    private int waterposition;
    private int hahaName;

    private boolean makeupLipstick;
    private boolean makeupEyelash;
    private boolean makeupEyeliner;
    private boolean makeupEyebrow;
    private boolean makeupBlush;


    private int[] useFaces;

    public String getTieZhiName() {
        return tieZhiName;
    }

    public void setTieZhiName(String tieZhiName) {
        this.tieZhiName = tieZhiName;
    }

    public int getTieZhiAction() {
        return tieZhiAction;
    }

    public void setTieZhiAction(int tieZhiAction) {
        this.tieZhiAction = tieZhiAction;
    }

    public boolean getTieZhiShow() {
        return tieZhiShow;
    }

    public void setTieZhiShow(boolean tieZhiShow) {
        this.tieZhiShow = tieZhiShow;
    }

    public MeiYanValueBean getMeiYanValueBean() {
        return meiYanValueBean;
    }

    public void setMeiYanValueBean(MeiYanValueBean meiYanValueBean) {
        this.meiYanValueBean = meiYanValueBean;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public int getTeXiaoId() {
        return teXiaoId;
    }

    public void setTeXiaoId(int teXiaoId) {
        this.teXiaoId = teXiaoId;
    }

    public Bitmap getWaterBitmap() {
        return waterBitmap;
    }

    public void setWaterBitmap(Bitmap waterBitmap) {
        this.waterBitmap = waterBitmap;
    }

    public int getWaterRes() {
        return waterRes;
    }

    public void setWaterRes(int waterRes) {
        this.waterRes = waterRes;
    }

    public int getWaterposition() {
        return waterposition;
    }

    public void setWaterposition(int waterposition) {
        this.waterposition = waterposition;
    }

    public int getHahaName() {
        return hahaName;
    }

    public void setHahaName(int hahaName) {
        this.hahaName = hahaName;
    }

    public boolean isMakeupLipstick() {
        return makeupLipstick;
    }

    public void setMakeupLipstick(boolean makeupLipstick) {
        this.makeupLipstick = makeupLipstick;
    }

    public boolean isMakeupEyelash() {
        return makeupEyelash;
    }

    public void setMakeupEyelash(boolean makeupEyelash) {
        this.makeupEyelash = makeupEyelash;
    }

    public boolean isMakeupEyeliner() {
        return makeupEyeliner;
    }

    public void setMakeupEyeliner(boolean makeupEyeliner) {
        this.makeupEyeliner = makeupEyeliner;
    }

    public boolean isMakeupEyebrow() {
        return makeupEyebrow;
    }

    public void setMakeupEyebrow(boolean makeupEyebrow) {
        this.makeupEyebrow = makeupEyebrow;
    }

    public boolean isMakeupBlush() {
        return makeupBlush;
    }

    public void setMakeupBlush(boolean makeupBlush) {
        this.makeupBlush = makeupBlush;
    }

    public int[] getUseFaces() {
        return useFaces;
    }

    public void setUseFaces(int[] useFaces) {
        this.useFaces = useFaces;
    }
}
