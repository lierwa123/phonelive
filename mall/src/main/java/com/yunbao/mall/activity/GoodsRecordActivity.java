package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.GoodsRecordAdapter;
import com.yunbao.mall.bean.GoodsRecordBean;
import com.yunbao.mall.bean.GoodsRecordItemBean;
import com.yunbao.mall.bean.GoodsRecordTitleBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 浏览记录
 */
public class GoodsRecordActivity extends AbsActivity implements View.OnClickListener, GoodsRecordAdapter.ActionListener {

    public static void forward(Context context) {
        context.startActivity(new Intent(context, GoodsRecordActivity.class));
    }


    private CommonRefreshView mRefreshView;
    private GoodsRecordAdapter mAdapter;
    private TextView mBtnManage;
    private View mBottom;
    private View mBtnDelete;
    private ImageView mImgCheck;
    private HttpCallback mDeleteCallback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_record;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_169));
        mBtnManage = findViewById(R.id.btn_manage);
        mBottom = findViewById(R.id.bottom);
        mBtnDelete = findViewById(R.id.btn_delete);
        mImgCheck = findViewById(R.id.img_check);

        mBtnManage.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        findViewById(R.id.btn_check_all).setOnClickListener(this);

        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_buyer_record);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsRecordBean>() {
            @Override
            public RefreshAdapter<GoodsRecordBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new GoodsRecordAdapter(mContext);
                    mAdapter.setActionListener(GoodsRecordActivity.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MallHttpUtil.getBuyerGoodsRecord(p, callback);
            }

            @Override
            public List<GoodsRecordBean> processData(String[] info) {
                JSONArray array = JSON.parseArray(Arrays.toString(info));
                List<GoodsRecordBean> list = new ArrayList<>();
                for (int i = 0, size = array.size(); i < size; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    GoodsRecordTitleBean titleBean = new GoodsRecordTitleBean();
                    titleBean.setDate(obj.getString("date"));
                    list.add(titleBean);
                    List<GoodsRecordItemBean> itemList = JSON.parseArray(obj.getString("list"), GoodsRecordItemBean.class);
                    titleBean.setItemList(itemList);
                    for (GoodsRecordItemBean bean : itemList) {
                        bean.setParent(titleBean);
                    }
                    list.addAll(itemList);
                }
                return list;
            }

            @Override
            public void onRefreshSuccess(List<GoodsRecordBean> list, int listCount) {
                if (mBtnManage != null) {
                    if (list != null && list.size() > 0) {
                        if (mBtnManage.getVisibility() != View.VISIBLE) {
                            mBtnManage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (mBtnManage.getVisibility() == View.VISIBLE) {
                            mBtnManage.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                if (mBottom != null && mBottom.getVisibility() != View.GONE) {
                    mBottom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<GoodsRecordBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mRefreshView.initData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_manage) {
            edit();
        } else if (id == R.id.btn_check_all) {
            checkedAll();
        } else if (id == R.id.btn_delete) {
            delete();
        }
    }

    /**
     * 管理 取消
     */
    private void edit() {
        if (mAdapter != null) {
            mAdapter.toggleEdit();
        }
    }

    /**
     * 全选
     */
    private void checkedAll() {
        if (mAdapter != null) {
            mAdapter.toggleCheckedAll();
        }
    }

    /**
     * 删除
     */
    private void delete() {
        if (mAdapter == null) {
            return;
        }
        String recordId = mAdapter.getCheckedId();
        if (TextUtils.isEmpty(recordId)) {
            return;
        }
        if (mDeleteCallback == null) {
            mDeleteCallback = new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        if (mRefreshView != null) {
                            mRefreshView.initData();
                        }
                    }
                    ToastUtil.show(msg);
                }
            };
        }
        MallHttpUtil.buyerDeleteBrowseRecord(recordId, mDeleteCallback);
    }

    @Override
    public void onCheckedItem(boolean checked) {
        if (mBtnDelete != null) {
            mBtnDelete.setEnabled(checked);
        }
    }

    @Override
    public void onCheckedAll(Drawable drawable) {
        if (mImgCheck != null) {
            mImgCheck.setImageDrawable(drawable);
        }
    }

    @Override
    public void onEditChanged(boolean isEdit) {
        if (isEdit) {
            if (mBtnManage != null) {
                mBtnManage.setText(R.string.cancel);
            }
            if (mBottom != null && mBottom.getVisibility() != View.VISIBLE) {
                mBottom.setVisibility(View.VISIBLE);
            }
        } else {
            if (mBtnManage != null) {
                mBtnManage.setText(R.string.manage);
            }
            if (mBottom != null && mBottom.getVisibility() != View.GONE) {
                mBottom.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BUYER_GOODS_RECORD);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_DELETE_BROWSE_RECORD);
        super.onDestroy();
    }
}
