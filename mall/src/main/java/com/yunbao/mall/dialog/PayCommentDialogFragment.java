package com.yunbao.mall.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.custom.RatingBar;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.PayContentDetailActivity;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

/**
 * 付费内容 评价
 */
public class PayCommentDialogFragment extends AbsDialogFragment implements RatingBar.OnRatingChangedListener, View.OnClickListener {

    private RatingBar mStar;
    private TextView mTipText;
    private int[] mTips;
    private String mGoodsId;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pay_comment;
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
        params.height = DpUtil.dp2px(240);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mGoodsId = bundle.getString(Constants.MALL_GOODS_ID);
        }
        mStar = findViewById(R.id.rating_bar);
        mStar.setOnRatingChangedListener(this);
        mTipText = findViewById(R.id.tip);
        mTips = new int[]{R.string.mall_360, R.string.mall_361, R.string.mall_362, R.string.mall_363, R.string.mall_364};
        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onRatingChanged(int curCount, int maxCount) {
        if (mTipText != null) {
            if (curCount == 0) {
                mTipText.setText(null);
            } else {
                mTipText.setText(mTips[curCount - 1]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mGoodsId)) {
            return;
        }
        int starCount = mStar.getFillCount();
        if (starCount == 0) {
            return;
        }
        MallHttpUtil.commentPayContent(mGoodsId, starCount, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ((PayContentDetailActivity) mContext).getData();
                    dismiss();
                }
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.COMMENT_PAY_CONTENT);
        mContext = null;
        super.onDestroy();
    }
}
