package com.yunbao.main.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.ActiveAllTopicAdapter;
import com.yunbao.main.bean.ActiveTopicBean;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 动态 全部话题
 */
public class ActiveAllTopicActivity extends AbsActivity {

    private ActiveAllTopicAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active_all_topic;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.active_topic_03));
        CommonRefreshView refreshView = findViewById(R.id.refreshView);
        refreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        refreshView.setDataHelper(new CommonRefreshView.DataHelper<ActiveTopicBean>() {
            @Override
            public RefreshAdapter<ActiveTopicBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new ActiveAllTopicAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.getActiveAllTopic(p, callback);
            }

            @Override
            public List<ActiveTopicBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), ActiveTopicBean.class);
            }

            @Override
            public void onRefreshSuccess(List<ActiveTopicBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<ActiveTopicBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        refreshView.initData();
    }

    @Override
    protected void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.GET_ACTIVE_ALL_TOPIC);
        super.onDestroy();
    }
}

