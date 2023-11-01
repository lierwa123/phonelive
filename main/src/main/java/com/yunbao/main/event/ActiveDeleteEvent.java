package com.yunbao.main.event;

public class ActiveDeleteEvent {

    private String mActiveId;

    public ActiveDeleteEvent(String activeId) {
        mActiveId = activeId;
    }

    public String getActiveId() {
        return mActiveId;
    }
}
