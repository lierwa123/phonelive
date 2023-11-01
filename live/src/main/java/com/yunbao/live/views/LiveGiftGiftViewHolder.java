package com.yunbao.live.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.LiveGiftBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.List;

/**
 * Created by cxf on 2019/8/28.
 * 礼物 礼物
 */

public class LiveGiftGiftViewHolder extends AbsLiveGiftViewHolder {


    private TextView mCoin;

    public LiveGiftGiftViewHolder(Context context, ViewGroup parentView, String liveUid, String stream) {
        super(context, parentView, liveUid, stream);
    }

    @Override
    protected void processArguments(Object... args) {
        mLiveUid = (String) args[0];
        mStream = (String) args[1];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_gift_gift;
    }

    @Override
    public void init() {
        super.init();
        mCoin = (TextView) findViewById(R.id.coin);
        findViewById(R.id.btn_charge).setOnClickListener(this);
    }

    public void loadData() {
        if (mActionListener != null) {
            mActionListener.onCountChanged(mCount);
        }
        if (!isFirstLoadData()) {
            return;
        }
//        List<LiveGiftBean> giftList = null;
//        String giftListJson = CommonAppConfig.getInstance().getGiftListJson();
//        if (!TextUtils.isEmpty(giftListJson)) {
//            try {
//                giftList = JSON.parseArray(giftListJson, LiveGiftBean.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (giftList == null) {
//            LiveHttpUtil.getGiftList(new HttpCallback() {
//                @Override
//                public void onSuccess(int code, String msg, String[] info) {
//                    if (code == 0 && info.length > 0) {
//                        JSONObject obj = JSON.parseObject(info[0]);
//                        String giftJson = obj.getString("giftlist");
//                        List<LiveGiftBean> list = JSON.parseArray(giftJson, LiveGiftBean.class);
//                        CommonAppConfig.getInstance().setGiftListJson(giftJson);
//                        CommonAppConfig.getInstance().setGiftDaoListJson(obj.getString("proplist"));
//                        showGiftList(list);
//                        mCoin.setText(obj.getString("coin"));
//                    }
//                }
//
//                @Override
//                public void onFinish() {
//                    if (mLoading != null) {
//                        mLoading.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
//
//        } else {
//            for (LiveGiftBean bean : giftList) {
//                bean.setChecked(false);
//            }
//            mLoading.setVisibility(View.INVISIBLE);
//            showGiftList(giftList);
//            LiveHttpUtil.getCoin(new HttpCallback() {
//                @Override
//                public void onSuccess(int code, String msg, String[] info) {
//                    if (code == 0 && info.length > 0) {
//                        mCoin.setText(JSONObject.parseObject(info[0]).getString("coin"));
//                    }
//                }
//            });
//        }


        LiveHttpUtil.getGiftList(((LiveActivity) mContext).isVoiceChatRoom() ? 1 : 0, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String giftJson = obj.getString("giftlist");
                    List<LiveGiftBean> list = JSON.parseArray(giftJson, LiveGiftBean.class);
//                    CommonAppConfig.getInstance().setGiftListJson(giftJson);
                    CommonAppConfig.getInstance().setGiftDaoListJson(obj.getString("proplist"));
                    showGiftList(list);
                    mCoin.setText(obj.getString("coin"));
                }
            }

            @Override
            public void onFinish() {
                if (mLoading != null) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    @Override
    public int getGiftType() {
        return Constants.GIFT_TYPE_NORMAL;
    }


    public void setCoinString(String coinString) {
        if (mCoin != null) {
            mCoin.setText(coinString);
        }
    }

}
