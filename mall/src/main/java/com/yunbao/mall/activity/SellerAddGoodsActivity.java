package com.yunbao.mall.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.activity.ChooseVideoActivity;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.ActivityResultCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.interfaces.PermissionCallback;
import com.yunbao.common.upload.UploadBean;
import com.yunbao.common.upload.UploadCallback;
import com.yunbao.common.upload.UploadStrategy;
import com.yunbao.common.upload.UploadUtil;
import com.yunbao.common.utils.ActivityResultUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.FloatWindowHelper;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MediaUtil;
import com.yunbao.common.utils.PermissionUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.AddGoodsDetailAdapter;
import com.yunbao.mall.adapter.AddGoodsSpecAdapter;
import com.yunbao.mall.adapter.AddGoodsTitleAdapter;
import com.yunbao.mall.bean.AddGoodsImageBean;
import com.yunbao.mall.bean.AddGoodsSpecBean;
import com.yunbao.mall.bean.GoodsClassBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 卖家添加商品
 */
public class SellerAddGoodsActivity extends AbsActivity {

    public static void forward(Context context, String goodsId) {
        Intent intent = new Intent(context, SellerAddGoodsActivity.class);
        if (!TextUtils.isEmpty(goodsId)) {
            intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
        }
        context.startActivity(intent);
    }

    private String mGoodsId;//要编辑的商品
    private TextView mGoodsClassName;
    private TextView mGoodsTitle;//商品标题
    private RecyclerView mRecyclerViewTitle;//商品标题图片
    private AddGoodsTitleAdapter mTitleAdapter;
    private TextView mGoodsDetail;//商品详情
    private RecyclerView mRecyclerViewDetail;//商品详情图片
    private AddGoodsDetailAdapter mDetailAdapter;
    private RecyclerView mRecyclerViewSpec;//商品规格
    private AddGoodsSpecAdapter mSpecAdapter;//商品规格
    private TextView mGoodsPostage;//邮费
    private int mTargetTitleImgPosition;
    private int mTargetDetailImgPosition;
    private int mTargetSpecImgPosition;
    private int mChooseImageType;//0  标题图片 1详情图片 2规格图片
    private CheckBox mCheckBox;
    private View mGroupPostage;
    private GoodsClassBean mGoodsClassBean;//商品类型
    private Dialog mLoading;
    private String mTitleVal;
    private String mDetailVal;
    private String mPostageVal;
    private ImageResultCallback mImageResultCallback = new ImageResultCallback() {
        @Override
        public void beforeCamera() {

        }

        @Override
        public void onSuccess(File file) {
            if (file != null && file.exists()) {
                if (mChooseImageType == 0) {
                    if (mTitleAdapter != null) {
                        mTitleAdapter.setImageFile(mTargetTitleImgPosition, file);
                    }
                } else if (mChooseImageType == 1) {
                    if (mDetailAdapter != null) {
                        mDetailAdapter.setImageFile(mTargetDetailImgPosition, file);
                    }
                } else if (mChooseImageType == 2) {
                    if (mSpecAdapter != null) {
                        mSpecAdapter.setImageFile(mTargetSpecImgPosition, file);
                    }
                }
            } else {
                ToastUtil.show(R.string.file_not_exist);
            }
        }


        @Override
        public void onFailure() {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_add_goods;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_074));
        mGoodsId = getIntent().getStringExtra(Constants.MALL_GOODS_ID);
        mGoodsClassName = findViewById(R.id.goods_class_name);
        mGoodsTitle = findViewById(R.id.goods_title);
        mGoodsDetail = findViewById(R.id.goods_detail);
        mGoodsPostage = findViewById(R.id.postage);
        mRecyclerViewTitle = findViewById(R.id.recyclerView_title);
        mRecyclerViewTitle.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 15, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerViewTitle.addItemDecoration(decoration);
        mTitleAdapter = new AddGoodsTitleAdapter(mContext);
        mTitleAdapter.setOnItemClickListener(new OnItemClickListener<AddGoodsImageBean>() {
            @Override
            public void onItemClick(AddGoodsImageBean bean, int position) {
                mTargetTitleImgPosition = position;
                mChooseImageType = 0;
                if (position == 0) {
                    chooseVideo();
                } else {
                    chooseImage();
                }
            }
        });
        mRecyclerViewTitle.setAdapter(mTitleAdapter);


        mRecyclerViewDetail = findViewById(R.id.recyclerView_detail);
        mRecyclerViewDetail.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration2 = new ItemDecoration(mContext, 0x00000000, 15, 0);
        decoration2.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerViewDetail.addItemDecoration(decoration2);
        mDetailAdapter = new AddGoodsDetailAdapter(mContext);
        mDetailAdapter.setOnItemClickListener(new OnItemClickListener<AddGoodsImageBean>() {
            @Override
            public void onItemClick(AddGoodsImageBean bean, int position) {
                mTargetDetailImgPosition = position;
                mChooseImageType = 1;
                chooseImage();
            }
        });
        mRecyclerViewDetail.setAdapter(mDetailAdapter);

        mRecyclerViewSpec = findViewById(R.id.recyclerView_spec);
        mRecyclerViewSpec.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSpecAdapter = new AddGoodsSpecAdapter(mContext);
        mSpecAdapter.setOnItemClickListener(new OnItemClickListener<AddGoodsSpecBean>() {
            @Override
            public void onItemClick(AddGoodsSpecBean bean, int position) {
                mTargetSpecImgPosition = position;
                mChooseImageType = 2;
                chooseImage();
            }
        });
        mRecyclerViewSpec.setAdapter(mSpecAdapter);
        mGroupPostage = findViewById(R.id.group_postage);
        mCheckBox = findViewById(R.id.checkbox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGroupPostage != null) {
                    if (isChecked) {
                        if (mGroupPostage.getVisibility() != View.GONE) {
                            mGroupPostage.setVisibility(View.GONE);
                        }
                    } else {
                        if (mGroupPostage.getVisibility() != View.VISIBLE) {
                            mGroupPostage.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        if (TextUtils.isEmpty(mGoodsId)) {
            List<AddGoodsImageBean> titleImgList = new ArrayList<>();
            titleImgList.add(new AddGoodsImageBean());
            titleImgList.add(new AddGoodsImageBean());
            mTitleAdapter.refreshData(titleImgList);

            List<AddGoodsImageBean> detailImgList = new ArrayList<>();
            detailImgList.add(new AddGoodsImageBean());
            mDetailAdapter.refreshData(detailImgList);

            List<AddGoodsSpecBean> specList = new ArrayList<>();
            specList.add(new AddGoodsSpecBean());
            mSpecAdapter.refreshData(specList);
        } else {
            MallHttpUtil.getGoodsInfo(mGoodsId, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        JSONObject goodsInfo = obj.getJSONObject("goods_info");

                        mGoodsClassBean = new GoodsClassBean();
                        mGoodsClassBean.setOneClassId(goodsInfo.getString("one_classid"));
                        mGoodsClassBean.setTwoClassId(goodsInfo.getString("two_classid"));
                        mGoodsClassBean.setId(goodsInfo.getString("three_classid"));

                        if (mGoodsClassName != null) {
                            mGoodsClassName.setText(goodsInfo.getString("three_class_name"));
                        }
                        if (mGoodsTitle != null) {
                            mGoodsTitle.setText(goodsInfo.getString("name"));
                        }
                        if (mGoodsDetail != null) {
                            mGoodsDetail.setText(goodsInfo.getString("content"));
                        }
                        double postage = goodsInfo.getDoubleValue("postage");
                        if (postage > 0) {
                            if (mCheckBox != null) {
                                mCheckBox.setChecked(false);
                            }
                            if (mGroupPostage != null && mGroupPostage.getVisibility() != View.VISIBLE) {
                                mGroupPostage.setVisibility(View.VISIBLE);
                            }
                            if (mGoodsPostage != null) {
                                mGoodsPostage.setText(String.valueOf(postage));
                            }
                        } else {
                            if (mGroupPostage != null && mGroupPostage.getVisibility() != View.GONE) {
                                mGroupPostage.setVisibility(View.GONE);
                            }
                            if (mCheckBox != null) {
                                mCheckBox.setChecked(true);
                            }
                        }
                        if (mTitleAdapter != null) {
                            mTitleAdapter.setVideoUrl(goodsInfo.getString("video_url"));
                            mTitleAdapter.setVideoImgUrl(goodsInfo.getString("video_thumb"));
                            mTitleAdapter.setVideoUrlPlay(goodsInfo.getString("video_url_format"));
                            mTitleAdapter.setVideoImgUrlPlay(goodsInfo.getString("video_thumb_format"));
                            List<AddGoodsImageBean> titleImgList = new ArrayList<>();
                            AddGoodsImageBean goodsVideoBean = new AddGoodsImageBean();
                            goodsVideoBean.setImgUrl(goodsInfo.getString("video_thumb_format"));
                            titleImgList.add(goodsVideoBean);
                            JSONArray titleImgArr = goodsInfo.getJSONArray("thumbs_format");
                            for (int i = 0, size = titleImgArr.size(); i < size; i++) {
                                String imgUrl = titleImgArr.getString(i);
                                if (!TextUtils.isEmpty(imgUrl)) {
                                    AddGoodsImageBean goodsImageBean = new AddGoodsImageBean();
                                    goodsImageBean.setImgUrl(imgUrl);
                                    titleImgList.add(goodsImageBean);
                                }
                            }
                            if (titleImgList.size() < 10) {
                                titleImgList.add(new AddGoodsImageBean());
                            }
                            mTitleAdapter.refreshData(titleImgList);
                        }

                        if (mDetailAdapter != null) {
                            List<AddGoodsImageBean> detailImgList = new ArrayList<>();
                            JSONArray titleImgArr = goodsInfo.getJSONArray("pictures_format");
                            for (int i = 0, size = titleImgArr.size(); i < size; i++) {
                                String imgUrl = titleImgArr.getString(i);
                                if (!TextUtils.isEmpty(imgUrl)) {
                                    AddGoodsImageBean goodsImageBean = new AddGoodsImageBean();
                                    goodsImageBean.setImgUrl(imgUrl);
                                    detailImgList.add(goodsImageBean);
                                }
                            }
                            if (detailImgList.size() < 20) {
                                detailImgList.add(new AddGoodsImageBean());
                            }
                            mDetailAdapter.refreshData(detailImgList);
                        }

                        if (mSpecAdapter != null) {
                            List<AddGoodsSpecBean> specList = JSON.parseArray(goodsInfo.getString("specs_format"), AddGoodsSpecBean.class);
                            mSpecAdapter.refreshData(specList);
                        }
                    }
                }
            });
        }
    }

    public void addGoodsClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_goods_class) {
            chooseGoodsClass();
        } else if (id == R.id.btn_add_spec) {
            addGoodsSpec();
        } else if (id == R.id.btn_submit) {
            submit();
        }
    }

    /**
     * 选择商品类型
     */
    private void chooseGoodsClass() {
        Intent intent = new Intent(mContext, ChooseGoodsClassActivity.class);
        ActivityResultUtil.startActivityForResult(this, intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    mGoodsClassBean = intent.getParcelableExtra(Constants.MALL_GOODS_CLASS);
                    if (mGoodsClassName != null) {
                        mGoodsClassName.setText(mGoodsClassBean.getName());
                    }
                }
            }
        });
    }

    /**
     * 添加商品规格
     */
    private void addGoodsSpec() {
        if (mSpecAdapter != null) {
            mSpecAdapter.insertItem();
        }
    }


    /**
     * 选择图片
     */
    private void chooseImage() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.alumb, R.string.camera}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    MediaUtil.getImageByCamera(SellerAddGoodsActivity.this, false, mImageResultCallback);
                } else if (tag == R.string.alumb) {
                    MediaUtil.getImageByAlumb(SellerAddGoodsActivity.this, false, mImageResultCallback);
                }
            }
        });
    }

    /**
     * 选择视频
     */
    private void chooseVideo() {
        if (!FloatWindowHelper.checkVoice(false)) {
            return;
        }
        PermissionUtil.request(this, new PermissionCallback() {
                    @Override
                    public void onAllGranted() {
                        Intent intent = new Intent(mContext, ChooseVideoActivity.class);
                        ActivityResultUtil.startActivityForResult(SellerAddGoodsActivity.this, intent, new ActivityResultCallback() {
                            @Override
                            public void onSuccess(Intent intent) {
                                String videoPath = intent.getStringExtra(Constants.VIDEO_PATH);
                                if (!TextUtils.isEmpty(videoPath) && mTitleAdapter != null) {
                                    mTitleAdapter.setImageFile(0, new File(videoPath));
                                }
                            }
                        });
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO);
    }

    private void submit() {
        if (mGoodsClassBean == null) {
            ToastUtil.show(R.string.mall_101);
            return;
        }
        String title = mGoodsTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.show(R.string.mall_102);
            return;
        }
        mTitleVal = title;
        String detail = mGoodsDetail.getText().toString().trim();
        if (TextUtils.isEmpty(detail)) {
            ToastUtil.show(R.string.mall_103);
            return;
        }
        mDetailVal = detail;
        if (!mCheckBox.isChecked()) {
            String postage = mGoodsPostage.getText().toString().trim();
            if (TextUtils.isEmpty(postage)) {
                ToastUtil.show(R.string.mall_104);
                return;
            }
            mPostageVal = postage;
        } else {
            mPostageVal = "0";
        }
        if (mSpecAdapter != null) {
            List<AddGoodsSpecBean> list = mSpecAdapter.getList();
            for (AddGoodsSpecBean bean : list) {
                if (TextUtils.isEmpty(bean.getName())) {
                    ToastUtil.show(R.string.mall_105);
                    return;
                }
                if (TextUtils.isEmpty(bean.getNum())) {
                    ToastUtil.show(R.string.mall_106);
                    return;
                }
                if (TextUtils.isEmpty(bean.getPrice())) {
                    ToastUtil.show(R.string.mall_107);
                    return;
                }
            }
        }
        uploadFile();

    }

    /**
     * 上传图片视频等文件
     */
    private void uploadFile() {
        final List<UploadBean> uploadList = new ArrayList<>();
        if (mTitleAdapter != null) {
            File videoFile = mTitleAdapter.getVideoFile();
            if (videoFile != null && videoFile.exists()) {
                UploadBean upVideoBean = new UploadBean(videoFile, UploadBean.VIDEO);
                upVideoBean.setTag(mTitleAdapter);
                uploadList.add(upVideoBean);
                File coverImageFile = createVideoCoverImage(videoFile.getAbsolutePath());
                if (coverImageFile != null && coverImageFile.length() > 0 && coverImageFile.exists()) {
                    UploadBean upCoverImgBean = new UploadBean(coverImageFile, UploadBean.IMG);
                    upCoverImgBean.setTag(mTitleAdapter);
                    uploadList.add(upCoverImgBean);
                }
            }

            List<AddGoodsImageBean> titleImgList = mTitleAdapter.getList();
            for (int i = 1, size = titleImgList.size(); i < size; i++) {
                AddGoodsImageBean bean = titleImgList.get(i);
                File uploadFile = bean.getFile();
                if (uploadFile != null && uploadFile.exists()) {
                    UploadBean uploadBean = new UploadBean(uploadFile, UploadBean.IMG);
                    uploadBean.setTag(bean);
                    uploadList.add(uploadBean);
                }
            }
        }

        if (mDetailAdapter != null) {
            List<AddGoodsImageBean> detailImgList = mDetailAdapter.getList();
            for (int i = 0, size = detailImgList.size(); i < size; i++) {
                AddGoodsImageBean bean = detailImgList.get(i);
                File uploadFile = bean.getFile();
                if (uploadFile != null && uploadFile.exists()) {
                    UploadBean uploadBean = new UploadBean(uploadFile, UploadBean.IMG);
                    uploadBean.setTag(bean);
                    uploadList.add(uploadBean);
                }
            }
        }

        if (mSpecAdapter != null) {
            List<AddGoodsSpecBean> specList = mSpecAdapter.getList();
            for (int i = 0, size = specList.size(); i < size; i++) {
                AddGoodsSpecBean bean = specList.get(i);
                File uploadFile = bean.getFile();
                if (uploadFile != null && uploadFile.exists()) {
                    UploadBean uploadBean = new UploadBean(uploadFile, UploadBean.IMG);
                    uploadBean.setTag(bean);
                    uploadList.add(uploadBean);
                }
            }
        }

        if (uploadList.size() > 0) {
            showLoading();
            UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
                @Override
                public void callback(UploadStrategy strategy) {
                    strategy.upload(uploadList, true, new UploadCallback() {
                        @Override
                        public void onFinish(List<UploadBean> list, boolean success) {
                            if (!success) {
                                hideLoading();
                                return;
                            }
                            for (UploadBean bean : list) {
                                Object tag = bean.getTag();
                                if (tag != null) {
                                    if (tag == mTitleAdapter) {
                                        if (bean.getType() == UploadBean.VIDEO) {
                                            mTitleAdapter.setVideoUrl(bean.getRemoteFileName());
                                        } else if (bean.getType() == UploadBean.IMG) {
                                            mTitleAdapter.setVideoImgUrl(bean.getRemoteFileName());
                                        }
                                    } else if (tag instanceof AddGoodsImageBean) {
                                        ((AddGoodsImageBean) tag).setUploadResultUrl(bean.getRemoteFileName());
                                    } else if (tag instanceof AddGoodsSpecBean) {
                                        ((AddGoodsSpecBean) tag).setUploadResultUrl(bean.getRemoteFileName());
                                    }
                                }
                            }
                            saveGoodsInfo();
                        }
                    });
                }
            });
        } else {
            if (TextUtils.isEmpty(mGoodsId)) {
                hideLoading();
                ToastUtil.show(R.string.mall_108);
            } else {
                saveGoodsInfo();
            }
        }
    }

    private void saveGoodsInfo() {
        StringBuilder sb = new StringBuilder();
        List<AddGoodsImageBean> titleImgList = mTitleAdapter.getList();
        for (int i = 1, size = titleImgList.size(); i < size; i++) {
            String uploadResultUrl = titleImgList.get(i).getUploadResultUrl();
            if (!TextUtils.isEmpty(uploadResultUrl)) {
                sb.append(uploadResultUrl);
                sb.append(",");
            }
        }
        String thumbs = sb.toString();
        if (thumbs.length() > 1) {
            thumbs = thumbs.substring(0, thumbs.length() - 1);
        }
        sb.delete(0, sb.length());
        List<AddGoodsImageBean> detailImgList = mDetailAdapter.getList();
        for (int i = 0, size = detailImgList.size(); i < size; i++) {
            String uploadResultUrl = detailImgList.get(i).getUploadResultUrl();
            if (!TextUtils.isEmpty(uploadResultUrl)) {
                sb.append(uploadResultUrl);
                sb.append(",");
            }
        }
        String pictures = sb.toString();
        if (pictures.length() > 1) {
            pictures = pictures.substring(0, pictures.length() - 1);
        }
        List<AddGoodsSpecBean> specList = mSpecAdapter.getList();
        for (int i = 0, size = specList.size(); i < size; i++) {
            AddGoodsSpecBean bean = specList.get(i);
            bean.setId(String.valueOf(i + 1));
            String uploadResultUrl = bean.getUploadResultUrl();
            if (!TextUtils.isEmpty(uploadResultUrl)) {
                bean.setThumb(uploadResultUrl);
            }
        }
        String specs = JSON.toJSONString(specList);
        L.e("specs-------> " + specs);
        MallHttpUtil.setGoods(
                mGoodsClassBean.getOneClassId(),
                mGoodsClassBean.getTwoClassId(),
                mGoodsClassBean.getId(),
                mTitleVal,
                mDetailVal,
                specs,
                mPostageVal,
                mTitleAdapter.getVideoUrl(),
                mTitleAdapter.getVideoImgUrl(),
                thumbs,
                pictures,
                mGoodsId,
                new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            finish();
                        }
                        ToastUtil.show(msg);
                    }

                    @Override
                    public void onFinish() {
                        hideLoading();
                    }
                }

        );

    }


    /**
     * 生成视频封面图
     */
    private File createVideoCoverImage(String videoPath) {
        MediaMetadataRetriever mmr = null;
        Bitmap bitmap = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            bitmap = mmr.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            bitmap = null;
            e.printStackTrace();
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }
        if (bitmap == null) {
            return null;
        }
        String coverImagePath = videoPath.replace(".mp4", ".jpg");
        File imageFile = new File(coverImagePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        return imageFile;

    }


    private void showLoading() {
        if (mLoading == null) {
            mLoading = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.video_pub_ing));
        }
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }


    private void hideLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.SET_GOODS);
        MallHttpUtil.cancel(MallHttpConsts.GET_GOODS_INFO);
        UploadUtil.cancelUpload();
        hideLoading();
        super.onDestroy();
    }
}
