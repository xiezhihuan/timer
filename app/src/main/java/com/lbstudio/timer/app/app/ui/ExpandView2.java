package com.lbstudio.timer.app.app.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.util.CommonUilts;

import java.util.ArrayList;
import java.util.List;


public class ExpandView2 extends ListView {

    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;

    public ExpandView2(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public ExpandView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ExpandView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initExpandView();
    }


    private void initExpandView() {
//
//        //设置展开动画
//        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
//        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                setVisibility(View.VISIBLE);
//            }
//        });
//
//        //设置收缩动画
//        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
//        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                setVisibility(View.GONE);
//            }
//        });
    }

    //开始收缩动画
    public void collapse() {
        if (mIsExpand) {
            mIsExpand = false;
            clearAnimation();
            startAnimation(mCollapseAnimation);
        }
    }

    //开始时展开动画
    public void expand() {
        if (!mIsExpand) {
            mIsExpand = true;
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    //判断是否为展开
    public boolean isExpand() {
        return mIsExpand;
    }


}
