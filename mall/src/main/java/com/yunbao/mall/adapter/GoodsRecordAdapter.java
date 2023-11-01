package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.bean.GoodsRecordBean;
import com.yunbao.mall.bean.GoodsRecordItemBean;
import com.yunbao.mall.bean.GoodsRecordTitleBean;

import java.util.List;

public class GoodsRecordAdapter extends RefreshAdapter<GoodsRecordBean> {

    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;
    private boolean mEdit;
    private View.OnClickListener mOnClickListener;
    private ActionListener mActionListener;

    public GoodsRecordAdapter(Context context) {
        super(context);
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_1);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_0);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                GoodsRecordBean bean = mList.get(position);
                if (!mEdit) {
                    if (bean instanceof GoodsRecordItemBean) {
                        GoodsRecordItemBean itemBean = (GoodsRecordItemBean) bean;
                        if (itemBean.getStatus() == 1) {
                            GoodsDetailActivity.forward(mContext, itemBean.getGoodsId(), itemBean.getType());
                        } else {
                            ToastUtil.show(R.string.mall_373);
                        }
                    }
                    return;
                }
                bean.toggle();
                if (bean instanceof GoodsRecordTitleBean) {
                    for (GoodsRecordBean bean1 : mList) {
                        if (bean1 instanceof GoodsRecordItemBean) {
                            if (((GoodsRecordItemBean) bean1).getParent() == bean) {
                                bean1.setChecked(bean.isChecked());
                            }
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    notifyItemChanged(position, Constants.PAYLOAD);
                    GoodsRecordTitleBean titleBean = ((GoodsRecordItemBean) bean).getParent();
                    if (titleBean.isItemAllChecked()) {
                        if (!titleBean.isChecked()) {
                            titleBean.setChecked(true);
                            notifyItemChanged(titleBean.getPosition(), Constants.PAYLOAD);
                        }
                    } else {
                        if (titleBean.isChecked()) {
                            titleBean.setChecked(false);
                            notifyItemChanged(titleBean.getPosition(), Constants.PAYLOAD);
                        }
                    }
                }
                if (mActionListener != null) {
                    int checedCount = getCheckedCount();
                    mActionListener.onCheckedItem(checedCount > 0);
                    mActionListener.onCheckedAll(checedCount == mList.size() ? mCheckedDrawable : mUnCheckedDrawable);
                }
            }
        };
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    public String getCheckedId() {
        StringBuilder sb = null;
        for (GoodsRecordBean bean : mList) {
            if (bean instanceof GoodsRecordItemBean && bean.isChecked()) {
                String id = ((GoodsRecordItemBean) bean).getId();
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(id);
                sb.append(",");
            }
        }
        if (sb != null) {
            String res = sb.toString();
            if (res.length() > 0) {
                return res.substring(0, res.length() - 1);
            }
        }
        return null;
    }

    private int getCheckedCount() {
        int count = 0;
        for (GoodsRecordBean bean : mList) {
            if (bean.isChecked()) {
                count++;
            }
        }
        return count;
    }


    public boolean isEdit() {
        return mEdit;
    }

    public void toggleEdit() {
        mEdit = !mEdit;
        for (GoodsRecordBean bean : mList) {
            bean.setEdit(mEdit);
            bean.setChecked(false);
        }
        notifyDataSetChanged();
        if (mActionListener != null) {
            mActionListener.onCheckedAll(mUnCheckedDrawable);
            mActionListener.onCheckedItem(false);
            mActionListener.onEditChanged(mEdit);
        }
    }


    public void toggleCheckedAll() {
        boolean checkedAll = getCheckedCount() != mList.size();
        for (GoodsRecordBean bean : mList) {
            bean.setChecked(checkedAll);
        }
        notifyDataSetChanged();
        if (mActionListener != null) {
            mActionListener.onCheckedItem(checkedAll);
            mActionListener.onCheckedAll(checkedAll ? mCheckedDrawable : mUnCheckedDrawable);
        }
    }

    @Override
    public void refreshData(List<GoodsRecordBean> list) {
        mEdit = false;
        super.refreshData(list);
        if (mActionListener != null) {
            mActionListener.onCheckedAll(mUnCheckedDrawable);
            mActionListener.onCheckedItem(false);
            mActionListener.onEditChanged(false);
        }
    }

    @Override
    public void insertList(List<GoodsRecordBean> list) {
        if (mRecyclerView != null && mList != null && list != null && list.size() > 0) {
            boolean checkedAll = getCheckedCount() == mList.size();
            for (GoodsRecordBean bean : list) {
                bean.setEdit(mEdit);
                bean.setChecked(checkedAll);
            }
            int p = mList.size();
            mList.addAll(list);
            notifyItemRangeInserted(p, list.size());
        }
    }


    @Override
    public int getItemViewType(int position) {
        GoodsRecordBean bean = mList.get(position);
        if (bean instanceof GoodsRecordTitleBean) {
            return -1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemViewType) {
        if (itemViewType == -1) {
            return new TitleVh(mInflater.inflate(R.layout.item_goods_record_title, viewGroup, false));
        }
        return new ItemVh(mInflater.inflate(R.layout.item_goods_record_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mImgCheck;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mImgCheck = itemView.findViewById(R.id.img_check);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsRecordBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (bean.isEdit()) {
                if (mImgCheck.getVisibility() != View.VISIBLE) {
                    mImgCheck.setVisibility(View.VISIBLE);
                }
                mImgCheck.setImageDrawable(bean.isChecked() ? mCheckedDrawable : mUnCheckedDrawable);
            } else {
                if (mImgCheck.getVisibility() != View.GONE) {
                    mImgCheck.setVisibility(View.GONE);
                }
            }
        }
    }

    class TitleVh extends Vh {

        TextView mDate;

        public TitleVh(@NonNull View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.date);
        }

        @Override
        void setData(GoodsRecordBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                GoodsRecordTitleBean titleBean = (GoodsRecordTitleBean) bean;
                titleBean.setPosition(position);
                mDate.setText(titleBean.getDate());
            }
        }
    }


    class ItemVh extends Vh {

        ImageView mThumb;
        TextView mName;
        TextView mPrice;
        TextView mOriginPrice;

        public ItemVh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            mOriginPrice = itemView.findViewById(R.id.origin_price);
            mOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        void setData(GoodsRecordBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                GoodsRecordItemBean itemBean = (GoodsRecordItemBean) bean;
                ImgLoader.display(mContext, itemBean.getThumb(), mThumb);
                mName.setText(itemBean.getName());
                mPrice.setText(itemBean.getPrice());
                if (itemBean.getType() == 1) {
                    mOriginPrice.setText(itemBean.getOriginPrice());
                } else {
                    mOriginPrice.setText(null);
                }
            }
        }
    }

    public interface ActionListener {
        void onCheckedItem(boolean checked);

        void onCheckedAll(Drawable drawable);

        void onEditChanged(boolean isEdit);
    }
}
