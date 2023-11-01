
package com.yunbao.live.views;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautySpecialEffectsFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.yunbao.beauty.bean.MeiYanValueBean;
import com.yunbao.beauty.bean.SimpleFilterBean;
import com.yunbao.beauty.interfaces.IBeautyEffectListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.beauty.utils.SimpleDataManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.LiveConfig;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveConfigBean;
import com.yunbao.live.utils.KsyMhFilter;


/**
 * Created by cxf on 2018/10/7.
 * 金山云直播推流
 */


public class LivePushKsyViewHolder extends AbsLivePushViewHolder implements
        KSYStreamer.OnInfoListener, KSYStreamer.OnErrorListener, StatsLogReport.OnLogEventListener {

    private KSYStreamer mStreamer;//金山推流器
    private LiveConfigBean mLiveKsyConfigBean;
    private ImgBeautyProFilter mImgBeautyProFilter;//美白磨皮红润
    private ImgBeautySpecialEffectsFilter mBitmapFilter;//滤镜
    private KsyMhFilter mKsyMhFilter;//美狐
    private float mMeiBai;
    private float mMoPi;
    private float mHongRun;


    public LivePushKsyViewHolder(Context context, ViewGroup parentView, LiveConfigBean liveConfigBean) {
        super(context, parentView, liveConfigBean);
    }

    @Override
    protected void processArguments(Object... args) {
        if (args.length > 0) {
            mLiveKsyConfigBean = (LiveConfigBean) args[0];
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_push_ksy;
    }

    @Override
    public void init() {
        super.init();
        if (mLiveKsyConfigBean == null) {
            mLiveKsyConfigBean = LiveConfig.getDefaultKsyConfig();
        }
        mPreView = findViewById(R.id.camera_preview);
        mStreamer = new KSYStreamer(mContext);
        mStreamer.setPreviewFps(mLiveKsyConfigBean.getPreviewFps());//预览采集帧率
        mStreamer.setTargetFps(mLiveKsyConfigBean.getTargetFps());//推流采集帧率
        mStreamer.setVideoKBitrate(mLiveKsyConfigBean.getVideoKBitrate(), mLiveKsyConfigBean.getVideoKBitrateMax(), mLiveKsyConfigBean.getVideoKBitrateMin());//视频码率
        mStreamer.setAudioKBitrate(mLiveKsyConfigBean.getAudioKBitrate());//音频码率
        mStreamer.setCameraCaptureResolution(LiveConfig.PUSH_CAP_RESOLUTION);//采集分辨率
        mStreamer.setPreviewResolution(mLiveKsyConfigBean.getPreviewResolution());//预览分辨率
        mStreamer.setTargetResolution(mLiveKsyConfigBean.getTargetResolution());//推流分辨率
        mStreamer.setIFrameInterval(mLiveKsyConfigBean.getTargetGop());
        mStreamer.setVideoCodecId(LiveConfig.PUSH_ENCODE_TYPE);//H264
        mStreamer.setEncodeMethod(mLiveKsyConfigBean.getEncodeMethod());//软编
        mStreamer.setVideoEncodeScene(LiveConfig.PUSH_ENCODE_SCENE);//秀场模式
        mStreamer.setVideoEncodeProfile(LiveConfig.PUSH_ENCODE_PROFILE);
        mStreamer.setAudioChannels(2);//双声道推流
        mStreamer.setVoiceVolume(2f);
        mStreamer.setEnableAudioMix(true);//设置背景音乐可用
        mStreamer.getAudioPlayerCapture().setVolume(0.5f);//设置背景音乐音量
        mStreamer.setEnableRepeatLastFrame(false);  // 切后台的时候不使用最后一帧
        mStreamer.setEnableAutoRestart(true, 3000); // 自动重启推流
        mStreamer.setCameraFacing(CameraCapture.FACING_FRONT);
        mStreamer.setFrontCameraMirror(true);
        mStreamer.setOnInfoListener(this);
        mStreamer.setOnErrorListener(this);
        mStreamer.setOnLogEventListener(this);
        mStreamer.setDisplayPreview((GLSurfaceView) mPreView);
        mStreamer.startCameraPreview();//启动预览
        getBeautyValue();
    }

    private void getBeautyValue() {
        CommonHttpUtil.getBeautyValue(new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (CommonAppConfig.getInstance().isMhBeautyEnable()) {
                        MhDataManager.getInstance().init().setMeiYanChangedListener(getMeiYanChangedListener());
                        MhDataManager.getInstance().createBeautyManager();
                        KsyMhFilter mhFilter = new KsyMhFilter(MhDataManager.getInstance().getMhManager(), mStreamer.getGLRender());
                        mKsyMhFilter = mhFilter;
                        if (mStreamer != null) {
                            mStreamer.getImgTexFilterMgt().setFilter(mhFilter);
                        }
                        MeiYanValueBean meiYanValueBean = JSON.parseObject(info[0], MeiYanValueBean.class);
                        MhDataManager.getInstance()
                                .setMeiYanValue(meiYanValueBean)
                                .useMeiYan().restoreBeautyValue();
                    } else {
                        SimpleDataManager.getInstance().create().setMeiYanChangedListener(getMeiYanChangedListener());
                        int meiBai = obj.getIntValue("skin_whiting");
                        int moPi = obj.getIntValue("skin_smooth");
                        int hongRun = obj.getIntValue("skin_tenderness");
                        mMeiBai = meiBai / 9f;
                        mMoPi = moPi / 9f;
                        mHongRun = hongRun / 4.5f - 1f;
                        SimpleDataManager.getInstance().setData(meiBai, moPi, hongRun);
                    }
                }
            }
        });
    }


    @Override
    public IBeautyEffectListener getMeiYanChangedListener() {
        return new IBeautyEffectListener() {
            @Override
            public void onMeiYanChanged(int meiBai, boolean meiBaiChanged, int moPi, boolean moPiChanged, int hongRun, boolean hongRunChanged) {
//                L.e("onMeiYanChanged---meiBai--> " + meiBai + " --moPi--> " + moPi + " --hongRun--> " + hongRun);
                if (mImgBeautyProFilter == null) {
                    initBaseBeauty();
                }
                if (meiBaiChanged) {
                    mMeiBai = meiBai / 9f;
                    if (mImgBeautyProFilter != null) {
                        mImgBeautyProFilter.setWhitenRatio(mMeiBai);
                    }
                }
                if (moPiChanged) {
                    mMoPi = moPi / 9f;
                    if (mImgBeautyProFilter != null) {
                        mImgBeautyProFilter.setGrindRatio(mMoPi);
                    }
                }
                if (hongRunChanged) {
                    mHongRun = hongRun / 4.5f - 1f;
                    if (mImgBeautyProFilter != null) {
                        mImgBeautyProFilter.setRuddyRatio(mHongRun);
                    }
                }

            }

            @Override
            public void onFilterChanged(int filterName) {
                if (!CommonAppConfig.getInstance().isMhBeautyEnable()) {
                    if (mStreamer != null) {
                        int type = 0;
                        if (filterName == R.mipmap.filter_langman) {
                            type = 1;
                        } else if (filterName == R.mipmap.filter_qingxin) {
                            type = 2;
                        } else if (filterName == R.mipmap.filter_weimei) {
                            type = 3;
                        } else if (filterName == R.mipmap.filter_fennen) {
                            type = 4;
                        } else if (filterName == R.mipmap.filter_huaijiu) {
                            type = 5;
                        } else if (filterName == R.mipmap.filter_qingliang) {
                            type = 6;
                        } else if (filterName == R.mipmap.filter_landiao) {
                            type = 7;
                        } else if (filterName == R.mipmap.filter_rixi) {
                            type = 8;
                        }
                        if (type == 0) {
                            ImgBeautyProFilter filter = new ImgBeautyProFilter(mStreamer.getGLRender(), mContext, 4);
                            filter.setWhitenRatio(mMeiBai);
                            filter.setGrindRatio(mMoPi);
                            filter.setRuddyRatio(mHongRun);
                            mStreamer.getImgTexFilterMgt().setFilter(filter);
                            mImgBeautyProFilter = filter;
                            mBitmapFilter = null;
                        } else {
                            ImgBeautySpecialEffectsFilter filter = new ImgBeautySpecialEffectsFilter(
                                    mStreamer.getGLRender(), mContext, type);
                            if (mBitmapFilter != null) {
                                mStreamer.getImgTexFilterMgt().replaceFilter(mBitmapFilter, filter);
                            } else {
                                if (mImgBeautyProFilter == null) {
                                    initBaseBeauty();
                                }
                                mStreamer.getImgTexFilterMgt().addFilterAfter(mImgBeautyProFilter, filter);
                            }
                            mBitmapFilter = filter;
                        }
                    }
                }
            }

            @Override
            public boolean isUseMhFilter() {
                return true;
            }

            @Override
            public boolean isTieZhiEnable() {
                return !mIsPlayGiftSticker;
            }
        };
    }


    /**
     * 初始化基础美颜
     */
    private void initBaseBeauty() {
        if (CommonAppConfig.getInstance().isMhBeautyEnable()) {
            if (mStreamer != null) {
                ImgBeautyProFilter filter = new ImgBeautyProFilter(mStreamer.getGLRender(), mContext, 4);
                filter.setWhitenRatio(mMeiBai);
                filter.setGrindRatio(mMoPi);
                filter.setRuddyRatio(mHongRun);
                if (mImgBeautyProFilter == null) {
                    if (mKsyMhFilter != null) {
                        mStreamer.getImgTexFilterMgt().addFilterBefore(mKsyMhFilter, filter);
                    } else {
                        mStreamer.getImgTexFilterMgt().setFilter(filter);
                    }
                } else {
                    mStreamer.getImgTexFilterMgt().replaceFilter(mImgBeautyProFilter, filter);
                }
                mImgBeautyProFilter = filter;
            }
        } else {
            if (mStreamer != null) {
                ImgBeautyProFilter filter = new ImgBeautyProFilter(mStreamer.getGLRender(), mContext, 4);
                filter.setWhitenRatio(mMeiBai);
                filter.setGrindRatio(mMoPi);
                filter.setRuddyRatio(mHongRun);
                if (mImgBeautyProFilter == null) {
                    mStreamer.getImgTexFilterMgt().setFilter(filter);
                } else {
                    mStreamer.getImgTexFilterMgt().replaceFilter(mImgBeautyProFilter, filter);
                }
                mImgBeautyProFilter = filter;
            }
        }

    }


    @Override
    public void onInfo(int what, int msg1, int msg2) {
        switch (what) {
            case 1000://初始化完毕
                L.e(TAG, "mStearm--->初始化完毕");
                if (mLivePushListener != null) {
                    mLivePushListener.onPreviewStart();
                }
                break;
            case 0://推流成功
                L.e(TAG, "mStearm--->推流成功");
                if (!mStartPush) {
                    mStartPush = true;
                    if (mLivePushListener != null) {
                        mLivePushListener.onPushStart();
                    }
                }
                break;
        }
    }

    @Override
    public void onError(int what, int msg1, int msg2) {
        boolean needStopPushStream = false;//是否需要停止推流
        switch (what) {
            case -1009://推流url域名解析失败
                L.e(TAG, "mStearm--->推流url域名解析失败");
                break;
            case -1006://网络连接失败，无法建立连接
                L.e(TAG, "mStearm--->网络连接失败，无法建立连接");
                break;
            case -1010://跟RTMP服务器完成握手后,推流失败
                L.e(TAG, "mStearm--->跟RTMP服务器完成握手后,推流失败");
                break;
            case -1007://网络连接断开
                L.e(TAG, "mStearm--->网络连接断开");
                break;
            case -2004://音视频采集pts差值超过5s
                L.e(TAG, "mStearm--->音视频采集pts差值超过5s");
                break;
            case -1004://编码器初始化失败
                L.e(TAG, "mStearm--->编码器初始化失败");
                needStopPushStream = true;
                break;
            case -1003://视频编码失败
                L.e(TAG, "mStearm--->视频编码失败");
                needStopPushStream = true;
                break;
            case -1008://音频初始化失败
                L.e(TAG, "mStearm--->音频初始化失败");
                needStopPushStream = true;
                break;
            case -1011://音频编码失败
                L.e(TAG, "mStearm--->音频编码失败");
                needStopPushStream = true;
                break;
            case -2001: //摄像头未知错误
                L.e(TAG, "mStearm--->摄像头未知错误");
                needStopPushStream = true;
                break;
            case -2002://打开摄像头失败
                L.e(TAG, "mStearm--->打开摄像头失败");
                needStopPushStream = true;
                break;
            case -2003://录音开启失败
                L.e(TAG, "mStearm--->录音开启失败");
                needStopPushStream = true;
                break;
            case -2005://录音开启未知错误
                L.e(TAG, "mStearm--->录音开启未知错误");
                needStopPushStream = true;
                break;
            case -2006://系统Camera服务进程退出
                L.e(TAG, "mStearm--->系统Camera服务进程退出");
                needStopPushStream = true;
                break;
            case -2007://Camera服务异常退出
                L.e(TAG, "mStearm--->Camera服务异常退出");
                needStopPushStream = true;
                break;
        }
        if (mStreamer != null) {
            if (needStopPushStream) {
                mStreamer.stopCameraPreview();
            }
            if (mStartPush && mLivePushListener != null) {
                mLivePushListener.onPushFailed();
            }
        }
    }

    @Override
    public void onLogEvent(StringBuilder singleLogContent) {
        //打印推流信息
        //L.e("mStearm--->" + singleLogContent.toString());
    }


    /**
     * 切换镜头
     */

    @Override
    public void toggleCamera() {
        if (mStreamer != null) {
            if (mFlashOpen) {
                toggleFlash();
            }
            mStreamer.switchCamera();
            mCameraFront = !mCameraFront;
        }
    }


    /**
     * 打开关闭闪光灯
     */

    @Override
    public void toggleFlash() {
        if (mCameraFront) {
            ToastUtil.show(R.string.live_open_flash);
            return;
        }
        if (mStreamer != null) {
            CameraCapture capture = mStreamer.getCameraCapture();
            Camera.Parameters parameters = capture.getCameraParameters();
            if (parameters == null) {
                if (!mFlashOpen) {
                    ToastUtil.show(R.string.live_open_flash_error);
                }
            } else {
                if (Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {//如果闪光灯已开启
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//设置成关闭的
                    mFlashOpen = false;
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//设置成开启的
                    mFlashOpen = true;
                }
                capture.setCameraParameters(parameters);
            }

        }
    }

    /**
     * 开始推流
     *
     * @param pushUrl 推流地址
     */
    @Override
    public void startPush(String pushUrl) {
        if (mStreamer != null) {
            mStreamer.setUrl(pushUrl);
            mStreamer.startStream();
        }
        startCountDown();
    }


    @Override
    public void onPause() {
        mPaused = true;
        if (mStartPush && mStreamer != null) {
            mStreamer.onPause();
            // 切后台时，将SDK设置为离屏推流模式，继续采集camera数据
            mStreamer.setOffscreenPreview(mStreamer.getPreviewWidth(), mStreamer.getPreviewHeight());
        }
    }

    @Override
    public void onResume() {
        if (mPaused && mStartPush && mStreamer != null) {
            mStreamer.onResume();
        }
        mPaused = false;
    }

    @Override
    public void startBgm(String path) {
        if (mStreamer != null) {
            mStreamer.startBgm(path, true);
        }
    }

    @Override
    public void pauseBgm() {
        if (mStreamer != null) {
            mStreamer.getAudioPlayerCapture().getMediaPlayer().pause();
        }
    }

    @Override
    public void resumeBgm() {
        if (mStreamer != null) {
            mStreamer.getAudioPlayerCapture().getMediaPlayer().start();
        }
    }

    @Override
    public void stopBgm() {
        if (mStreamer != null) {
            mStreamer.stopBgm();
        }
    }

    @Override
    protected void onCameraRestart() {
        if (mStreamer != null) {
            mStreamer.startCameraPreview();//启动预览
        }
    }

    @Override
    public void release() {
        super.release();
        CommonHttpUtil.cancel(CommonHttpConsts.GET_BEAUTY_VALUE);
        if (mStreamer != null) {
            mStreamer.stopStream();
            mStreamer.stopCameraPreview();
//            mStreamer.release();
            mStreamer.setOnInfoListener(null);
            mStreamer.setOnErrorListener(null);
            mStreamer.setOnLogEventListener(null);
        }
        mStreamer = null;
    }

    @Override
    public void changeToLeft() {
        if (mPreView != null && mLeftContainer != null) {
            ViewParent parent = mPreView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(mPreView);
            }
            int h = mPreView.getHeight() / 2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mPreView.getWidth() / 2, h);
            params.setMargins(0, (DpUtil.dp2px(250) - h) / 2, 0, 0);
            mPreView.setLayoutParams(params);
            mLeftContainer.addView(mPreView);
        }
    }

    @Override
    public void changeToBig() {
        if (mPreView != null && mBigContainer != null) {
            ViewParent parent = mPreView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(mPreView);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPreView.setLayoutParams(layoutParams);
            mBigContainer.addView(mPreView);
        }
    }


    /**
     * 切换镜像
     */
    @Override
    public void togglePushMirror() {

    }
}

