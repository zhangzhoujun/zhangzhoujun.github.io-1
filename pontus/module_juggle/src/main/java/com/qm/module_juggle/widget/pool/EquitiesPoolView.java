package com.qm.module_juggle.widget.pool;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dim.library.utils.DLog;
import com.qm.lib.widget.dim.AppImageView;
import com.qm.module_juggle.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class EquitiesPoolView extends FrameLayout {

    private List<EquitiesPollItem> beanList = new ArrayList<>();    // 存储要显示的能量bean,最大 everyTimeMaxListSize 个

    private List<EquitiesPollItem> surplusBeanList = new ArrayList<>();
    //  超出everyTimeMaxListSize 个的存储在这里                                                                                                // 如果服务器返回的不止10个，其他的就都放到这个集合中

    private List<View> viewList = new ArrayList<>();

    public List<EquitiesPollItem> disappearWaterBallList = new ArrayList<>();
    // 需要上传到服务器的数据

    private LayoutInflater mInflater;
    // 加载

    private int width;
    // 宽度

    private int height;
    // 高度

    private Random mRandom = new Random();
    // 随机数

    //    private List<Float> xCanChooseList = Arrays.asList(
    //            0.787f, 0.906f, 0.093f, 0.233f, 0.397f, 0.667f, 0.879f, 0.081f, 0.352f,
    //            0.228f, 0.529f, 0.691f, 0.896f, 0.837f, 0.116f);
    //
    //    private List<Float> yCanChooseList = Arrays.asList(
    //            0.497f, 0.573f, 0.803f, 0.772f, 0.807f, 0.822f, 0.772f, 0.202f, 0.093f,
    //            0.273f, 0.118f, 0.140f, 0.093f, 0.312f, 0.497f);

    //    private List<Float> xCanChooseList = Arrays.asList(
    //            0.081f, 0.281f, 0.480f, 0.680f, 0.850f, 0.085f, 0.400f, 0.490f, 0.690f,
    //            0.890f, 0.095f, 0.300f, 0.510f, 0.710f, 0.890f);
    //
    //    private List<Float> yCanChooseList = Arrays.asList(
    //            0.093f, 0.090f, 0.112f, 0.108f, 0.130f, 0.325f, 0.330f, 0.340f, 0.328f,
    //            0.335f, 0.545f, 0.540f, 0.560f, 0.548f, 0.555f);

    private List<Float> xCanChooseList = Arrays.asList(
            0.081f, 0.281f, 0.480f, 0.680f, 0.085f, 0.400f, 0.490f, 0.690f,
            0.095f, 0.300f, 0.510f, 0.710f);

    //    private List<Float> yCanChooseList = Arrays.asList(
    //            0.093f, 0.090f, 0.112f, 0.108f, 0.325f, 0.330f, 0.340f, 0.328f,
    //             0.545f, 0.540f, 0.560f, 0.548f);

    //测试
    private List<Float> yCanChooseList = Arrays.asList(
            0.093f, 0.090f, 0.112f, 0.108f, 0.479f, 0.407f, 0.417f, 0.405f,
            0.622f, 0.617f, 0.637f, 0.625f);

    private List<Float> xCurrentList = new ArrayList<>(xCanChooseList);

    private List<Float> yCurrentList = new ArrayList<>(yCanChooseList);

    private final int RANGE_OF_MOTION = 10;
    // 初始运动范围

    private final int MSG_WHART = 0;
    // 发送消息的标志

    private int childViewWidth, childViewHeight;

    private final int DURATION_APPEARANCE = 800;
    // 显示

    private final int DURATION_DISAPPEARANCE = 800;
    // 显示

    private final int MAX_COUNT = 10;
    // 一个页面最多能显示多少个

    private int totalWaterBeanSize;
    // 总的能量数                                                                                                                         // 全部小球集合长度

    private boolean needShowDefaultDynamic = false;
    // 是否需要显示默认弹力球

    private boolean isNewUserDefault;
    // 新注册用户，默认弹力球

    private int viewDisappearX, viewDisAppearY;
    // 小球动画消失的x,y轴

    private List<Float> randomSpeed = Arrays.asList(0.25f, 0.3f, 0.15f, 0.2f, 0.35f);
    // 每次最大显示多少
    private int everyTimeMaxListSize = 12;
    // 每次最大显示多少

    private float lastViewLocationX, lastViewLocationY;

    private long lastClickTime = 0;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            setAllViewTranslation();
            handler.sendEmptyMessageDelayed(MSG_WHART, 12);
        }
    };

    /***
     * 以Y轴为例，设置view的初始Y坐标为20,方向是y轴，当前运动到了30,辣么 view.setTranslationY();
     */
    private void setAllViewTranslation() {
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            View view = viewList.get(i);
            // 拿到上次view保存的速度
            float spd = (float) view.getTag(R.string.string_origin_spe);
            // 水滴初始的位置
            float original = (float) view.getTag(R.string.string_origin_location);
            float step = spd;
            boolean isUp = (boolean) view.getTag(R.string.string_origin_direction);
            float translationY;
            // 根据水滴tag中的上下移动标识移动view
            if (isUp) {
                translationY = view.getY() - step;
            } else {
                translationY = view.getY() + step;
            }
            // 对水滴位移范围的控制
            if (translationY - original > RANGE_OF_MOTION) {
                translationY = original + RANGE_OF_MOTION;
                view.setTag(R.string.string_origin_direction, true);
            } else if (translationY - original < -RANGE_OF_MOTION) {
                translationY = original - RANGE_OF_MOTION;
                // FIXME:每次当水滴回到初始点时再一次设置水滴的速度，从而达到时而快时而慢
                // view.setTag(R.string.string_origin_spe, spd);
                view.setTag(R.string.string_origin_spe, randomSpeed.get(mRandom.nextInt(randomSpeed.size())));
                view.setTag(R.string.string_origin_direction, false);
            }
            view.setY(translationY);
        }
    }

    public EquitiesPoolView(@NonNull Context context) {
        this(context, null);
    }

    public EquitiesPoolView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EquitiesPoolView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<EquitiesPollItem> list, int maxBallCount) {
        if (list == null) {
            return;
        }
        beanList.clear();
        viewList.clear();

        xCurrentList = new ArrayList<>(xCanChooseList);
        yCurrentList = new ArrayList<>(yCanChooseList);

        totalWaterBeanSize = list.size();
        everyTimeMaxListSize = maxBallCount;
        cuttingList(list);

        postRunnable();
    }

    private void postRunnable() {
        post(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < beanList.size(); i++) {
                    EquitiesPollItem bean = beanList.get(i);
                    addViewList(bean, i);
                }
                handler.sendEmptyMessageDelayed(MSG_WHART, DURATION_APPEARANCE);
            }
        });
    }

    private void cuttingList(List<EquitiesPollItem> list) {
        if (list.size() > everyTimeMaxListSize) {
            beanList = list.subList(0, everyTimeMaxListSize);
            for (int i = everyTimeMaxListSize; i < list.size(); i++) {
                surplusBeanList.add(list.get(i));
            }
        } else {
            beanList = list;
        }
    }

    // 生成能量球
    public void addViewList(EquitiesPollItem bean, int i) {
        View view = mInflater.inflate(R.layout.poll_item, this, false);
        findView(bean, view);

        if (bean.isDefault()) {
            setDefaultViewLocation(view, bean.getType());
            setOnClickBallView(view, bean, i);
        } else {
            setOnClickBallView(view, bean, i);
            if (i == -1) {
                setViewLocation(view);
            } else {
                setViewLocation(view, i);
            }
        }
        addView(view);
        setViewAnimation(view);
        viewList.add(view);
    }

    // 设置能量球图片和数值
    private void findView(EquitiesPollItem bean, View view) {
        TextView id_tv_waterball = view.findViewById(R.id.ball_name);
        AppImageView id_iv_waterball = view.findViewById(R.id.ball_img);
        AppImageView id_iv_waterball_default = view.findViewById(R.id.ball_img_default);

        if (bean.isDefault()) {
            id_iv_waterball.setVisibility(View.GONE);
            id_iv_waterball_default.setVisibility(View.VISIBLE);
            id_tv_waterball.setVisibility(View.GONE);
            if (TextUtils.isEmpty(bean.getItem().getImg_url())) {
                id_iv_waterball_default.setImageUrl(R.mipmap.home_title_default);
            } else {
                id_iv_waterball_default.setImageUrl(bean.getItem().getImg_url());
            }
        } else {
            id_iv_waterball.setVisibility(View.VISIBLE);
            id_iv_waterball_default.setVisibility(View.GONE);
            id_tv_waterball.setText(bean.getNt_amount());
            id_iv_waterball.setImageUrl(R.mipmap.home_pool_item);
            id_tv_waterball.setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickBallView(final View view, final EquitiesPollItem bean, final int position) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (now - lastClickTime > 1000) {
                    lastClickTime = now;

                    if (onStopAnimateListener != null) {
                        onStopAnimateListener.onBallClick(view, bean, position);
                    } else {
                        startballDismiss(view, bean, position);
                    }
                } else {
                    DLog.d("防重点....");
                }
            }
        });
    }

    public void startballDismiss(final View view, final EquitiesPollItem bean, final int position) {
        if (!disappearWaterBallList.contains(bean)) {
            disappearWaterBallList.add(bean);
        }
        viewList.remove(view);
        beanList.remove(bean);

        startClickBallDisappearAnim(view, bean, position);
    }

    private void startClickBallDisappearAnim(final View view, final EquitiesPollItem bean, final int position) {
        lastViewLocationX = view.getX();
        lastViewLocationY = view.getY();

        viewDisappearX = (int) lastViewLocationX;

        view.animate()
                .translationY(viewDisAppearY)
                .translationX(viewDisappearX)
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 弹力值全部收取完成的时候，才出现默认的小球，不可重复出现默认小球
                        onBallDisappearAnimEnd(view, bean);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .setDuration(DURATION_DISAPPEARANCE)
                .start();
    }

    private void onBallDisappearAnimEnd(View view, EquitiesPollItem bean) {
        removeView(view);
        if (onStopAnimateListener != null) {
            onStopAnimateListener.onBallDisappearAnimListener(bean.getNt_amount());
        }

        if (!surplusBeanList.isEmpty()) {
            addViewList(surplusBeanList.get(0), -1);
            surplusBeanList.remove(0);
        } else {
            //            if (needShowDefaultDynamic && totalWaterBeanSize == disappearWaterBallList.size()) {
            //                needShowDefaultDynamic = false;
            //                addViewList(getDefaultEquitiesPollItem(), -1);
            //            }
        }
    }

    private void setViewAnimation(final View view) {
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(DURATION_APPEARANCE).start();
    }

    private void setViewLocation(View view, int i) {
        int randomInt = 0;
        if (i >= 0) {
            //            KLog.printTagLuo("xxxxxx: " + xCurrentList.size());
            randomInt = mRandom.nextInt(xCurrentList.size());
        }

        float x = (xCurrentList.get(randomInt) - 0.049f) * width;
        //        float x = (xCurrentList.get(randomInt)) * width;
        view.setX(x);

        float y = (yCurrentList.get(randomInt) - 0.075f) * height;
        //        float y = (yCurrentList.get(randomInt)) * height;
        view.setY(y);

        //        KLog.printTagLuo("randomInt=" + randomInt + "设置的x位置是" + xCurrentList.get(randomInt) + "y轴位置是" + yCurrentList.get(randomInt));

        view.setTag(R.string.string_origin_location, (float) y);
        view.setTag(R.string.string_origin_direction, mRandom.nextBoolean());
        view.setTag(R.string.string_origin_spe, randomSpeed.get(mRandom.nextInt(randomSpeed.size())));

        xCurrentList.remove(randomInt);
        yCurrentList.remove(randomInt);
    }

    private void setViewLocation(View view) {
        view.setX(lastViewLocationX);
        view.setY(lastViewLocationY);

        view.setTag(R.string.string_origin_location, lastViewLocationY);
        view.setTag(R.string.string_origin_direction, mRandom.nextBoolean());
        view.setTag(R.string.string_origin_spe, randomSpeed.get(mRandom.nextInt(randomSpeed.size())));
    }

    /***
     * 设置默认的小球位置
     *
     * @param view
     */
    private void setDefaultViewLocation(View view, int type) {
        float x = width - childViewWidth;
        float y = height;
        if (type == 1) {
            x = x / 5;
            y = y / 6;
        } else if (type == 2) {
            x = x / 5;
            y = y / 2;
        } else if (type == 3) {
            y = 0;
        } else if (type == 4) {
            y = (int)(y / 10 * 3.5);
        } else if (type == 5) {
            y = y / 10 * 7;
        }
        view.setX(x);
        view.setY(y);

        view.setTag(R.string.string_origin_location, y);
        view.setTag(R.string.string_origin_direction, mRandom.nextBoolean());
        view.setTag(R.string.string_origin_spe, 0.5f);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimate();
    }

    /**
     * 获取默认的弹力值
     */
    //    public EquitiesPollItem getDefaultEquitiesPollItem() {
    //        return new EquitiesPollItem(EquitiesPollItem.GosType.CPU, "", "收gos", "", true);
    //    }
    public void stopAnimate() {
        if (viewList == null || viewList.isEmpty()) {
            destroy();
            return;
        }

        for (int i = 0; i < viewList.size(); i++) {
            startDisappearAnim(viewList.get(i), i);
        }
    }

    private void startDisappearAnim(View childView, final int i) {
        ViewPropertyAnimator viewPropertyAnimator =
                childView.animate().alpha(0).scaleX(0).scaleY(0).setDuration(DURATION_APPEARANCE);
        if (i + 1 == viewList.size()) {
            viewPropertyAnimator.setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    destroy();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        viewPropertyAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();

        childViewWidth = dip2px(getContext(), 60);
        childViewHeight = dip2px(getContext(), 60);

        //        KLog.printTagLuo("width : " + width + " height : " + height
        //                + " childViewWidth : " + childViewWidth + " childViewHeight: " + childViewHeight);

    }

    public void setViewDisappearLocation(int[] location) {
        viewDisappearX = location[0];
        viewDisAppearY = location[1];
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void destroy() {
        handler.removeMessages(MSG_WHART);
        if (onStopAnimateListener != null) {
            onStopAnimateListener.onExitAnimateListener();
        }
        removeAllViews();
    }

    private OnStopAnimateListener onStopAnimateListener;

    public void setOnStopAnimateListener(OnStopAnimateListener listener) {
        this.onStopAnimateListener = listener;
    }

    public interface OnStopAnimateListener {

        void onBallDisappearAnimListener(String number);

        void onExitAnimateListener();

        void onBallClick(View view, EquitiesPollItem bean, int position);
    }
}