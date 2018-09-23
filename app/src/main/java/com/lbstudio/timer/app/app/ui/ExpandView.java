package com.lbstudio.timer.app.app.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.util.CommonUilts;

import java.util.ArrayList;
import java.util.List;


public class ExpandView extends ConstraintLayout {

    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;
    private View view;
    private List<Item> itemList;

    public ExpandView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public ExpandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ExpandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initExpandView();
    }


    public void initExpandView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_expand, null);
        //设置展开动画
        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
            }
        });

        //设置收缩动画
        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }
        });
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

    public void addItem(String name, String content,
                        OnItemClickTextViewListener itemClickTextViewListener, OnItemClickButtonListener itemClickButtonListener) {

        if (itemList == null) {
            itemList = new ArrayList<Item>();
        }
        itemList.add(new Item(name, content, itemClickTextViewListener, itemClickButtonListener));
    }


    public void show() {
        setContentView();
    }

    public void setContentView() {
        for (int i = 1; i <= itemList.size(); i++) {
            final int index = i;
            Item item = itemList.get(i - 1);
            String Title = item.title;
            String Content = item.content;
            OnItemClickTextViewListener itemClickTextViewListener = item.itemClickTextViewListener;
            OnItemClickButtonListener itemClickButtonListener = item.itemClickButtonListener;

            //选择id
            Item_id item_id = selectId(i);
            int guidelineId = item_id.getGuidelineId();
            int id_item_title = item_id.getId_item_title();
            int id_item_content = item_id.getId_item_content();
            int id_item_bottom = item_id.getId_item_bottom();

            //获得guideline的位置
            int guideLineTop1 = getGuideLineY(i);

            creatNewItem(index, view, id_item_title, Title, guidelineId, guideLineTop1,
                    id_item_content, Content, id_item_bottom, itemClickTextViewListener, itemClickButtonListener);

        }
//        removeAllViews();
        addView(view);
    }

    //建立条目
    private void creatNewItem(final int index, View view, int id_item_title, String text_item_title,
                              int guidelineId, int guidelineTop, int id_item_content,
                              String text_item_content, final int id_item_bottom,
                              final OnItemClickTextViewListener itemClickTextViewListener,
                              final OnItemClickButtonListener itemClickBottomListener) {

        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.ll_item);//找到容器布局ConstraintLayout
        ConstraintSet constraintSet = new ConstraintSet();//新建一个ConstraintSet

        notifySubtreeAccessibilityStateChanged(constraintLayout,view,1);

        TextView item_title = new TextView(getContext());//动态添加一个TextView
        item_title.setId(id_item_title);//设置id
        item_title.setText(text_item_title);//设置text
        item_title.setOnClickListener(new OnClickListener() { //item的点击事件
            @Override
            public void onClick(View view) {
                itemClickTextViewListener.onClickTextView(index);
            }
        });
        constraintLayout.addView(item_title);//添加到容器布局中
        constraintSet.clone(constraintLayout);
        constraintSet.create(guidelineId, ConstraintSet.HORIZONTAL);//新建一个水平的辅助线
        constraintSet.setGuidelineBegin(guidelineId, CommonUilts.dp2px(guidelineTop));//设置辅助线距父布局顶部多少dp
        constraintSet.constrainWidth(item_title.getId(), 0);//设置TextView的宽
        constraintSet.constrainHeight(item_title.getId(), ConstraintLayout.LayoutParams.WRAP_CONTENT);//设置TextView的高
        constraintSet.connect(item_title.getId(), ConstraintSet.TOP, guidelineId, ConstraintSet.BOTTOM, CommonUilts.dp2px(2));//设置约束条件，令TextView的顶部与辅助线的下部同一水平，再设置margin值顶部距离辅助线2dp
        constraintSet.connect(item_title.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 20);

        TextView item_content = new TextView(getContext());
        item_content.setId(id_item_content);
        item_content.setText(text_item_content);
        item_content.setTextSize(10);
        item_content.setOnClickListener(new OnClickListener() {//item的点击事件
            @Override
            public void onClick(View view) {
                itemClickTextViewListener.onClickTextView(index);
            }
        });
        constraintLayout.addView(item_content);
        constraintSet.constrainWidth(item_content.getId(), 0);
        constraintSet.constrainHeight(item_content.getId(), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        constraintSet.connect(item_content.getId(), ConstraintSet.TOP, id_item_title, ConstraintSet.BOTTOM);
        constraintSet.connect(item_content.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 20);
        constraintSet.applyTo(constraintLayout);

        TextView item_buttom = new TextView(getContext());
        item_buttom.setId(id_item_bottom);
        item_buttom.setBackgroundResource(R.drawable.icon_three_dot);
        item_buttom.setOnClickListener(new OnClickListener() { // 按钮的点击事件
            @Override
            public void onClick(View view) {
                itemClickBottomListener.onClickButton(index);
            }
        });
        constraintLayout.addView(item_buttom);
        constraintSet.constrainWidth(item_buttom.getId(), CommonUilts.dp2px(25));
        constraintSet.constrainHeight(item_buttom.getId(), CommonUilts.dp2px(25));
        constraintSet.create(R.id.planlist_item_vertical_guideline, ConstraintSet.VERTICAL);
        constraintSet.setGuidelinePercent(R.id.planlist_item_vertical_guideline, 0.927f);
        constraintSet.connect(item_buttom.getId(), ConstraintSet.TOP, id_item_title, ConstraintSet.TOP);
        constraintSet.connect(item_buttom.getId(), ConstraintSet.START, R.id.planlist_item_vertical_guideline, ConstraintSet.END);
        constraintSet.applyTo(constraintLayout);

    }

    //选择条目的背景、文字、箭头的id
    private Item_id selectId(int position) {
        int guidelineId = 0;
        int id_item_title = 0;
        int id_item_content = 0;
        int id_item_bottom = 0;
        switch (position) {
            case 1:
                guidelineId = R.id.planlist_item1_guideline;
                id_item_title = R.id.planlist_item1_title;
                id_item_content = R.id.planlist_item1_content;
                id_item_bottom = R.id.planlist_item1_button;
                break;
            case 2:
                guidelineId = R.id.planlist_item2_guideline;
                id_item_title = R.id.planlist_item2_title;
                id_item_content = R.id.planlist_item2_content;
                id_item_bottom = R.id.planlist_item2_button;
                break;
            case 3:
                guidelineId = R.id.planlist_item3_guideline;
                id_item_title = R.id.planlist_item3_title;
                id_item_content = R.id.planlist_item3_content;
                id_item_bottom = R.id.planlist_item3_button;
                break;
            case 4:
                guidelineId = R.id.planlist_item4_guideline;
                id_item_title = R.id.planlist_item4_title;
                id_item_content = R.id.planlist_item4_content;
                id_item_bottom = R.id.planlist_item4_button;
                break;
            case 5:
                guidelineId = R.id.planlist_item5_guideline;
                id_item_title = R.id.planlist_item5_title;
                id_item_content = R.id.planlist_item5_content;
                id_item_bottom = R.id.planlist_item5_button;
                break;
            case 6:
                guidelineId = R.id.planlist_item6_guideline;
                id_item_title = R.id.planlist_item6_title;
                id_item_content = R.id.planlist_item6_content;
                id_item_bottom = R.id.planlist_item6_button;
                break;
            case 7:
                guidelineId = R.id.planlist_item7_guideline;
                id_item_title = R.id.planlist_item7_title;
                id_item_content = R.id.planlist_item7_content;
                id_item_bottom = R.id.planlist_item7_button;
                break;
            case 8:
                guidelineId = R.id.planlist_item8_guideline;
                id_item_title = R.id.planlist_item8_title;
                id_item_content = R.id.planlist_item8_content;
                id_item_bottom = R.id.planlist_item8_button;
                break;
            case 9:
                guidelineId = R.id.planlist_item9_guideline;
                id_item_title = R.id.planlist_item9_title;
                id_item_content = R.id.planlist_item9_content;
                id_item_bottom = R.id.planlist_item9_button;
                break;
            case 10:
                guidelineId = R.id.planlist_item10_guideline;
                id_item_title = R.id.planlist_item10_title;
                id_item_content = R.id.planlist_item10_content;
                id_item_bottom = R.id.planlist_item10_button;
                break;
            case 11:
                guidelineId = R.id.planlist_item11_guideline;
                id_item_title = R.id.planlist_item11_title;
                id_item_content = R.id.planlist_item11_content;
                id_item_bottom = R.id.planlist_item11_button;
                break;
            case 12:
                guidelineId = R.id.planlist_item12_guideline;
                id_item_title = R.id.planlist_item12_title;
                id_item_content = R.id.planlist_item12_content;
                id_item_bottom = R.id.planlist_item12_button;
                break;
            case 13:
                guidelineId = R.id.planlist_item13_guideline;
                id_item_title = R.id.planlist_item13_title;
                id_item_content = R.id.planlist_item13_content;
                id_item_bottom = R.id.planlist_item22_button;
                break;
            case 14:
                guidelineId = R.id.planlist_item14_guideline;
                id_item_title = R.id.planlist_item14_title;
                id_item_content = R.id.planlist_item14_content;
                id_item_bottom = R.id.planlist_item14_button;
                break;
            case 15:
                guidelineId = R.id.planlist_item15_guideline;
                id_item_title = R.id.planlist_item15_title;
                id_item_content = R.id.planlist_item15_content;
                id_item_bottom = R.id.planlist_item15_button;
                break;
            case 16:
                guidelineId = R.id.planlist_item16_guideline;
                id_item_title = R.id.planlist_item16_title;
                id_item_content = R.id.planlist_item16_content;
                id_item_bottom = R.id.planlist_item16_button;
                break;
            case 17:
                guidelineId = R.id.planlist_item17_guideline;
                id_item_title = R.id.planlist_item17_title;
                id_item_content = R.id.planlist_item17_content;
                id_item_bottom = R.id.planlist_item17_button;
                break;
            case 18:
                guidelineId = R.id.planlist_item18_guideline;
                id_item_title = R.id.planlist_item18_title;
                id_item_content = R.id.planlist_item18_content;
                id_item_bottom = R.id.planlist_item18_button;
                break;
            case 19:
                guidelineId = R.id.planlist_item19_guideline;
                id_item_title = R.id.planlist_item19_title;
                id_item_content = R.id.planlist_item19_content;
                id_item_bottom = R.id.planlist_item19_button;
                break;
            case 20:
                guidelineId = R.id.planlist_item20_guideline;
                id_item_title = R.id.planlist_item20_title;
                id_item_content = R.id.planlist_item20_content;
                id_item_bottom = R.id.planlist_item20_button;
                break;
            case 21:
                guidelineId = R.id.planlist_item21_guideline;
                id_item_title = R.id.planlist_item21_title;
                id_item_content = R.id.planlist_item21_content;
                id_item_bottom = R.id.planlist_item21_button;
                break;
            case 22:
                guidelineId = R.id.planlist_item22_guideline;
                id_item_title = R.id.planlist_item22_title;
                id_item_content = R.id.planlist_item22_content;
                id_item_bottom = R.id.planlist_item22_button;
                break;
            case 23:
                guidelineId = R.id.planlist_item23_guideline;
                id_item_title = R.id.planlist_item23_title;
                id_item_content = R.id.planlist_item23_content;
                id_item_bottom = R.id.planlist_item23_button;
                break;
            case 24:
                guidelineId = R.id.planlist_item24_guideline;
                id_item_title = R.id.planlist_item24_title;
                id_item_content = R.id.planlist_item24_content;
                id_item_bottom = R.id.planlist_item24_button;
                break;
            case 25:
                guidelineId = R.id.planlist_item25_guideline;
                id_item_title = R.id.planlist_item25_title;
                id_item_content = R.id.planlist_item25_content;
                id_item_bottom = R.id.planlist_item25_button;
                break;
            case 26:
                guidelineId = R.id.planlist_item26_guideline;
                id_item_title = R.id.planlist_item26_title;
                id_item_content = R.id.planlist_item26_content;
                id_item_bottom = R.id.planlist_item26_button;
                break;
            case 27:
                guidelineId = R.id.planlist_item27_guideline;
                id_item_title = R.id.planlist_item27_title;
                id_item_content = R.id.planlist_item27_content;
                id_item_bottom = R.id.planlist_item27_button;
                break;
            case 28:
                guidelineId = R.id.planlist_item28_guideline;
                id_item_title = R.id.planlist_item28_title;
                id_item_content = R.id.planlist_item28_content;
                id_item_bottom = R.id.planlist_item28_button;
                break;
            case 29:
                guidelineId = R.id.planlist_item29_guideline;
                id_item_title = R.id.planlist_item29_title;
                id_item_content = R.id.planlist_item29_content;
                id_item_bottom = R.id.planlist_item29_button;
                break;
            case 30:
                guidelineId = R.id.planlist_item30_guideline;
                id_item_title = R.id.planlist_item30_title;
                id_item_content = R.id.planlist_item30_content;
                id_item_bottom = R.id.planlist_item30_button;
                break;
            case 31:
                guidelineId = R.id.planlist_item31_guideline;
                id_item_title = R.id.planlist_item31_title;
                id_item_content = R.id.planlist_item31_content;
                id_item_bottom = R.id.planlist_item31_button;
                break;
            case 32:
                guidelineId = R.id.planlist_item32_guideline;
                id_item_title = R.id.planlist_item32_title;
                id_item_content = R.id.planlist_item32_content;
                id_item_bottom = R.id.planlist_item32_button;
                break;
            case 33:
                guidelineId = R.id.planlist_item33_guideline;
                id_item_title = R.id.planlist_item33_title;
                id_item_content = R.id.planlist_item33_content;
                id_item_bottom = R.id.planlist_item33_button;
                break;
            case 34:
                guidelineId = R.id.planlist_item34_guideline;
                id_item_title = R.id.planlist_item34_title;
                id_item_content = R.id.planlist_item34_content;
                id_item_bottom = R.id.planlist_item34_button;
                break;
            case 35:
                guidelineId = R.id.planlist_item35_guideline;
                id_item_title = R.id.planlist_item35_title;
                id_item_content = R.id.planlist_item35_content;
                id_item_bottom = R.id.planlist_item35_button;
                break;
            case 36:
                guidelineId = R.id.planlist_item36_guideline;
                id_item_title = R.id.planlist_item36_title;
                id_item_content = R.id.planlist_item36_content;
                id_item_bottom = R.id.planlist_item36_button;
                break;
            case 37:
                guidelineId = R.id.planlist_item37_guideline;
                id_item_title = R.id.planlist_item37_title;
                id_item_content = R.id.planlist_item37_content;
                id_item_bottom = R.id.planlist_item37_button;
                break;
            case 38:
                guidelineId = R.id.planlist_item38_guideline;
                id_item_title = R.id.planlist_item38_title;
                id_item_content = R.id.planlist_item38_content;
                id_item_bottom = R.id.planlist_item38_button;
                break;
            case 39:
                guidelineId = R.id.planlist_item39_guideline;
                id_item_title = R.id.planlist_item39_title;
                id_item_content = R.id.planlist_item39_content;
                id_item_bottom = R.id.planlist_item39_button;
                break;
            case 40:
                guidelineId = R.id.planlist_item40_guideline;
                id_item_title = R.id.planlist_item40_title;
                id_item_content = R.id.planlist_item40_content;
                id_item_bottom = R.id.planlist_item40_button;
                break;
            case 41:
                guidelineId = R.id.planlist_item41_guideline;
                id_item_title = R.id.planlist_item41_title;
                id_item_content = R.id.planlist_item41_content;
                id_item_bottom = R.id.planlist_item41_button;
                break;
        }
        Item_id item_id = new Item_id();
        item_id.setGuidelineId(guidelineId);
        item_id.setId_item_title(id_item_title);
        item_id.setId_item_content(id_item_content);
        item_id.setId_item_bottom(id_item_bottom);

        return item_id;
    }

    //获得每个条目的辅助线的y坐标
    private int getGuideLineY(int position) {
        int guideLineTop = 0;
        for (int i = 1; i < position; i++) {
            guideLineTop += 50;
        }
        return guideLineTop;
    }

    public class Item {
        String title;
        String content;
        OnItemClickTextViewListener itemClickTextViewListener;
        OnItemClickButtonListener itemClickButtonListener;


        public Item(String title, String content, OnItemClickTextViewListener itemClickTextViewListener,
                    OnItemClickButtonListener itemClickButtonListener) {
            this.title = title;
            this.content = content;
            this.itemClickTextViewListener = itemClickTextViewListener;
            this.itemClickButtonListener = itemClickButtonListener;
        }
    }

    public interface OnItemClickTextViewListener {
        void onClickTextView(int which);
    }

    public interface OnItemClickButtonListener {
        void onClickButton(int which);

    }


    public enum ItemColor {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        private ItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class Item_id {
        int guidelineId;
        int id_item_title;
        int id_item_content;
        int id_item_bottom;

        public int getGuidelineId() {
            return guidelineId;
        }

        public void setGuidelineId(int guidelineId) {
            this.guidelineId = guidelineId;
        }

        public int getId_item_title() {
            return id_item_title;
        }

        public void setId_item_title(int id_item_title) {
            this.id_item_title = id_item_title;
        }

        public int getId_item_content() {
            return id_item_content;
        }

        public void setId_item_content(int id_item_content) {
            this.id_item_content = id_item_content;
        }

        public int getId_item_bottom() {
            return id_item_bottom;
        }

        public void setId_item_bottom(int id_item_bottom) {
            this.id_item_bottom = id_item_bottom;
        }
    }

}
