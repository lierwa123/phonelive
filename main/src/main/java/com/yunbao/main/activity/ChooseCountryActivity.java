package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.SideIndexBar;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.ChooseCountryAdapter;
import com.yunbao.main.bean.ChooseCountryBean;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.suspension.SuspensionDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseCountryActivity extends AbsActivity implements View.OnClickListener, OnItemClickListener<ChooseCountryBean> {

    private SideIndexBar mIndexBar;
    private RecyclerView mRecyclerView;
    private SuspensionDecoration mDecoration;
    private LinearLayoutManager mLayoutManager;
    private ChooseCountryAdapter mAdapter;
    private ChooseCountryAdapter mSearchAdapter;
    private SparseIntArray mSparseArray;
    private CommonRefreshView mRefreshView;
    private EditText mEditText;
    private View mBtnClear;
    private MyHandler mHandler;
    private String mKey;
    private InputMethodManager imm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_country;
    }


    @Override
    protected void main() {
        mSparseArray = new SparseIntArray();
        mIndexBar = findViewById(R.id.index_bar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mIndexBar.setActionListener(new SideIndexBar.ActionListener() {
            @Override
            public void onSelectionPosition(int position) {
                int pos = mSparseArray.get(position);
                if (mLayoutManager != null) {
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
        mDecoration = new SuspensionDecoration(mContext);
        mDecoration.setActionListener(new SuspensionDecoration.ActionListener() {
            @Override
            public void onTagChanged(String tag) {
                if (mIndexBar != null) {
                    mIndexBar.setSelectionIndex(tag);
                }
            }
        });
        mDecoration.setHeaderViewCount(0);
        mRecyclerView.addItemDecoration(mDecoration);
        mHandler = new MyHandler(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mBtnClear = findViewById(R.id.btn_clear);
        mBtnClear.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.edit);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
                MainHttpUtil.cancel(MainHttpConsts.GET_COUNTRY_CODE);
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                if (!TextUtils.isEmpty(s)) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 500);
                    }
                    if (mBtnClear != null && mBtnClear.getVisibility() != View.VISIBLE) {
                        mBtnClear.setVisibility(View.VISIBLE);
                    }
                } else {
                    mKey = null;
                    if (mSearchAdapter != null) {
                        mSearchAdapter.clearData();
                    }
                    if (mBtnClear != null && mBtnClear.getVisibility() == View.VISIBLE) {
                        mBtnClear.setVisibility(View.INVISIBLE);
                    }
                    if (mRefreshView.getVisibility() == View.VISIBLE) {
                        mRefreshView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<ChooseCountryBean>() {
            @Override
            public RefreshAdapter<ChooseCountryBean> getAdapter() {
                if (mSearchAdapter == null) {
                    mSearchAdapter = new ChooseCountryAdapter(mContext);
                    mSearchAdapter.setOnItemClickListener(ChooseCountryActivity.this);
                }
                return mSearchAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mKey)) {
                    MainHttpUtil.getCountryCode(mKey, callback);
                }
            }

            @Override
            public List<ChooseCountryBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), ChooseCountryBean.class);
            }

            @Override
            public void onRefreshSuccess(List<ChooseCountryBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<ChooseCountryBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });

        MainHttpUtil.getCountryCode(null, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

                if (code == 0) {
                    JSONArray arr = (JSONArray) JSON.parse(Arrays.toString(info));
                    final List<String> indexList = new ArrayList<>();
                    List<ChooseCountryBean> cityList = new ArrayList<>();
                    for (int i = 0, size = arr.size(); i < size; i++) {
                        mSparseArray.put(i, cityList.size());
                        JSONObject obj = arr.getJSONObject(i);
                        String index = obj.getString("title");
                        JSONArray arr2 = obj.getJSONArray("lists");
                        if (arr2.size() > 0) {
                            indexList.add(index);
                        }
                        for (int j = 0, size2 = arr2.size(); j < size2; j++) {
                            ChooseCountryBean bean = JSON.parseObject(arr2.getString(j), ChooseCountryBean.class);
                            bean.setIndex(index);
                            cityList.add(bean);
                        }
                    }
                    if (mIndexBar != null) {
                        mIndexBar.setData(indexList);
                    }
                    if (mDecoration != null) {
                        mDecoration.setmDatas(cityList);
                    }
                    if (mRecyclerView != null) {
                        mAdapter = new ChooseCountryAdapter(mContext, cityList);
                        mAdapter.setOnItemClickListener(ChooseCountryActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            }
        });

    }

    @Override
    public void onItemClick(ChooseCountryBean bean, int position) {
        if (imm != null && mEditText != null) {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.TO_NAME, bean.getTel());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.GET_COUNTRY_CODE);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        super.onDestroy();
    }

    private void search() {
        String key = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ToastUtil.show(R.string.content_empty);
            return;
        }
        MainHttpUtil.cancel(MainHttpConsts.GET_COUNTRY_CODE);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mKey = key;
        if (mRefreshView != null) {
            if (mRefreshView.getVisibility() != View.VISIBLE) {
                mRefreshView.setVisibility(View.VISIBLE);
            }
            mRefreshView.initData();
        }

    }

    @Override
    public void onClick(View v) {
        if (mEditText != null) {
            mEditText.setText(null);
        }
    }


    private static class MyHandler extends Handler {

        private ChooseCountryActivity mActivity;

        public MyHandler(ChooseCountryActivity activity) {
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
