package com.yunbao.mall.bean;

import android.text.TextUtils;

import com.yunbao.common.utils.StringUtil;

public class GoodsCommentTypeBean {
    private String mType;
    private String mTip;
    private String mCount;
    private boolean mChecked;

    public GoodsCommentTypeBean(String type, String tip) {
        mType = type;
        mTip = tip;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTip() {
        return mTip;
    }

    public void setTip(String tip) {
        mTip = tip;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public String getText() {
        if (TextUtils.isEmpty(mCount)) {
            return mTip;
        }
        return StringUtil.contact(mTip, "(", mCount, ")");
    }
}
