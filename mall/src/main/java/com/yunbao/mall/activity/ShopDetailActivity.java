package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;

/**
 * 小店详情
 */
public class ShopDetailActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String certText, String certImgUrl) {
        Intent intent = new Intent(context, ShopDetailActivity.class);
        intent.putExtra(Constants.MALL_CERT_TEXT, certText);
        intent.putExtra(Constants.MALL_CERT_IMG, certImgUrl);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_147));
        findViewById(R.id.btn_cert).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cert) {
            forwardCert();
        }
    }

    /**
     * 资质证明
     */
    private void forwardCert() {
        Intent intent = getIntent();
        String text = intent.getStringExtra(Constants.MALL_CERT_TEXT);
        String imgUrl = intent.getStringExtra(Constants.MALL_CERT_IMG);
        ShopCertActivity.forward(mContext, text, imgUrl);
    }
}
