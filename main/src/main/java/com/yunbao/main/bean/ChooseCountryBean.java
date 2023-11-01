package com.yunbao.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.main.utils.suspension.ISuspensionInterface;

public class ChooseCountryBean implements ISuspensionInterface {
    private String mName;
    private String mNameEn;
    private String mTel;
    private String mIndex;

    @JSONField(name = "name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "tel")
    public String getTel() {
        return mTel;
    }

    @JSONField(name = "tel")
    public void setTel(String tel) {
        mTel = tel;
    }

    @JSONField(name = "name_en")
    public String getNameEn() {
        return mNameEn;
    }
    @JSONField(name = "name_en")
    public void setNameEn(String nameEn) {
        mNameEn = nameEn;
    }

    public String getIndex() {
        return mIndex;
    }
    public void setIndex(String index) {
        mIndex = index;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return mIndex;
    }
}
