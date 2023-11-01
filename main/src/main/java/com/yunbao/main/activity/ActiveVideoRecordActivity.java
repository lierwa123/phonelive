package com.yunbao.main.activity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.live.TXUGCRecord;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCPartsManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MediaUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.video.custom.VideoRecordBtnView;

public class ActiveVideoRecordActivity extends AbsActivity implements View.OnClickListener, TXRecordCommon.ITXVideoRecordListener {

    private static final String TAG = "ActiveVideoRecordActivity";
    private static final int MAX_DURATION = 15000;//最大录制时间15s
    TXCloudVideoView mVideoView1;
    TXCloudVideoView mVideoView2;
    private View mGroup1;
    private View mGroup2;
    private View mGroup3;
    private View mBottomGroup;
    private TextView mTime;
    private ImageView mbtnFlash;
    private ImageView mBtnPlay;
    private Drawable mFlashDrawable0;
    private Drawable mFlashDrawable1;
    private Drawable mPlayDrawable0;
    private Drawable mPlayDrawable1;
    //按钮动画相关
    private VideoRecordBtnView mVideoRecordBtnView;
    private View mRecordView;
    private ValueAnimator mRecordBtnAnimator;//录制开始后，录制按钮动画
    private Drawable mRecordDrawable;
    private Drawable mUnRecordDrawable;


    private TXUGCRecord mRecorder;//录制器
    private boolean mFrontCamera = true;//是否是前置摄像头
    private boolean mFlashOpen;
    private String mVideoPath;//视频的保存路径
    private int mVideoDuration;//视频时长

    private boolean mRecordStarted;//是否开始了录制（true 已开始 false 未开始）
    private boolean mRecordStoped;//是否结束了录制
    private Dialog mStopRecordDialog;//停止录制的时候的dialog
    private boolean mPaused;
    private boolean mIsReachMaxRecordDuration;//是否达到最大录制时间
    private TXVodPlayer mPlayer;
    private boolean mPlayStarted;
    private boolean mPlaying;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active_video_record;
    }

    @Override
    protected void main() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideoView1 = findViewById(R.id.video_view_1);
        mVideoView2 = findViewById(R.id.video_view_2);
        mGroup1 = findViewById(R.id.group_1);
        mGroup2 = findViewById(R.id.group_2);
        mGroup3 = findViewById(R.id.group_3);
        mBottomGroup = findViewById(R.id.bottom_group);
        mTime = findViewById(R.id.time);
        mbtnFlash = findViewById(R.id.btn_flash);
        mBtnPlay = findViewById(R.id.btn_play);
        mFlashDrawable0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_video_flash_0);
        mFlashDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_video_flash_1);
        mPlayDrawable0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_play_0);
        mPlayDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_play_1);
        mbtnFlash.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        findViewById(R.id.btn_start_record).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
//        findViewById(R.id.btn_re_record).setOnClickListener(this);
        findViewById(R.id.btn_use).setOnClickListener(this);

        //按钮动画相关
        mVideoRecordBtnView = (VideoRecordBtnView) findViewById(com.yunbao.video.R.id.record_btn_view);
        mRecordView = findViewById(com.yunbao.video.R.id.record_view);
        mUnRecordDrawable = ContextCompat.getDrawable(mContext, com.yunbao.video.R.drawable.bg_btn_record_1);
        mRecordDrawable = ContextCompat.getDrawable(mContext, com.yunbao.video.R.drawable.bg_btn_record_2);
        mRecordBtnAnimator = ValueAnimator.ofFloat(100, 0);
        mRecordBtnAnimator.setDuration(500);
        mRecordBtnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                if (mVideoRecordBtnView != null) {
                    mVideoRecordBtnView.setRate((int) v);
                }
            }
        });
        mRecordBtnAnimator.setRepeatCount(-1);
        mRecordBtnAnimator.setRepeatMode(ValueAnimator.REVERSE);
        initCameraRecord();
    }


    /**
     * 初始化录制器
     */
    private void initCameraRecord() {
        mRecorder = TXUGCRecord.getInstance(CommonAppContext.getInstance());
        mRecorder.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
        mRecorder.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mRecorder.setRecordSpeed(TXRecordCommon.RECORD_SPEED_NORMAL);
        mRecorder.setAspectRatio(TXRecordCommon.VIDEO_ASPECT_RATIO_9_16);
        TXRecordCommon.TXUGCCustomConfig customConfig = new TXRecordCommon.TXUGCCustomConfig();
        customConfig.videoResolution = TXRecordCommon.VIDEO_RESOLUTION_540_960;
        customConfig.minDuration = 1000;
        customConfig.maxDuration = MAX_DURATION;
        customConfig.isFront = mFrontCamera;
        mRecorder.setVideoRecordListener(this);
        mRecorder.startCameraCustomPreview(customConfig, mVideoView1);
    }


    @Override
    public void onRecordEvent(int event, Bundle bundle) {
        if (event == TXRecordCommon.EVT_CAMERA_CANNOT_USE) {
            ToastUtil.show(com.yunbao.video.R.string.video_record_camera_failed);
        } else if (event == TXRecordCommon.EVT_MIC_CANNOT_USE) {
            ToastUtil.show(com.yunbao.video.R.string.video_record_audio_failed);
        }
    }

    @Override
    public void onRecordProgress(long milliSecond) {
        if (mTime != null) {
            mTime.setText(StringUtil.getDurationText(milliSecond));
        }
        if (milliSecond >= MAX_DURATION) {
            if (!mIsReachMaxRecordDuration) {
                mIsReachMaxRecordDuration = true;
                stopRecordBtnAnim();
                showProccessDialog();
            }
        }
    }

    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
        hideProccessDialog();
        mRecordStarted = false;
        mRecordStoped = true;
        if (mRecorder != null) {
            mRecorder.toggleTorch(false);
            mVideoDuration = mRecorder.getPartsManager().getDuration();
            releaseRecord();
            if (result.retCode < 0) {
                ToastUtil.show(R.string.video_record_failed);
            } else {
                if (mGroup1 != null && mGroup1.getVisibility() == View.VISIBLE) {
                    mGroup1.setVisibility(View.INVISIBLE);
                }
                if (mGroup3 != null && mGroup3.getVisibility() != View.VISIBLE) {
                    mGroup3.setVisibility(View.VISIBLE);
                }
                mPlayer = new TXVodPlayer(mContext);
                mPlayer.setConfig(new TXVodPlayConfig());
                mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                mPlayer.setLoop(true);
                mPlayer.setPlayerView(mVideoView2);
                if (mBottomGroup != null) {
                    mBottomGroup.requestLayout();
                }
            }
        }
    }

    private void clickPlay() {
        if (mPlayer == null || TextUtils.isEmpty(mVideoPath)) {
            return;
        }
        if (mPlayStarted) {
            if (mPlaying) {
                mPlayer.pause();
            } else {
                mPlayer.resume();
            }
            mPlaying = !mPlaying;
            if (mBtnPlay != null) {
                mBtnPlay.setImageDrawable(mPlaying ? mPlayDrawable0 : mPlayDrawable1);
            }
        } else {
            mPlayer.startPlay(mVideoPath);
            mPlayStarted = true;
            mPlaying = true;
            if (mBtnPlay != null) {
                mBtnPlay.setImageDrawable(mPlayDrawable0);
            }
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_flash) {
            clickFlash();
        } else if (i == R.id.btn_play) {
            clickPlay();
        } else if (i == R.id.btn_start_record) {
            clickRecord();
        } else if (i == R.id.btn_cancel) {
            onBackPressed();
        } else if (i == R.id.btn_camera) {
            clickCamera();
        } else if (i == R.id.btn_use) {
            useVideo();
        }
//        else if (i == R.id.btn_re_record) {
//            reRecord();
//        }
    }


    private void useVideo() {
        if (mVideoDuration == 0 || TextUtils.isEmpty(mVideoPath)) {
            return;
        }

        MediaUtil.saveVideoInfo(mContext, mVideoPath, mVideoDuration);
        Intent intent = new Intent();
        intent.putExtra(Constants.VIDEO_PATH, mVideoPath);
        intent.putExtra(Constants.VIDEO_DURATION, mVideoDuration);
        setResult(RESULT_OK, intent);
        finish();
    }


//    /**
//     * 重拍
//     */
//    private void reRecord() {
//        if (mPlayer != null) {
//            mPlayer.stopPlay(true);
//        }
//        if (mGroup3 != null && mGroup3.getVisibility() == View.VISIBLE) {
//            mGroup3.setVisibility(View.INVISIBLE);
//        }
//        if (mGroup1 != null && mGroup1.getVisibility() != View.VISIBLE) {
//            mGroup1.setVisibility(View.VISIBLE);
//        }
//        if (mGroup2 != null && mGroup2.getVisibility() != View.VISIBLE) {
//            mGroup2.setVisibility(View.VISIBLE);
//        }
//        if (mbtnFlash != null && mbtnFlash.getVisibility() != View.VISIBLE) {
//            mbtnFlash.setVisibility(View.VISIBLE);
//        }
//        if (mTime != null) {
//            mTime.setText("00:00");
//        }
//        mVideoDuration = 0;
//        mRecordStarted = false;
//        mRecordStoped = false;
//        mIsReachMaxRecordDuration = false;
//        initCameraRecord();
//    }


    /**
     * 点击闪光灯
     */
    private void clickFlash() {
        if (mFrontCamera) {
            ToastUtil.show(R.string.live_open_flash);
            return;
        }
        toggleFlash(!mFlashOpen);
    }

    /**
     * 切换闪光灯
     */
    private void toggleFlash(boolean open) {
        mFlashOpen = open;
        if (mbtnFlash != null) {
            mbtnFlash.setImageDrawable(open ? mFlashDrawable1 : mFlashDrawable0);
        }
        if (mRecorder != null) {
            mRecorder.toggleTorch(open);
        }
    }

    /**
     * 点击摄像头
     */
    private void clickCamera() {
        if (mRecorder != null) {
            if (mFlashOpen) {
                toggleFlash(false);
            }
            mFrontCamera = !mFrontCamera;
            mRecorder.switchCamera(mFrontCamera);
        }
    }


    /**
     * 点击录制
     */
    private void clickRecord() {
        if (mRecordStoped) {
            return;
        }
        if (mRecordStarted) {
            endRecord();
        } else {
            startRecord();
        }
    }


    /**
     * 结束录制，会触发 onRecordComplete
     */
    public void endRecord() {
        mRecordStoped = true;
        stopRecordBtnAnim();
        if (mRecorder != null) {
            mRecorder.stopRecord();
            showProccessDialog();
        }
    }


    /**
     * 开始录制
     */
    private void startRecord() {
        if (mRecorder != null) {
            mVideoPath = StringUtil.generateVideoOutputPath();//视频保存的路径
            int result = mRecorder.startRecord(mVideoPath, CommonAppConfig.VIDEO_RECORD_TEMP_PATH, null);//为空表示不需要生成视频封面
            if (result != TXRecordCommon.START_RECORD_OK) {
                if (result == TXRecordCommon.START_RECORD_ERR_NOT_INIT) {
                    ToastUtil.show(com.yunbao.video.R.string.video_record_tip_1);
                } else if (result == TXRecordCommon.START_RECORD_ERR_IS_IN_RECORDING) {
                    ToastUtil.show(com.yunbao.video.R.string.video_record_tip_2);
                } else if (result == TXRecordCommon.START_RECORD_ERR_VIDEO_PATH_IS_EMPTY) {
                    ToastUtil.show(com.yunbao.video.R.string.video_record_tip_3);
                } else if (result == TXRecordCommon.START_RECORD_ERR_API_IS_LOWER_THAN_18) {
                    ToastUtil.show(com.yunbao.video.R.string.video_record_tip_4);
                } else if (result == TXRecordCommon.START_RECORD_ERR_LICENCE_VERIFICATION_FAILED) {
                    ToastUtil.show(com.yunbao.video.R.string.video_record_tip_5);
                }
                return;
            }
        }
        mRecordStarted = true;
        startRecordBtnAnim();
        if (mGroup2 != null && mGroup2.getVisibility() == View.VISIBLE) {
            mGroup2.setVisibility(View.INVISIBLE);
        }
        if (mbtnFlash != null && mbtnFlash.getVisibility() == View.VISIBLE) {
            mbtnFlash.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 按钮录制动画开始
     */
    private void startRecordBtnAnim() {
        if (mRecordView != null) {
            mRecordView.setBackground(mRecordDrawable);
        }
        if (mRecordBtnAnimator != null) {
            mRecordBtnAnimator.start();
        }
    }

    /**
     * 按钮录制动画停止
     */
    private void stopRecordBtnAnim() {
        if (mRecordView != null) {
            mRecordView.setBackground(mUnRecordDrawable);
        }
        if (mRecordBtnAnimator != null) {
            mRecordBtnAnimator.cancel();
        }
        if (mVideoRecordBtnView != null) {
            mVideoRecordBtnView.reset();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
        if (mRecordStarted) {
            if (mFlashOpen) {
                toggleFlash(false);
            }
            if (mRecorder != null) {
                mRecorder.pauseRecord();
                stopRecordBtnAnim();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mRecordStarted) {
            if (mPaused) {
                if (mRecorder != null) {
                    mRecorder.resumeRecord();
                    startRecordBtnAnim();
                }
            }
            mPaused = false;
        }
    }

    private void releaseRecord() {
        if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
            mStopRecordDialog.dismiss();
        }
        if (mRecordBtnAnimator != null) {
            mRecordBtnAnimator.cancel();
        }
        if (mRecorder != null) {
            mRecorder.toggleTorch(false);
            if (mRecordStarted) {
                mRecorder.stopRecord();
            }
            mRecorder.stopCameraPreview();
            mRecorder.setVideoRecordListener(null);
            TXUGCPartsManager getPartsManager = mRecorder.getPartsManager();
            if (getPartsManager != null) {
                getPartsManager.deleteAllParts();
            }
            mRecorder.release();
        }
        mRecordBtnAnimator = null;
        mRecorder = null;
    }


    /**
     * 录制结束时候显示处理中的弹窗
     */
    private void showProccessDialog() {
        if (mStopRecordDialog == null) {
            mStopRecordDialog = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.video_processing));
        }
        mStopRecordDialog.show();
    }

    /**
     * 隐藏处理中的弹窗
     */
    private void hideProccessDialog() {
        if (mStopRecordDialog != null) {
            mStopRecordDialog.dismiss();
        }
        mStopRecordDialog = null;
    }

    @Override
    protected void onDestroy() {
        releaseRecord();
        super.onDestroy();
        L.e("ActiveVideoRecordActivity", "-------->onDestroy");
    }

}
