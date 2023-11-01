package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhHaHaAdapter;
import com.yunbao.beauty.bean.HaHaBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;

import java.util.ArrayList;
import java.util.List;

public class MhTeXiaoHaHaViewHolder extends MhTeXiaoChildViewHolder implements OnItemClickListener<HaHaBean> {

    public MhTeXiaoHaHaViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {

        List<HaHaBean> list = new ArrayList<>();
        list.add(new HaHaBean(MHSDK.HAHA_NONE, 0, R.mipmap.ic_mh_none, true));
        list.add(new HaHaBean(MHSDK.HAHA_WAIXING, R.string.beauty_mh_haha_waixingren, R.mipmap.ic_haha_waixingren));
        list.add(new HaHaBean(MHSDK.HAHA_LI, R.string.beauty_mh_haha_li, R.mipmap.ic_haha_li));
        list.add(new HaHaBean(MHSDK.HAHA_SHOU, R.string.beauty_mh_haha_shou, R.mipmap.ic_haha_shou));
        list.add(new HaHaBean(MHSDK.HAHA_JING_XIANG, R.string.beauty_mh_haha_jingxiang, R.mipmap.ic_haha_jingxiang));
        list.add(new HaHaBean(MHSDK.HAHA_PIAN_DUAN, R.string.beauty_mh_haha_pianduan, R.mipmap.ic_haha_pianduan));
        list.add(new HaHaBean(MHSDK.HAHA_DAO_YING, R.string.beauty_mh_haha_daoying, R.mipmap.ic_haha_daoying));
        list.add(new HaHaBean(MHSDK.HAHA_LUO_XUAN, R.string.beauty_mh_haha_xuanzhuan, R.mipmap.ic_haha_xuanzhuan));
        list.add(new HaHaBean(MHSDK.HAHA_YU_YAN, R.string.beauty_mh_haha_yuyan, R.mipmap.ic_haha_yuyan));
        list.add(new HaHaBean(MHSDK.HAHA_ZUO_YOU, R.string.beauty_mh_haha_zuoyou, R.mipmap.ic_haha_zuoyou));
        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        MhHaHaAdapter adapter = new MhHaHaAdapter(mContext, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(HaHaBean bean, int position) {

        int useFace;
        if(bean.getId() == MHSDK.HAHA_NONE){
            useFace = 0;
        }else{
            useFace = 1;
        }
        MHBeautyManager mhBeautyManager =  MhDataManager.getInstance().getMHBeautyManager();
        if (mhBeautyManager != null){
            int[]  useFaces = mhBeautyManager.getUseFaces();
            useFaces[3] = useFace;
            mhBeautyManager.setUseFaces(useFaces);
        }

        MhDataManager.getInstance().setHaHa(bean.getId());

    }


}
