package com.yunbao.live.utils;

import com.ksyun.media.streamer.capture.ImgTexSrcPin;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilter;
import com.ksyun.media.streamer.framework.ImgTexFormat;
import com.ksyun.media.streamer.framework.ImgTexFrame;
import com.ksyun.media.streamer.framework.SinkPin;
import com.ksyun.media.streamer.framework.SrcPin;
import com.ksyun.media.streamer.util.gles.GLRender;
import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.meihu.beautylibrary.render.filter.ksyFilter.GLImageVertFlipFilter;

public class KsyMhFilter extends ImgTexFilter {

    private final Object BUF_LOCK = new Object();
    private MHBeautyManager mhBeautyManager;
    private SinkPin<ImgTexFrame> mTexSinkPin;
    private ImgTexSrcPin mSrcPin;

    private GLImageVertFlipFilter glImageVertFlipFilter;

    public KsyMhFilter(MHBeautyManager mhSDKManager, GLRender glRender) {
        super(glRender);
        mhBeautyManager = mhSDKManager;
        mGLRender = glRender;
        mTexSinkPin = new TiFancyTexSinPin();
        mSrcPin = new ImgTexSrcPin(glRender);
    }

    @Override
    public SinkPin<ImgTexFrame> getSinkPin() {
        return mTexSinkPin;
    }

    @Override
    public SrcPin<ImgTexFrame> getSrcPin() {
        return mSrcPin;
    }

    @Override
    public int getSinkPinNum() {
        return 2;
    }

    private class TiFancyTexSinPin extends SinkPin<ImgTexFrame> {
        @Override
        public void onFormatChanged(Object format) {
            ImgTexFormat fmt = (ImgTexFormat) format;
            mSrcPin.onFormatChanged(fmt);
        }

        @Override
        public void onFrameAvailable(ImgTexFrame frame) {
            int texId = frame.textureId;
            if (mhBeautyManager == null) {
                ImgTexFrame outFrame = new ImgTexFrame(frame.format, texId, null, frame.pts);
                mSrcPin.onFrameAvailable(outFrame);
                return;
            }
            if (mSrcPin.isConnected()) {
                synchronized (BUF_LOCK) {
                    if (mhBeautyManager != null) {
                        int ret = mhBeautyManager.render13(frame.textureId, frame.format.width, frame.format.height, 2, 1);
                        if (ret != -1) {
                            texId = ret;
                        }
                    }
                }
            }
            ImgTexFrame outFrame = new ImgTexFrame(frame.format, texId, null, frame.pts);
            mSrcPin.onFrameAvailable(outFrame);
        }

        @Override
        public void onDisconnect(boolean recursive) {
            if (recursive) {
                mSrcPin.disconnect(true);
            }
        }
    }
}
