package com.yunbao.main.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.main.R;
import com.yunbao.main.adapter.ActiveAdapter;
import com.yunbao.main.bean.ActiveBean;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 首页 动态 最新
 */
public class MainActiveNewestViewHolder extends AbsMainActiveViewHolder {

    public MainActiveNewestViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_active_recommend;
    }

    @Override
    public void init() {
        super.init();
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_active_follow);
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
                MainHttpUtil.getActiveNewest(p, callback);
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
    }


    @Override
    public void loadData() {
        if(mRefreshView!=null){
            mRefreshView.initData();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.release();
        }
        MainHttpUtil.cancel(MainHttpConsts.GET_ACTIVE_NEWEST);

    }

}
