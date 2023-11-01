package com.yunbao.main.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.ActiveRecomTopicAdapter;
import com.yunbao.main.adapter.ActiveSearchTopicAdapter;
import com.yunbao.main.bean.ActiveTopicBean;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/**
 * 动态搜索话题
 */
public class ActiveSearchTopicActivity extends AbsActivity {

    private CommonRefreshView mRefreshView;
    private EditText mEditText;
    private View mGroupRecommend;
    private String mKey;
    private MyHandler mHandler;
    private InputMethodManager imm;
    private ActiveSearchTopicAdapter mAdapter;
    private RecyclerView mRecyclerViewRecommend;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_topic;
    }

    @Override
    protected void main() {
        mGroupRecommend = findViewById(R.id.group_recommend);
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
                MainHttpUtil.cancel(MainHttpConsts.SEARCH_ACTIVE_TOPIC);
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
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_search);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<ActiveTopicBean>() {
            @Override
            public RefreshAdapter<ActiveTopicBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new ActiveSearchTopicAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.searchActiveTopic(mKey, p, callback);
            }

            @Override
            public List<ActiveTopicBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), ActiveTopicBean.class);
            }

            @Override
            public void onRefreshSuccess(List<ActiveTopicBean> list, int listCount) {
                if (imm != null && mEditText != null) {
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<ActiveTopicBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mHandler = new MyHandler(this);
        mRecyclerViewRecommend = findViewById(R.id.recyclerView_recommend);
        mRecyclerViewRecommend.setHasFixedSize(true);
        mRecyclerViewRecommend.setLayoutManager(new FlexboxLayoutManager(mContext));
        MainHttpUtil.getActiveRecomTopic(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (mRecyclerViewRecommend != null) {
                        List<ActiveTopicBean> list = JSON.parseArray(Arrays.toString(info), ActiveTopicBean.class);
                        ActiveRecomTopicAdapter adapter = new ActiveRecomTopicAdapter(mContext, list);
                        mRecyclerViewRecommend.setAdapter(adapter);
                    }
                }
            }
        });

    }

    public void search() {
        mKey = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(mKey)) {
            if (mAdapter != null) {
                mAdapter.clearData();
            }
            if (mGroupRecommend != null && mGroupRecommend.getVisibility() != View.VISIBLE) {
                mGroupRecommend.setVisibility(View.VISIBLE);
            }
        } else {
            if (mGroupRecommend != null && mGroupRecommend.getVisibility() == View.VISIBLE) {
                mGroupRecommend.setVisibility(View.INVISIBLE);
            }
            if (mRefreshView != null) {
                mRefreshView.initData();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.release();
        }
        mHandler = null;
        MallHttpUtil.cancel(MainHttpConsts.GET_ACTIVE_RECOM_TOPIC);
        MallHttpUtil.cancel(MainHttpConsts.SEARCH_ACTIVE_TOPIC);
        super.onDestroy();
    }

    private static class MyHandler extends Handler {

        private ActiveSearchTopicActivity mActivity;

        public MyHandler(ActiveSearchTopicActivity activity) {
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
