package com.yunbao.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.common.bean.UserBean;

public class ActiveUserBean extends UserBean {
    private int mIsAttention;//是否关注

    @JSONField(name = "isAttention")
    public int getIsAttention() {
        return mIsAttention;
    }

    @JSONField(name = "isAttention")
    public void setIsAttention(int isAttention) {
        mIsAttention = isAttention;
    }

    public ActiveUserBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mIsAttention);
    }

    public ActiveUserBean(Parcel in) {
        super(in);
        this.mIsAttention = in.readInt();
    }

    public static final Parcelable.Creator<ActiveUserBean> CREATOR = new Parcelable.Creator<ActiveUserBean>() {
        @Override
        public ActiveUserBean[] newArray(int size) {
            return new ActiveUserBean[size];
        }

        @Override
        public ActiveUserBean createFromParcel(Parcel in) {
            return new ActiveUserBean(in);
        }
    };
}
