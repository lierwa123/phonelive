package com.yunbao.mall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 买家收货地址列表
 */
public class BuyerAddressBean implements Parcelable {

    private String mId;
    private String mName;
    private String mPhoneNum;
    private String mProvince;
    private String mCity;
    private String mZone;
    private String mAddress;
    private int mIsDefault;

    public BuyerAddressBean() {
    }

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "phone")
    public String getPhoneNum() {
        return mPhoneNum;
    }

    @JSONField(name = "phone")
    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    @JSONField(name = "province")
    public String getProvince() {
        return mProvince;
    }

    @JSONField(name = "province")
    public void setProvince(String province) {
        mProvince = province;
    }

    @JSONField(name = "city")
    public String getCity() {
        return mCity;
    }

    @JSONField(name = "city")
    public void setCity(String city) {
        mCity = city;
    }

    @JSONField(name = "area")
    public String getZone() {
        return mZone;
    }

    @JSONField(name = "area")
    public void setZone(String zone) {
        mZone = zone;
    }

    @JSONField(name = "address")
    public String getAddress() {
        return mAddress;
    }

    @JSONField(name = "address")
    public void setAddress(String address) {
        mAddress = address;
    }

    @JSONField(name = "is_default")
    public int getIsDefault() {
        return mIsDefault;
    }

    @JSONField(name = "is_default")
    public void setIsDefault(int isDefault) {
        mIsDefault = isDefault;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPhoneNum);
        dest.writeString(mProvince);
        dest.writeString(mCity);
        dest.writeString(mZone);
        dest.writeString(mAddress);
        dest.writeInt(mIsDefault);
    }

    public BuyerAddressBean(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPhoneNum = in.readString();
        mProvince = in.readString();
        mCity = in.readString();
        mZone = in.readString();
        mAddress = in.readString();
        mIsDefault = in.readInt();
    }

    public static final Creator<BuyerAddressBean> CREATOR = new Creator<BuyerAddressBean>() {
        @Override
        public BuyerAddressBean createFromParcel(Parcel in) {
            return new BuyerAddressBean(in);
        }

        @Override
        public BuyerAddressBean[] newArray(int size) {
            return new BuyerAddressBean[size];
        }
    };
}
