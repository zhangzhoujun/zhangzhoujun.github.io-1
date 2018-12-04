package com.dim.brandsort;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by dim on 2018/11/23.
 */

public class ViewBrandViewItem extends LinearLayout {

    private Context mContext;

    private ImageView mBg;
    private ImageView mStatue;
    private TextView mName;

    private MDiyBrandItem mBrandItem;
    private ViewBrandType mType;

    private OnViewBrandViewItemListener mListener;

    public ViewBrandViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.view_brandview_item, this);

        mBg = findViewById(R.id.view_brandview_item_bg);
        mStatue = findViewById(R.id.view_brandview_item_status);
        mName = findViewById(R.id.view_brandview_item_name);
    }

    public void init(Context context, MDiyBrandItem brandItem, ViewBrandType thisType) {
        mContext = context;
        this.mBrandItem = brandItem;
        this.mType = thisType;

        initView();
    }

    public void setmListener(OnViewBrandViewItemListener mListener) {
        this.mListener = mListener;
    }

    public void initView() {
        mName.setText(mBrandItem.getBrandName());
        mStatue.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemStatusClick();
            }
        });

        switch (mType) {
            case ADD:
                mStatue.setBackground(mContext.getResources().getDrawable(R.mipmap.img_brand_add));
                mStatue.setVisibility(View.VISIBLE);
                mBg.setBackground(mContext.getResources().getDrawable(R.drawable.shape_brand_item_normal));
                mName.setTextColor(mContext.getResources().getColor(R.color.c_66666));
                break;
            case DELETE:
                mStatue.setBackground(mContext.getResources().getDrawable(R.mipmap.img_brand_delete));
                mBg.setBackground(mContext.getResources().getDrawable(R.drawable.shape_brand_item_ok));
                mName.setTextColor(mContext.getResources().getColor(R.color.c_A27234));
                mStatue.setVisibility(View.VISIBLE);
                break;
            case NORMAL:
                mStatue.setVisibility(View.INVISIBLE);
                mBg.setBackground(mContext.getResources().getDrawable(R.drawable.shape_brand_item_ok));
                mName.setTextColor(mContext.getResources().getColor(R.color.c_A27234));
                break;
        }
    }

    public interface OnViewBrandViewItemListener {
        public void onItemStatusClick();
    }
}
