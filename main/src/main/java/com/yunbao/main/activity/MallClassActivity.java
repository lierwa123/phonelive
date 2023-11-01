package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MallClassAdapter;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.mall.bean.GoodsHomeClassBean;
import com.yunbao.mall.bean.GoodsSimpleBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.Arrays;
import java.util.List;

public class MallClassActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String className, String classId) {
        Intent intent = new Intent(context, MallClassActivity.class);
        intent.putExtra(Constants.CLASS_NAME, className);
        intent.putExtra(Constants.CLASS_ID, classId);
        context.startActivity(intent);
    }

    private static final String SORT_DESC = "desc";
    private static final String SORT_ASC = "asc";
    private static final String SORT_NONE = "";
    private TextView mBtnNew;
    private TextView mTvSale;
    private TextView mTvPrice;
    private ImageView mImgSale;
    private ImageView mImgPrice;
    private Drawable mDrawableUp;
    private Drawable mDrawableDown;
    private Drawable mDrawableNone;
    private int mColor0;
    private int mColor1;
    private CommonRefreshView mRefreshView;
    private MagicIndicator mIndicator;
    private List<GoodsHomeClassBean> mGoodsClassList;
    private int mTabIndex;
    private String mSaleSort = SORT_NONE;
    private String mPriceSort = SORT_NONE;
    private int mIsNew;
    private String mClassId;
    private MallClassAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_class;
    }

    @Override
    protected void main() {
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(Constants.CLASS_NAME));
        String classId = intent.getStringExtra(Constants.CLASS_ID);
        mBtnNew = findViewById(R.id.btn_new);
        mTvSale = findViewById(R.id.tv_sale);
        mTvPrice = findViewById(R.id.tv_price);
        mImgSale = findViewById(R.id.img_sale);
        mImgPrice = findViewById(R.id.img_price);
        mDrawableUp = ContextCompat.getDrawable(mContext, R.mipmap.icon_mall_up);
        mDrawableDown = ContextCompat.getDrawable(mContext, R.mipmap.icon_mall_down);
        mDrawableNone = ContextCompat.getDrawable(mContext, R.mipmap.icon_mall_none);
        mColor0 = ContextCompat.getColor(mContext, R.color.gray1);
        mColor1 = ContextCompat.getColor(mContext, R.color.textColor);
        mBtnNew.setOnClickListener(this);
        findViewById(R.id.btn_sale).setOnClickListener(this);
        findViewById(R.id.btn_price).setOnClickListener(this);
        mIndicator = (MagicIndicator) findViewById(R.id.indicator);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_main_mall);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 10, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsSimpleBean>() {
            @Override
            public RefreshAdapter<GoodsSimpleBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MallClassAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.getShopClassList(mClassId, mSaleSort, mPriceSort, mIsNew, p, callback);
            }

            @Override
            public List<GoodsSimpleBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), GoodsSimpleBean.class);
            }

            @Override
            public void onRefreshSuccess(List<GoodsSimpleBean> list, int listCount) {

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
        MainHttpUtil.getShopThreeClass(classId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    mGoodsClassList = JSON.parseArray(Arrays.toString(info), GoodsHomeClassBean.class);
                    if (mGoodsClassList != null && mGoodsClassList.size() > 0) {
                        mClassId = mGoodsClassList.get(0).getId();
                        showTab();
                        loadData();
                    }

                }
            }
        });
    }

    private void showTab() {
        if (mIndicator == null) {
            return;
        }
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                if (mGoodsClassList != null) {
                    return mGoodsClassList.size();
                }
                return 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
//                simplePagerTitleView.setPadding(0, 0, 0, 0);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.textColor));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.global));
                simplePagerTitleView.setText(mGoodsClassList.get(index).getName());
                simplePagerTitleView.setTextSize(14);
//                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTabIndex != index) {
                            mTabIndex = index;
                            mIndicator.onPageScrollStateChanged(2);
                            mIndicator.onPageSelected(index);
                            mIndicator.onPageScrolled(index, 0, 0);
                            mIndicator.onPageScrollStateChanged(0);
                            mClassId = mGoodsClassList.get(index).getId();
                            loadData();
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setXOffset(DpUtil.dp2px(2));
                linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.global));
                return linePagerIndicator;

            }

        });
        mIndicator.setNavigator(commonNavigator);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_new) {
            mSaleSort = SORT_NONE;
            mPriceSort = SORT_NONE;
            mIsNew = 1;
            mImgSale.setImageDrawable(mDrawableNone);
            mImgPrice.setImageDrawable(mDrawableNone);
            mBtnNew.setTextColor(mColor1);
            mTvSale.setTextColor(mColor0);
            mTvPrice.setTextColor(mColor0);
        } else if (id == R.id.btn_sale) {
            if (SORT_NONE.equals(mSaleSort) || SORT_DESC.equals(mSaleSort)) {
                mSaleSort = SORT_ASC;
                mImgSale.setImageDrawable(mDrawableUp);
            } else if (SORT_ASC.equals(mSaleSort)) {
                mSaleSort = SORT_DESC;
                mImgSale.setImageDrawable(mDrawableDown);
            }
            mPriceSort = SORT_NONE;
            mIsNew = 0;
            mImgPrice.setImageDrawable(mDrawableNone);
            mBtnNew.setTextColor(mColor0);
            mTvSale.setTextColor(mColor1);
            mTvPrice.setTextColor(mColor0);
        } else if (id == R.id.btn_price) {
            mSaleSort = SORT_NONE;
            if (SORT_NONE.equals(mPriceSort) || SORT_DESC.equals(mPriceSort)) {
                mPriceSort = SORT_ASC;
                mImgPrice.setImageDrawable(mDrawableUp);
            } else if (SORT_ASC.equals(mPriceSort)) {
                mPriceSort = SORT_DESC;
                mImgPrice.setImageDrawable(mDrawableDown);
            }
            mIsNew = 0;
            mImgSale.setImageDrawable(mDrawableNone);
            mBtnNew.setTextColor(mColor0);
            mTvSale.setTextColor(mColor0);
            mTvPrice.setTextColor(mColor1);
        }
        loadData();
    }

    private void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Override
    protected void onDestroy() {
        mIndicator = null;
        MainHttpUtil.cancel(MainHttpConsts.GET_SHOP_THREE_CLASS);
        MainHttpUtil.cancel(MainHttpConsts.GET_SHOP_CLASS_LIST);
        super.onDestroy();
    }


}
