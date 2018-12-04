package com.dim.brandsort;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 拖拽排序 + 增删
 * Created by dim on 2018/11/23.
 */
public class ViewTuijianBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    // 我的品牌 标题部分
    public static final int TYPE_MY_BRAND_HEADER = 0;
    // 我的品牌
    public static final int TYPE_MY = 1;
    // 其他品牌 标题部分
    public static final int TYPE_OTHER_BRAND_HEADER = 2;
    // 其他品牌
    public static final int TYPE_OTHER = 3;

    // 我的品牌之前的header数量  即标题部分 为 1
    private static final int COUNT_PRE_MY_HEADER = 1;
    // 其他品牌之前的header数量  即标题部分 为 COUNT_PRE_MY_HEADER + 1
    private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;

    private static final long ANIM_TIME = 360L;

    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;

    private Context mContext;

    private LayoutInflater mInflater;
    private ItemTouchHelper mItemTouchHelper;

    private List<MDiyBrandItem> mMyBrandItems, mOtherBrandItems;

    // 我的品牌点击事件
    private OnMyBrandItemClickListener mBrandItemClickListener;

    public ViewTuijianBrandAdapter(Context context, ItemTouchHelper helper, List<MDiyBrandItem> mMyBrandItems, List<MDiyBrandItem> mOtherBrandItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mItemTouchHelper = helper;
        this.mMyBrandItems = mMyBrandItems;
        this.mOtherBrandItems = mOtherBrandItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {    // 我的品牌 标题部分
            return TYPE_MY_BRAND_HEADER;
        } else if (position == mMyBrandItems.size() + 1) {    // 其他品牌 标题部分
            return TYPE_OTHER_BRAND_HEADER;
        } else if (position > 0 && position < mMyBrandItems.size() + 1) {
            return TYPE_MY;
        } else {
            return TYPE_OTHER;
        }
    }

    /**
     * 排序。sort大的排在前面
     *
     * @param currentPos
     * @return
     */
    private int getPosFromMyToOther(int currentPos) {
        int currentSortId = mMyBrandItems.get(currentPos).getSort();
        for (int i = 0; i < mOtherBrandItems.size(); i++) {
            if (currentSortId > mOtherBrandItems.get(i).getSort()) {
                return i;
            }
        }
        return mOtherBrandItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY_BRAND_HEADER:
                view = mInflater.inflate(R.layout.view_diy_pinpai_head, parent, false);
                MyBrandHeaderViewHolder holder = new MyBrandHeaderViewHolder(view);
                return holder;
            case TYPE_MY:
                view = mInflater.inflate(R.layout.view_tuijian_brand_adapter_item, parent, false);
                final MyViewHolder myHolder = new MyViewHolder(view);

                myHolder.bandView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        int currentPiosition = myHolder.getAdapterPosition();
                        RecyclerView recyclerView = ((RecyclerView) parent);
                        int targetViewPos = getPosFromMyToOther(currentPiosition - 1);
                        View targetView = recyclerView.getLayoutManager().findViewByPosition(mMyBrandItems.size() + targetViewPos + COUNT_PRE_OTHER_HEADER);
                        View currentView = recyclerView.getLayoutManager().findViewByPosition(currentPiosition);

                        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if (recyclerView.indexOfChild(targetView) >= 0) {
                            int targetX, targetY;

                            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                            GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
                            int spanCount = gridLayoutManager.getSpanCount();
                            targetX = targetView.getLeft();
                            targetY = targetView.getTop();

                            // 如果当前是myBrand的最后一个，移除后没有了已选的
                            if (mMyBrandItems.size() == 1) {
                                targetY -= targetView.getHeight();
                            }

                            // 如果当前位置是myBrand可见的最后一个
                            // 并且 当前位置不在grid的第一个位置
                            // 并且 目标位置不在grid的第一个位置

                            // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                            // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                            if (currentPiosition == gridLayoutManager.findLastVisibleItemPosition()
                                    && (currentPiosition - COUNT_PRE_MY_HEADER) % spanCount != 0
                                    && (targetViewPos - mMyBrandItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0) {
                                moveMyToOtherDelay(myHolder, targetViewPos);
                            } else {
                                moveMyToOther(myHolder, targetViewPos);
                            }

                            startAnimation(recyclerView, currentView, targetX, targetY);
                        } else {
                            // 需要移动到其他品牌的最后一个
                            if (targetViewPos == mOtherBrandItems.size() && mOtherBrandItems.size() > 0) {
                                int targetX, targetY;
                                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                                GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
                                int spanCount = gridLayoutManager.getSpanCount();
                                View preTargetView;
                                // 移动后 新的一个item在新的一行第一个
                                if (mOtherBrandItems.size() % spanCount == 0) {
                                    preTargetView = recyclerView.getLayoutManager().findViewByPosition(mMyBrandItems.size() + targetViewPos + COUNT_PRE_OTHER_HEADER - spanCount);
                                    targetX = preTargetView.getLeft();
                                    targetY = preTargetView.getTop() + preTargetView.getHeight();
                                } else {
                                    preTargetView = recyclerView.getLayoutManager().findViewByPosition(mMyBrandItems.size() + targetViewPos + COUNT_PRE_OTHER_HEADER - 1);
                                    targetX = preTargetView.getLeft() + preTargetView.getWidth();
                                    targetY = preTargetView.getTop();
                                }
                                // 如果当前是myBrand的最后一个，移除后没有了已选的
                                if (mMyBrandItems.size() == 1) {
                                    targetY -= preTargetView.getHeight();
                                }

                                // 如果当前位置是myBrand可见的最后一个
                                // 并且 当前位置不在grid的第一个位置
                                // 并且 目标位置不在grid的第一个位置

                                // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                                // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                                if (currentPiosition == gridLayoutManager.findLastVisibleItemPosition()
                                        && (currentPiosition - COUNT_PRE_MY_HEADER) % spanCount != 0
                                        && (targetViewPos - mMyBrandItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0) {
                                    moveMyToOtherDelay(myHolder, targetViewPos);
                                } else {
                                    moveMyToOther(myHolder, targetViewPos);
                                }

                                startAnimation(recyclerView, currentView, targetX, targetY);
                            } else {
                                moveMyToOther(myHolder, targetViewPos);
                                if (mBrandItemClickListener != null) {
                                    mBrandItemClickListener.onItemClick(v, currentPiosition - COUNT_PRE_MY_HEADER);
                                }
                            }
                        }
                    }
                });

                myHolder.bandView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        mItemTouchHelper.startDrag(myHolder);
                        return true;
                    }
                });

                myHolder.bandView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (MotionEventCompat.getActionMasked(event)) {
                            case MotionEvent.ACTION_DOWN:
                                startTime = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                    mItemTouchHelper.startDrag(myHolder);
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                startTime = 0;
                                break;
                        }
                        return false;
                    }
                });
                return myHolder;

            case TYPE_OTHER_BRAND_HEADER:
                view = mInflater.inflate(R.layout.view_diy_pinpai_head, parent, false);
                OtherBrandHeaderViewHolder otherholder = new OtherBrandHeaderViewHolder(view);
                return otherholder;

            case TYPE_OTHER:
                view = mInflater.inflate(R.layout.view_tuijian_brand_adapter_item, parent, false);
                final OtherViewHolder otherHolder = new OtherViewHolder(view);
                otherHolder.bandView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mMyBrandItems.size() >= 3) {
                            Toast.makeText(mContext, "已满3个推荐品牌，请取消任意一个品牌再进行切换", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        RecyclerView recyclerView = ((RecyclerView) parent);
                        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                        int currentPiosition = otherHolder.getAdapterPosition();
                        // 如果RecyclerView滑动到底部,移动的目标位置的y轴 - height
                        View currentView = manager.findViewByPosition(currentPiosition);
                        // 目标位置的前一个item  即当前MyBrand的最后一个
                        View preTargetView = manager.findViewByPosition(mMyBrandItems.size() - 1 + COUNT_PRE_MY_HEADER);

                        // 如果targetView不在屏幕内,则为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if (recyclerView.indexOfChild(preTargetView) >= 0) {
                            int targetX = preTargetView.getLeft();
                            int targetY = preTargetView.getTop();

                            int targetPosition = mMyBrandItems.size() - 1 + COUNT_PRE_OTHER_HEADER;

                            GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
                            int spanCount = gridLayoutManager.getSpanCount();
                            // target 在最后一行第一个
                            if ((targetPosition - COUNT_PRE_MY_HEADER) % spanCount == 0) {
                                View targetView = manager.findViewByPosition(targetPosition);
                                targetX = targetView.getLeft();
                                targetY = targetView.getTop();
                            } else {
                                targetX += preTargetView.getWidth();

                                // 最后一个item可见
                                if (gridLayoutManager.findLastVisibleItemPosition() == getItemCount() - 1) {
                                    // 最后的item在最后一行第一个位置
                                    if ((getItemCount() - 1 - mMyBrandItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
                                        // RecyclerView实际高度 > 屏幕高度 && RecyclerView实际高度 < 屏幕高度 + item.height
                                        int firstVisiblePostion = gridLayoutManager.findFirstVisibleItemPosition();
                                        if (firstVisiblePostion == 0) {
                                            // FirstCompletelyVisibleItemPosition == 0 即 内容不满一屏幕 , targetY值不需要变化
                                            // // FirstCompletelyVisibleItemPosition != 0 即 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                                int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
                                                targetY += offset;
                                            }
                                        } else { // 在这种情况下 并且 RecyclerView高度变化时(即可见第一个item的 position != 0),
                                            // 移动后, targetY值  + 一个item的高度
                                            targetY += preTargetView.getHeight();
                                        }
                                    }
                                } else {
                                    System.out.println("current--No");
                                }
                            }

                            // 如果当前位置是otherBrand可见的最后一个
                            // 并且 当前位置不在grid的第一个位置
                            // 并且 目标位置不在grid的第一个位置

                            // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                            // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                            if (currentPiosition == gridLayoutManager.findLastVisibleItemPosition()
                                    && (currentPiosition - mMyBrandItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0
                                    && (targetPosition - COUNT_PRE_MY_HEADER) % spanCount != 0) {
//                                moveOtherToMyWithDelay(otherHolder);
                                moveOtherToMy(otherHolder);
                            } else {
                                moveOtherToMy(otherHolder);
                            }
                            startAnimation(recyclerView, currentView, targetX, targetY);
                        } else {
                            moveOtherToMy(otherHolder);
                        }
                    }
                });
                return otherHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.bandView.init(mContext, mMyBrandItems.get(position - COUNT_PRE_MY_HEADER), ViewBrandType.DELETE);
        } else if (holder instanceof OtherViewHolder) {
            ((OtherViewHolder) holder).bandView.init(mContext, mOtherBrandItems.get(position - mMyBrandItems.size() - COUNT_PRE_OTHER_HEADER), ViewBrandType.ADD);
        } else if (holder instanceof MyBrandHeaderViewHolder) {
            MyBrandHeaderViewHolder headerHolder = (MyBrandHeaderViewHolder) holder;
            headerHolder.name.setText("已推荐到封面");
        } else if (holder instanceof OtherBrandHeaderViewHolder) {
            OtherBrandHeaderViewHolder oheaderHolder = (OtherBrandHeaderViewHolder) holder;
            oheaderHolder.name.setText("其他品牌（" + mOtherBrandItems.size() + "）");
        }
    }

    @Override
    public int getItemCount() {
        // 我的品牌  标题 + 我的品牌.size + 其他品牌 标题 + 其他品牌.size
        return mMyBrandItems.size() + mOtherBrandItems.size() + COUNT_PRE_OTHER_HEADER;
    }

    /**
     * 开始增删动画
     */
    private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 我的品牌 移动到 其他品牌
     *
     * @param myHolder
     */
    private void moveMyToOther(MyViewHolder myHolder, int toPos) {
        int position = myHolder.getAdapterPosition();

        int startPosition = position - COUNT_PRE_MY_HEADER;
        if (startPosition > mMyBrandItems.size() - 1) {
            return;
        }
        MDiyBrandItem item = mMyBrandItems.get(startPosition);
        mMyBrandItems.remove(startPosition);
        mOtherBrandItems.add(toPos, item);

        notifyItemMoved(position, mMyBrandItems.size() + toPos + COUNT_PRE_OTHER_HEADER);
        notifyItemChanged(mMyBrandItems.size() + COUNT_PRE_MY_HEADER);
    }

    /**
     * 我的品牌 移动到 其他品牌 伴随延迟
     *
     * @param myHolder
     */
    private void moveMyToOtherDelay(MyViewHolder myHolder, int toPos) {
        int position = myHolder.getAdapterPosition();

        int startPosition = position - COUNT_PRE_MY_HEADER;
        if (startPosition > mMyBrandItems.size() - 1) {
            return;
        }
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveMyToOther(myHolder, toPos);
            }
        }, ANIM_TIME);
    }

    /**
     * 其他品牌 移动到 我的品牌
     *
     * @param otherHolder
     */
    private void moveOtherToMy(OtherViewHolder otherHolder) {
        int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        notifyItemMoved(position, mMyBrandItems.size() - 1 + COUNT_PRE_MY_HEADER);
        notifyItemChanged(mMyBrandItems.size() + COUNT_PRE_MY_HEADER);
    }

    /**
     * 其他品牌 移动到 我的品牌 伴随延迟
     *
     * @param otherHolder
     */
    private void moveOtherToMyWithDelay(OtherViewHolder otherHolder) {
        final int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemMoved(position, mMyBrandItems.size() - 1 + COUNT_PRE_MY_HEADER);
            }
        }, ANIM_TIME);
    }

    private Handler delayHandler = new Handler();

    private int processItemRemoveAdd(OtherViewHolder otherHolder) {
        int position = otherHolder.getAdapterPosition();

        int startPosition = position - mMyBrandItems.size() - COUNT_PRE_OTHER_HEADER;
        if (startPosition > mOtherBrandItems.size() - 1) {
            return -1;
        }
        MDiyBrandItem item = mOtherBrandItems.get(startPosition);
        mOtherBrandItems.remove(startPosition);
        mMyBrandItems.add(item);
        return position;
    }


    /**
     * 添加需要移动的 镜像View
     */
    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        recyclerView.getLocationOnScreen(parenLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);

        return mirrorView;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        MDiyBrandItem item = mMyBrandItems.get(fromPosition - COUNT_PRE_MY_HEADER);
        mMyBrandItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
        mMyBrandItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 获取位移动画
     */
    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public interface OnMyBrandItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnMyBrandItemClickListener(OnMyBrandItemClickListener listener) {
        this.mBrandItemClickListener = listener;
    }

    /**
     * 我的品牌
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewBrandViewItem bandView;

        public MyViewHolder(View itemView) {
            super(itemView);
            bandView = (ViewBrandViewItem) itemView.findViewById(R.id.view_brandview_adapter_item_view);
        }
    }

    /**
     * 其他品牌
     */
    class OtherViewHolder extends RecyclerView.ViewHolder {
        private ViewBrandViewItem bandView;

        public OtherViewHolder(View itemView) {
            super(itemView);
            bandView = (ViewBrandViewItem) itemView.findViewById(R.id.view_brandview_adapter_item_view);
        }
    }

    /**
     * 我的品牌  标题部分
     */
    class MyBrandHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public MyBrandHeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.view_diy_pinpai_name);
        }
    }

    /**
     * 其他品牌  标题部分
     */
    class OtherBrandHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public OtherBrandHeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.view_diy_pinpai_name);
        }
    }
}
