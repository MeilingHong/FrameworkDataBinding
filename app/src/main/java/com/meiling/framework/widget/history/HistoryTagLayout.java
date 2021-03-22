package com.meiling.framework.widget.history;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import com.meiling.framework.R;

import androidx.annotation.NonNull;

/**
 * todo 建议将WIDTH设置为  WRAP_CONTENT ，方便进行计算-----当使用MATCH_PARENT时会导致调用【measureChild】方法时，拿不到子View真正的宽高
 * MATCH_PARENT  ---  MeasureSpec.EXACTLY
 * WRAP_CONTENT  ---  MeasureSpec.AT_MOST
 * <p>
 * Created by marisareimu@126.com on 2021-02-28  10:57
 * project UEvidenceProtect
 */
public class HistoryTagLayout extends ViewGroup {
    private int childHorizontalSpace;
    private int childVerticalSpace;

    public HistoryTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.HistoryTagLayout);
        if (attrArray != null) {
            childHorizontalSpace = attrArray.getDimensionPixelSize(R.styleable.HistoryTagLayout_tagHorizontalSpace, 0);
            childVerticalSpace = attrArray.getDimensionPixelSize(R.styleable.HistoryTagLayout_tagVerticalSpace, 0);
            attrArray.recycle();
        }
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        // 记录相关的一些可能需要使用到的参数
        int count = getChildCount();// 一算有多少子View
        int leftPadding = getPaddingLeft();// 计算左边的偏移值
        int topPadding = getPaddingTop();// 计算顶部的偏移值
        int rightPadding = getPaddingRight();// 计算右边的偏移值    实际可用的一行的宽度为 = width - left - right;
        int bottomPadding = getPaddingBottom();
        // 实际一行可用的最大宽度【一个子View最大可用宽度】
        int maxWidthForChild = sizeWidth - leftPadding - rightPadding;//但是，当使用WRAP_CONTENT 方式可能
        // todo 当 modeWidth 为 MeasureSpec.EXACTLY 时，布局子VIew的位置出现了混乱
        // 记录宽高【当使用的是wrap_content时方面进行适应】
        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度【记录当前行】
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height【记录当前高，当当前行达到最大值时，转向下一行】
         */
        int lineHeight = 0;
        /**
         * 记录当前行有几个View已经填充进去了，当
         */
        // 遍历每个子元素
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);// 这个时候调用：child.getMeasuredWidth()/child.getMeasuredHeight()其实是拿不到
            if (child.getVisibility() == GONE)
                continue;
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);// todo 这里有问题，计算后得到的子View宽度是整个View的宽度
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + childHorizontalSpace;// 当前子View的宽度
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin + childVerticalSpace;// 当前子View的高度
            /**
             * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
             */
            if (lineWidth + childWidth > maxWidthForChild) {// 判断是否需要进行换行
                lineWidth = childWidth; // 重新开启新行，开始记录
                // 叠加当前高度，
                height += lineHeight;// 计算换行后的累加高度【】
                // 开启记录下一行的高度
                lineHeight = childHeight;
                Location location = new Location(leftPadding, topPadding + height, childWidth + leftPadding - childHorizontalSpace, height + childHeight + topPadding);
                child.setTag(location);
            } else {// 否则累加值lineWidth,lineHeight取最大高度【不需要换行】
                Location location = new Location(lineWidth + leftPadding, topPadding + height, lineWidth + childWidth - childHorizontalSpace + leftPadding, height + childHeight + topPadding);
                child.setTag(location);
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);// 判断一行的最大高度是多少
            }
        }
        height += lineHeight;
        sizeHeight += topPadding + bottomPadding;
        height += topPadding + bottomPadding;
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : sizeWidth, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            Location location = (Location) child.getTag();
            if (location != null) {
                child.layout(location.left, location.top, location.right, location.bottom);
            }
        }
    }

    public static class MyLayoutParams extends MarginLayoutParams {

        public MyLayoutParams(LayoutParams source) {
            super(source);
        }

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public MyLayoutParams(int width, int height) {
            super(width, height);
        }
    }

    @Override
    protected MyLayoutParams generateDefaultLayoutParams() {// 主要还是这里的默认参数有问题，导致后续的刷新后，子VIew的宽高在测量时出现了问题
        return new MyLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MyLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MyLayoutParams;
    }

    /**
     * 记录子控件的坐标
     */
    public class Location {
        public Location(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public int left;
        public int top;
        public int right;
        public int bottom;

        @NonNull
        @Override
        public String toString() {
            return String.format("left:[%d]--top:[%d]--right:[%d]--bottom:[%d]", left, top, right, bottom);
        }
    }
}
