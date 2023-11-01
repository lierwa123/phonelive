package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhMeiYanFilterAdapter;
import com.yunbao.beauty.bean.MeiYanFilterBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.meihu.beautylibrary.MHSDK;

import java.util.ArrayList;
import java.util.List;

public class MhMeiYanFilterViewHolder extends MhMeiYanChildViewHolder implements OnItemClickListener<MeiYanFilterBean> {

    public MhMeiYanFilterViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        List<MeiYanFilterBean> list = new ArrayList<>();
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_no, R.mipmap.ic_filter_no, MHSDK.FILTER_NONE, true));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_langman, R.mipmap.ic_filter_langman, MHSDK.FILTER_LANG_MAN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_qingxin, R.mipmap.ic_filter_qingxin, MHSDK.FILTER_QING_XIN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_weimei, R.mipmap.ic_filter_weimei, MHSDK.FILTER_WEI_MEI));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_fennen, R.mipmap.ic_filter_fennen, MHSDK.FILTER_FEN_NEN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_huaijiu, R.mipmap.ic_filter_huaijiu, MHSDK.FILTER_HUAI_JIU));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_qingliang, R.mipmap.ic_filter_qingliang, MHSDK.FILTER_QING_LIANG));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_landiao, R.mipmap.ic_filter_landiao, MHSDK.FILTER_LAN_DIAO));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_rixi, R.mipmap.ic_filter_rixi, MHSDK.FILTER_RI_XI));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_chengshi, R.mipmap.ic_filter_chengshi, MHSDK.FILTER_CHENG_SHI));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_chulian, R.mipmap.ic_filter_chulian, MHSDK.FILTER_CHU_LIAN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_chuxin, R.mipmap.ic_filter_chuxin, MHSDK.FILTER_CHU_XIN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_danse, R.mipmap.ic_filter_danse, MHSDK.FILTER_DAN_SE));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_fanchase, R.mipmap.ic_filter_fanchase, MHSDK.FILTER_FA_CHA_SE));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_hupo, R.mipmap.ic_filter_hupo, MHSDK.FILTER_HU_PO));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_meiwei, R.mipmap.ic_filter_meiwei, MHSDK.FILTER_MEI_WEI));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_mitaofen, R.mipmap.ic_filter_mitaofen, MHSDK.FILTER_MI_TAO_FEN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_naicha, R.mipmap.ic_filter_naicha, MHSDK.FILTER_NAI_CHA));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_pailide, R.mipmap.ic_filter_pailide, MHSDK.FILTER_PAI_LI_DE));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_wutuobang, R.mipmap.ic_filter_wutuobang, MHSDK.FILTER_WU_TUO_BANG));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_xiyou, R.mipmap.ic_filter_xiyou, MHSDK.FILTER_XI_YOU));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_filter_riza, R.mipmap.ic_filter_riza, MHSDK.FILTER_RI_ZA));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_heimao, R.mipmap.ic_filter_heimao, MHSDK.FILTER_HEI_MAO));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_heibai, R.mipmap.ic_filter_heibai, MHSDK.FILTER_HEI_BAI));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_bulukelin, R.mipmap.ic_filter_bulukelin, MHSDK.FILTER_BU_LU_KE_LIN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_pingjing, R.mipmap.ic_filter_pingjing, MHSDK.FILTER_PING_JING));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_lengku, R.mipmap.ic_filter_lengku, MHSDK.FILTER_LENG_KU));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_kaiwen, R.mipmap.ic_filter_kaiwen, MHSDK.FILTER_KAI_WEN));
        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_lianai, R.mipmap.ic_filter_lianai, MHSDK.FILTER_LIAN_AI));
//        list.add(new MeiYanFilterBean(R.string.beauty_mh_pro_filter_jianbao, R.mipmap.ic_filter_jianbao, MHSDK.FILTER_JIAN_BAO));

        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        MhMeiYanFilterAdapter adapter = new MhMeiYanFilterAdapter(mContext, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(MeiYanFilterBean bean, int position) {
        MhDataManager.getInstance().setFilter(bean);
    }


    @Override
    public void showSeekBar() {
        if (mActionListener != null) {
            mActionListener.changeProgress(false, 0, 0);
        }
    }


}
