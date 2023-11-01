package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhTeXiaoSpecialAdapter;
import com.yunbao.beauty.bean.TeXiaoSpecialBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.meihu.beautylibrary.MHSDK;

import java.util.ArrayList;
import java.util.List;

public class MhTeXiaoSpecialViewHolder extends MhTeXiaoChildViewHolder implements OnItemClickListener<TeXiaoSpecialBean> {

    public MhTeXiaoSpecialViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        List<TeXiaoSpecialBean> list = new ArrayList<>();
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_NONE, R.string.beauty_mh_filter_no, R.mipmap.ic_tx_no, true));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_LING_HUN, R.string.beauty_mh_texiao_linghun, R.mipmap.ic_tx_linghun));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_DOU_DONG, R.string.beauty_mh_texiao_doudong, R.mipmap.ic_tx_doudong));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_SHAN_BAI, R.string.beauty_mh_texiao_shanbai, R.mipmap.ic_tx_shanbai));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_MAI_CI, R.string.beauty_mh_texiao_maoci, R.mipmap.ic_tx_maoci));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_HUAN_JUE, R.string.beauty_mh_texiao_huanjue, R.mipmap.ic_tx_huanjue));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_MSK, R.string.beauty_mh_texiao_msk, R.mipmap.ic_tx_msk));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_MSK_0, R.string.beauty_mh_texiao_msk_yuan, R.mipmap.ic_tx_msk_yuan));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_MSK_3, R.string.beauty_mh_texiao_msk_san, R.mipmap.ic_tx_msk_san));
        list.add(new TeXiaoSpecialBean(MHSDK.SPECIAL_MSK_6, R.string.beauty_mh_texiao_msk_liu, R.mipmap.ic_tx_msk_liu));
        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        MhTeXiaoSpecialAdapter adapter = new MhTeXiaoSpecialAdapter(mContext, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(TeXiaoSpecialBean bean, int position) {
        MhDataManager.getInstance().setTeXiao(bean.getId());
    }


}
