package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.im.http.ImHttpUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.GoodsCommentAdapter;
import com.yunbao.mall.adapter.GoodsDetailAdapter;
import com.yunbao.mall.adapter.GoodsTitleAdapter;
import com.yunbao.mall.bean.GoodsChooseSpecBean;
import com.yunbao.mall.bean.GoodsCommentBean;
import com.yunbao.mall.bean.GoodsSpecBean;
import com.yunbao.mall.dialog.GoodsCertDialogFragment;
import com.yunbao.mall.dialog.GoodsSpecDialogFragment;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
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
 * 商品详情
 */
public class GoodsDetailActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String goodsId, int type) {
        forward(context, goodsId, false, type);
    }

    public static void forward(Context context, String goodsId, boolean fromShop, int type) {
        forward(context, goodsId, fromShop, type, "0");
    }

    public static void forward(final Context context, final String goodsId, final boolean fromShop, final int type, final String liveUid) {
        CommonHttpUtil.checkGoodsExist(goodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (type == 1) {
                        Intent intent = new Intent(context, GoodsOutSideDetailActivity.class);
                        intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
                        intent.putExtra(Constants.MALL_GOODS_FROM_SHOP, fromShop);
                        intent.putExtra(Constants.LIVE_UID, liveUid);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
                        intent.putExtra(Constants.MALL_GOODS_FROM_SHOP, fromShop);
                        intent.putExtra(Constants.LIVE_UID, liveUid);
                        context.startActivity(intent);
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }

    private ViewPager mViewPager;
    private GoodsTitleAdapter mTitleAdapter;
    private TextView mPageIndex;
    private TextView mGoodsPrice;
    private TextView mGoodsPriceYong;//佣金
    private TextView mGoodsName;
    private TextView mGoodsPostage;
    private TextView mGoodsSaleNum;
    private TextView mSellerAddress;//卖家地区
    private TextView mSaleNumAll;//总销量
    private TextView mGoodsQuality;//商品质量
    private TextView mTaiDuFuWu;//服务态度
    private TextView mTaiDuWuLiu;//物流态度
    private ImageView mShopThumb;
    private TextView mShopName;
    private View mBtnCommentMore;
    private String mUnitString;
    private MagicIndicator mIndicator;
    private String mCommentString;

    private View mGroupCommment;
    private View mGroupDetail;
    private RecyclerView mRecyclerViewComment;
    private RecyclerView mRecyclerViewDetail;
    private View mNoComment;
    private View mNoDetail;
    private TextView mCommentCountTextView;
    private int mTabIndex;

    private String mToUid;
    private String mGoodsId;
    private boolean mIsCanBuy;//是否可以购买
    private boolean mFromShop;
    private boolean mPaused;
    private List<GoodsChooseSpecBean> mSpecList;
    private double mPostageVal;

    private Drawable mBgCollect0;
    private Drawable mBgCollect1;
    private ImageView mImgCollect;
    private View mXiajiaStatus;//下架
    private boolean mXiajia;
    private String mLiveUid = "0";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void main() {
        mCommentString = WordUtil.getString(R.string.mall_131);
        Intent intent = getIntent();
        mGoodsId = intent.getStringExtra(Constants.MALL_GOODS_ID);
        mFromShop = intent.getBooleanExtra(Constants.MALL_GOODS_FROM_SHOP, false);
        mLiveUid = intent.getStringExtra(Constants.LIVE_UID);
        if (Constants.MALL_PLAT_UID.equals(mLiveUid)) {
            mLiveUid = "0";
        }
        mGoodsPrice = findViewById(R.id.goods_price);
        mGoodsPriceYong = findViewById(R.id.price_yong);
        mGoodsName = findViewById(R.id.goods_name);
        mGoodsPostage = findViewById(R.id.goods_postage);
        mGoodsSaleNum = findViewById(R.id.goods_sale_num);
        mSellerAddress = findViewById(R.id.address);

        mSaleNumAll = findViewById(R.id.sale_num_all);
        mGoodsQuality = findViewById(R.id.goods_quality);
        mTaiDuFuWu = findViewById(R.id.taidu_fuwu);
        mTaiDuWuLiu = findViewById(R.id.taidu_wuliu);
        mUnitString = WordUtil.getString(R.string.mall_168);
        mShopThumb = findViewById(R.id.shop_thumb);
        mShopName = findViewById(R.id.shop_name);
        mBtnCommentMore = findViewById(R.id.btn_comment_more);
        mXiajiaStatus = findViewById(R.id.xiajia_status);

        findViewById(R.id.btn_choose_spec).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);
        findViewById(R.id.btn_shop_home).setOnClickListener(this);
        findViewById(R.id.btn_shop).setOnClickListener(this);
        findViewById(R.id.btn_kefu).setOnClickListener(this);
        findViewById(R.id.btn_buy_now).setOnClickListener(this);
        findViewById(R.id.btn_collect).setOnClickListener(this);
        mBtnCommentMore.setOnClickListener(this);

        mPageIndex = findViewById(R.id.page_index);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mTitleAdapter != null) {
                    if (position == 0) {
                        mTitleAdapter.resumePlayVideo();
                    } else {
                        mTitleAdapter.pausePlayVideo();
                    }
                    if (mPageIndex != null) {
                        mPageIndex.setText(StringUtil.contact(String.valueOf(position + 1), "/", String.valueOf(mTitleAdapter.getCount())));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mIndicator = (MagicIndicator) findViewById(R.id.indicator);
        final String[] titles = new String[]{WordUtil.getString(R.string.mall_132), mCommentString};
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray3));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.textColor));
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTabIndex == index) {
                            return;
                        }
                        mTabIndex = index;
                        mIndicator.onPageScrollStateChanged(2);
                        mIndicator.onPageSelected(index);
                        mIndicator.onPageScrolled(index, 0, 0);
                        mIndicator.onPageScrollStateChanged(0);
                        tab(index);
                    }
                });
                if (index == 1) {
                    mCommentCountTextView = simplePagerTitleView;
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

        mGroupCommment = findViewById(R.id.group_comment);
        mGroupDetail = findViewById(R.id.group_detail);
        mRecyclerViewComment = findViewById(R.id.recyclerView_comment);
        mRecyclerViewDetail = findViewById(R.id.recyclerView_detail);
        mNoComment = findViewById(R.id.no_comment);
        mNoDetail = findViewById(R.id.no_detail);
        mRecyclerViewComment.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewDetail.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mImgCollect = findViewById(R.id.img_collect);
        mBgCollect0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_shop_collect_0);
        mBgCollect1 = ContextCompat.getDrawable(mContext, R.drawable.icon_shop_collect_0);
        getGoodsInfo();

    }

    /**
     * 获取商品详情，展示数据
     */
    private void getGoodsInfo() {
        MallHttpUtil.getGoodsInfo(mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    JSONObject goodsInfo = obj.getJSONObject("goods_info");
                    List<String> thumbs = JSON.parseArray(goodsInfo.getString("thumbs_format"), String.class);
                    mTitleAdapter = new GoodsTitleAdapter(mContext, goodsInfo.getString("video_url_format"),
                            goodsInfo.getString("video_thumb_format"), thumbs);
                    if (mViewPager != null) {
                        mViewPager.setOffscreenPageLimit(thumbs.size());
                        mViewPager.setAdapter(mTitleAdapter);
                    }
                    if (mPageIndex != null) {
                        mPageIndex.setText(StringUtil.contact("1/", String.valueOf(mTitleAdapter.getCount())));
                    }
                    if (mCommentCountTextView != null) {
                        mCommentCountTextView.setText(StringUtil.contact(mCommentString, "(", goodsInfo.getString("comment_nums"), ")"));
                    }
                    List<GoodsCommentBean> commentList = JSON.parseArray(obj.getString("comment_lists"), GoodsCommentBean.class);
                    if (commentList.size() == 0) {
                        if (mNoComment != null && mNoComment.getVisibility() != View.VISIBLE) {
                            mNoComment.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (mBtnCommentMore != null && mBtnCommentMore.getVisibility() != View.VISIBLE) {
                            mBtnCommentMore.setVisibility(View.VISIBLE);
                        }
                        if (mRecyclerViewComment != null) {
                            GoodsCommentAdapter commentAdapter = new GoodsCommentAdapter(mContext, commentList, false);
                            mRecyclerViewComment.setAdapter(commentAdapter);
                        }
                    }
                    String detailText = goodsInfo.getString("content");
                    boolean hasDetailText = !TextUtils.isEmpty(detailText);
                    List<String> detailList = new ArrayList<>();
                    if (hasDetailText) {
                        detailList.add(detailText);
                    }
                    JSONArray detailImgArray = goodsInfo.getJSONArray("pictures_format");
                    for (int i = 0, size = detailImgArray.size(); i < size; i++) {
                        detailList.add(detailImgArray.getString(i));
                    }
                    if (detailList.size() == 0) {
                        if (mNoDetail.getVisibility() != View.VISIBLE) {
                            mNoDetail.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (mRecyclerViewDetail != null) {
                            GoodsDetailAdapter detailAdapter = new GoodsDetailAdapter(mContext, detailList, hasDetailText);
                            mRecyclerViewDetail.setAdapter(detailAdapter);
                        }
                    }
                    mPostageVal = goodsInfo.getDoubleValue("postage");
                    if (mGoodsPostage != null) {
                        mGoodsPostage.setText(String.format(WordUtil.getString(R.string.mall_179), goodsInfo.getString("postage")));
                    }
                    if (mGoodsSaleNum != null) {
                        mGoodsSaleNum.setText(String.format(WordUtil.getString(R.string.mall_114), goodsInfo.getString("sale_nums")));
                    }
                    if (mGoodsName != null) {
                        mGoodsName.setText(goodsInfo.getString("name"));
                    }
                    mSpecList = JSON.parseArray(goodsInfo.getString("specs_format"), GoodsChooseSpecBean.class);
                    if (mSpecList != null && mSpecList.size() > 0) {
                        GoodsChooseSpecBean bean = mSpecList.get(0);
                        bean.setChecked(true);
                        if (mGoodsPrice != null) {
                            mGoodsPrice.setText(bean.getPrice());
                        }
                    }
                    if (goodsInfo.getIntValue("is_sale_platform") == 1 && mGoodsPriceYong != null) {
                        mGoodsPriceYong.setText(goodsInfo.getIntValue("type") == 2 ?
                                StringUtil.contact(WordUtil.getString(R.string.mall_408), WordUtil.getString(R.string.money_symbol), goodsInfo.getString("commission")) : null
                        );
                    }
                    JSONObject shopInfo = obj.getJSONObject("shop_info");
                    mToUid = shopInfo.getString("uid");
                    if (mShopThumb != null) {
                        ImgLoader.display(mContext, shopInfo.getString("avatar"), mShopThumb);
                    }
                    if (mShopName != null) {
                        mShopName.setText(shopInfo.getString("name"));
                    }
                    if (mSaleNumAll != null) {
                        mSaleNumAll.setText(String.format(mUnitString, shopInfo.getString("sale_nums")));
                    }
                    if (mGoodsQuality != null) {
                        mGoodsQuality.setText(shopInfo.getString("quality_points"));
                    }
                    if (mTaiDuFuWu != null) {
                        mTaiDuFuWu.setText(shopInfo.getString("service_points"));
                    }
                    if (mTaiDuWuLiu != null) {
                        mTaiDuWuLiu.setText(shopInfo.getString("express_points"));
                    }
                    if (mSellerAddress != null) {
                        mSellerAddress.setText(StringUtil.contact(shopInfo.getString("city"), shopInfo.getString("area")));
                    }
                    String sellerId = goodsInfo.getString("uid");
                    mIsCanBuy = !TextUtils.isEmpty(sellerId) && !sellerId.equals(CommonAppConfig.getInstance().getUid());
                    if (mIsCanBuy) {
                        setTitle(WordUtil.getString(R.string.mall_120));
                        MallHttpUtil.buyerAddBrowseRecord(mGoodsId);
                    } else {
                        setTitle(WordUtil.getString(R.string.mall_119));
                    }
                    showCollect(goodsInfo.getIntValue("iscollect") == 1);
                    mXiajia = goodsInfo.getIntValue("status") == -1;
                    if (mXiajia) {//下架
                        if (mXiajiaStatus != null && mXiajiaStatus.getVisibility() != View.VISIBLE) {
                            mXiajiaStatus.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }


    private void tab(int index) {
        if (index == 0) {
            if (mGroupCommment.getVisibility() != View.GONE) {
                mGroupCommment.setVisibility(View.GONE);
            }
            if (mGroupDetail.getVisibility() != View.VISIBLE) {
                mGroupDetail.setVisibility(View.VISIBLE);
            }
        } else {
            if (mGroupCommment.getVisibility() != View.VISIBLE) {
                mGroupCommment.setVisibility(View.VISIBLE);
            }
            if (mGroupDetail.getVisibility() != View.GONE) {
                mGroupDetail.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mViewPager != null && mViewPager.getCurrentItem() == 0 && mTitleAdapter != null) {
            mTitleAdapter.pausePlayVideo();
        }
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPaused) {
            if (mViewPager != null && mViewPager.getCurrentItem() == 0 && mTitleAdapter != null) {
                mTitleAdapter.resumePlayVideo();
            }
        }
        mPaused = false;

    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_GOODS_INFO);
        MallHttpUtil.cancel(MallHttpConsts.SET_GOODS_COLLECT);
        ImHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        if (mTitleAdapter != null) {
            mTitleAdapter.release();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_service) {
            clickService();
        } else if (id == R.id.btn_choose_spec || id == R.id.btn_buy_now) {
            chooseSpec();
        } else if (id == R.id.btn_shop_home || id == R.id.btn_shop) {
            forwardShopHome();
        } else if (id == R.id.btn_kefu) {
            if (Constants.MALL_PLAT_UID.equals(mToUid)) {
                DialogUitl.showSimpleTipDialog(mContext, null, true, WordUtil.getString(R.string.mall_412), true);
            } else {
                forwardChat();
            }
        } else if (id == R.id.btn_comment_more) {
            GoodsCommentActivity.forward(mContext, mGoodsId);
        } else if (id == R.id.btn_collect) {
            clickCollect();
        }
    }

    /**
     * 选择规格
     */
    private void chooseSpec() {
        if (!mIsCanBuy) {
            ToastUtil.show(R.string.mall_307);
            return;
        }
        if (mXiajia) {
            ToastUtil.show(R.string.mall_403);
            return;
        }
        if (mSpecList == null || mSpecList.size() == 0) {
            return;
        }
        GoodsSpecDialogFragment fragment = new GoodsSpecDialogFragment();
        fragment.setSpecList(mSpecList);
        fragment.show(getSupportFragmentManager(), "GoodsSpecDialogFragment");
    }

    /**
     * 服务资质
     */
    private void clickService() {
        if (!mIsCanBuy) {
            ToastUtil.show(R.string.mall_307);
            return;
        }
        GoodsCertDialogFragment fragment = new GoodsCertDialogFragment();
        fragment.show(getSupportFragmentManager(), "GoodsCertDialogFragment");
    }


    /**
     * 前往店铺
     */
    private void forwardShopHome() {
        if (!mIsCanBuy) {
            ToastUtil.show(R.string.mall_307);
            return;
        }
        if (mFromShop) {
            onBackPressed();
        } else {
            ShopHomeActivity.forward(mContext, mToUid);
        }
    }

    /**
     * 跳转到个人主页
     */
    private void forwardUserHome() {
        if (!mIsCanBuy) {
            ToastUtil.show(R.string.mall_307);
            return;
        }
        if (!TextUtils.isEmpty(mToUid)) {
            RouteUtil.forwardUserHome(mContext, mToUid);
        }
    }


    /**
     * 私信聊天
     */
    private void forwardChat() {
        if (!mIsCanBuy) {
            ToastUtil.show(R.string.mall_307);
            return;
        }
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        ImHttpUtil.getImUserInfo(mToUid, new HttpCallback() {
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

    /**
     * 下单
     */
    public void forwardMakeOrder(int countVal) {
        if (mSpecList == null || mSpecList.size() == 0) {
            return;
        }
        GoodsSpecBean specBean = null;
        for (GoodsChooseSpecBean bean : mSpecList) {
            if (bean.isChecked()) {
                specBean = bean;
                break;
            }
        }
        if (specBean == null) {
            return;
        }
        String shopName = mShopName.getText().toString();
        String goodsName = mGoodsName.getText().toString();
        GoodsMakeOrderActivity.forward(mContext, shopName, mGoodsId, goodsName, specBean, countVal, mPostageVal, mLiveUid);
    }


    private void showCollect(boolean isCollect) {
        if (mImgCollect != null) {
            mImgCollect.setImageDrawable(isCollect ? mBgCollect1 : mBgCollect0);
        }
    }


    /**
     * 点击收藏
     */
    private void clickCollect() {
        if (!mIsCanBuy) {
            ToastUtil.show(R.string.mall_307);
            return;
        }
        MallHttpUtil.setGoodsCollect(mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    showCollect(obj.getIntValue("iscollect") == 1);
                }
                ToastUtil.show(msg);
            }
        });
    }

}
