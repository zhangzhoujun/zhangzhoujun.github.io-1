package com.qm.module_juggle.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.dim.library.utils.DLog;
import com.kunminx.linkage.adapter.LinkagePrimaryAdapter;
import com.kunminx.linkage.adapter.LinkageSecondaryAdapter;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.kunminx.linkage.defaults.DefaultLinkagePrimaryAdapterConfig;
import com.kunminx.linkage.defaults.DefaultLinkageSecondaryAdapterConfig;
import com.kunminx.linkage.manager.RecyclerViewScrollHelper;
import com.qm.module_juggle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName QYLinkageRecyclerView
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 12/18/20 11:25 AM
 * @Version 1.0
 */
public class QYLinkageRecyclerView<T extends BaseGroupedItem.ItemInfo> extends RelativeLayout {

    private static final int DEFAULT_SPAN_COUNT = 1;
    private static final int SCROLL_OFFSET = 0;

    private Context mContext;

    private QYLDRecyclerView mRvPrimary;
    private QYLDRecyclerView mRvSecondary;
    private ConstraintLayout mLinkageLayout;

    private LinkagePrimaryAdapter mPrimaryAdapter;
    private LinkageSecondaryAdapter mSecondaryAdapter;
    private TextView mTvHeader;
    private FrameLayout mHeaderContainer;

    private List<String> mInitGroupNames;
    private List<BaseGroupedItem<T>> mInitItems;

    private List<Integer> mHeaderPositions = new ArrayList<>();
    private int mTitleHeight;
    private int mFirstVisiblePosition;
    private String mLastGroupName;
    private QYGridLayoutManager mSecondaryLayoutManager;
    private LinearLayoutManager mPrimaryLayoutManager;
    private View view;
    private boolean mScrollSmoothly = true;
    private boolean mPrimaryClicked = false;

    private boolean groupScrollToBottom = false;

    public void setGroupScrollToBottom(boolean bottom) {
        groupScrollToBottom = bottom;
        mSecondaryLayoutManager.setScrollBottom(groupScrollToBottom);
        mRvSecondary.setGroupScrollBottom(groupScrollToBottom);
    }

    public QYLinkageRecyclerView(Context context) {
        super(context);
    }

    public QYLinkageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public QYLinkageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.home_linkage_view_ex, this);
        mRvPrimary = (QYLDRecyclerView) view.findViewById(R.id.rv_primary);
        mRvSecondary = (QYLDRecyclerView) view.findViewById(R.id.rv_secondary);
        mHeaderContainer = (FrameLayout) view.findViewById(R.id.header_container);
        mLinkageLayout = (ConstraintLayout) view.findViewById(R.id.linkage_layout);

    }

    private void setLevel2LayoutManager() {
//        if (mSecondaryAdapter.isGridMode()) {
        mSecondaryLayoutManager = new QYGridLayoutManager(mContext,
                mSecondaryAdapter.getConfig().getSpanCountOfGridMode(), groupScrollToBottom);
        ((QYGridLayoutManager) mSecondaryLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (((BaseGroupedItem<T>) mSecondaryAdapter.getItems().get(position)).isHeader) {
                    return mSecondaryAdapter.getConfig().getSpanCountOfGridMode();
                }
                if ((TextUtils.isEmpty(((BaseGroupedItem<T>) mSecondaryAdapter.getItems().get(position)).info.getTitle()) &&
                        !TextUtils.isEmpty(((BaseGroupedItem<T>) mSecondaryAdapter.getItems().get(position)).info.getGroup()))) {
                    return mSecondaryAdapter.getConfig().getSpanCountOfGridMode();
                }
                return DEFAULT_SPAN_COUNT;
            }
        });
//        } else {
//            mSecondaryLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//        }
        mRvSecondary.setLayoutManager(mSecondaryLayoutManager);
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    private void initRecyclerView(ILinkagePrimaryAdapterConfig primaryAdapterConfig,
                                  ILinkageSecondaryAdapterConfig secondaryAdapterConfig) {
        // setScrollSmoothly(false);

        mPrimaryAdapter = new LinkagePrimaryAdapter(mInitGroupNames, primaryAdapterConfig,
                new LinkagePrimaryAdapter.OnLinkageListener() {
                    @Override
                    public void onLinkageClick(LinkagePrimaryViewHolder holder, String title) {
                        if (isScrollSmoothly()) {
                            QYRecyclerViewScrollHelper.smoothScrollToPosition(mRvSecondary,
                                    LinearSmoothScroller.SNAP_TO_START,
                                    mHeaderPositions.get(holder.getBindingAdapterPosition()));
                        } else {
                            mSecondaryLayoutManager.scrollToPositionWithOffset(
                                    mHeaderPositions.get(holder.getBindingAdapterPosition()), SCROLL_OFFSET);
                        }
                        mPrimaryAdapter.setSelectedPosition(holder.getBindingAdapterPosition());
                        mPrimaryClicked = true;
                    }
                });

        mPrimaryLayoutManager = new LinearLayoutManager(mContext);
        mRvPrimary.setLayoutManager(mPrimaryLayoutManager);
        mRvPrimary.setAdapter(mPrimaryAdapter);

        mSecondaryAdapter = new LinkageSecondaryAdapter(mInitItems, secondaryAdapterConfig);
        setLevel2LayoutManager();
        mRvSecondary.setAdapter(mSecondaryAdapter);
    }

    private void initLinkageSecondary() {

        // Note: headerLayout is shared by both SecondaryAdapter's header and HeaderView

        if (mTvHeader == null && mSecondaryAdapter.getConfig() != null) {
            ILinkageSecondaryAdapterConfig config = mSecondaryAdapter.getConfig();
            int layout = config.getHeaderLayoutId();
            View view = LayoutInflater.from(mContext).inflate(layout, null);
            mHeaderContainer.addView(view);
            mTvHeader = view.findViewById(config.getHeaderTextViewId());
        }

        if (mInitItems.get(mFirstVisiblePosition).isHeader) {
            mTvHeader.setText(mInitItems.get(mFirstVisiblePosition).header);
        }

        mRvSecondary.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mTitleHeight = mTvHeader.getMeasuredHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstPosition = mSecondaryLayoutManager.findFirstVisibleItemPosition();
                int firstCompletePosition = mSecondaryLayoutManager.findFirstCompletelyVisibleItemPosition();
                List<BaseGroupedItem<T>> items = mSecondaryAdapter.getItems();

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    // Log.i(TAG, "滑动到顶部");
                    DLog.d("SSSSSSSS", "child 滑动到顶部");
                    mRvSecondary.setScrollToTop(true);
                } else {
                    mRvSecondary.setScrollToTop(false);
                }
                // Here is the logic of the sticky:

                if (firstCompletePosition > 0 && (firstCompletePosition) < items.size()
                        && items.get(firstCompletePosition).isHeader) {

                    View view = mSecondaryLayoutManager.findViewByPosition(firstCompletePosition);
                    if (view != null && view.getTop() <= mTitleHeight) {
                        mTvHeader.setY(view.getTop() - mTitleHeight);
                    }
                }

                // Here is the logic of group title changes and linkage:

                boolean groupNameChanged = false;

                if (mFirstVisiblePosition != firstPosition && firstPosition >= 0) {
                    mFirstVisiblePosition = firstPosition;
                    mTvHeader.setY(0);

                    String currentGroupName = items.get(mFirstVisiblePosition).isHeader
                            ? items.get(mFirstVisiblePosition).header
                            : items.get(mFirstVisiblePosition).info.getGroup();

                    if (TextUtils.isEmpty(mLastGroupName) || !mLastGroupName.equals(currentGroupName)) {
                        mLastGroupName = currentGroupName;
                        groupNameChanged = true;
                        mTvHeader.setText(mLastGroupName);
                    }
                }

                // the following logic can not be perfect, because tvHeader's title may not
                // always equals to the title of selected primaryItem, while there
                // are several groups which has little items to stick group item to tvHeader.
                //
                // To avoid to this extreme situation, my idea is to add a footer on the bottom,
                // to help wholly execute this logic.
                //
                // Note: 2019.5.22 KunMinX

                if (groupNameChanged) {
                    List<String> groupNames = mPrimaryAdapter.getStrings();
                    for (int i = 0; i < groupNames.size(); i++) {
                        if (groupNames.get(i).equals(mLastGroupName)) {
                            if (mPrimaryClicked) {
                                if (mPrimaryAdapter.getSelectedPosition() == i) {
                                    mPrimaryClicked = false;
                                }
                            } else {
                                mPrimaryAdapter.setSelectedPosition(i);
                                RecyclerViewScrollHelper.smoothScrollToPosition(mRvPrimary,
                                        LinearSmoothScroller.SNAP_TO_END, i);
                            }
                        }
                    }
                }
            }
        });
    }

    private int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

    /**
     * init LinkageRV by items and configs
     *
     * @param linkageItems
     * @param primaryAdapterConfig
     * @param secondaryAdapterConfig
     */
    public void init(List<BaseGroupedItem<T>> linkageItems,
                     ILinkagePrimaryAdapterConfig primaryAdapterConfig,
                     ILinkageSecondaryAdapterConfig secondaryAdapterConfig) {

        initRecyclerView(primaryAdapterConfig, secondaryAdapterConfig);

        this.mInitItems = linkageItems;

        String lastGroupName = null;
        List<String> groupNames = new ArrayList<>();
        if (mInitItems != null && mInitItems.size() > 0) {
            for (BaseGroupedItem<T> item1 : mInitItems) {
                if (item1.isHeader) {
                    groupNames.add(item1.header);
                    lastGroupName = item1.header;
                }
            }
        }

        if (mInitItems != null) {
            for (int i = 0; i < mInitItems.size(); i++) {
                if (mInitItems.get(i).isHeader) {
                    mHeaderPositions.add(i);
                }
            }
        }

//        DefaultGroupedItem.ItemInfo info = new DefaultGroupedItem.ItemInfo(null, lastGroupName);
//        BaseGroupedItem<T> footerItem = (BaseGroupedItem<T>) new DefaultGroupedItem(info);
//        mInitItems.add(footerItem);

        this.mInitGroupNames = groupNames;
        mPrimaryAdapter.initData(mInitGroupNames);
        mSecondaryAdapter.initData(mInitItems);
        initLinkageSecondary();
    }

    /**
     * simplify init by only items and default configs
     *
     * @param linkageItems
     */
    public void init(List<BaseGroupedItem<T>> linkageItems) {
        init(linkageItems, new DefaultLinkagePrimaryAdapterConfig(), new DefaultLinkageSecondaryAdapterConfig());
    }

    /**
     * bind listeners for primary or secondary adapter
     *
     * @param primaryItemClickListner
     * @param primaryItemBindListener
     * @param secondaryItemBindListener
     * @param headerBindListener
     * @param footerBindListener
     */
    public void setDefaultOnItemBindListener(
            DefaultLinkagePrimaryAdapterConfig.OnPrimaryItemClickListner primaryItemClickListner,
            DefaultLinkagePrimaryAdapterConfig.OnPrimaryItemBindListener primaryItemBindListener,
            DefaultLinkageSecondaryAdapterConfig.OnSecondaryItemBindListener secondaryItemBindListener,
            DefaultLinkageSecondaryAdapterConfig.OnSecondaryHeaderBindListener headerBindListener,
            DefaultLinkageSecondaryAdapterConfig.OnSecondaryFooterBindListener footerBindListener) {

        if (mPrimaryAdapter.getConfig() != null) {
            ((DefaultLinkagePrimaryAdapterConfig) mPrimaryAdapter.getConfig())
                    .setListener(primaryItemBindListener, primaryItemClickListner);
        }
        if (mSecondaryAdapter.getConfig() != null) {
            ((DefaultLinkageSecondaryAdapterConfig) mSecondaryAdapter.getConfig())
                    .setItemBindListener(secondaryItemBindListener, headerBindListener, footerBindListener);
        }
    }

    /**
     * custom linkageRV width in some scene like dialog
     *
     * @param dp
     */
    public void setLayoutHeight(float dp) {
        ViewGroup.LayoutParams lp = mLinkageLayout.getLayoutParams();
        lp.height = dpToPx(getContext(), dp);
        mLinkageLayout.setLayoutParams(lp);
    }

    /**
     * custom primary list width.
     * <p>
     * The reason for this design is that：The width of the first-level list must be an accurate value,
     * otherwise the onBindViewHolder may be called multiple times due to the RecyclerView's own bug.
     *
     * @param dp
     */
    public void setPrimaryWidget(float dp) {
        ViewGroup.LayoutParams lpLeft = mRvPrimary.getLayoutParams();
        lpLeft.width = dpToPx(getContext(), dp);
        mRvPrimary.setLayoutParams(lpLeft);

        ViewGroup.LayoutParams lpRight = mRvSecondary.getLayoutParams();
        lpRight.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mRvSecondary.setLayoutParams(lpRight);
    }

    public boolean isGridMode() {
        return mSecondaryAdapter.isGridMode();
    }

    /**
     * custom if secondary list is hope to be grid mode
     *
     * @return
     */
    public void setGridMode(boolean isGridMode) {
        mSecondaryAdapter.setGridMode(isGridMode);
        setLevel2LayoutManager();
        mRvSecondary.requestLayout();
    }

    public boolean isScrollSmoothly() {
        return mScrollSmoothly;
    }

    /**
     * custom if is hope to scroll smoothly while click primary item to linkage secondary list
     *
     * @return
     */
    public void setScrollSmoothly(boolean scrollSmoothly) {
        this.mScrollSmoothly = scrollSmoothly;
    }

    public LinkagePrimaryAdapter getPrimaryAdapter() {
        return mPrimaryAdapter;
    }

    public LinkageSecondaryAdapter getSecondaryAdapter() {
        return mSecondaryAdapter;
    }

    public List<Integer> getHeaderPositions() {
        return mHeaderPositions;
    }

    /**
     * set percent of primary list and secondary list width
     *
     * @param percent
     */
    public void setPercent(float percent) {
        Guideline guideline = (Guideline) view.findViewById(R.id.guideline);
        guideline.setGuidelinePercent(percent);
    }

    public void setRvPrimaryBackground(int resId) {
        mRvPrimary.setBackgroundResource(resId);
    }

    public void setRvSecondaryBackground(int resId) {
        mRvSecondary.setBackgroundResource(resId);
    }
}
