package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsCommonViewHolder;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.SearchGoodsBean;
import com.yunbao.mall.bean.SearchPayBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;
import com.yunbao.mall.views.SearchGoodsViewHolder;
import com.yunbao.mall.views.SearchPayViewHolder;
import com.yunbao.mall.views.SearchPlatGoodsViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索商品和付费内容
 */
public class GoodsSearchActivity extends AbsActivity {

    private static final int PAGE_COUNT = 3;
    private List<FrameLayout> mViewList;
    private MagicIndicator mIndicator;
    private AbsCommonViewHolder[] mViewHolders;
    private ViewPager mViewPager;
    private MyHandler mHandler;
    private SearchGoodsViewHolder mGoodsViewHolder;
    private SearchPlatGoodsViewHolder mPlatGoodsViewHolder;
    private SearchPayViewHolder mPayViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void main() {
        mViewList = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        if (PAGE_COUNT > 1) {
            mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        }
        mViewPager.setAdapter(new ViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadPageData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewHolders = new AbsCommonViewHolder[PAGE_COUNT];
        mIndicator = (MagicIndicator) findViewById(R.id.indicator);
        final String[] titles = new String[]{WordUtil.getString(R.string.mall_354), WordUtil.getString(R.string.mall_405), WordUtil.getString(R.string.mall_355)};
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray1));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.textColor));
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(17);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(index);
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

        });
        commonNavigator.setAdjustMode(true);
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
        mHandler = new MyHandler(this);
        loadPageData(0);
    }


    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsCommonViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mGoodsViewHolder = new SearchGoodsViewHolder(mContext, parent, mHandler);
                    vh = mGoodsViewHolder;
                } else if (position == 1) {
                    mPlatGoodsViewHolder = new SearchPlatGoodsViewHolder(mContext, parent, mHandler);
                    vh = mPlatGoodsViewHolder;
                } else if (position == 2) {
                    mPayViewHolder = new SearchPayViewHolder(mContext, parent, mHandler);
                    vh = mPayViewHolder;
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (vh != null) {
            vh.loadData();
        }
    }

    private void search() {
        if (mViewPager.getCurrentItem() == 0) {
            if (mGoodsViewHolder != null) {
                mGoodsViewHolder.search();
            }
        } else if (mViewPager.getCurrentItem() == 1) {
            if (mPlatGoodsViewHolder != null) {
                mPlatGoodsViewHolder.search();
            }
        } else {
            if (mPayViewHolder != null) {
                mPayViewHolder.search();
            }
        }
    }


    public void cancelCheckPlatGoods() {
        if (mPlatGoodsViewHolder != null) {
            mPlatGoodsViewHolder.cancelCheck();
        }
    }


    public void cancelCheckGoods() {
        if (mGoodsViewHolder != null) {
            mGoodsViewHolder.cancelCheck();
        }
    }

    public void cancelCheckPay() {
        if (mPayViewHolder != null) {
            mPayViewHolder.cancelCheck();
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.SEARCH_GOODS_LIST);
        MallHttpUtil.cancel(MallHttpConsts.SEARCH_PLAT_GOODS_LIST);
        MallHttpUtil.cancel(MallHttpConsts.SEARCH_PAY_LIST);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (mGoodsViewHolder != null) {
            SearchGoodsBean goodsBean = mGoodsViewHolder.getCheckedBean();
            if (goodsBean != null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MALL_GOODS_ID, goodsBean.getId());
                intent.putExtra(Constants.MALL_GOODS_NAME, goodsBean.getName());
                intent.putExtra(Constants.LIVE_TYPE, 1);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
        }
        if (mPlatGoodsViewHolder != null) {
            SearchGoodsBean goodsBean = mPlatGoodsViewHolder.getCheckedBean();
            if (goodsBean != null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MALL_GOODS_ID, goodsBean.getId());
                intent.putExtra(Constants.MALL_GOODS_NAME, goodsBean.getName());
                intent.putExtra(Constants.LIVE_TYPE, 3);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
        }
        if (mPayViewHolder != null) {
            SearchPayBean payBean = mPayViewHolder.getCheckedBean();
            if (payBean != null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MALL_GOODS_ID, payBean.getId());
                intent.putExtra(Constants.MALL_GOODS_NAME, payBean.getName());
                intent.putExtra(Constants.LIVE_TYPE, 2);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
        }
        super.onBackPressed();
    }

    private static class MyHandler extends Handler {

        private GoodsSearchActivity mActivity;

        public MyHandler(GoodsSearchActivity activity) {
            mActivity = new WeakReference<>(activity).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                mActivity.search();
            }
        }

        public void release() {
            mActivity = null;
        }
    }

}
