package com.yunbao.mall.dialog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.adapter.GoodsChooseSpecAdapter;
import com.yunbao.mall.bean.GoodsChooseSpecBean;

import java.util.List;

public class GoodsSpecDialogFragment extends AbsDialogFragment implements OnItemClickListener<GoodsChooseSpecBean>, View.OnClickListener {


    private List<GoodsChooseSpecBean> mSpecList;
    private ImageView mThumb;
    private TextView mGoodsPrice;
    private TextView mGoodsNum;//库存
    private TextView mGoodsName;
    private TextView mCount;//购买数量
    private View mBtnReduce;
    private View mBtnAdd;
    private int mCountVal;
    private String mGoodsNumString;
    private String mGoodsNameString;
    private String mMoneySymbol;


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_goods_spec;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mThumb = findViewById(R.id.thumb);
        mGoodsPrice = findViewById(R.id.goods_price);
        mGoodsNum = findViewById(R.id.goods_num);
        mGoodsName = findViewById(R.id.goods_name);
        mCount = findViewById(R.id.count);
        mBtnReduce = findViewById(R.id.btn_reduce);
        mBtnAdd = findViewById(R.id.btn_add);

        mGoodsNumString = WordUtil.getString(R.string.mall_177);
        mGoodsNameString = WordUtil.getString(R.string.mall_178);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_buy).setOnClickListener(this);
        mBtnReduce.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);

        if (mSpecList != null && mSpecList.size() > 0) {
            GoodsChooseSpecBean checkedSpecBean = null;
            for (GoodsChooseSpecBean bean : mSpecList) {
                if (bean.isChecked()) {
                    checkedSpecBean = bean;
                    break;
                }
            }
            if (checkedSpecBean != null) {
                showCheckedSpec(checkedSpecBean);
            }
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new FlexboxLayoutManager(mContext));
            GoodsChooseSpecAdapter adapter = new GoodsChooseSpecAdapter(mContext, mSpecList);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }


    }

    private void showCheckedSpec(GoodsChooseSpecBean bean) {
        ImgLoader.display(mContext, bean.getThumb(), mThumb);
        mGoodsPrice.setText(StringUtil.contact(mMoneySymbol, bean.getPrice()));
        mGoodsNum.setText(String.format(mGoodsNumString, bean.getNum()));
        mGoodsName.setText(String.format(mGoodsNameString, bean.getName()));
        mCountVal = 1;
        mCount.setText(String.valueOf(mCountVal));
        mBtnReduce.setEnabled(false);
    }


    public void setSpecList(List<GoodsChooseSpecBean> list) {
        mSpecList = list;
    }

    @Override
    public void onItemClick(GoodsChooseSpecBean bean, int position) {
        showCheckedSpec(bean);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reduce) {
            reduce();
        } else if (id == R.id.btn_add) {
            add();
        } else if (id == R.id.btn_buy) {
            makeOrder();
        } else if (id == R.id.btn_close) {
            dismiss();
        }
    }

    private void reduce() {
        if (mCountVal > 1) {
            mCountVal--;
            mCount.setText(String.valueOf(mCountVal));
            if (mCountVal == 1) {
                mBtnReduce.setEnabled(false);
            }
        }
    }

    private void add() {
        mCountVal++;
        mCount.setText(String.valueOf(mCountVal));
        if (mCountVal > 1) {
            mBtnReduce.setEnabled(true);
        }
    }

    private void makeOrder() {
        if (mContext != null) {
            dismiss();
            ((GoodsDetailActivity) mContext).forwardMakeOrder(mCountVal);
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mSpecList = null;
        super.onDestroy();
    }
}
