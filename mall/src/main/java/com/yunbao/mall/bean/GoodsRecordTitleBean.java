package com.yunbao.mall.bean;

import java.util.List;

public class GoodsRecordTitleBean extends GoodsRecordBean {

    private String mDate;
    private List<GoodsRecordItemBean> mItemList;
    private int mPosition;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public List<GoodsRecordItemBean> getItemList() {
        return mItemList;
    }

    public void setItemList(List<GoodsRecordItemBean> itemList) {
        mItemList = itemList;
    }

    public boolean isItemAllChecked() {
        if (mItemList == null || mItemList.size() == 0) {
            return false;
        }
        for (GoodsRecordItemBean bean : mItemList) {
            if (!bean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }
}
