package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsCommonViewHolder;
import com.yunbao.mall.R;
import com.yunbao.mall.event.SetPlatGoodsEvent;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;
import com.yunbao.mall.views.SellerDaimaiViewHolder;
import com.yunbao.mall.views.SellerShenHeViewHolder;
import com.yunbao.mall.views.SellerXiaJiaViewHolder;
import com.yunbao.mall.views.SellerZaiShouViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SellerManageGoodsActivity extends AbsActivity implements View.OnClickListener {

    private static final int PAGE_COUNT = 4;
    private List<FrameLayout> mViewList;
    private MagicIndicator mIndicator;
    private AbsCommonViewHolder[] mViewHolders;
    private ViewPager mViewPager;
    private String mZaiShouString;//在售
    private String mShenHeString;//审核中
    private String mXiaJiaString;//下架
    private String mDaimaiString;//下架
    private TextView mZaiShou;
    private TextView mShenHe;
    private TextView mXiaJia;
    private TextView mDaimai;
    private SellerDaimaiViewHolder mDaimaiViewHolder;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, SellerManageGoodsActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_manage_goods;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_075));
        mZaiShouString = WordUtil.getString(R.string.mall_109);
        mShenHeString = WordUtil.getString(R.string.mall_110);
        mXiaJiaString = WordUtil.getString(R.string.mall_111);
        mDaimaiString = WordUtil.getString(R.string.mall_409);
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
        final String[] titles = new String[]{
                mZaiShouString, mDaimaiString, mShenHeString, mXiaJiaString};
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
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(index);
                        }
                    }
                });
                if (index == 0) {
                    mZaiShou = simplePagerTitleView;
                } else if (index == 1) {
                    mDaimai = simplePagerTitleView;
                } else if (index == 2) {
                    mShenHe = simplePagerTitleView;
                } else if (index == 3) {
                    mXiaJia = simplePagerTitleView;
                }
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
        findViewById(R.id.btn_add_goods).setOnClickListener(this);
        EventBus.getDefault().register(this);
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
                    vh = new SellerZaiShouViewHolder(mContext, parent);
                } else if (position == 1) {
                    mDaimaiViewHolder = new SellerDaimaiViewHolder(mContext, parent);
                    vh = mDaimaiViewHolder;
                } else if (position == 2) {
                    vh = new SellerShenHeViewHolder(mContext, parent);
                } else if (position == 3) {
                    vh = new SellerXiaJiaViewHolder(mContext, parent);
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

    /**
     * 获取商品数量
     */
    public void getGoodsNum() {
        MallHttpUtil.getGoodsNum(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (mZaiShou != null) {
                        mZaiShou.setText(StringUtil.contact(mZaiShouString, " ", obj.getString("onsale")));
                    }
                    if (mDaimai != null) {
                        mDaimai.setText(StringUtil.contact(mDaimaiString, " ", obj.getString("platform")));
                    }
                    if (mShenHe != null) {
                        mShenHe.setText(StringUtil.contact(mShenHeString, " ", obj.getString("onexamine")));
                    }
                    if (mXiaJia != null) {
                        mXiaJia.setText(StringUtil.contact(mXiaJiaString, " ", obj.getString("remove_shelves")));
                    }
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetPlatGoodsEvent(SetPlatGoodsEvent e) {
        getGoodsNum();
        if (mDaimaiViewHolder != null) {
            mDaimaiViewHolder.loadData();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        MallHttpUtil.cancel(MallHttpConsts.GET_GOODS_NUM);
        MallHttpUtil.cancel(MallHttpConsts.GET_MANAGE_GOODS_LIST);
        MallHttpUtil.cancel(MallHttpConsts.GOODS_UP_STATUS);
        MallHttpUtil.cancel(MallHttpConsts.GOODS_DELETE);
        MallHttpUtil.cancel(MallHttpConsts.GET_MANAGE_PLAT_GOODS);
        MallHttpUtil.cancel(MallHttpConsts.SET_PLAT_GOODS);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoodsNum();
        if (mViewPager != null) {
            loadPageData(mViewPager.getCurrentItem());
        }
    }

    @Override
    public void onClick(View v) {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.mall_406, R.string.mall_374, R.string.mall_375,
        }, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.mall_374) {
                    SellerAddGoodsActivity.forward(mContext, null);
                } else if (tag == R.string.mall_375) {
                    GoodsAddOutSideActivity.forward(mContext, null);
                } else if (tag == R.string.mall_406) {
                    SellerAddPlatActivity.forward(mContext);
                }
            }
        });
    }
}
