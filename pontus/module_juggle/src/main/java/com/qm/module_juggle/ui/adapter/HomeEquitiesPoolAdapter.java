package com.qm.module_juggle.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.dim.library.utils.DLog;
import com.qm.lib.base.LocalUserManager;
import com.qm.lib.router.RouterManager;
import com.qm.lib.utils.JYMMKVManager;
import com.qm.lib.utils.SLSLogUtils;
import com.qm.lib.utils.StringUtils;
import com.qm.lib.widget.dim.AppTextView;
import com.qm.module_juggle.R;
import com.qm.module_juggle.databinding.HomeEquitiespollItemBinding;
import com.qm.module_juggle.entity.MHomeDataBean;
import com.qm.module_juggle.utils.JugglePathUtils;
import com.qm.module_juggle.widget.pool.EquitiesPollItem;
import com.qm.module_juggle.widget.pool.EquitiesPoolView;
import com.qm.module_juggle.widget.pool.HomeEquitiesPoolBean;

/**
 * @author 作者 luohl
 * @time 创建时间：2019/8/30
 * @explain 类说明:
 */
public class HomeEquitiesPoolAdapter
        extends DelegateAdapter.Adapter<HomeEquitiesPoolAdapter.HomeEquitiesPoolViewHolder> {

    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private HomeEquitiesPoolBean bean;
    private MHomeDataBean.MHomeDataItem item;
    private AppTextView showNtText;
    private AppTextView watchNum;
    private RelativeLayout watchLayout;

    private BallClickListener mBallClickListener;

    private int maxBallCount;

    public HomeEquitiesPoolAdapter(Context context, LayoutHelper layoutHelper, HomeEquitiesPoolBean bean,
                                   MHomeDataBean.MHomeDataItem item, int maxBallCount, BallClickListener ballClickListener) {
        mContext = context;
        mLayoutHelper = layoutHelper;
        this.bean = bean;
        this.item = item;
        this.maxBallCount = maxBallCount;
        this.mBallClickListener = ballClickListener;
    }

    public void setBean(HomeEquitiesPoolBean bean) {
        this.bean = bean;
        notifyDataSetChanged();


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    public void setBallClickListener(BallClickListener ballClickListener) {
        mBallClickListener = ballClickListener;
    }

    @NonNull
    @Override
    public HomeEquitiesPoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_equitiespoll_item, parent, false);
        return new HomeEquitiesPoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeEquitiesPoolViewHolder holder, int position) {
        HomeEquitiespollItemBinding bind = DataBindingUtil.bind(holder.itemView);
        bind.poolBg.setImageUrl(item.getRetouch());
        if (item.getData() != null && item.getData().size() > 0 && item.getData().get(0) != null && item.getData().get(0).getSeldHide() == false &&
                item.getData().get(0).getImg_url() != null && !StringUtils.Companion.getInstance().isEmpty(item.getData().get(0).getImg_url())) {
            bind.qiandao.setImageUrl(item.getData().get(0).getImg_url());
            bind.qiandao.setVisibility(View.VISIBLE);
        } else {
            bind.qiandao.setVisibility(View.GONE);
        }
        showNtText = bind.bt;
        watchNum = bind.watchNum;
        watchLayout = bind.watchBg;

        if (LocalUserManager.Companion.getInstance().getUser() != null) {
            //bind.bt.setText("BT:" + LocalUserManager.Companion.getInstance().getUser().getTotalAmount());
            //bind.addressTv.setText("ID:" + LocalUserManager.Companion.getInstance().getUser().getPublicKey());
        }

        if (bean != null && bean.getNt_list() != null && bean.getNt_list().size() > 0) {
            bind.poolView.setData(bean.getNt_list(), maxBallCount);
            bind.poolView.setOnStopAnimateListener(new EquitiesPoolView.OnStopAnimateListener() {
                @Override
                public void onBallDisappearAnimListener(String number) {
                    DLog.d("onBallDisappearAnimListener -> " + number);
                }

                @Override
                public void onExitAnimateListener() {
                    DLog.d("onExitAnimateListener");
                }

                @Override
                public void onBallClick(View view, EquitiesPollItem bean, int position) {
                    if (mBallClickListener != null) {
                        mBallClickListener.onBallClick(bind.poolView, view, bean, position);
                    }
                }
            });
        }
        bind.qiandao.setOnClickListener(e -> {
            JugglePathUtils.Companion.getInstance().onJuggleItemClick(item.getData().get(0), "HOME_BALL_TOP", -1, "");
        });

        bind.address.setOnClickListener(e -> {
            SLSLogUtils.Companion.getInstance().sendLogClick("JuggleFragment", item.getData().get(0).getPageKey(), "ADDRESS", -1, "", "", "");
            RouterManager.Companion.getInstance().gotoBTRecordActivity();
        });

        String num = JYMMKVManager.Companion.getInstance().getWatchNum();
        if (TextUtils.isEmpty(num)) {
            watchLayout.setVisibility(View.GONE);
        } else {
            watchNum.setText(num);
            watchLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class HomeEquitiesPoolViewHolder extends RecyclerView.ViewHolder {


        public HomeEquitiesPoolViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface BallClickListener {
        public void onBallClick(EquitiesPoolView mPoolView, View view, EquitiesPollItem bean, int position);
    }

    public void setUsetBtChanged() {
//        notifyItemChanged(0, R.id.bt);
        if (showNtText != null && LocalUserManager.Companion.getInstance().getUser() != null) {
            //showNtText.setText("BT:" + LocalUserManager.Companion.getInstance().getUser().getTotalAmount());
        }
    }
}
