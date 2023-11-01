package com.yunbao.live.presenter;

import android.app.Dialog;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.activity.LiveAudienceActivity;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.live.dialog.LiveRoomCheckDialogFragment;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;

/**
 * Created by cxf on 2017/9/29.
 */

public class LiveRoomCheckLivePresenter2 {

    private Context mContext;
    private int mLiveType;//直播间的类型  普通 密码 门票 计时等
    private int mLiveTypeVal;//收费价格等
    private String mLiveTypeMsg;//直播间提示信息或房间密码
    private LiveBean mLiveBean;
    private ActionListener mActionListener;
    private int mLiveSdk;

    public LiveRoomCheckLivePresenter2(Context context, ActionListener actionListener) {
        mContext = context;
        mActionListener = actionListener;
    }

    /**
     * 观众 观看直播
     */
    public void checkLive(LiveBean bean) {
        mLiveBean = bean;
        LiveHttpUtil.checkLive(bean.getUid(), bean.getStream(), mCheckLiveCallback);
    }

    private HttpCallback mCheckLiveCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mLiveType = obj.getIntValue("type");
                    mLiveTypeVal = obj.getIntValue("type_val");
                    mLiveTypeMsg = obj.getString("type_msg");
                    if (CommonAppConfig.LIVE_SDK_CHANGED) {
                        mLiveSdk = obj.getIntValue("live_sdk");
                    } else {
                        mLiveSdk = CommonAppConfig.LIVE_SDK_USED;
                    }
                    if (mLiveType == Constants.LIVE_TYPE_NORMAL) {
                        enterLiveRoom();
                    } else {
                        LiveRoomCheckDialogFragment fragment = new LiveRoomCheckDialogFragment();
                        if (mLiveType == Constants.LIVE_TYPE_PWD) {
                            fragment.setLiveType(mLiveType, mLiveTypeMsg);
                        } else {
                            fragment.setLiveType(mLiveType, String.valueOf(mLiveTypeVal));
                        }
                        fragment.setActionListener(new LiveRoomCheckDialogFragment.ActionListener() {
                            @Override
                            public void onCloseClick() {
                                if (mContext != null && mContext instanceof LiveAudienceActivity) {
                                    ((LiveAudienceActivity) mContext).exitLiveRoom();
                                }
                            }

                            @Override
                            public void onConfirmClick() {
                                if (mLiveType == Constants.LIVE_TYPE_PWD) {
                                    enterLiveRoom();
                                } else {
                                    roomCharge();
                                }
                            }

                            @Override
                            public void onNextClick() {
                                if (mContext != null && mContext instanceof LiveAudienceActivity) {
                                    ((LiveAudienceActivity) mContext).scrollNextPosition();
                                }
                            }
                        });
                        fragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "LiveRoomCheckDialogFragment");
                    }

                }
            } else {
                ToastUtil.show(msg);
            }
        }

        @Override
        public boolean showLoadingDialog() {
            return true;
        }

        @Override
        public Dialog createLoadingDialog() {
            return DialogUitl.loadingDialog(mContext);
        }
    };


    public void roomCharge() {
        if (mLiveBean == null) {
            return;
        }
        LiveHttpUtil.roomCharge(mLiveBean.getUid(), mLiveBean.getStream(), mRoomChargeCallback);
    }

    private HttpCallback mRoomChargeCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0) {
                enterLiveRoom();
            } else {
                ToastUtil.show(msg);
            }
        }

        @Override
        public boolean showLoadingDialog() {
            return true;
        }

        @Override
        public Dialog createLoadingDialog() {
            return DialogUitl.loadingDialog(mContext);
        }
    };

    public void cancel() {
        mActionListener = null;
        LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE);
        LiveHttpUtil.cancel(LiveHttpConsts.ROOM_CHARGE);
    }

    /**
     * 进入直播间
     */
    private void enterLiveRoom() {
        if (mActionListener != null) {
            mActionListener.onLiveRoomChanged(mLiveBean, mLiveType, mLiveTypeVal, mLiveSdk);
        }
    }


    public interface ActionListener {
        void onLiveRoomChanged(LiveBean liveBean, int liveType, int liveTypeVal, int liveSdk);
    }
}
