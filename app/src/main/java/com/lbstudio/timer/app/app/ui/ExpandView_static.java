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

public class ExpandView_static extends ConstraintLayout {

    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;
    private View view;
    private List<Item> itemList;

    public ExpandView_static(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public ExpandView_static(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ExpandView_static(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initExpandView();
    }


    private ExpandView_static initExpandView() {
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
        return this;
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

    public ExpandView_static addItem(String name, String content,
                                     OnItemClickListener itemClickListener, OnItemClickBottomListener itemClickBottomListener) {

        if (itemList == null) {
            itemList = new ArrayList<Item>();
        }
        itemList.add(new Item(name, content, itemClickListener,itemClickBottomListener));
        return this;
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
            OnItemClickListener itemClickListener = item.itemClickListener;
            OnItemClickBottomListener itemClickBottomListener = item.itemClickBottomListener;

            //选择id
            Item_id item_id = selectId(i);
            int guidelineId = item_id.getGuidelineId();
            int id_item_title = item_id.getId_item_title();
            int id_item_content = item_id.getId_item_content();
            int id_item_bottom = item_id.getId_item_bottom();

            //获得guideline的位置
            int guideLineTop1 = getGuideLineY(i);

            creatNewItem(index,view, id_item_title, Title, guidelineId, guideLineTop1,
                    id_item_content, Content, id_item_bottom, itemClickListener,itemClickBottomListener);

        }
        removeAllViews();
        addView(view);
    }

    //建立条目
    private void creatNewItem(final int index, View view, int id_item_title, String text_item_title,
                              int guidelineId, int guidelineTop, int id_item_content,
                              String text_item_content, final int id_item_bottom,
                              final OnItemClickListener itemClickListener,
                              final OnItemClickBottomListener itemClickBottomListener) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.ll_item);//找到容器布局ConstraintLayout
        ConstraintSet constraintSet = new ConstraintSet();//新建一个ConstraintSet
        TextView item_title = new TextView(getContext());//动态添加一个TextView
        item_title.setId(id_item_title);//设置id
        item_title.setText(text_item_title);//设置text
        item_title.setOnClickListener(new OnClickListener() { //item的点击事件
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(index);
            }
        });
        constraintLayout.addView(item_title);//添加到容器布局中
        constraintSet.clone(constraintLayout);
        constraintSet.create(guidelineId, ConstraintSet.HORIZONTAL);//新建一个水平的辅助线
        constraintSet.setGuidelineBegin(guidelineId, CommonUilts.dp2px(guidelineTop));//设置辅助线距父布局顶部多少dp
        constraintSet.constrainWidth(item_title.getId(), 0);//设置TextView的宽
        constraintSet.constrainHeight(item_title.getId(), LayoutParams.WRAP_CONTENT);//设置TextView的高
        constraintSet.connect(item_title.getId(), ConstraintSet.TOP, guidelineId, ConstraintSet.BOTTOM, CommonUilts.dp2px(2));//设置约束条件，令TextView的顶部与辅助线的下部同一水平，再设置margin值顶部距离辅助线2dp
        constraintSet.connect(item_title.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 20);

        TextView item_content = new TextView(getContext());
        item_content.setId(id_item_content);
        item_content.setText(text_item_content);
        item_content.setTextSize(10);
        item_content.setOnClickListener(new OnClickListener() {//item的点击事件
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(index);
            }
        });
        constraintLayout.addView(item_content);
        constraintSet.constrainWidth(item_content.getId(), 0);
        constraintSet.constrainHeight(item_content.getId(), LayoutParams.WRAP_CONTENT);
        constraintSet.connect(item_content.getId(), ConstraintSet.TOP, id_item_title, ConstraintSet.BOTTOM);
        constraintSet.connect(item_content.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 20);
        constraintSet.applyTo(constraintLayout);

        TextView item_buttom = new TextView(getContext());
        item_buttom.setId(id_item_bottom);
        item_buttom.setBackgroundColor(getResources().getColor(R.color.lightRed));
        item_buttom.setOnClickListener(new OnClickListener() { // 按钮的点击事件
            @Override
            public void onClick(View view) {
                itemClickBottomListener.onClick(index);
            }
        });
        constraintLayout.addView(item_buttom);
        constraintSet.constrainWidth(item_buttom.getId(), CommonUilts.dp2px(25));
        constraintSet.constrainHeight(item_buttom.getId(), CommonUilts.dp2px(25));
        constraintSet.create(R.id.planlist_item_vertical_guideline, ConstraintSet.VERTICAL);
        constraintSet.setGuidelinePercent(R.id.planlist_item_vertical_guideline, 0.89f);
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
        OnItemClickListener itemClickListener;
        OnItemClickBottomListener itemClickBottomListener;

        public Item(String title, String content, OnItemClickListener itemClickListener,
                    OnItemClickBottomListener itemClickBottomListener) {
            this.title = title;
            this.content = content;
            this.itemClickListener = itemClickListener;
            this.itemClickBottomListener=itemClickBottomListener;
        }
    }

    public interface OnItemClickListener {
        void onClick(int which);
    }

    public interface OnItemClickBottomListener {
        void onClick(int which);
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

/**
 * 使用例子

expandViewRunning
        .addItem("标题一", "内容一"
        , new ExpandView.OnItemClickListener() {
        @Override
        public void onClick(int which) {
             CommonUilts.showToast("标题一", false);
        }
        }, new ExpandView.OnItemClickBottomListener() {
        @Override
        public void onClick(int which) {
            new ActionSheetDialog(getContext())
                .builder()
                .setTitle("请选择")
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem("完成", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
             CommonUilts.showToast("完成计划", false);
        }
        })
        .addSheetItem("明天继续", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                CommonUilts.showToast("完成计划,明天继续", false);
            }
        }).show();
        }
        })
        .addItem("标题二", "内容二", new ExpandView.OnItemClickListener() {
        @Override
        public void onClick(int which) {
             CommonUilts.showToast("标题二", false);
        }
        }, new ExpandView.OnItemClickBottomListener() {
        @Override
        public void onClick(int which) {
             CommonUilts.showToast("按钮一", false);
        }
        }).show();
 */