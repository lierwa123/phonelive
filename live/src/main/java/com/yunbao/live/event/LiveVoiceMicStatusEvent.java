package com.yunbao.live.event;

public class LiveVoiceMicStatusEvent {
    private int mPosition;
    private int mStatus;

    public LiveVoiceMicStatusEvent(int position, int status) {
        mPosition = position;
        mStatus = status;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getStatus() {
        return mStatus;
    }
}
