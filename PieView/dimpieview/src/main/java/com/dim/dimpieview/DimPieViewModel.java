package com.dim.dimpieview;

import android.graphics.Color;

/**
 * @author dim
 * @create at 2019-05-23 16:05
 * @description:
 */
public class DimPieViewModel {

    /**
     * 中间字体大小
     */
    private float centerTextSize = 80;

    /**
     * 数据字体大小
     */
    private float dataTextSize = 30;

    /**
     * 中间字体颜色
     */
    private int centerTextColor = Color.BLACK;

    /**
     * 数据字体颜色
     */
    private int dataTextColor = Color.BLACK;

    /**
     * 圆圈的宽度
     */
    private float circleWidth = 100;

    /**
     * 圆圈宽度递减的值
     */
    private float circleWidthReduce = 10;

    /**
     * 圆圈宽度是否需要递减
     */
    private boolean needCircleWidthReduce = true;

    /**
     * 绘制图形起点的偏移量，默认是 90度方向开始绘制，默认偏移往左上递减
     */
    private int circleStartDeviation;

    /**
     * 是否显示各个区块的所占比例
     */
    private boolean showPartPercent;

    public float getCenterTextSize() {
        return centerTextSize;
    }

    public void setCenterTextSize(float centerTextSize) {
        this.centerTextSize = centerTextSize;
    }

    public float getDataTextSize() {
        return dataTextSize;
    }

    public void setDataTextSize(float dataTextSize) {
        this.dataTextSize = dataTextSize;
    }

    public int getCenterTextColor() {
        return centerTextColor;
    }

    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
    }

    public int getDataTextColor() {
        return dataTextColor;
    }

    public void setDataTextColor(int dataTextColor) {
        this.dataTextColor = dataTextColor;
    }

    public float getCircleWidth() {
        return circleWidth;
    }

    public void setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
    }

    public float getCircleWidthReduce() {
        return circleWidthReduce;
    }

    public void setCircleWidthReduce(float circleWidthReduce) {
        this.circleWidthReduce = circleWidthReduce;
    }

    public boolean isNeedCircleWidthReduce() {
        return needCircleWidthReduce;
    }

    public void setNeedCircleWidthReduce(boolean needCircleWidthReduce) {
        this.needCircleWidthReduce = needCircleWidthReduce;
    }

    public int getCircleStartDeviation() {
        return circleStartDeviation;
    }

    public void setCircleStartDeviation(int circleStartDeviation) {
        this.circleStartDeviation = circleStartDeviation;
    }

    public boolean isShowPartPercent() {
        return showPartPercent;
    }

    public void setShowPartPercent(boolean showPartPercent) {
        this.showPartPercent = showPartPercent;
    }
}
