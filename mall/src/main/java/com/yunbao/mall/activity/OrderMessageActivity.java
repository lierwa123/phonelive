package com.yunbao.mall.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.OrderMessageAdapter;
import com.yunbao.mall.bean.OrderMsgBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.Arrays;
import java.util.List;

public class OrderMessageActivity extends AbsActivity {

    private CommonRefreshView mRefreshView;
    private OrderMessageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_message;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_365));
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<OrderMsgBean>() {
            @Override
            public RefreshAdapter<OrderMsgBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new OrderMessageAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MallHttpUtil.getOrderMsgList(p, callback);
            }

            @Override
            public List<OrderMsgBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), OrderMsgBean.class);
            }

            @Override
            public void onRefreshSuccess(List<OrderMsgBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<OrderMsgBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mRefreshView.initData();
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_ORDER_MSG_LIST);
        super.onDestroy();
    }
}
