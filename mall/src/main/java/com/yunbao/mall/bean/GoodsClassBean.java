package com.yunbao.mall.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsClassBean extends ManageClassBean implements Parcelable {

    private boolean mTitle;
    private String mOneClassId;
    private String mTwoClassId;

    public GoodsClassBean(){

    }

    public boolean isTitle() {
        return mTitle;
    }

    public void setTitle(boolean title) {
        mTitle = title;
    }

    public String getOneClassId() {
        return mOneClassId;
    }

    public void setOneClassId(String oneClassId) {
        mOneClassId = oneClassId;
    }

    public String getTwoClassId() {
        return mTwoClassId;
    }

    public void setTwoClassId(String twoClassId) {
        mTwoClassId = twoClassId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mOneClassId);
        dest.writeString(mTwoClassId);
    }


    public GoodsClassBean(Parcel in) {
        super(in);
        mOneClassId = in.readString();
        mTwoClassId = in.readString();
    }

    public static final Creator<ManageClassBean> CREATOR = new Creator<ManageClassBean>() {
        @Override
        public ManageClassBean createFromParcel(Parcel in) {
            return new GoodsClassBean(in);
        }

        @Override
        public ManageClassBean[] newArray(int size) {
            return new GoodsClassBean[size];
        }
    };
}
