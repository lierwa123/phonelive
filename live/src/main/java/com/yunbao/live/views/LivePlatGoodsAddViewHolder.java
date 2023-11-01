package com.yunbao.live.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.GoodsBean;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsLivePageViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveAnchorActivity;
import com.yunbao.live.adapter.LivePlatGoodsAddAdapter;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class LivePlatGoodsAddViewHolder extends AbsLivePageViewHolder {

    private EditText mEditText;
    private String mKey;
    private CommonRefreshView mRefreshView;
    private LivePlatGoodsAddAdapter mLiveGoodsAddAdapter;
    private MyHandler mHandler;


    public LivePlatGoodsAddViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_goods_add;
    }


    @Override
    public void init() {
        super.init();
        TextView textView = findViewById(R.id.titleView);
        textView.setText(WordUtil.getString(R.string.goods_tip_21));
        mEditText = findViewById(R.id.edit);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LiveHttpUtil.cancel(LiveHttpConsts.SEARCH_LIVE_GOODS_LIST);
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    search();
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
                LiveHttpUtil.cancel(LiveHttpConsts.SEARCH_LIVE_GOODS_LIST);
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(0, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_shop_add);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsBean>() {
            @Override
            public RefreshAdapter<GoodsBean> getAdapter() {
                if (mLiveGoodsAddAdapter == null) {
                    mLiveGoodsAddAdapter = new LivePlatGoodsAddAdapter(mContext);
                }
                return mLiveGoodsAddAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                LiveHttpUtil.searchLivePlatGoodsList(p, mKey, callback);
            }

            @Override
            public List<GoodsBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), GoodsBean.class);
            }

            @Override
            public void onRefreshSuccess(List<GoodsBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<GoodsBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mHandler = new MyHandler(this);
    }


    private void search() {
        mKey = mEditText.getText().toString().trim();
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }


    @Override
    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }


    @Override
    public void hide() {
        super.hide();
        if (mLiveGoodsAddAdapter != null) {
            mLiveGoodsAddAdapter.clearData();
        }
        if(mContext!=null){
            ((LiveAnchorActivity)mContext).openGoodsWindow();
        }
    }


    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.SEARCH_LIVE_PLAT_GOODS_LIST);
        LiveHttpUtil.cancel(LiveHttpConsts.SET_PLAT_GOODS_SALE);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }


    private static class MyHandler extends Handler {

        private LivePlatGoodsAddViewHolder mViewHolder;

        public MyHandler(LivePlatGoodsAddViewHolder viewHolder) {
            mViewHolder = new WeakReference<>(viewHolder).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mViewHolder != null) {
                mViewHolder.search();
            }
        }

        public void release() {
            mViewHolder = null;
        }
    }
}
