package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MallSearchAdapter;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.mall.bean.GoodsSimpleBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class MallSearchActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context) {
        context.startActivity(new Intent(context, MallSearchActivity.class));
    }

    private static final String SORT_DESC = "desc";
    private static final String SORT_ASC = "asc";
    private static final String SORT_NONE = "";
    private EditText mEditText;
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
    private String mSaleSort = SORT_NONE;
    private String mPriceSort = SORT_NONE;
    private int mIsNew;
    private CommonRefreshView mRefreshView;
    private MallSearchAdapter mAdapter;
    private String mKey;
    private MyHandler mHandler;
    private ImageView mBtnChange;
    private Drawable mDrawable0;
    private Drawable mDrawable1;
    private boolean mLayoutLinear;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private InputMethodManager imm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_search;
    }

    @Override
    protected void main() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = findViewById(R.id.edit);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    MallHttpUtil.cancel(MallHttpConsts.SEARCH_GOODS_LIST);
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    if (TextUtils.isEmpty(mEditText.getText().toString())) {
                        ToastUtil.show(R.string.content_empty);
                    } else {
                        search();
                    }
                    return true;
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainHttpUtil.cancel(MainHttpConsts.SEARCH_GOODS_LIST);
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(0, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_main_mall);
        mGridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        mRefreshView.setLayoutManager(mGridLayoutManager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 10, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsSimpleBean>() {
            @Override
            public RefreshAdapter<GoodsSimpleBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MallSearchAdapter(mContext);
                    mAdapter.setLayoutLinear(mLayoutLinear);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.searchGoodsList(mKey, mSaleSort, mPriceSort, mIsNew, p, callback);
            }

            @Override
            public List<GoodsSimpleBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), GoodsSimpleBean.class);
            }

            @Override
            public void onRefreshSuccess(List<GoodsSimpleBean> list, int listCount) {
                if (imm != null && mEditText != null) {
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
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
        mDrawable0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_mall_search_heng);
        mDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_mall_search_shu);
        mBtnChange = findViewById(R.id.btn_change);
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutLinear = !mLayoutLinear;
                if (mLayoutLinear) {
                    mBtnChange.setImageDrawable(mDrawable1);
                    if (mLinearLayoutManager == null) {
                        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    }
                    if (mRefreshView != null) {
                        mRefreshView.setLayoutManager(mLinearLayoutManager);
                    }
                    if (mAdapter != null) {
                        mAdapter.setLayoutLinear(mLayoutLinear);
                        mAdapter.notifyDataSetChanged();
                    }

                } else {
                    mBtnChange.setImageDrawable(mDrawable0);
                    if (mRefreshView != null) {
                        mRefreshView.setLayoutManager(mGridLayoutManager);
                    }
                    if (mAdapter != null) {
                        mAdapter.setLayoutLinear(mLayoutLinear);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        mEditText.requestFocus();
        mHandler = new MyHandler(this);
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


    public void search() {
        mKey = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(mKey)) {
            if (mAdapter != null) {
                mAdapter.clearData();
            }
        } else {
            loadData();
        }
    }


    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.release();
        }
        mHandler = null;
        MainHttpUtil.cancel(MainHttpConsts.SEARCH_GOODS_LIST);
        super.onDestroy();
    }

    private static class MyHandler extends Handler {

        private MallSearchActivity mActivity;

        public MyHandler(MallSearchActivity activity) {
            mActivity = new WeakReference<>(activity).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                mActivity.search();
            }
        }

        public void release() {
            removeCallbacksAndMessages(null);
            mActivity = null;
        }
    }
}
