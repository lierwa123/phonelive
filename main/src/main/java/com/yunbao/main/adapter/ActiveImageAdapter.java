package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.glide.ImgLoader;
import com.yunbao.main.R;
import com.yunbao.main.bean.ActiveImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActiveImageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ActiveImageBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private ActionListener mActionListener;

    public ActiveImageAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                ActiveImageBean bean = mList.get(position);
                if (bean.getImageFile() == null) {
                    if (mActionListener != null) {
                        mActionListener.onAddClick();
                    }
                } else {
                    if (mActionListener != null) {
                        mActionListener.onItemClick(position);
                    }
                }
            }
        };
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void insertList(List<String> imagePathList) {
        int index = 0;
        for (ActiveImageBean bean : mList) {
            if (bean.getImageFile() == null) {
                bean.setImageFile(new File(imagePathList.get(index)));
                index++;
            }
        }
        int moreCount = 9 - mList.size();
        if (moreCount > 0) {
            moreCount = Math.min(moreCount, imagePathList.size() - index);
            for (int i = 0; i < moreCount; i++) {
                mList.add(new ActiveImageBean(new File(imagePathList.get(index + i))));
            }
        }
        if (mList.size() < 9) {
            mList.add(new ActiveImageBean());
        }
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        if (mList == null) {
            return;
        }
        if (position >= 0 && position < mList.size()) {
            mList.remove(position);
        }
        int size = mList.size();
        ActiveImageBean bean = mList.get(size - 1);
        if (bean.getImageFile() != null) {
            mList.add(new ActiveImageBean());
        } else {
            if (size == 1) {
                mList.clear();
            }
        }
        notifyDataSetChanged();
        if (mList.size() == 0 && mActionListener != null) {
            mActionListener.onDeleteAll();
        }
    }

    public List<File> getImageFileList() {
        List<File> list = null;
        for (ActiveImageBean bean : mList) {
            File file = bean.getImageFile();
            if (file != null && file.exists()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(file);
            }
        }
        return list;
    }

    public int getImageFileCount() {
        int count = 0;
        for (ActiveImageBean bean : mList) {
            File file = bean.getImageFile();
            if (file != null && file.exists()) {
                count++;
            }
        }
        return count;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new Vh(mInflater.inflate(R.layout.item_active_image, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {

        View mIconAdd;
        ImageView mImg;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mIconAdd = itemView.findViewById(R.id.icon_add);
            mImg = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ActiveImageBean bean, int position) {
            itemView.setTag(position);
            if (bean.getImageFile() == null) {
                mImg.setImageDrawable(null);
                if (mIconAdd.getVisibility() != View.VISIBLE) {
                    mIconAdd.setVisibility(View.VISIBLE);
                }
            } else {
                if (mIconAdd.getVisibility() == View.VISIBLE) {
                    mIconAdd.setVisibility(View.INVISIBLE);
                }
                ImgLoader.display(mContext, bean.getImageFile(), mImg);
            }
        }
    }


    public interface ActionListener {
        void onAddClick();

        void onItemClick(int position);

        void onDeleteAll();
    }
}
