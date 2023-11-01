package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.TieZhiAdapter;
import com.yunbao.beauty.bean.TieZhiBean;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.common.views.AbsCommonViewHolder;

import java.util.List;

public class MhTieZhiChildViewHolder extends AbsCommonViewHolder implements OnItemClickListener<TieZhiBean> {

    private int mId;
    private RecyclerView mRecyclerView;
    private TieZhiAdapter mAdapter;
    private ActionListener mActionListener;

    public MhTieZhiChildViewHolder(Context context, ViewGroup parentView, int id) {
        super(context, parentView, id);
    }

    @Override
    protected void processArguments(Object... args) {
        mId = (int) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_tiezhi_child;
    }

    @Override
    public void init() {
        mRecyclerView = (RecyclerView) mContentView;
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5, GridLayoutManager.VERTICAL, false));
    }

    @Override
    public void loadData() {
        if (!mFirstLoadData) {
            return;
        }
        MhDataManager.getTieZhiList(mId, new CommonCallback<String>() {
            @Override
            public void callback(String jsonStr) {
                if (TextUtils.isEmpty(jsonStr)) {
                    return;
                }
                try {
                    JSONObject obj = JSON.parseObject(jsonStr);
                    List<TieZhiBean> list = JSON.parseArray(obj.getString("list"), TieZhiBean.class);
                    if (list != null && list.size() > 0) {
                        for (TieZhiBean bean : list) {
                            bean.checkDownloaded();
                        }
                        mAdapter = new TieZhiAdapter(mContext, list);
                        mAdapter.setOnItemClickListener(MhTieZhiChildViewHolder.this);
                        if (mRecyclerView != null) {
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        if (mActionListener != null) {
                            if ("".equals(mActionListener.getCheckedTieZhiName())) {
                                setCheckedPosition(0);
                            }
                        }
                    }
                    mFirstLoadData = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setCheckedPosition(int position) {
        if (mAdapter != null) {
            mAdapter.setCheckedPosition(position);
        }
    }

    @Override
    public void onItemClick(TieZhiBean bean, int position) {
        if (mActionListener != null) {
            mActionListener.onTieZhiChecked(this, bean);
        }
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onTieZhiChecked(MhTieZhiChildViewHolder vh, TieZhiBean bean);

        String getCheckedTieZhiName();
    }
}
