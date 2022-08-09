package com.qm.lib.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author dim
 * @create at 2019-05-17 11:44
 * @description:
 */
public class JYRecyclerView extends RecyclerView implements NestedScrollingChild {

    public JYRecyclerView(@NonNull Context context) {
        super(context);
    }

    public JYRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JYRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int startX, startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int disX = Math.abs(endX - startX);
                int disY = Math.abs(endY - startY);
                if (disX > disY) {
                    getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(startX - endX));
                } else {
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(startY - endY));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        boolean intercepted = false;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                mLastXIntercept = (int) event.getX();
//                mLastYIntercept = (int) event.getY();
//                intercepted = false;
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                int endX = (int) event.getX();
//                int endY = (int) event.getY();
//                int disX = Math.abs(endX - mLastXIntercept);
//                int disY = Math.abs(endY - mLastYIntercept);
//                if (disX > disY) {
//                    intercepted = true;
//                } else {
//                    intercepted = false;
//                }
//                DLog.d("EVENTEVENT", "disX -> " + disX + ", disY -> " + disY + "intercepted = " + intercepted);
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                intercepted = false;
//                break;
//            }
//            default:
//                break;
//        }
//        return intercepted;
//    }
//
//    private int mLastXIntercept, mLastYIntercept;
}
