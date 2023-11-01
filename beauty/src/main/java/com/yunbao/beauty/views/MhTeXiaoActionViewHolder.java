package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhTeXiaoActionAdapter;
import com.yunbao.beauty.bean.TeXiaoActionBean;
import com.yunbao.beauty.bean.TieZhiBean;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.interfaces.OnTieZhiActionClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.beauty.utils.ToastUtil;
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;

import java.util.ArrayList;
import java.util.List;

public class MhTeXiaoActionViewHolder extends MhTeXiaoChildViewHolder implements OnItemClickListener<TeXiaoActionBean> {

    private final  String  TAG = MhTeXiaoActionViewHolder.class.getName();
    private  MhTeXiaoActionAdapter adapter;


    public MhTeXiaoActionViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        List<TeXiaoActionBean> list = new ArrayList<>();

        list.add(new TeXiaoActionBean(R.string.beauty_none, R.mipmap.ic_mh_none, R.mipmap.ic_mh_none,"",0));
        list.add(new TeXiaoActionBean(R.string.beauty_mh_texiao_action_head, R.mipmap.ic_meiyan_meibai_0, R.mipmap.ic_meiyan_meibai_1,"",1));
        list.add(new TeXiaoActionBean(R.string.beauty_mh_texiao_action_mouth, R.mipmap.ic_meiyan_mopi_0, R.mipmap.ic_meiyan_mopi_1,"",2));
        list.add(new TeXiaoActionBean(R.string.beauty_mh_texiao_action_eye, R.mipmap.ic_meiyan_hongrun_0, R.mipmap.ic_meiyan_hongrun_1,"",3));

        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        adapter =  new MhTeXiaoActionAdapter(mContext, list);
        adapter.setOnItemClickListener(this);
        adapter.setOnTieZhiActionClickListener(new OnTieZhiActionClickListener() {
            @Override
            public void OnTieZhiActionClick(int action) {
                if (mOnTieZhiActionClickListener != null){
                    mOnTieZhiActionClickListener.OnTieZhiActionClick(action);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    public void setItemClick(int postion){
        if (adapter != null){
            adapter.setItemClick(postion);
        }else{
            Log.e(TAG, "setItemClick: ");
        }
    }

    @Override
    public void onItemClick(TeXiaoActionBean bean, int position) {
        int action  = bean.getAction();
        if (action == 0){
            enableUseFace(null);
            MhDataManager.getInstance().setTieZhi(null);
            if (mOnTieZhiActionListener != null){
                mOnTieZhiActionListener.OnTieZhiAction(bean.getAction());
            }
        }else{
            String stickerName = MhDataManager.getInstance().getActionStickerName();
            if (MhDataManager.isTieZhiDownloaded(stickerName)){
                setTieZhi(bean,stickerName);
            }else{
                requestSticker(bean);
            }
        }
    }
    
    private void requestSticker(final TeXiaoActionBean bean){

        if (mOnTieZhiActionDownloadListener != null){
            mOnTieZhiActionDownloadListener.OnTieZhiActionDownload(0);
        }

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
                                    String  actionStickerName = list.get(0).getName();
                                    MhDataManager.getInstance().setActionStickerName(actionStickerName);
                                    bean.setStickerName(actionStickerName);
                                    downloadSticker(bean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
         });
    }

    private void downloadSticker(final TeXiaoActionBean bean){
        final String stickerName = bean.getStickerName();
        MhDataManager.downloadTieZhi(stickerName, new CommonCallback<Boolean>() {
            @Override
            public void callback(Boolean isSuccess) {
                if (isSuccess) {
                    setTieZhi(bean,stickerName);
                    if (mOnTieZhiActionListener != null){
                        mOnTieZhiActionListener.OnTieZhiAction(bean.getAction());
                    }
                } else {
                    if (mOnTieZhiActionDownloadListener != null){
                        mOnTieZhiActionDownloadListener.OnTieZhiActionDownload(1);
                    }
                    ToastUtil.show(R.string.beauty_mh_009);
                }
            }
        });
    }

    private void setTieZhi(TeXiaoActionBean bean,String stickerName){
        enableUseFace(stickerName);
        MhDataManager.getInstance().setTieZhi(stickerName,bean.getAction());
        if (mOnTieZhiActionDownloadListener != null){
            mOnTieZhiActionDownloadListener.OnTieZhiActionDownload(1);
        }
        if (mOnTieZhiActionListener != null){
            mOnTieZhiActionListener.OnTieZhiAction(bean.getAction());
        }
    }

    private void enableUseFace(String stickerName){
        int useFace;
        if(TextUtils.isEmpty(stickerName)){
            useFace = 0;
        }else{
            useFace = 1;
        }
        MHBeautyManager mhBeautyManager =  MhDataManager.getInstance().getMHBeautyManager();
        if (mhBeautyManager != null){
            int[]  useFaces = mhBeautyManager.getUseFaces();
            useFaces[4] = useFace;
            mhBeautyManager.setUseFaces(useFaces);
        }
    }



}
