package com.yunbao.mall.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.SellerRejectRefundActivity;
import com.yunbao.mall.bean.RejectRefundBean;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.widget.WheelView;

/**
 * 卖家 选择拒绝原因
 */
public class SellerRejectReasonDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private WheelView mWheelView;
    private List<RejectRefundBean> mList;
    private int mCheckIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_seller_refund_reason;
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
        if (mList == null) {
            return;
        }
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_done).setOnClickListener(this);
        List<String> stringList = new ArrayList<>();
        for (RejectRefundBean bean : mList) {
            stringList.add(bean.getName());
        }
        mWheelView = (WheelView) findViewById(R.id.wheelView);
        mWheelView.setTextSize(17);
        mWheelView.setTextColor(0xff646464, 0xffff5878);
        mWheelView.setCycleDisable(true);//禁用循环
        mWheelView.setGravity(Gravity.CENTER);
        mWheelView.setVisibleItemCount(5);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setColor(0xffe6e6e6);//线颜色
        config.setRatio(1f);//线比率
        mWheelView.setDividerConfig(config);
        mWheelView.setItems(stringList);
        mWheelView.setSelectedIndex(0);
        mWheelView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int i) {
                mCheckIndex = i;
            }
        });
    }

    public void setList(List<RejectRefundBean> list) {
        mList = list;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_done) {
            if (mContext != null && mList != null) {
                ((SellerRejectRefundActivity) mContext).setRejectReason(mList.get(mCheckIndex));
                dismiss();
            }
        }
    }


    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
