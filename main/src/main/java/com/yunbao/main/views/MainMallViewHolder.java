package com.yunbao.main.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.main.R;
import com.yunbao.main.activity.MallSearchActivity;
import com.yunbao.main.adapter.MainMallAdapter;
import com.yunbao.main.adapter.MainMallClassAdapter;
import com.yunbao.main.bean.BannerBean;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.bean.GoodsHomeClassBean;
import com.yunbao.mall.bean.GoodsSimpleBean;

import java.util.List;

/**
 * 首页 商城
 */
public class MainMallViewHolder extends AbsMainViewHolder implements OnItemClickListener<GoodsSimpleBean>, View.OnClickListener {

    private CommonRefreshView mRefreshView;
    private MainMallAdapter mAdapter;
    private Banner mBanner;
    private View mBannerWrap;
    private boolean mBannerNeedUpdate;
    private List<BannerBean> mBannerList;
    private List<GoodsHomeClassBean> mClassList;
    private RecyclerView mRecyclerViewClass;
    private boolean mClassShowed;
    private View mScrollIndicator;
    private int mDp25;

    public MainMallViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_mall;
    }

    @Override
    public void init() {
        setStatusHeight();
        findViewById(R.id.btn_search).setOnClickListener(this);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_main_mall);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;
            }
        });
        mRefreshView.setLayoutManager(gridLayoutManager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 10, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mAdapter = new MainMallAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        mRefreshView.setRecyclerViewAdapter(mAdapter);
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsSimpleBean>() {
            @Override
            public RefreshAdapter<GoodsSimpleBean> getAdapter() {
                return null;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.getHomeGoodsList(p, callback);
            }

            @Override
            public List<GoodsSimpleBean> processData(String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                mBannerNeedUpdate = false;
                List<BannerBean> bannerList = JSON.parseArray(obj.getString("slide"), BannerBean.class);
                if (bannerList != null && bannerList.size() > 0) {
                    if (mBannerList == null || mBannerList.size() != bannerList.size()) {
                        mBannerNeedUpdate = true;
                    } else {
                        for (int i = 0; i < mBannerList.size(); i++) {
                            BannerBean bean = mBannerList.get(i);
                            if (bean == null || !bean.isEqual(bannerList.get(i))) {
                                mBannerNeedUpdate = true;
                                break;
                            }
                        }
                    }
                }
                mBannerList = bannerList;
                mClassList = JSON.parseArray(obj.getString("shoptwoclass"), GoodsHomeClassBean.class);
                return JSON.parseArray(obj.getString("list"), GoodsSimpleBean.class);
            }

            @Override
            public void onRefreshSuccess(List<GoodsSimpleBean> list, int listCount) {
                showBanner();
                showClass();
            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<GoodsSimpleBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        View headView = mAdapter.getHeadView();
        mScrollIndicator = headView.findViewById(R.id.scroll_indicator);
        mDp25 = DpUtil.dp2px(25);
        mBannerWrap = headView.findViewById(R.id.banner_wrap);
        mBanner = (Banner) headView.findViewById(R.id.banner);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImgLoader.display(mContext, ((BannerBean) path).getImageUrl(), imageView);
            }
        });
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int p) {
                if (mBannerList != null) {
                    if (p >= 0 && p < mBannerList.size()) {
                        BannerBean bean = mBannerList.get(p);
                        if (bean != null) {
                            String link = bean.getLink();
                            if (!TextUtils.isEmpty(link)) {
                                WebViewActivity.forward(mContext, link, false);
                            }
                        }
                    }
                }
            }
        });
        mRecyclerViewClass = headView.findViewById(R.id.recyclerView_class);
        mRecyclerViewClass.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (mScrollIndicator != null) {
                    mScrollIndicator.setTranslationX(mDp25 * computeScrollPercent());
                }
            }
        });
    }

    /**
     * 计算滑动的百分比
     */
    private float computeScrollPercent() {
        if (mRecyclerViewClass != null) {
            //当前RcyclerView显示区域的高度。水平列表屏幕从左侧到右侧显示范围
            int extent = mRecyclerViewClass.computeHorizontalScrollExtent();
            //整体的高度，注意是整体，包括在显示区域之外的
            int range = mRecyclerViewClass.computeHorizontalScrollRange();
            //已经向下滚动的距离，为0时表示已处于顶部
            float offset = mRecyclerViewClass.computeHorizontalScrollOffset();
            //已经滚动的百分比 0~1
            float percent = offset / (range - extent);
            if (percent > 1) {
                percent = 1;
            }
            return percent;
        }
        return 0;
    }


    private void showBanner() {
        if (mBanner == null || mBannerWrap == null) {
            return;
        }
        if (mBannerList == null || mBannerList.size() == 0) {
            mBannerWrap.setVisibility(View.GONE);
            return;
        }
        if (mBannerNeedUpdate) {
            mBanner.update(mBannerList);
        }
    }


    private void showClass() {
        if (mRecyclerViewClass == null) {
            return;
        }
        if (mClassList == null || mClassList.size() == 0) {
            mRecyclerViewClass.setVisibility(View.GONE);
            return;
        }
        if (mClassShowed) {
            return;
        }
        mClassShowed = true;
        int size = mClassList.size();
        if (size <= 12) {
            mRecyclerViewClass.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        } else {
            mRecyclerViewClass.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false));
        }
        MainMallClassAdapter adapter = new MainMallClassAdapter(mContext, mClassList);
        mRecyclerViewClass.setAdapter(adapter);
    }

    @Override
    public void onItemClick(GoodsSimpleBean bean, int position) {
        GoodsDetailActivity.forward(mContext, bean.getId(), false, bean.getType());
    }


    @Override
    public void loadData() {
        if (isFirstLoadData() && mRefreshView != null) {
            mRefreshView.initData();
        }
    }


    @Override
    public void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.GET_HOME_GOODS_LIST);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search) {
            MallSearchActivity.forward(mContext);
        }
    }
}
