package com.yunbao.main.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.custom.LineProgress;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.activity.ActivePubActivity;
import com.yunbao.main.utils.AudioRecorderEx;

import java.io.File;


/**
 * 发布动态的时候录音
 */
public class ActiveRecordVoiceViewHolder2 extends AbsViewHolder implements View.OnClickListener {
    private static final String TAG = "ActiveRecordVoiceViewHolder";
    private static final int MAX_PROGRESS = 120;
    private static final int WHAT_LISTEN = 0;
    private static final int WHAT_RECORD_PROGRESS = 1;
    private static final int WHAT_RECORD_ANIM = 2;
    //    private static final int WHAT_VOICE_RECORD_COMPLETE = 5;
    private ImageView mBtnListen;
    private LineProgress mLineProgress;
    private TextView mTime;
    private ImageView mRecordImg;
    private TextView mRecordTip;
    private View mBtnDelete;
    private View mBtnRecord;
    private View mBtnConfirm;
    private String mRecordStringStart;
    private String mRecordStringPause;
    private Drawable mListenDrawableStart;
    private Drawable mListenDrawablePause;
    private Drawable[] mRecordDrawable;
    private boolean mListening;
    private boolean mRecording;
    private boolean mRecordAnimFlag;
    private Handler mHandler;
    private int mRecordProgress;
    private int mListenProgress;
    private File mRecordFile;
    private boolean mIsShowing;

    private AudioRecorderEx mAudioRecorderEx;


    public ActiveRecordVoiceViewHolder2(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_active_record_voice;
    }

    @Override
    public void init() {
        mBtnListen = findViewById(R.id.btn_listen);
        mLineProgress = findViewById(R.id.line_progress);
        mTime = findViewById(R.id.time);
        mRecordImg = findViewById(R.id.record_img);
        mRecordTip = findViewById(R.id.record_tip);
        mBtnDelete = findViewById(R.id.btn_delete);
        mRecordStringStart = WordUtil.getString(R.string.active_record_voice_1);
        mRecordStringPause = WordUtil.getString(R.string.active_record_voice_0);
        mListenDrawableStart = ContextCompat.getDrawable(mContext, R.mipmap.icon_active_voice_listen_1);
        mListenDrawablePause = ContextCompat.getDrawable(mContext, R.mipmap.icon_active_voice_listen_0);
        mRecordDrawable = new Drawable[2];
        mRecordDrawable[0] = ContextCompat.getDrawable(mContext, R.mipmap.icon_active_voice_recording_0);
        mRecordDrawable[1] = ContextCompat.getDrawable(mContext, R.mipmap.icon_active_voice_recording_1);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
        mBtnRecord = findViewById(R.id.btn_record);
        mBtnRecord.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnListen.setOnClickListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_LISTEN:
                        showListenProgress();
                        break;
                    case WHAT_RECORD_PROGRESS:
                        showRecordProgress();
                        break;
                    case WHAT_RECORD_ANIM:
                        startRecordAnim();
                        break;
//                    case WHAT_VOICE_RECORD_COMPLETE:
//                        onRecordComplete();
//                        break;
                }
            }
        };
    }

    @Override
    public void addToParent() {
        super.addToParent();
        mIsShowing = true;
        mAudioRecorderEx = AudioRecorderEx.getInstance();
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();
        mIsShowing = false;
        if (mAudioRecorderEx != null) {
            mAudioRecorderEx.release();
        }
        mAudioRecorderEx = null;
    }

    public boolean isShowing() {
        return mIsShowing;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            close();
        } else if (i == R.id.btn_confirm) {
            clickConfirm();
        } else if (i == R.id.btn_record) {
            clickRecord();
        } else if (i == R.id.btn_delete) {
            clickDelete();
        } else if (i == R.id.btn_listen) {
            clickListen();
        }

    }

    /**
     * 点击确认
     */
    private void clickConfirm() {
        if (mRecordProgress == 0) {
            ToastUtil.show(R.string.active_voice_1);
            return;
        }
        if (mRecordProgress < 8) {
            ToastUtil.show(R.string.im_record_audio_too_short);
        }
        if (mRecording) {
            pauseRecord();
        }
        mRecordFile = mAudioRecorderEx.mergeAudioFile();
        useVoice();
    }

    /**
     * 确认使用语音文件
     */
    private void useVoice() {
        ((ActivePubActivity) mContext).useVoice(mRecordFile, mRecordProgress / 2);
        close();
    }


    /**
     * 关闭
     */
    private void close() {
        if (mRecording) {
            pauseRecord();
        }
        if (mListening) {
            stopPlayVoice();
        }
        release();
        reset();
        removeFromParent();
    }

    /**
     * 点击删除
     */
    private void clickDelete() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        reset();
        if (mAudioRecorderEx != null) {
            mAudioRecorderEx.reset();
            mAudioRecorderEx.clearData();
        }
        if (mRecordFile != null && mRecordFile.exists()) {
            mRecordFile.delete();
        }
    }

    /**
     * 重置各种状态
     */
    private void reset() {
        mListening = false;
        mRecording = false;
        mRecordAnimFlag = false;
        mRecordProgress = 0;
        mListenProgress = 0;
        if (mLineProgress != null) {
            mLineProgress.setProgress(0);
        }
        if (mTime != null) {
            mTime.setText("0s");
        }
        if (mBtnRecord != null) {
            if (mBtnRecord.getVisibility() != View.VISIBLE) {
                mBtnRecord.setVisibility(View.VISIBLE);
            }
//            mBtnRecord.setClickable(true);
        }
        if (mRecordImg != null) {
            mRecordImg.setImageDrawable(mRecordDrawable[0]);
        }
        if (mBtnListen != null) {
            mBtnListen.setImageDrawable(mListenDrawableStart);
            if (mBtnListen.getVisibility() == View.VISIBLE) {
                mBtnListen.setVisibility(View.INVISIBLE);
            }
        }
        if (mBtnConfirm != null) {
            if (mBtnConfirm.getVisibility() == View.VISIBLE) {
                mBtnConfirm.setVisibility(View.INVISIBLE);
            }
        }
        if (mRecordTip != null) {
            mRecordTip.setText(mRecordStringStart);
        }
        if (mBtnDelete != null && mBtnDelete.getVisibility() == View.VISIBLE) {
            mBtnDelete.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 试听
     */
    private void clickListen() {
        if (!ClickUtil.canClick()) {
            return;
        }
        if (mListening) {
            stopPlayVoice();
        } else {
            if (mRecordProgress == 0) {
                ToastUtil.show(R.string.active_voice_1);
                return;
            }
            if (mRecordProgress < 8) {
                ToastUtil.show(R.string.im_record_audio_too_short);
                return;
            }
            if (mRecording) {
                pauseRecord();
            }
            mRecordFile = mAudioRecorderEx.mergeAudioFile();
            if (mRecordFile != null && mRecordFile.exists()) {
                playVoice();
            } else {
                ToastUtil.show(R.string.active_voice_1);
            }
        }
    }

    /**
     * 录音时间到
     */
    private void onRecordComplete() {
        mRecording = false;
        ToastUtil.show(R.string.active_voice_2);
        stopRecordAnim();
        if (mBtnRecord != null) {
            if (mBtnRecord.getVisibility() == View.VISIBLE) {
                mBtnRecord.setVisibility(View.INVISIBLE);
            }
//            mBtnRecord.setClickable(false);
        }
        if (mRecordImg != null) {
            mRecordImg.setImageDrawable(mRecordDrawable[0]);
        }
        if (mBtnListen.getVisibility() != View.VISIBLE) {
            mBtnListen.setVisibility(View.VISIBLE);
        }
        if (mBtnConfirm.getVisibility() != View.VISIBLE) {
            mBtnConfirm.setVisibility(View.VISIBLE);
        }
        if (mBtnDelete != null && mBtnDelete.getVisibility() != View.VISIBLE) {
            mBtnDelete.setVisibility(View.VISIBLE);
        }
        if (mAudioRecorderEx != null) {
            mAudioRecorderEx.stop();
            mAudioRecorderEx.reset();
        }
    }


    /**
     * 开始试听
     */
    private void playVoice() {
        mListening = true;
        mListenProgress = 0;
        if (mLineProgress != null) {
            mLineProgress.setProgress(0);
        }
        if (mTime != null) {
            mTime.setText("0s");
        }
        if (mBtnListen != null) {
            mBtnListen.setImageDrawable(mListenDrawablePause);
        }
        if (mBtnRecord != null && mBtnRecord.getVisibility() == View.VISIBLE) {
            mBtnRecord.setVisibility(View.INVISIBLE);
        }
        if (mBtnDelete != null && mBtnDelete.getVisibility() == View.VISIBLE) {
            mBtnDelete.setVisibility(View.INVISIBLE);
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageAtTime(WHAT_LISTEN, getNextTime(500));
        }
        ((ActivePubActivity) mContext).playVoiceFile(mRecordFile);
    }


    /**
     * 停止试听
     */
    private void stopPlayVoice() {
        onListenEnd();
        ((ActivePubActivity) mContext).stopPlayVoiceFile();
    }

    /**
     * 试听结束
     */
    public void onListenEnd() {
        mListening = false;
        mListenProgress = 0;
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_LISTEN);
        }
        if (mLineProgress != null) {
            mLineProgress.setProgress(mRecordProgress);
        }
        if (mTime != null) {
            mTime.setText(StringUtil.contact(String.valueOf(mRecordProgress / 2), "s"));
        }
        if (mBtnListen != null) {
            mBtnListen.setImageDrawable(mListenDrawableStart);
        }
        if (mBtnRecord != null) {
            if (mRecordProgress == MAX_PROGRESS) {
                if (mBtnRecord.getVisibility() == View.VISIBLE) {
                    mBtnRecord.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mBtnRecord.getVisibility() != View.VISIBLE) {
                    mBtnRecord.setVisibility(View.VISIBLE);
                }
            }
        }
        if (mBtnDelete != null && mBtnDelete.getVisibility() != View.VISIBLE) {
            mBtnDelete.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 试听进度
     */
    private void showListenProgress() {
        mListenProgress++;
        if (mListenProgress > mRecordProgress) {
            mListenProgress = mRecordProgress;
        }
        if (mLineProgress != null) {
            mLineProgress.setProgress(mListenProgress);
        }
        if (mTime != null) {
            mTime.setText(StringUtil.contact(String.valueOf(mListenProgress / 2), "s"));
        }
        if (mListenProgress < mRecordProgress) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageAtTime(WHAT_LISTEN, getNextTime(500));
            }
        }
    }

    /**
     * 点击录音
     */
    private void clickRecord() {
        if (mRecording) {
            pauseRecord();
        } else {
            startRecord();
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mListening) {
            stopPlayVoice();
        }
        mRecording = true;
        mRecordTip.setText(mRecordStringPause);
        if (mHandler != null) {
            mHandler.sendEmptyMessageAtTime(WHAT_RECORD_PROGRESS, getNextTime(500));
        }
        startRecordAnim();
        if (mBtnListen.getVisibility() == View.VISIBLE) {
            mBtnListen.setVisibility(View.INVISIBLE);
        }
        if (mBtnConfirm.getVisibility() == View.VISIBLE) {
            mBtnConfirm.setVisibility(View.INVISIBLE);
        }
        if (mBtnDelete != null && mBtnDelete.getVisibility() == View.VISIBLE) {
            mBtnDelete.setVisibility(View.INVISIBLE);
        }
        File dir = new File(CommonAppConfig.MUSIC_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, StringUtil.contact(StringUtil.generateFileName(), ".mp3"));
        mAudioRecorderEx.setOutputFile(file.getAbsolutePath());
        mAudioRecorderEx.prepare();
        mAudioRecorderEx.start();
    }


    /**
     * 暂停录制
     */
    private void pauseRecord() {
        mRecording = false;
        mRecordTip.setText(mRecordStringStart);
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_RECORD_PROGRESS);
        }
        if (mBtnListen.getVisibility() != View.VISIBLE) {
            mBtnListen.setVisibility(View.VISIBLE);
        }
        if (mBtnConfirm.getVisibility() != View.VISIBLE) {
            mBtnConfirm.setVisibility(View.VISIBLE);
        }
        if (mBtnDelete != null && mBtnDelete.getVisibility() != View.VISIBLE) {
            mBtnDelete.setVisibility(View.VISIBLE);
        }
        stopRecordAnim();
        if (mAudioRecorderEx != null) {
            mAudioRecorderEx.stop();
            mAudioRecorderEx.reset();
        }
    }

    /**
     * 录制进度
     */
    private void showRecordProgress() {
        mRecordProgress++;
        if (mRecordProgress > MAX_PROGRESS) {
            mRecordProgress = MAX_PROGRESS;
        }
        if (mLineProgress != null) {
            mLineProgress.setProgress(mRecordProgress);
        }
        if (mTime != null) {
            mTime.setText(StringUtil.contact(String.valueOf(mRecordProgress / 2), "s"));
        }
        if (mRecordProgress < MAX_PROGRESS) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageAtTime(WHAT_RECORD_PROGRESS, getNextTime(500));
            }
        } else {
            onRecordComplete();
        }
    }

    /**
     * 开始录制按钮动画
     */
    private void startRecordAnim() {
        mRecordAnimFlag = !mRecordAnimFlag;
        mRecordImg.setImageDrawable(mRecordAnimFlag ? mRecordDrawable[1] : mRecordDrawable[0]);
        if (mHandler != null) {
            mHandler.sendEmptyMessageAtTime(WHAT_RECORD_ANIM, getNextTime(200));
        }
    }

    /**
     * 停止录制按钮动画
     */
    private void stopRecordAnim() {
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_RECORD_ANIM);
        }
    }


    private long getNextTime(int time) {
        long now = SystemClock.uptimeMillis();
        if (time < 1000) {
            return now + time;
        }
        return now + time + -now % 1000;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
        mHandler = null;
    }

    @Override
    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mAudioRecorderEx != null) {
            mAudioRecorderEx.release();
        }
        mAudioRecorderEx = null;
    }
}
