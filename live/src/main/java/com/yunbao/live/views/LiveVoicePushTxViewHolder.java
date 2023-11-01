package com.yunbao.live.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.live.TXLivePusher;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.List;

/**
 * Created by cxf on 2018/10/7.
 * 腾讯云直播推流 语音聊天室
 */

public class LiveVoicePushTxViewHolder extends AbsLivePushViewHolder implements ITXLivePushListener {

    private TXLivePusher mLivePusher;
    private Handler mMixHandler;
    private ViewGroup mContainer;
    private boolean mPaused;

    public LiveVoicePushTxViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.view_live_push_tx_voice;
    }

    @Override
    public void init() {
        mContainer = findViewById(R.id.voice_push_container);
        mLivePusher = new TXLivePusher(mContext);
        TXLivePushConfig livePushConfig = new TXLivePushConfig();
        livePushConfig.enableAEC(true);//设置回声消除
        livePushConfig.enableANS(true);//设置噪声抑制
        livePushConfig.setVolumeType(TXLiveConstants.AUDIO_VOLUME_TYPE_MEDIA);//表示通话音量类型
//        livePushConfig.enablePureAudioPush(true);//启动纯音频推流
        mLivePusher.setConfig(livePushConfig);
        mLivePusher.setPushListener(this);
        mLivePusher.startCameraPreview((TXCloudVideoView) findViewById(R.id.camera_preview));

    }


    @Override
    public void onPause() {
        if (mLivePusher != null) {
            mLivePusher.setMute(true);
        }
        mPaused = true;
    }


    @Override
    public void onResume() {
        if (mPaused) {
            if (mLivePusher != null) {
                mLivePusher.setMute(false);
            }
        }
        mPaused = false;
    }

    public ViewGroup getContainer() {
        return mContainer;
    }



    @Override
    public void changeToLeft() {
    }

    @Override
    public void changeToBig() {
    }

    /**
     * 切换镜像
     */
    @Override
    public void togglePushMirror() {

    }


    /**
     * 切换镜头
     */
    @Override
    public void toggleCamera() {
    }

    /**
     * 打开关闭闪光灯
     */
    @Override
    public void toggleFlash() {
    }

    /**
     * 开始推流
     *
     * @param pushUrl 推流地址
     */
    @Override
    public void startPush(String pushUrl) {
        if (mLivePusher != null) {
            mLivePusher.startPusher(pushUrl);
        }
        startCountDown();
    }


    @Override
    public void startBgm(String path) {
        if (mLivePusher != null) {
            mLivePusher.playBGM(path);
        }
    }

    @Override
    public void pauseBgm() {
        if (mLivePusher != null) {
            mLivePusher.pauseBGM();
        }
    }

    @Override
    public void resumeBgm() {
        if (mLivePusher != null) {
            mLivePusher.resumeBGM();
        }
    }

    @Override
    public void stopBgm() {
        if (mLivePusher != null) {
            mLivePusher.stopBGM();
        }
//        mBgmPath = null;
    }

    @Override
    protected void onCameraRestart() {
    }

    @Override
    public void release() {
        super.release();
        if (mMixHandler != null) {
            mMixHandler.removeCallbacksAndMessages(null);
        }
        mMixHandler = null;
        LiveHttpUtil.cancel(LiveHttpConsts.LINK_MIC_TX_MIX_STREAM);
        if (mLivePusher != null) {
            mLivePusher.stopBGM();
            mLivePusher.stopPusher();
            mLivePusher.stopScreenCapture();
            mLivePusher.stopCameraPreview(false);
            mLivePusher.setPushListener(null);
        }
        mLivePusher = null;
    }

    @Override
    public void onPushEvent(int e, Bundle bundle) {
        L.e(TAG, "---------->" + e);
        if (e == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            ToastUtil.show(R.string.live_push_failed_2);
        } else if (e == TXLiveConstants.PUSH_ERR_NET_DISCONNECT || e == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS) {
            L.e(TAG, "网络断开，推流失败------>");

        } else if (e == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {//推流成功
            L.e(TAG, "mStearm--->推流成功");
            if (!mStartPush) {
                mStartPush = true;
                if (mLivePushListener != null) {
                    mLivePushListener.onPushStart();
                }
            }
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    /**
     * 语音直播间主播混流
     * 参考文档 https://cloud.tencent.com/document/api/267/20474#CommonMixLayoutParams
     *
     * @param userStreamList 上麦观众的流名
     */
    public void voiceRoomAnchorMix(List<String> userStreamList) {

        String selfAnchorStream = ((LiveActivity) mContext).getStream();
        JSONObject mixParam = new JSONObject();
        mixParam.put("MixStreamSessionId", selfAnchorStream);
        JSONArray inputParams = new JSONArray();
        //主播的
        JSONObject anchorParam = new JSONObject();
        anchorParam.put("InputStreamName", selfAnchorStream);
        JSONObject anchorLayoutParams = new JSONObject();
        anchorLayoutParams.put("ImageLayer", 1);
        anchorLayoutParams.put("InputType", 0);
        anchorParam.put("LayoutParams", anchorLayoutParams);
        inputParams.add(anchorParam);

        //上麦观众的
        int size = 0;
        if (userStreamList != null && (size = userStreamList.size()) > 0) {
            for (int i = 0; i < size; i++) {
                JSONObject userParam = new JSONObject();
                userParam.put("InputStreamName", userStreamList.get(i));
                JSONObject userLayoutParams = new JSONObject();
                userLayoutParams.put("ImageLayer", i + 2);
                userLayoutParams.put("InputType", 0);
                userLayoutParams.put("ImageWidth", 0.25);
                userLayoutParams.put("ImageHeight", 0.25);
                userLayoutParams.put("LocationX", (i % 4) * 0.25);
                userLayoutParams.put("LocationY", 0.5 + (i / 4) * 0.25);
                userParam.put("LayoutParams", userLayoutParams);
                inputParams.add(userParam);
            }
        }

        mixParam.put("InputStreamList", inputParams);
        JSONObject outputParams = new JSONObject();
        outputParams.put("OutputStreamName", selfAnchorStream);
        mixParam.put("OutputParams", outputParams);

        final String finalMixParams = mixParam.toString();
//        L.e("linkMicTxMixStream--混流参数----> " + finalMixParams);
        if (mMixHandler != null) {
            mMixHandler.removeCallbacksAndMessages(null);
        }
        LiveHttpUtil.linkMicTxMixStream(finalMixParams, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
//                L.e("linkMicTxMixStream---1---code---> " + code + " ---msg---> " + msg);
                if (true) {//原来这里判断是 code != 0，但还是有混流失败的情况

                    //第2次 5秒后重新请求混流接口
                    if (mMixHandler == null) {
                        mMixHandler = new Handler();
                    }
                    mMixHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            LiveHttpUtil.linkMicTxMixStream(finalMixParams, new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
//                                    L.e("linkMicTxMixStream---2---code---> " + code + " ---msg---> " + msg);
                                    if (true) {//原来这里判断是 code != 0，但还是有混流失败的情况

                                        //第3次 5秒后重新请求混流接口
                                        if (mMixHandler == null) {
                                            mMixHandler = new Handler();
                                        }
                                        mMixHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                LiveHttpUtil.linkMicTxMixStream(finalMixParams, new HttpCallback() {
                                                    @Override
                                                    public void onSuccess(int code, String msg, String[] info) {
//                                                        L.e("linkMicTxMixStream---3---code---> " + code + " ---msg---> " + msg);
                                                    }
                                                });
                                            }
                                        }, 5000);
                                    }
                                }
                            });


                        }
                    }, 5000);
                }
            }
        });
    }


}
