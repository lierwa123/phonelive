package com.yunbao.live.event;

import com.yunbao.live.bean.LiveBean;

public class LiveAudienceVoiceExitEvent {

    private LiveBean mLiveBean;

    public LiveAudienceVoiceExitEvent(LiveBean liveBean) {
        mLiveBean = liveBean;
    }

    public LiveBean getLiveBean() {
        return mLiveBean;
    }

    public void setLiveBean(LiveBean liveBean) {
        mLiveBean = liveBean;
    }
}
