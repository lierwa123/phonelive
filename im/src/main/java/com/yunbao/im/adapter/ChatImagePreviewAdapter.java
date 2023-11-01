package com.yunbao.im.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.BitmapUtil;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.im.R;
import com.yunbao.im.bean.ImMessageBean;
import com.yunbao.im.custom.MyImageView2;
import com.yunbao.im.utils.ImMessageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by cxf on 2018/11/28.
 */

public class ChatImagePreviewAdapter extends RecyclerView.Adapter<ChatImagePreviewAdapter.Vh> {

    private Context mContext;
    private List<ImMessageBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mLongClickListener;
    private ActionListener mActionListener;

    public ChatImagePreviewAdapter(Context context, List<ImMessageBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                if (mActionListener != null) {
                    mActionListener.onImageClick();
                }
            }
        };
        mLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final File imageFile = ((MyImageView2) v).getFile();
                if (imageFile != null && imageFile.exists()) {
                    DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.save_image_album}, new DialogUitl.StringArrayDialogCallback() {
                        @Override
                        public void onItemClick(String text, int tag) {
                            FileInputStream inputStream = null;
                            FileOutputStream outputStream = null;
                            try {
                                inputStream = new FileInputStream(imageFile);
                                File dir = new File(CommonAppConfig.CAMERA_IMAGE_PATH);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                File saveFile = new File(dir, StringUtil.generateFileName() + ".png");
                                outputStream = new FileOutputStream(saveFile);
                                byte[] buf = new byte[4096];
                                int len = 0;
                                while ((len = inputStream.read(buf)) > 0) {
                                    outputStream.write(buf, 0, len);
                                }
                                BitmapUtil.saveImageInfo(saveFile);
                                ToastUtil.show(R.string.save_success);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    return true;
                }
                return false;
            }
        };
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_im_chat_img, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
    }

    class Vh extends RecyclerView.ViewHolder {

        MyImageView2 mImg;
        CommonCallback<File> mCommonCallback;
        ImMessageBean mImMessageBean;

        public Vh(View itemView) {
            super(itemView);
            mImg = (MyImageView2) itemView;
            mImg.setOnClickListener(mOnClickListener);
            mImg.setOnLongClickListener(mLongClickListener);
            mCommonCallback = new CommonCallback<File>() {
                @Override
                public void callback(File file) {
                    if (mImMessageBean != null && mImg != null) {
                        mImMessageBean.setImageFile2(file);
                        mImg.setFile(file);
                        ImgLoader.display(mContext, file, mImg);
                    }
                }
            };
        }

        void setData(ImMessageBean bean) {
            mImMessageBean = bean;
            File imageFile = bean.getImageFile2();
            if (imageFile != null && imageFile.exists()) {
                mImg.setFile(imageFile);
                ImgLoader.display(mContext, imageFile, mImg);
            } else {
                ImMessageUtil.getInstance().displayImageFile(mContext, bean, mCommonCallback, false);
            }
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onImageClick();
    }
}
