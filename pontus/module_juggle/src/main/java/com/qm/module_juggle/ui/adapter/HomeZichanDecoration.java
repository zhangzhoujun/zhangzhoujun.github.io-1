package com.qm.module_juggle.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qm.module_juggle.R;

/**
 * @ClassName MyIndexZichanDecoration
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/27 8:43 PM
 * @Version 1.0
 */
public class HomeZichanDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };
    private Drawable mDivider;
    private int spanCount = -1;
    //height width 单位为px
    private int width = 5;
    private int pading = 5;
    private Paint paint;

    public HomeZichanDecoration(Context context) {
        init(context, width, android.R.color.black);
    }

    public HomeZichanDecoration(Context context, int width) {
        init(context, width, android.R.color.black);
    }

    public HomeZichanDecoration(Context context, int width, int color) {
        init(context, width, color);
    }

    private void init(Context context, int width, int color) {
        if (width < 1) {
            width = 1;
        }
        this.width = width;
        pading = (int) context.getResources().getDimension(R.dimen.dp_10);
        paint = new Paint();
        paint.setColor(context.getResources().getColor(color));
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int childCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int drawRight = width;
        int drawBottom = width;
        int drawLeft = width;
        int drawTop = width;
        if (isFirstRaw(parent, position, spanCount, childCount)) {
            drawTop = 0;
        }
        if (isLastColum(parent, position, spanCount, childCount)) {
            drawRight = 0;
        }
        if (isFirstColum(parent, position, spanCount, childCount)) {
            drawLeft = 0;
        }
        if (isLastItem(parent, position, childCount)) {
            drawRight = 0;
        }
        outRect.set(drawLeft, drawTop, drawRight, drawBottom);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        spanCount = getSpanCount(parent);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
             int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            final int top = child.getBottom() + params.bottomMargin;
            //            int bottom = top + mDivider.getIntrinsicHeight();
            int bottom = top + width;
            //if (isLastRaw(parent, i, spanCount, childCount)) {
            //    // 如果是最后一行，则不需要绘制底部
            //    bottom = top;
            //}
            //if (!isLastColum(parent, i, spanCount, childCount)) {
            right = child.getRight() + params.rightMargin + width;
            //}

            if (isFirstColum(parent, i, spanCount, childCount)) {
                left = left + pading;
            } else {
                right = right - pading;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        spanCount = getSpanCount(parent);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
            final int top = child.getTop() - params.topMargin + pading;
            final int bottom = child.getBottom() + params.bottomMargin - pading;
            int left = child.getRight() + params.rightMargin;
            //            int right = left + mDivider.getIntrinsicWidth();
            int right = left + width;

            //if (isLastColum(parent, i, spanCount, childCount)) {
            //    // 如果是最后一列，则不需要绘制右边
            //    right = left;
            //}
            //if (isLastItem(parent, i, childCount)) {
            //    right = left;
            //}

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (childCount % spanCount == 0) {
                childCount = childCount - spanCount;
            } else {
                childCount = childCount - childCount % spanCount;
            }
            if (pos >= childCount) {
                // 如果是最后一行，则不需要绘制底部
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount) {
                    return true;
                }
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (pos < spanCount) {
                // 如果是第一行，则不需要绘制底部
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (pos < spanCount) {
                    // 如果是第一行，则不需要绘制底部
                    return true;
                }
            } else {
                // 如果是第一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
            if ((pos + 1) % spanCount == 1 && childCount == 1) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFirstColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 1) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 1) {
                    return true;
                }
            } else {
                if (pos < spanCount) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastItem(RecyclerView parent, int pos, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) == childCount) {
                return true;
            }
        }
        return false;
    }
}
