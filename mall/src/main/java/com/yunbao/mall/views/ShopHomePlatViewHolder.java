package com.yunbao.mall.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.views.AbsCommonViewHolder;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.activity.ShopHomeActivity;
import com.yunbao.mall.adapter.ShopHomePlatAdapter;
import com.yunbao.mall.bean.GoodsSimpleBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.List;

/**
 * 店铺 平台自营商品
 */
public class ShopHomePlatViewHolder extends AbsCommonViewHolder implements OnItemClickListener<GoodsSimpleBean> {

    private CommonRefreshView mRefreshView;
    private ShopHomePlatAdapter mAdapter;
    private String mToUid;

    public ShopHomePlatViewHolder(Context context, ViewGroup parentView, String toUid) {
        super(context, parentView, toUid);
    }

    @Override
    protected void processArguments(Object... args) {
        if (args.length > 0) {
            mToUid = (String) args[0];
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_shop_home_my;
    }

    @Override
    public void init() {
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_goods_seller_2);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 10, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mAdapter = new ShopHomePlatAdapter(mContext, !TextUtils.isEmpty(mToUid) && mToUid.equals(CommonAppConfig.getInstance().getUid()));
        mAdapter.setOnItemClickListener(this);
        mRefreshView.setRecyclerViewAdapter(mAdapter);
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsSimpleBean>() {
            @Override
            public RefreshAdapter<GoodsSimpleBean> getAdapter() {
                return null;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MallHttpUtil.getPlatShop(mToUid, p, callback);
            }

            @Override
            public List<GoodsSimpleBean> processData(String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                ((ShopHomeActivity) mContext).showShopInfo(obj.getJSONObject("shop_info"));
                return JSON.parseArray(obj.getString("list"), GoodsSimpleBean.class);
            }

            @Override
            public void onRefreshSuccess(List<GoodsSimpleBean> list, int listCount) {
                ((ShopHomeActivity) mContext).finishRefresh();
            }

            @Override
            public void onRefreshFailure() {
                ((ShopHomeActivity) mContext).finishRefresh();
            }

            @Override
            public void onLoadMoreSuccess(List<GoodsSimpleBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
    }

    @Override
    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }


    @Override
    public void onItemClick(GoodsSimpleBean bean, int position) {
        GoodsDetailActivity.forward(mContext, bean.getId(), true, bean.getType(), mToUid);
    }

    @Override
    public void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_PLAT_SHOP);
        super.onDestroy();
    }
}
