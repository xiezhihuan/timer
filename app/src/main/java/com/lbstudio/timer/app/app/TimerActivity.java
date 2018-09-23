package com.lbstudio.timer.app.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.fragment.CourseFragment;
import com.lbstudio.timer.app.app.fragment.PlanTableFragment;
import com.lbstudio.timer.app.app.fragment.MeFragment;
import com.lbstudio.timer.app.app.fragment.PlanFrament3;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.DelectCourse;
import com.lbstudio.timer.app.app.net.DelectPlan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.PullCourseData;
import com.lbstudio.timer.app.app.net.PullPlanData;
import com.lbstudio.timer.app.app.net.SyncCourse;
import com.lbstudio.timer.app.app.net.SyncPlan;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TimerActivity extends BaseActivity implements android.view.GestureDetector.OnGestureListener {


    @BindView(R.id.bottom_bt_01_img)
    ImageView bottomBt01Img;
    @BindView(R.id.bottom_bt_01_text)
    TextView bottomBt01Text;
    @BindView(R.id.bottom_bt_01)
    LinearLayout bottomBt01;
    @BindView(R.id.bottom_bt_02_img)
    ImageView bottomBt02Img;
    @BindView(R.id.bottom_bt_02_text)
    TextView bottomBt02Text;
    @BindView(R.id.bottom_bt_02)
    LinearLayout bottomBt02;
    @BindView(R.id.bottom_bt_03_img)
    ImageView bottomBt03Img;
    @BindView(R.id.bottom_bt_03_text)
    TextView bottomBt03Text;
    @BindView(R.id.bottom_bt_03)
    LinearLayout bottomBt03;
    @BindView(R.id.bottom_bt_04_img)
    ImageView bottomBt04Img;
    @BindView(R.id.bottom_bt_04_text)
    TextView bottomBt04Text;
    @BindView(R.id.bottom_bt_04)
    LinearLayout bottomBt04;
    private PlanFrament3 planFrament;
    private static CourseFragment courseFragment;
    private PlanTableFragment calendarFragment;
    private MeFragment meFragment;
    private FragmentManager fm;
    private static FragmentTransaction ft;

    //定义手势检测器实例
    GestureDetector detector;

    int bottomBarNum = 1;

    private String userId;
    private String token;
    private String fragmentNum;
    private boolean shouldGetDate;//是否在服务器获取用户全部数据

    @Override
    protected void init() {
        try{
            userId = NetConfig.getUserId(this);
            token = NetConfig.getToken(this);
            Log.d("token", "init: "+token+"id:"+userId);
            fragmentNum = CommonUilts.getFragmentNum(this);
            shouldGetDate = CommonUilts.getShouldGetData(this);

            initPage(Integer.parseInt(fragmentNum));

            newDetector();
            //全屏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }

            //如果是新登录，从服务器拿数据
            //TODO  暂停此功能
//        CommonUilts.pullData(this,shouldGetDate,userId,token);
            //先同步逻辑删除计划、再同步其他状态下的状态
            CommonUilts.syncDelctPlan(this,userId,token);
            //先同步逻辑删除课程、再同步其他状态下的课程
            CommonUilts.syncDelctCourse(this,userId,token);

        }catch (Exception ex){
            Log.d("debug", ex.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void newDetector() {
        detector = new GestureDetector(this, this);
    }

    /**
     * 默认每次打开应用展示首页
     */
    private void initPage(int fragmentNum) {
        select(fragmentNum);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_timer;
    }

    //todo  改掉魔法数字  命名要规范
    @OnClick({R.id.bottom_bt_01, R.id.bottom_bt_02, R.id.bottom_bt_03, R.id.bottom_bt_04})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.bottom_bt_01:
                    select(1);
                    break;
                case R.id.bottom_bt_02:
                    select(2);
                    break;
                case R.id.bottom_bt_03:
                    select(3);
                    break;
                case R.id.bottom_bt_04:
                    select(4);
                    break;
            }
        }catch (Exception e){
            Log.d("debug", e.getMessage());
        }

    }


    public void select(int whichPage) {
        resetTab();
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        hideFragment();

        switch (whichPage) {
            case 1:
                clickFirstBottomBar();
                break;
            case 2:
                clickSecondBottomBar();
                break;
            case 3:
                clickThirdBottomBar();
                break;
            case 4:
                clickFourthBottomBar();
                break;
        }
        ft.commit();
    }

    private void clickFirstBottomBar() {
        bottomBt01Img.setImageResource(R.drawable.icon_bottom_01_select);
        bottomBt01Text.setTextColor(getColor(R.color.themeCorol));

        planFrament = new PlanFrament3();
        ft.add(R.id.fragment_container, planFrament);

        ft.show(planFrament);
    }

    private void clickSecondBottomBar() {
        CommonUilts.cacheFragmentNum(this, "2");
        bottomBt02Img.setImageResource(R.drawable.icon_bottom_02_select);
        bottomBt02Text.setTextColor(getColor(R.color.themeCorol));
        Log.d("debug", "onViewClicked: 2");

        courseFragment = new CourseFragment();
        ft.add(R.id.fragment_container, courseFragment);

        ft.show(courseFragment);
    }

    private void clickThirdBottomBar() {
        bottomBt03Img.setImageResource(R.drawable.icon_bottom_03_select);
        bottomBt03Text.setTextColor(getColor(R.color.themeCorol));
        Log.d("debug", "onViewClicked: 3");
        calendarFragment = new PlanTableFragment();
        ft.add(R.id.fragment_container, calendarFragment);
        ft.show(calendarFragment);
    }

    private void clickFourthBottomBar() {
        bottomBt04Img.setImageResource(R.drawable.icon_bottom_04_select);
        bottomBt04Text.setTextColor(getColor(R.color.themeCorol));
        Log.d("debug", "onViewClicked: 4");
        if (meFragment == null) {
            meFragment = new MeFragment();
            ft.add(R.id.fragment_container, meFragment);
        }
        ft.show(meFragment);
    }

    /**
     * 在每次点击bottomBar之前重置按钮样式
     */
    private void resetTab() {
        bottomBt01Img.setImageResource(R.drawable.icon_bottom_01_unselected);
        bottomBt02Img.setImageResource(R.drawable.icon_bottom_02_unselected);
        bottomBt03Img.setImageResource(R.drawable.icon_bottom_03_unselected);
        bottomBt04Img.setImageResource(R.drawable.icon_bottom_04_unselected);

        bottomBt01Text.setTextColor(getColor(R.color.txtGray));
        bottomBt02Text.setTextColor(getColor(R.color.txtGray));
        bottomBt03Text.setTextColor(getColor(R.color.txtGray));
        bottomBt04Text.setTextColor(getColor(R.color.txtGray));
    }

    private void hideFragment() {
        if (planFrament != null) {
            ft.hide(planFrament);
        }
        if (courseFragment != null) {
            ft.hide(courseFragment);
        }
        if (calendarFragment != null) {
            ft.hide(calendarFragment);
        }
        if (meFragment != null) {
            ft.hide(meFragment);
        }

    }


    //用GestureDetector处理在该activity上发生的所有触碰事件
    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * 滑屏监测
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //定义最小滑动距离
        float minVelocity = 0;      //定义最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();


        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {   //左滑
            bottomBarNum += 1;
            if (bottomBarNum == 5) {
                bottomBarNum = 1;
            }
            switch (bottomBarNum) {
                case 1:
                    select(1);
                    break;
                case 2:
                    select(2);
                    break;
                case 3:
                    select(3);
                    break;
                case 4:
                    select(4);
                    break;
            }
            Toast.makeText(this, "左滑" + bottomBarNum, Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {   //右滑
            bottomBarNum -= 1;
            if (bottomBarNum == 0) {
                bottomBarNum = 4;
            }
            switch (bottomBarNum) {
                case 1:
                    select(1);
                    break;
                case 2:
                    select(2);
                    break;
                case 3:
                    select(3);
                    break;
                case 4:
                    select(4);
                    break;
            }
            Toast.makeText(this, "右滑", Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑
            Toast.makeText(this, "上滑", Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {   //下滑
            Toast.makeText(this, "下滑", Toast.LENGTH_SHORT).show();  //此处可以更改为当前动作下你想要做的事情
        }

        return false;
    }
}
