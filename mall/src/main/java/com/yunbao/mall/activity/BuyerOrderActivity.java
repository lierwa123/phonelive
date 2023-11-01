package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;
import com.yunbao.mall.views.AbsBuyerOrderViewHolder;
import com.yunbao.mall.views.BuyerOrderAllViewHolder;
import com.yunbao.mall.views.BuyerOrderCommentViewHolder;
import com.yunbao.mall.views.BuyerOrderPayViewHolder;
import com.yunbao.mall.views.BuyerOrderReceiveViewHolder;
import com.yunbao.mall.views.BuyerOrderRefundViewHolder;
import com.yunbao.mall.views.BuyerOrderSendViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 买家 订单列表
 */
public class BuyerOrderActivity extends AbsActivity {

    public static void forward(Context context, int index) {
        Intent intent = new Intent(context, BuyerOrderActivity.class);
        intent.putExtra(Constants.MALL_ORDER_INDEX, index);
        context.startActivity(intent);
    }

    private static final int PAGE_COUNT = 6;
    private List<FrameLayout> mViewList;
    private MagicIndicator mIndicator;
    private AbsBuyerOrderViewHolder[] mViewHolders;
    private ViewPager mViewPager;
    private boolean mPaused;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer_order_activity;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_192));
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
        mViewHolders = new AbsBuyerOrderViewHolder[PAGE_COUNT];
        mIndicator = (MagicIndicator) findViewById(R.id.indicator);
        final String[] titles = new String[]{
                WordUtil.getString(R.string.mall_193),//全部
                WordUtil.getString(R.string.mall_007),//待付款
                WordUtil.getString(R.string.mall_008),//待发货
                WordUtil.getString(R.string.mall_009),//待收货
                WordUtil.getString(R.string.mall_010),//待评价
                WordUtil.getString(R.string.mall_011)//退款
        };
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setPadding(0, 0, 0, 0);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray1));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.textColor));
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(index, false);
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth(DpUtil.dp2px(20));
                linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.global));
                return linePagerIndicator;
            }

        });
        commonNavigator.setAdjustMode(true);
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
        int index = getIntent().getIntExtra(Constants.MALL_ORDER_INDEX, 0);
        if (index == 0) {
            loadPageData(0);
        } else {
            mViewPager.setCurrentItem(index, false);
        }
    }


    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsBuyerOrderViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    vh = new BuyerOrderAllViewHolder(mContext, parent);
                } else if (position == 1) {
                    vh = new BuyerOrderPayViewHolder(mContext, parent);
                } else if (position == 2) {
                    vh = new BuyerOrderSendViewHolder(mContext, parent);
                } else if (position == 3) {
                    vh = new BuyerOrderReceiveViewHolder(mContext, parent);
                } else if (position == 4) {
                    vh = new BuyerOrderCommentViewHolder(mContext, parent);
                } else if (position == 5) {
                    vh = new BuyerOrderRefundViewHolder(mContext, parent);
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

    @Override
    protected void onPause() {
        mPaused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPaused) {
            if (mViewPager != null && mViewHolders != null) {
                AbsBuyerOrderViewHolder vh = mViewHolders[mViewPager.getCurrentItem()];
                if (vh != null) {
                    vh.refreshData();
                }
            }
        }
        mPaused = false;
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BUYER_ORDER_LIST);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_DELETE_ORDER);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_CANCEL_ORDER);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_CONFIRM_RECEIVE);
        MallHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        super.onDestroy();
    }
}
