package com.lbstudio.timer.app.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/19.
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 求取布局的宽高
     *
     * @param widthMeasureSpec  ----宽度的测量规格
     * @param heightMeasureSpec ---高度的测量规格
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        //求取at_most模式下的布局宽、高值
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams mp = ((MarginLayoutParams) child.getLayoutParams());
            if (lineWidth + childWidth + mp.leftMargin + mp.rightMargin > widthSize) {
                //换行:宽度--对比获取
                width = Math.max(width, lineWidth);
                height += lineHeight;
                //重置一下
                lineWidth = childWidth + mp.leftMargin + mp.rightMargin;
                lineHeight = childHeight + mp.topMargin + mp.bottomMargin;
            } else {
                //不换行:行高--对比获得
                lineWidth += childWidth + mp.leftMargin + mp.rightMargin;
                lineHeight = Math.max(lineHeight, childHeight + mp.topMargin + mp.bottomMargin);
            }
            //最后一次还要再参与计算一次
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        Log.e("zoubo", "width:" + width + "----heigth:" + height);
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    //每一行所包含的所有子view
    private List<List<View>> allViews = new ArrayList<>();

    private List<Integer> allWidths = new ArrayList<>();


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        int heith = getHeight();  //获取本布局的高  即FlowLayout的高
//        int cCount = getChildCount(); //该布局的子view数量
//
//        int lineWidth = 0;
//        int lineHeight = 0;
//
//        List<View> columnViews = new ArrayList<>();  //一列所包含的view
//        for (int i = 0; i < cCount; i++) {
//            View child = getChildAt(i);
//            int childWidth = child.getMeasuredWidth();//获取child的宽
//            int childHeight = child.getMeasuredHeight();//获取child的高
//            MarginLayoutParams mp = ((MarginLayoutParams) child.getLayoutParams()); //获得child的参数 如：margin值 padding值
//            if (lineHeight + childHeight + mp.topMargin + mp.bottomMargin > heith) {
//                //换行
//                allViews.add(columnViews);
//                allWidths.add(lineWidth);
//                //重置一下变量
//                columnViews = new ArrayList<>();
//                lineHeight = 0;
//                lineWidth = childWidth + mp.leftMargin + mp.rightMargin;
//            }
//            //不换行
//            lineHeight += childHeight + mp.topMargin + mp.bottomMargin;
//            lineWidth = Math.max(lineWidth, childWidth + mp.leftMargin + mp.rightMargin);
//            columnViews.add(child);
//
//            if (i == cCount - 1) {
//                allViews.add(columnViews);
//                allWidths.add(lineWidth);
//            }
//        }
//
//
//        //通过计算每一行的每一个子view的left,top,right,bottom,摆放每一行的每一个子view的位置
//        int left = 0;
//        int top = 0;
//
//        for (int i = 0; i < allViews.size(); i++) {
//            int curColumnWidth = allWidths.get(i);
//
//            List<View> views = allViews.get(i); //当前列的所有view
//            for (View view : views) {
//                int viewWidth = view.getMeasuredWidth(); //获得view的宽
//                int viewHeight = view.getMeasuredHeight();//获得view的高
//                MarginLayoutParams mp = ((MarginLayoutParams) view.getLayoutParams()); //获得这个view的参数 如 maegin值 padding值 等等
//                int left_coordinate = left + mp.leftMargin;
//                int top_coordinate = top + mp.topMargin;
//                int right_coordinate = left_coordinate + viewWidth;
//                int bottom_coordinate = top_coordinate + viewHeight;
//                view.layout(left_coordinate, top_coordinate, right_coordinate, bottom_coordinate);
//                top += viewHeight + mp.topMargin + mp.bottomMargin;
//            }
//            left += curColumnWidth;
//            top = 0;
//        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        MarginLayoutParams mp = new MarginLayoutParams(getContext(), attrs);
        return mp;
    }
}
