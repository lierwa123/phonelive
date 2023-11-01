package com.yunbao.beauty.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.yunbao.beauty.bean.MeiYanDataBean;
import com.yunbao.beauty.bean.MeiYanFilterBean;
import com.yunbao.beauty.bean.MeiYanValueBean;
import com.yunbao.beauty.bean.TieZhiBean;
import com.yunbao.beauty.interfaces.IBeautyEffectListener;
import com.yunbao.beauty.views.BeautyViewHolder;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.interfaces.CommonCallback;

import java.util.List;

public class MhDataManager {

    private final  String  TAG = MhDataManager.class.getName();
    private static MhDataManager sInstance;
    private MHBeautyManager mMhManager;
    private MeiYanValueBean mInputValue;//美狐sdk当前使用的数值
    private MeiYanValueBean mUseValue;//当前使用的是美颜美型还是一键美颜
    private MeiYanValueBean mMeiYanValue;//用户设置的美颜，美型数值
    private MeiYanValueBean mOneKeyValue;//用户设置的一键美颜数值
    private IBeautyEffectListener mMeiYanChangedListener;
    private Context mContext;
    private SPUtil mSPUtil;
    private String mActionStickerName = "";

    private boolean isShowCapture = true;
    private BeautyViewHolder mBeautyViewHolder;
    private MeiYanDataBean mMeiYanDataBean;

    private int mFilterId;
    private int mHahaName;
    private String mTieZhiName;
    private int  mTieZhiAction;
    private boolean  mTieZhiShow;
    private Bitmap mWaterBitmap;
    private int mWaterRes;
    private int mWaterPosition;
    private int mTeXiaoId;

    private boolean mMakeupLipstick;
    private boolean mMakeupEyelash;
    private boolean mMakeupEyeliner;
    private boolean mMakeupEyebrow;
    private boolean mMakeupBlush;

    private MhDataManager() {

    }

    public boolean getShowCapture(){
        return isShowCapture;
    }

    public static MhDataManager getInstance() {
        if (sInstance == null) {
            synchronized (MhDataManager.class) {
                if (sInstance == null) {
                    sInstance = new MhDataManager();
                }
            }
        }
        return sInstance;
    }

    public Context getContext(){
        return CommonAppContext.getInstance();
    }

    public MHBeautyManager getMHBeautyManager(){
        return mMhManager;
    }

    public void setBeautyViewHolder(BeautyViewHolder beautyViewHolder){
        mBeautyViewHolder = beautyViewHolder;
    }


    public MhDataManager init() {
        mInputValue = new MeiYanValueBean();
        mUseValue = null;
        mMeiYanValue = null;
        mOneKeyValue = null;
        mTieZhiName = null;
        mTeXiaoId = MHSDK.SPECIAL_NONE;
        mFilterId = MHSDK.FILTER_NONE;
        mWaterRes = 0;
        mWaterPosition = MHSDK.WATER_NONE;
        mHahaName = MHSDK.HAHA_NONE;
        releaseBeautyManager();
        return this;
    }



    public void createBeautyManager() {
        if (mInputValue != null) {
            mInputValue.reset();
        } else {
            mInputValue = new MeiYanValueBean();
        }
        try {
            mMhManager = new MHBeautyManager(CommonAppContext.getInstance());
        } catch (Exception e) {
            mMhManager = null;
            e.printStackTrace();
        }
    }



    public MHBeautyManager getMhManager(){
        return mMhManager;
    }




    public String getActionStickerName(){
        return mActionStickerName;
    }

    public void setActionStickerName(String actionStickerName){
        mActionStickerName = actionStickerName;
    }


    private void requestActionStickers(){
        MhDataManager.getTieZhiList(MHSDK.TIEZHI_ACTION, new CommonCallback<String>() {
            @Override
            public void callback(String jsonStr) {
                if (TextUtils.isEmpty(jsonStr)) {
                    return;
                }
                try {
                    JSONObject obj = JSON.parseObject(jsonStr);
                    List<TieZhiBean> list = JSON.parseArray(obj.getString("list"), TieZhiBean.class);
                    if (list != null && list.size() > 0) {
                        if (list.size() == 1){
                            String actionStickerName = list.get(0).getName();
                            setActionStickerName(actionStickerName);
                            downloadActionStickers(actionStickerName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void downloadActionStickers(String stickerName){
        downloadTieZhi(stickerName, new CommonCallback<Boolean>() {
            @Override
            public void callback(Boolean isSuccess) {
            }
        });
    }


    public void setMeiYanChangedListener(IBeautyEffectListener meiYanChangedListener) {
        mMeiYanChangedListener = meiYanChangedListener;
    }

    public void release() {
        mMeiYanChangedListener = null;
        mInputValue = null;
        mUseValue = null;
        mMeiYanValue = null;
        mOneKeyValue = null;
        mTieZhiName = null;
        mTeXiaoId = MHSDK.SPECIAL_NONE;
        mFilterId = MHSDK.FILTER_NONE;
        mWaterRes = 0;
        mWaterPosition = MHSDK.WATER_NONE;
        mHahaName = MHSDK.HAHA_NONE;
        releaseBeautyManager();
    }

    public void releaseBeautyManager() {
        if (mMhManager != null) {
            try {
                mMhManager.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMhManager = null;
    }

    public int render(int texture, int width, int height) {
        if (mMhManager != null) {
            try {
                texture = mMhManager.render12(texture, width, height,2,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Log.e(TAG, "render: ");
        }
        return texture;
    }


    public void restoreBeautyValue() {
        notifyMeiYanChanged();
        notifyLiangDuChanged();
        if (!TextUtils.isEmpty(mTieZhiName)) {
            setTieZhi(mTieZhiName);
        }
        if (mFilterId != MHSDK.FILTER_NONE) {
            if (isUseMhFilter() && mMhManager != null) {
                mMhManager.setFilter(mFilterId);
            }
        }
        if (mTeXiaoId != MHSDK.SPECIAL_NONE) {
            setTeXiao(mTeXiaoId);
        }
        if (mWaterRes != 0 && mWaterPosition != MHSDK.WATER_NONE) {
            setWater(mWaterRes, mWaterPosition);
        }
        if (mHahaName != MHSDK.HAHA_NONE) {
            setHaHa(mHahaName);
        }
    }

    public void saveBeautyValue() {
        if (mMeiYanValue != null) {
            CommonHttpUtil.setBeautyValue(JSON.toJSONString(mMeiYanValue));
        }
    }

    public MeiYanValueBean getMeiYanValue() {
        return mMeiYanValue;
    }

    public MeiYanValueBean getOneKeyValue() {
        return mOneKeyValue;
    }

    public MhDataManager setMeiYanValue(MeiYanValueBean meiYanValue) {
        mMeiYanValue = meiYanValue;
        return this;
    }

    public MhDataManager setOneKeyValue(MeiYanValueBean oneKeyValue) {
        mOneKeyValue = oneKeyValue;
        return this;
    }


    public MhDataManager useMeiYan() {
        mUseValue = mMeiYanValue;
        return this;
    }

    public MhDataManager useOneKey() {
        mUseValue = mOneKeyValue;
        return this;
    }


    public void setMeiBai(int meiBai) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setMeiBai(meiBai);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setMoPi(int moPi) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setMoPi(moPi);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setHongRun(int hongRun) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setHongRun(hongRun);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setLiangDu(int liangDu) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setLiangDu(liangDu);
        }
        useMeiYan().notifyLiangDuChanged();
    }


    public void setDaYan(int daYan) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setDaYan(daYan);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setMeiMao(int meiMao) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setMeiMao(meiMao);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setYanJu(int yanJu) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setYanJu(yanJu);
        }
        useMeiYan().notifyMeiYanChanged();
    }

    public void setYanJiao(int yanJiao) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setYanJiao(yanJiao);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setShouLian(int shouLian) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setShouLian(shouLian);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setZuiXing(int zuiXing) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setZuiXing(zuiXing);
        }
        useMeiYan().notifyMeiYanChanged();
    }

    public void setShouBi(int shouBi) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setShouBi(shouBi);
        }
        useMeiYan().notifyMeiYanChanged();
    }

    public void setXiaBa(int xiaBa) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setXiaBa(xiaBa);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setETou(int ETou) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setETou(ETou);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    public void setChangBi(int changBi) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setChangBi(changBi);
        }
        useMeiYan().notifyMeiYanChanged();
    }

    public void setXueLian(int xueLian) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setXueLian(xueLian);
        }
        useMeiYan().notifyMeiYanChanged();
    }

    public void setKaiYanJiao(int kaiYanJiao) {
        if (mMeiYanValue != null) {
            mMeiYanValue.setKaiYanJiao(kaiYanJiao);
        }
        useMeiYan().notifyMeiYanChanged();
    }


    /**
     * 哈哈镜
     */
    public void setHaHa(int hahaName) {
        if (mMhManager != null) {
            mMhManager.setDistortionEffect(hahaName);
            mHahaName = hahaName;
        }
    }

    /**
     * 贴纸是否可用
     */
    public boolean isTieZhiEnable() {
        return mMeiYanChangedListener == null || mMeiYanChangedListener.isTieZhiEnable();
    }

    /**
     * 贴纸
     */
    public void setTieZhi(String tieZhiName) {
        if (mMhManager != null) {
            boolean isShow = true;
            mMhManager.setSticker(tieZhiName,0,isShow);
            mTieZhiName = tieZhiName;
            mTieZhiAction = 0;
            mTieZhiShow  = isShow;
        }
    }

    /**
     * 贴纸
     */
    public void setTieZhi(String tieZhiName,int action) {
        if (mMhManager != null) {
            boolean isShow = false;
            mMhManager.setSticker(tieZhiName,action,isShow);
            mTieZhiName = tieZhiName;
            mTieZhiAction = action;
            mTieZhiShow  = isShow;
        }
    }

    /**
     * 水印
     */
    public void setWater(int waterRes, int position) {
        if (mMhManager != null) {
            Bitmap bitmap = null;
            if (waterRes == 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
            } else {
                bitmap = BitmapFactory.decodeResource(MhDataManager.getInstance().getContext().getResources(),waterRes);
            }
            if (bitmap != null) {
                mMhManager.setWaterMark(bitmap,position);
            }
            mWaterBitmap = bitmap;
            mWaterRes = waterRes;
            mWaterPosition = position;
        }
    }


    /**
     * 特效
     */
    public void setTeXiao(int teXiaoId) {
        if (mMhManager != null) {
            mMhManager.setSpeciallyEffect(teXiaoId);
            mTeXiaoId = teXiaoId;
        }
    }

    /**
     * 是否使用用美狐的滤镜 true使用美狐滤镜，false不使用
     */
    public boolean isUseMhFilter() {
        return mMeiYanChangedListener == null || mMeiYanChangedListener.isUseMhFilter();
    }


    /**
     * 滤镜
     */
    public void setFilter(MeiYanFilterBean bean) {
        if (isUseMhFilter()) {
            mFilterId = bean.getFilterRes();
            if (mMhManager != null) {
                mMhManager.setFilter(bean.getFilterRes());
            }
        } else {
            if (mMeiYanChangedListener != null) {
                mMeiYanChangedListener.onFilterChanged(bean.getName());
            }
        }

    }

    /**
     * 亮度数值发生变化
     */
    private void notifyLiangDuChanged() {
        if (mMhManager == null || mInputValue == null || mUseValue == null) {
            return;
        }
        if (mInputValue.getLiangDu() != mUseValue.getLiangDu()) {
            mInputValue.setLiangDu(mUseValue.getLiangDu());
            mMhManager.setBrightness(mUseValue.getLiangDu());
        }
    }

    /**
     * 美颜美型数值发生变化
     */
    public void notifyMeiYanChanged() {
        if (mMhManager == null || mInputValue == null || mUseValue == null) {
            return;
        }
        MeiYanValueBean input = mInputValue;
        MeiYanValueBean use = mUseValue;
        if (mMeiYanChangedListener != null) {
            boolean meiBaiChanged = false;
            boolean moPiChanged = false;
            boolean hongRunChanged = false;
            if (input.getMeiBai() != use.getMeiBai()) {
                input.setMeiBai(use.getMeiBai());
                meiBaiChanged = true;
            }
            if (input.getMoPi() != use.getMoPi()) {
                input.setMoPi(use.getMoPi());
                moPiChanged = true;
            }
            if (input.getHongRun() != use.getHongRun()) {
                input.setHongRun(use.getHongRun());
                hongRunChanged = true;
            }
            mMeiYanChangedListener.onMeiYanChanged(input.getMeiBai(), meiBaiChanged, input.getMoPi(), moPiChanged, input.getHongRun(), hongRunChanged);

        } else {
            //美白
            if (input.getMeiBai() != use.getMeiBai()) {
                input.setMeiBai(use.getMeiBai());
                mMhManager.setSkinWhiting(input.getMeiBai());
            }
            //磨皮
            if (input.getMoPi() != use.getMoPi()) {
                input.setMoPi(use.getMoPi());
                mMhManager.setSkinSmooth(input.getMoPi());

            }
            //红润
            if (input.getHongRun() != use.getHongRun()) {
                input.setHongRun(use.getHongRun());
                mMhManager.setSkinTenderness(input.getHongRun());
            }
        }

        //大眼
        if (input.getDaYan() != use.getDaYan()) {
            input.setDaYan(use.getDaYan());
            mMhManager.setBigEye(input.getDaYan());
        }
        //眉毛
        if (input.getMeiMao() != use.getMeiMao()) {
            input.setMeiMao(use.getMeiMao());
            mMhManager.setEyeBrow(input.getMeiMao());
        }
        //眼距
        if (input.getYanJu() != use.getYanJu()) {
            input.setYanJu(use.getYanJu());
            mMhManager.setEyeLength(input.getYanJu());
        }
        //眼角
        if (input.getYanJiao() != use.getYanJiao()) {
            input.setYanJiao(use.getYanJiao());
            mMhManager.setEyeCorner(input.getYanJiao());
        }
        //瘦脸
        if (input.getShouLian() != use.getShouLian()) {
            input.setShouLian(use.getShouLian());
            mMhManager.setFaceLift(input.getShouLian());
        }
        //嘴型
        if (input.getZuiXing() != use.getZuiXing()) {
            input.setZuiXing(use.getZuiXing());
            mMhManager.setMouseLift(input.getZuiXing());
        }
        //瘦鼻
        if (input.getShouBi() != use.getShouBi()) {
            input.setShouBi(use.getShouBi());
            mMhManager.setNoseLift(input.getShouBi());
        }
        //下巴
        if (input.getXiaBa() != use.getXiaBa()) {
            input.setXiaBa(use.getXiaBa());
            mMhManager.setChinLift(input.getXiaBa());
        }
        //额头
        if (input.getETou() != use.getETou()) {
            input.setETou(use.getETou());
            mMhManager.setForeheadLift(input.getETou());
        }
        //长鼻
        if (input.getChangBi() != use.getChangBi()) {
            input.setChangBi(use.getChangBi());
            mMhManager.setLengthenNoseLift(input.getChangBi());
        }
        //削脸
        if (input.getXueLian() != use.getXueLian()) {
            input.setXueLian(use.getXueLian());
            mMhManager.setFaceShave(input.getXueLian());
        }
        //开眼角
        if (input.getKaiYanJiao() != use.getKaiYanJiao()) {
            input.setKaiYanJiao(use.getKaiYanJiao());
            mMhManager.setEyeAlat(input.getKaiYanJiao());
        }
    }


    /**
     * 获取贴纸列表
     */
    public static void getTieZhiList(int id, final CommonCallback<String> commonCallback) {
        MHSDK.getTieZhiList(id, new MHSDK.TieZhiListCallback() {
            @Override
            public void getTieZhiList(String data) {
                if (commonCallback != null) {
                    commonCallback.callback(data);
                }
            }
        });
    }


    /**
     * 下载贴纸
     */
    public static void downloadTieZhi(String tieZhiName, final CommonCallback<Boolean> commonCallback) {
        MHSDK.downloadSticker(tieZhiName, new MHSDK.TieZhiDownloadCallback() {
            @Override
            public void tieZhiDownload(String tieZhiName, boolean success) {
                if (success) {
                    if (commonCallback != null) {
                        commonCallback.callback(true);
                    }
                } else {
                    if (commonCallback != null) {
                        commonCallback.callback(false);
                    }
                }
            }
        });

    }


    /**
     * 贴纸是否下载了
     */
    public static boolean isTieZhiDownloaded(String name) {
        return MHSDK.isTieZhiDownloaded(name);
    }


    /**
     * 美妆
     * @param makeupId
     * @param enable
     */
    public void setMakeup(int makeupId,boolean enable) {
        if (mMhManager != null) {

            switch (makeupId){
                case MHSDK.MAKEUP_NONE:
                    mMakeupLipstick = false;
                    mMakeupEyelash = false;
                    mMakeupEyeliner = false;
                    mMakeupEyebrow = false;
                    mMakeupBlush = false;
                    break;
                case MHSDK.MAKEUP_LIPSTICK:
                    mMakeupLipstick = enable;
                    break;
                case MHSDK.MAKEUP_EYELASH:
                    mMakeupEyelash = enable;
                    break;
                case MHSDK.MAKEUP_EYELINER:
                    mMakeupEyeliner = enable;
                    break;
                case MHSDK.MAKEUP_EYEBROW:
                    mMakeupEyebrow = enable;
                    break;
                case MHSDK.MAKEUP_BLUSH:
                    mMakeupBlush = enable;
                    break;
            }

            mMhManager.setMakeup(makeupId,enable);
        }
    }


}
