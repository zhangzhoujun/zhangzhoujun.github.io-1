package com.qm.module_juggle.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.qm.module_juggle.R
import java.lang.Math.abs

/**
 * @ClassName FixDragLayout
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/28 5:45 PM
 * @Version 1.0
 */
class FixDragLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var isDragged = false
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    val HORIZONTAL = LinearLayout.HORIZONTAL
    val VERTICAL = LinearLayout.VERTICAL
    private var orientation = HORIZONTAL

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FixDragLayout)
            orientation = a.getInt(R.styleable.FixDragLayout_android_orientation, HORIZONTAL)
            a.recycle()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                isDragged = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragged) {
                    val dx = abs(ev.x - downX)
                    val dy = abs(ev.y - downY)
                    if (orientation == HORIZONTAL) {
                        isDragged = dx > touchSlop && dx > dy
                    } else if (orientation == VERTICAL) {
                        isDragged = dy > touchSlop && dy > dx
                    }
                }
                parent.requestDisallowInterceptTouchEvent(isDragged)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragged = false
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}

