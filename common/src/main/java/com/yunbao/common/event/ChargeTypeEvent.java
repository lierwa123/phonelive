package com.yunbao.common.event;

public class ChargeTypeEvent {
    private String mPayType;

    public ChargeTypeEvent(String payType) {
        mPayType = payType;
    }

    public String getPayType() {
        return mPayType;
    }
}
