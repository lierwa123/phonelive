package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsRecordBean {
    private boolean mChecked;
    private boolean mEdit;

    @JSONField(serialize = false)
    public boolean isChecked() {
        return mChecked;
    }

    @JSONField(serialize = false)
    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    @JSONField(serialize = false)
    public boolean isEdit() {
        return mEdit;
    }

    @JSONField(serialize = false)
    public void setEdit(boolean edit) {
        mEdit = edit;
    }

    public void toggle() {
        mChecked = !mChecked;
    }
}
