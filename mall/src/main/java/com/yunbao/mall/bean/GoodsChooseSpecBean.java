package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsChooseSpecBean extends GoodsSpecBean {

    @JSONField(serialize = false)
    private boolean mChecked;

    @JSONField(serialize = false)
    public boolean isChecked() {
        return mChecked;
    }

    @JSONField(serialize = false)
    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
