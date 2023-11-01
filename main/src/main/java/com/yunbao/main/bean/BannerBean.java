package com.yunbao.main.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by cxf on 2019/3/30.
 */

public class BannerBean {
    private String mImageUrl;
    private String mLink;

    @JSONField(name = "slide_pic")
    public String getImageUrl() {
        return mImageUrl;
    }

    @JSONField(name = "slide_pic")
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @JSONField(name = "slide_url")
    public String getLink() {
        return mLink;
    }

    @JSONField(name = "slide_url")
    public void setLink(String link) {
        mLink = link;
    }

    public boolean isEqual(BannerBean bean) {
        if (bean == null) {
            return false;
        }
        if (TextUtils.isEmpty(mImageUrl) || !mImageUrl.equals(bean.getImageUrl())) {
            return false;
        }
        if (TextUtils.isEmpty(mLink) || !mLink.equals(bean.getLink())) {
            return false;
        }
        return true;
    }
}
