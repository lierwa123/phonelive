package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.event.FollowEvent;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.main.R;
import com.yunbao.main.adapter.ActiveAdapter;
import com.yunbao.main.bean.ActiveBean;
import com.yunbao.main.event.ActiveCommentEvent;
import com.yunbao.main.event.ActiveDeleteEvent;
import com.yunbao.main.event.ActiveLikeEvent;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

public class ActiveTopicActivity extends AbsActivity {

    public static void forward(Context context, String topicId, String topicName) {
        Intent intent = new Intent(context, ActiveTopicActivity.class);
        intent.putExtra(Constants.CLASS_ID, topicId);
        intent.putExtra(Constants.CLASS_NAME, topicName);
        context.startActivity(intent);
    }

    private String mTopicId;
    private CommonRefreshView mRefreshView;
    private ActiveAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active_topic;
    }

    @Override
    protected void main() {
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(Constants.CLASS_NAME));
        mTopicId = intent.getStringExtra(Constants.CLASS_ID);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_active_home);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<ActiveBean>() {
            @Override
            public RefreshAdapter<ActiveBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new ActiveAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.getTopicActiveList(mTopicId, p, callback);
            }

            @Override
            public List<ActiveBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), ActiveBean.class);
            }

            @Override
            public void onRefreshSuccess(List<ActiveBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<ActiveBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        EventBus.getDefault().register(this);
        mRefreshView.initData();
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


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mAdapter != null) {
            mAdapter.release();
        }
        MainHttpUtil.cancel(MainHttpConsts.GET_TOPIC_ACTIVE_LIST);
        super.onDestroy();
    }
}
