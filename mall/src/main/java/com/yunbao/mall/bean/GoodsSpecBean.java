package com.yunbao.mall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsSpecBean implements Parcelable {

    @JSONField(serialize = false)
    protected String mId;
    @JSONField(serialize = false)
    protected String mName;
    @JSONField(serialize = false)
    protected String mNum;
    @JSONField(serialize = false)
    protected String mPrice;
    @JSONField(serialize = false)
    protected String mThumb;

    public GoodsSpecBean() {
    }

    @JSONField(name = "spec_id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "spec_id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "spec_name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "spec_name")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "spec_num")
    public String getNum() {
        return mNum;
    }

    @JSONField(name = "spec_num")
    public void setNum(String num) {
        mNum = num;
    }

    @JSONField(name = "price")
    public String getPrice() {
        return mPrice;
    }

    @JSONField(name = "price")
    public void setPrice(String price) {
        mPrice = price;
    }

    @JSONField(name = "thumb")
    public String getThumb() {
        return mThumb;
    }

    @JSONField(name = "thumb")
    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mNum);
        dest.writeString(mPrice);
        dest.writeString(mThumb);
    }


    public GoodsSpecBean(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mNum = in.readString();
        mPrice = in.readString();
        mThumb = in.readString();
    }

    public static final Creator<GoodsSpecBean> CREATOR = new Creator<GoodsSpecBean>() {
        @Override
        public GoodsSpecBean createFromParcel(Parcel in) {
            return new GoodsSpecBean(in);
        }

        @Override
        public GoodsSpecBean[] newArray(int size) {
            return new GoodsSpecBean[size];
        }
    };

}
