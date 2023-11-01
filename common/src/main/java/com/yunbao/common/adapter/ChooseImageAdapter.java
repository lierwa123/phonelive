package com.yunbao.common.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.Constants;
import com.yunbao.common.R;
import com.yunbao.common.bean.ChooseImageBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseImageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ChooseImageBean> mList;
    private View.OnClickListener mOnClickListener;
    private Drawable mCheckDrawable;
    private Drawable mUnCheckDrawable;
    private ActionListener mActionListener;
    private List<ChooseImageBean> mCheckedImageList;//选中的图片列表
    private int mMaxCount;

    public ChooseImageAdapter(Context context, List<ChooseImageBean> list, int maxCount) {
        mMaxCount = maxCount;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        mCheckedImageList = new ArrayList<>();
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                ChooseImageBean bean = mList.get(position);
                if (bean.getType() == ChooseImageBean.CAMERA) {
                    if (mActionListener != null) {
                        mActionListener.onCameraClick();
                    }
                    return;
                }
                if (bean.isChecked()) {
                    bean.setChecked(false);
                    notifyItemChanged(position, Constants.PAYLOAD);
                    mCheckedImageList.remove(bean);
                    if (mActionListener != null) {
                        mActionListener.onCheckedCountChanged(mCheckedImageList.size());
                    }
                } else {
                    if (mCheckedImageList.size() >= mMaxCount) {
                        ToastUtil.show(String.format(WordUtil.getString(R.string.choose_img_max), mMaxCount));
                        return;
                    }
                    bean.setChecked(true);
                    notifyItemChanged(position, Constants.PAYLOAD);
                    mCheckedImageList.add(bean);
                    if (mActionListener != null) {
                        mActionListener.onCheckedCountChanged(mCheckedImageList.size());
                    }
                }
            }
        };
        mCheckDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_select_1);
        mUnCheckDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_select_0);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChooseImageBean.CAMERA) {
            return new Vh(mInflater.inflate(R.layout.item_choose_img_camera, parent, false));
        }
        return new FileVh(mInflater.inflate(R.layout.item_choose_img_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public ArrayList<String> getImagePathList() {
        if (mCheckedImageList == null || mCheckedImageList.size() == 0) {
            return null;
        }
        ArrayList<String> list = null;
        for (ChooseImageBean bean : mCheckedImageList) {
            if (bean.getType() == ChooseImageBean.FILE && bean.isChecked()) {
                File file = bean.getImageFile();
                if (file != null && file.exists()) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(file.getAbsolutePath());
                }
            }
        }
        return list;
    }


    class Vh extends RecyclerView.ViewHolder {

        public Vh(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ChooseImageBean bean, int position, Object payload) {
            itemView.setTag(position);
        }
    }


    class FileVh extends Vh {

        ImageView mImg;
        ImageView mCheck;

        public FileVh(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mCheck = itemView.findViewById(R.id.check);
        }

        void setData(ChooseImageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                ImgLoader.display(mContext, bean.getImageFile(), mImg);
            }
            mCheck.setImageDrawable(bean.isChecked() ? mCheckDrawable : mUnCheckDrawable);
        }
    }


    public interface ActionListener {
        void onCameraClick();

        void onCheckedCountChanged(int checkedCount);
    }

}
