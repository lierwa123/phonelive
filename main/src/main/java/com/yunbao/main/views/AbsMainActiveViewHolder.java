package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.event.FollowEvent;
import com.yunbao.main.adapter.ActiveAdapter;
import com.yunbao.main.event.ActiveCommentEvent;
import com.yunbao.main.event.ActiveDeleteEvent;
import com.yunbao.main.event.ActiveLikeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 首页 动态
 */
public abstract class AbsMainActiveViewHolder extends AbsMainHomeChildViewHolder {

    protected CommonRefreshView mRefreshView;
    protected ActiveAdapter mAdapter;


    public AbsMainActiveViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (mAdapter != null && e != null) {
            mAdapter.onFollowChanged(e.getToUid(), e.getIsAttention());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActiveCommentEvent(ActiveCommentEvent e) {
        if (mAdapter != null && e != null) {
            mAdapter.onCommentNumChanged(e.getActiveId(), e.getCommentNum());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActiveDeleted(ActiveDeleteEvent e) {
        if (mAdapter != null && e != null) {
            mAdapter.onActiveDeleted(e.getActiveId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActiveLikeEvent(ActiveLikeEvent e) {
        if (mAdapter != null && e != null) {
            mAdapter.onLikeChanged(e.getFrom(), e.getActiveId(), e.getLikeNum(), e.getIsLike());
        }
    }

    /**
     * 停止播放动态声音
     */
    public void stopActiveVoice() {
        if(mAdapter!=null){
            mAdapter.stopActiveVoice();
        }
    }

}
