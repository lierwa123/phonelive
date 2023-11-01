package com.yunbao.mall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.custom.StarCountView;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.PayContentVideoPlayAdapter;
import com.yunbao.mall.bean.PayContentVideoPlayBean;
import com.yunbao.mall.dialog.PayCommentDialogFragment;
import com.yunbao.mall.dialog.PayContentPayDialogFragment;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.List;

/**
 * 付费内容详情
 */
public class PayContentDetailActivity extends AbsActivity implements OnItemClickListener<PayContentVideoPlayBean>, PayContentPayDialogFragment.ActionListener {

    private String mGoodsId;
    private String mToUid;
    private ImageView mThumb;
    private TextView mTitle;
    private TextView mNum;
    private TextView mSaleNum;
    private TextView mDes;
    private StarCountView mStar;
    private TextView mCommentNum;
    private View mGroupMul;
    private View mGroupComment;
    private ImageView mAvatar;
    private TextView mUserName;
    private TextView mUserIntro;
    private ViewGroup mGroupBottom;
    private RecyclerView mRecyclerView;
    private PayContentVideoPlayAdapter mAdapter;
    private Double mMoneyVal;
    private String mGoodsNameVal;
    private List<PayContentVideoPlayBean> mVideoList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_content_detail;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_343));

        mGoodsId = getIntent().getStringExtra(Constants.MALL_GOODS_ID);
        mThumb = findViewById(R.id.thumb);
        mTitle = findViewById(R.id.title);
        mNum = findViewById(R.id.num);
        mSaleNum = findViewById(R.id.sale_num);
        mDes = findViewById(R.id.des);
        mStar = findViewById(R.id.star);
        mCommentNum = findViewById(R.id.comment_num);
        mGroupMul = findViewById(R.id.group_mul);
        mGroupComment = findViewById(R.id.group_comment);
        mAvatar = findViewById(R.id.avatar);
        mUserName = findViewById(R.id.name);
        mUserIntro = findViewById(R.id.user_intro);
        mGroupBottom = findViewById(R.id.group_bottom);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new PayContentVideoPlayAdapter(mContext);
        mAdapter.setOnItemClickListener(PayContentDetailActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        getData();
    }


    public void getData() {
        MallHttpUtil.getPayContentDetail(mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mToUid = obj.getString("uid");
                    mMoneyVal = obj.getDoubleValue("money");
                    if (mThumb != null) {
                        ImgLoader.display(mContext, obj.getString("thumb"), mThumb);
                    }
                    mGoodsNameVal = obj.getString("title");
                    if (mTitle != null) {
                        mTitle.setText(mGoodsNameVal);
                    }
                    if (mSaleNum != null) {
                        mSaleNum.setText(String.format(WordUtil.getString(R.string.mall_341), obj.getString("sale_nums")));
                    }
                    if (mDes != null) {
                        mDes.setText(obj.getString("content"));
                    }
                    if (mGroupComment != null) {
                        if (obj.getIntValue("can_comment") == 1) {
                            if (mGroupComment.getVisibility() != View.VISIBLE) {
                                mGroupComment.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (mGroupComment.getVisibility() != View.GONE) {
                                mGroupComment.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (mUserIntro != null) {
                        mUserIntro.setText(obj.getString("personal_desc"));
                    }
                    JSONObject userInfo = obj.getJSONObject("userinfo");
                    if (userInfo != null) {
                        if (mAvatar != null) {
                            ImgLoader.display(mContext, userInfo.getString("avatar"), mAvatar);
                        }
                        if (mUserName != null) {
                            mUserName.setText(userInfo.getString("user_nicename"));
                        }
                    }
                    if (mStar != null) {
                        mStar.setFillCount(obj.getIntValue("evaluate_point"));
                    }
                    if (mCommentNum != null) {
                        mCommentNum.setText(String.format(WordUtil.getString(R.string.mall_351), obj.getString("evaluate_nums")));
                    }
                    boolean hasBuy = obj.getIntValue("is_buy") == 1;
                    List<PayContentVideoPlayBean> videoList = JSON.parseArray(obj.getString("videos"), PayContentVideoPlayBean.class);
                    mVideoList = videoList;
                    if (videoList != null && videoList.size() > 0) {
                        if (videoList.size() > 1) {
                            if (mNum != null) {
                                mNum.setText(obj.getString("video_num"));
                            }
                            if (mGroupMul != null && mGroupMul.getVisibility() != View.VISIBLE) {
                                mGroupMul.setVisibility(View.VISIBLE);
                            }
                            if (mAdapter != null) {
                                mAdapter.setHasBuy(hasBuy);
                                mAdapter.refreshData(videoList);
                            }
                            if (mGroupBottom != null) {
                                if (hasBuy) {
                                    if (mGroupBottom.getVisibility() != View.GONE) {
                                        mGroupBottom.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (mGroupBottom.getChildCount() > 0) {
                                        mGroupBottom.removeAllViews();
                                    }
                                    View v = LayoutInflater.from(mContext).inflate(R.layout.view_content_detail_buy, mGroupBottom, false);
                                    TextView price = v.findViewById(R.id.price);
                                    price.setText(String.valueOf(mMoneyVal));
                                    mGroupBottom.addView(v);
                                }
                            }
                        } else {
                            if (mNum != null) {
                                mNum.setText(String.format(WordUtil.getString(R.string.mall_349), videoList.get(0).getDurationString()));
                            }
                            if (mGroupMul != null && mGroupMul.getVisibility() != View.GONE) {
                                mGroupMul.setVisibility(View.GONE);
                            }
                            if (mGroupBottom != null) {
                                if (mGroupBottom.getChildCount() > 0) {
                                    mGroupBottom.removeAllViews();
                                }
                                if (hasBuy) {
                                    LayoutInflater.from(mContext).inflate(R.layout.view_content_detail_play, mGroupBottom, true);
                                } else {
                                    View v = LayoutInflater.from(mContext).inflate(R.layout.view_content_detail_buy, mGroupBottom, false);
                                    TextView price = v.findViewById(R.id.price);
                                    price.setText(String.valueOf(mMoneyVal));
                                    mGroupBottom.addView(v);
                                }
                            }

                        }
                    }

                }
            }
        });
    }


    public void payContentClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_user_home) {
            RouteUtil.forwardUserHome(mContext, mToUid);
        } else if (id == R.id.group_comment) {
            openCommentDialog();
        } else if (id == R.id.btn_buy) {
            showPayDialog();
        } else if (id == R.id.btn_play) {
            play();
        }
    }

    private void openCommentDialog() {
        PayCommentDialogFragment fragment = new PayCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MALL_GOODS_ID, mGoodsId);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "PayCommentDialogFragment");
    }


    /**
     * 打开支付弹窗
     */
    private void showPayDialog() {
        PayContentPayDialogFragment fragment = new PayContentPayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MALL_GOODS_ID, mGoodsId);
        bundle.putDouble(Constants.MALL_ORDER_MONEY, mMoneyVal);
        bundle.putString(Constants.MALL_GOODS_NAME, mGoodsNameVal);
        fragment.setArguments(bundle);
        fragment.setActionListener(this);
        fragment.show(getSupportFragmentManager(), "PayContentPayDialogFragment");
    }

    /**
     * 播放
     */
    private void play() {
        if (mVideoList != null && mVideoList.size() > 0) {
            RouteUtil.forwardVideoPlay(mContext, mVideoList.get(0).getUploadResultUrl(), null);
        }
    }


    @Override
    public void onItemClick(PayContentVideoPlayBean bean, int position) {
        RouteUtil.forwardVideoPlay(mContext, bean.getUploadResultUrl(), null);
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_PAY_CONTENT_DETAIL);
        super.onDestroy();
    }

    @Override
    public void onPaySuccess() {
        getData();
    }
}
