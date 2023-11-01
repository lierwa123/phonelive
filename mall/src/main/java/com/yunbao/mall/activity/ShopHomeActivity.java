package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsCommonViewHolder;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.im.http.ImHttpUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.custom.ShopIndicatorTitle;
import com.yunbao.mall.views.ShopHomeMyViewHolder;
import com.yunbao.mall.views.ShopHomePlatViewHolder;

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
 * 卖家店铺主页
 */
public class ShopHomeActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String toUid) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
        intent.putExtra(Constants.TO_UID, toUid);
        context.startActivity(intent);
    }

    private int PAGE_COUNT = 2;
    private SmartRefreshLayout mSmartRefreshLayout;
    private ClassicsHeader mHeader;
    private List<FrameLayout> mViewList;
    private MagicIndicator mIndicator;
    private AbsCommonViewHolder[] mViewHolders;
    private ViewPager mViewPager;
    private ImageView mAvatar;
    private TextView mName;
    private TextView mMyGoodsNum;//我的商品数量
    private TextView mPlatGoodsNum;//平台商品数量
    private TextView mSaleNumAll;
    private TextView mGoodsQuality;
    private TextView mTaiDuFuWu;//服务态度
    private TextView mTaiDuWuLiu;//物流态度
    private String mToUid;
    private JSONObject mShopInfo;
    private String mUnitString;
    private boolean mIsPlat;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_home;
    }

    @Override
    protected void main() {
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        if (configBean != null) {
            setTitle(configBean.getShopSystemName());
        } else {
            setTitle(WordUtil.getString(R.string.mall_001));
        }
        mToUid = getIntent().getStringExtra(Constants.TO_UID);
        mIsPlat = Constants.MALL_PLAT_UID.equals(mToUid);
        if (mIsPlat) {
            PAGE_COUNT = 1;
        }
        mAvatar = findViewById(R.id.avatar);
        mName = findViewById(R.id.name);
        mSaleNumAll = findViewById(R.id.sale_num_all);
        mGoodsQuality = findViewById(R.id.goods_quality);
        mTaiDuFuWu = findViewById(R.id.taidu_fuwu);
        mTaiDuWuLiu = findViewById(R.id.taidu_wuliu);
        findViewById(R.id.btn_cert).setOnClickListener(this);
        View btnKefu = findViewById(R.id.btn_kefu);
        if (!TextUtils.isEmpty(mToUid) && mToUid.equals(CommonAppConfig.getInstance().getUid())) {
            btnKefu.setVisibility(View.INVISIBLE);
        } else {
            btnKefu.setOnClickListener(this);
        }
        mUnitString = WordUtil.getString(R.string.mall_168);

        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(com.yunbao.common.R.id.refreshLayout);
        mSmartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
        mSmartRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);//是否在全部加载结束之后Footer跟随内容
        mSmartRefreshLayout.setEnableOverScrollBounce(false);//设置是否开启越界回弹功能（默认true）
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mViewPager != null) {
                    loadPageData(mViewPager.getCurrentItem());
                }
            }
        });
        int textColor = ContextCompat.getColor(mContext, com.yunbao.common.R.color.textColor);
        mHeader = findViewById(com.yunbao.common.R.id.header);
        mHeader.setAccentColor(textColor);
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
        final String[] titles = mIsPlat ? new String[]{
                WordUtil.getString(R.string.mall_405)
        } : new String[]{
                WordUtil.getString(R.string.mall_404), WordUtil.getString(R.string.mall_405)
        };
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ShopIndicatorTitle indicatorTitle = new ShopIndicatorTitle(mContext);
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setPadding(0, 0, 0, 0);
                simplePagerTitleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray1));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.textColor));
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                indicatorTitle.addView(simplePagerTitleView);
                indicatorTitle.setTitleView(simplePagerTitleView);
                indicatorTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(index);
                        }
                    }
                });
                if (mIsPlat) {
                    if (index == 0) {
                        mPlatGoodsNum = new TextView(mContext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = DpUtil.dp2px(5);
                        mPlatGoodsNum.setLayoutParams(params);
                        indicatorTitle.addView(mPlatGoodsNum);
                    }
                } else {
                    if (index == 0) {
                        mMyGoodsNum = new TextView(mContext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = DpUtil.dp2px(5);
                        mMyGoodsNum.setLayoutParams(params);
                        indicatorTitle.addView(mMyGoodsNum);
                    } else if (index == 1) {
                        mPlatGoodsNum = new TextView(mContext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = DpUtil.dp2px(5);
                        mPlatGoodsNum.setLayoutParams(params);
                        indicatorTitle.addView(mPlatGoodsNum);
                    }
                }

                return indicatorTitle;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setXOffset(DpUtil.dp2px(10));
                linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.global));
                return linePagerIndicator;
            }

        });
        mIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DpUtil.dp2px(30);
            }
        });
        ViewPagerHelper.bind(mIndicator, mViewPager);
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
                if (mIsPlat) {
                    if (position == 0) {
                        vh = new ShopHomePlatViewHolder(mContext, parent, mToUid);
                    }
                } else {
                    if (position == 0) {
                        vh = new ShopHomeMyViewHolder(mContext, parent, mToUid);
                    } else if (position == 1) {
                        vh = new ShopHomePlatViewHolder(mContext, parent, mToUid);
                    }
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cert) {
            forwardCert();
        } else if (id == R.id.btn_kefu) {
            if (mIsPlat) {
                DialogUitl.showSimpleTipDialog(mContext, null, true, WordUtil.getString(R.string.mall_412), true);
            } else {
                forwardChat();
            }

        }
    }

    public void finishRefresh() {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.finishRefresh(true);
        }
    }

    public void showShopInfo(JSONObject shopInfo) {
        mShopInfo = shopInfo;
        if (mShopInfo != null) {
            if (mAvatar != null) {
                ImgLoader.displayAvatar(mContext, mShopInfo.getString("avatar"), mAvatar);
            }
            if (mName != null) {
                mName.setText(mShopInfo.getString("name"));
            }
            if (mMyGoodsNum != null) {
                mMyGoodsNum.setText(String.format(mUnitString, mShopInfo.getString("goods_nums")));
            }
            if (mPlatGoodsNum != null) {
                mPlatGoodsNum.setText(String.format(mUnitString, mShopInfo.getString("platform_goods_nums")));
            }
            if (mSaleNumAll != null) {
                mSaleNumAll.setText(String.format(mUnitString, mShopInfo.getString("sale_nums")));
            }
            if (mGoodsQuality != null) {
                mGoodsQuality.setText(mShopInfo.getString("quality_points"));
            }
            if (mTaiDuFuWu != null) {
                mTaiDuFuWu.setText(mShopInfo.getString("service_points"));
            }
            if (mTaiDuWuLiu != null) {
                mTaiDuWuLiu.setText(mShopInfo.getString("express_points"));
            }
        }
    }

    /**
     * 资质证明
     */
    private void forwardCert() {
        if (mShopInfo != null) {
            ShopDetailActivity.forward(mContext, mShopInfo.getString("certificate_desc"), mShopInfo.getString("certificate"));
        }
    }

    /**
     * 私信聊天
     */
    private void forwardChat() {
        if (mShopInfo == null) {
            return;
        }
        ImHttpUtil.getImUserInfo(mShopInfo.getString("uid"), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    ImUserBean bean = JSON.parseObject(info[0], ImUserBean.class);
                    if (bean != null) {
                        ChatRoomActivity.forward(mContext, bean, bean.getAttent() == 1, false);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        ImHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        super.onDestroy();
    }
}
