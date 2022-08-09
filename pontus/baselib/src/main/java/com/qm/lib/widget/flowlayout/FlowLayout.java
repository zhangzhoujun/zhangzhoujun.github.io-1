package com.qm.lib.widget.flowlayout;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private SparseArray<View> mSelectView;
    private MotionEvent mMotionEvent;
    private boolean isSingleSel;
    private int selectNum = 1;//可以选择的最大数目
    private int maxLines = Integer.MAX_VALUE;//最大行数
    private boolean isModifyClick = false;//修改点击事件
    private boolean isSelectClickable = true;//已经选中的时候允许点击，默认允许

    private float lineH;
    private int lines;

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSelectView = new SparseArray<>();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public int getLines() {
        return lines;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;


        int lineWidth = 0;
        int lineHeight = 0;


        int cCount = getChildCount();
        int line = 0;//行数开始计数
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
                line++;
                if (line >= maxLines) {
                    break;
                }
            } else {
                // 叠加行宽
                lineWidth += childWidth;
                // 得到当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            lines = line;
            // 最后一个控件
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
                lines += 1;
            }
        }


        setMeasuredDimension(
                //
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() +
                        getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() +
                        getPaddingBottom()//
        );

    }

    @Override
    public boolean performClick() {
        if (mMotionEvent == null) {
            return super.performClick();
        }
        int rawX = (int) mMotionEvent.getRawX();
        int rawY = (int) mMotionEvent.getRawY();
        View view = findChild(rawX, rawY);
        if (view == null) {
            return true;
        }
        int position = findPosByView(view);
        if (!isModifyClick) {
            if (mOnCustomClickListener != null) {
                mOnCustomClickListener.onClick(position);
                return true;
            }
            return super.performClick();
        }

        doselect(view, position);
        return true;
    }

    private void doselect(View clickView, int selePos) {
        if (!clickView.isSelected()) {
            if (selectNum == 1 && mSelectView.size() == 1) {
                clickView.setSelected(true);
                //获取之前被选中的view
                View lastSeleView = mSelectView.get(mSelectView.keyAt(0));
                lastSeleView.setSelected(false);
                mSelectView.removeAt(0);
                mSelectView.put(selePos, clickView);
                if (mOnTagSelectListener != null) {
                    mOnTagSelectListener.onTagSelect(clickView, true, selePos, this);
                }
            } else {
                if (selectNum > 0 && mSelectView.size() >= selectNum) return;
                clickView.setSelected(true);
                mSelectView.put(selePos, clickView);
                if (mOnTagSelectListener != null) {
                    mOnTagSelectListener.onTagSelect(clickView, true, selePos, this);
                }
            }
        } else {
            if (isSelectClickable) {
                clickView.setSelected(false);
                mSelectView.remove(selePos);
                if (mOnTagSelectListener != null) {
                    mOnTagSelectListener.onTagSelect(clickView, false, selePos, this);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mMotionEvent = MotionEvent.obtain(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft
                    () - getPaddingRight()) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);

                if (mAllViews.size() >= maxLines) {
                    break;
                }

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);


        }
        // for end
        if (mAllViews.size() < maxLines) {
            mLineHeight.add(lineHeight);
            mAllViews.add(lineViews);
        }
        // 设置子View的位置

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            // 当前行的所有的View
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                // 判断child的状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                // 为子View进行布局
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }

    }

    private View findChild(int x, int y) {
        final int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() == View.GONE) continue;
//            Rect outRect = new Rect();
//            v.getGlobalVisibleRect(outRect);
//            if (outRect.contains(x, y)) {
//                return v;
//            }
            RectF rectF = calcViewScreenLocation(v);
            if (rectF.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight());
    }

    private int findPosByView(View child) {
        final int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View v = getChildAt(i);
            if (v == child) return i;
        }
        return -1;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public boolean isSingleSel() {
        return isSingleSel;
    }

    public void setSingleSel(boolean singleSel) {
        isSingleSel = singleSel;
    }

    public interface OnCustomClickListener {
        void onClick(int position);
    }

    public interface OnTagSelectListener {
        void onTagSelect(View view, boolean isSelect, int position, FlowLayout parent);
    }

    private OnCustomClickListener mOnCustomClickListener;

    public void setOnCustomClickListener(OnCustomClickListener onCustomClickListener) {
        mOnCustomClickListener = onCustomClickListener;
    }

    private OnTagSelectListener mOnTagSelectListener;

    public void setOnTagSelectListener(OnTagSelectListener mOnTagSelectListener) {
        this.mOnTagSelectListener = mOnTagSelectListener;
        if (mOnTagSelectListener != null) setClickable(true);
    }

    public void setSelectView(int pos, View view) {
        mSelectView.put(pos, view);
//        doselect(view, pos);
    }

    /**
     * 只适用于选中数目为1个的情况
     */
    public void setSelectTab(int position) {
        View lastSeleView = mSelectView.get(mSelectView.keyAt(0));
        lastSeleView.setSelected(false);
        mSelectView.removeAt(0);
        getChildAt(position).setSelected(true);
        mSelectView.put(position, getChildAt(position));
    }

    public void setSelectClickable(boolean selectClickable) {
        isSelectClickable = selectClickable;
    }

    public void setModifyClick(boolean isModifyClick) {
        this.isModifyClick = isModifyClick;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }
}
