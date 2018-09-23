package com.lbstudio.timer.app.app.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ie1e.mdialog.view.ActionSheetDialog;
import com.lbstudio.timer.app.MainActivityy;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.activity.LoginAcitvity;
import com.lbstudio.timer.app.app.activity.EnBuild_Activity;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.net.LoginOut;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.loopj.android.http.RequestParams;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeFragment extends Fragment {
    @BindView(R.id.me_setting)
    TextView meSetting;
    @BindView(R.id.me_portrait)
    ImageView mePortrait;
    @BindView(R.id.me_personalInfo)
    TextView mePersonalInfo;
    @BindView(R.id.me_application_more)
    LinearLayout meApplicationMore;
    @BindView(R.id.me_huangLi)
    LinearLayout meHuangLi;
    Unbinder unbinder;
    String userId = MainActivityy.userId;
    String token = MainActivityy.token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.me_setting, R.id.me_portrait, R.id.me_personalInfo, R.id.me_application_more, R.id.me_huangLi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_setting:
                new ActionSheetDialog(getContext())
                        .builder()
                        .setTitle("设置")
                        .setCanceledOnTouchOutside(true)
                        .setDialogLocation(Gravity.CENTER)
                        .addSheetItem("同步数据", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {

                            private boolean ss;

                            @Override
                            public void onClick(int which) {

                                final ProgressDialog dialog = CommonUilts.getProcessDialog(getContext(), "正在同步数据...");
                                dialog.show();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                CommonUilts.showDialog(getContext(), "数据同步成功");
                                            }
                                        }.run();
                                    }
                                }, 3000);
                                ss = false;
                                if (ss) {
                                    //先同步逻辑删除计划、再同步其他状态下的状态
                                    CommonUilts.syncDelctPlan(getContext(), userId, token);
                                    //先同步逻辑删除课程、再同步其他状态下的课程
                                    CommonUilts.syncDelctCourse(getContext(), userId, token);

                                    //清空计划、清空课程
                                    CourseTool.delectAllCourse();
                                    PlanTool.delectAllPlan();
                                    LitePal.deleteAll(Course.class, "term = ?", "2018");
                                    LitePal.deleteAll(Course.class, "id > ?", "0");
                                    //清空课表html缓存
                                    CommonUilts.cacheHtml(getContext(), null);

                                    //从服务器获取所有数据
                                    CommonUilts.pullData(getContext(), true, userId, token);

                                }

                            }
                        })
                        .addSheetItem("退出登录", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                new LoginOut(Integer.parseInt(userId), token, new LoginOut.SuccessCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        //清空userId、token
                                        NetConfig.cacheUserId(getContext(), null);
                                        NetConfig.cacheToken(getContext(), null);
                                        //清空计划、清空课程
                                        CourseTool.delectAllCourse();
                                        PlanTool.delectAllPlan();
                                        LitePal.deleteAll(Course.class, "term = ?", "2018");
                                        LitePal.deleteAll(Course.class, "id > ?", "0");

                                        //清空课表html缓存
                                        CommonUilts.cacheHtml(getContext(), null);

                                        CommonUilts.thisToActivity(LoginAcitvity.class);
                                        getActivity().finish();
                                    }
                                }, new LoginOut.FailCallBack() {
                                    @Override
                                    public void onFail(int status) {
                                        //清空userId、token
                                        NetConfig.cacheUserId(getContext(), null);
                                        NetConfig.cacheToken(getContext(), null);
                                        //清空计划、清空课程
                                        CourseTool.delectAllCourse();
                                        PlanTool.delectAllPlan();
                                        LitePal.deleteAll(Course.class, "term = ?", "2018");
                                        LitePal.deleteAll(Course.class, "id > ?", "0");

                                        //清空课表html缓存
                                        CommonUilts.cacheHtml(getContext(), null);

                                        CommonUilts.thisToActivity(LoginAcitvity.class);
                                        getActivity().finish();

                                    }
                                });
                            }
                        })

                        .show();
                break;
            case R.id.me_portrait:
                CommonUilts.thisToActivity(EnBuild_Activity.class);
                break;
            case R.id.me_personalInfo:
                CommonUilts.thisToActivity(EnBuild_Activity.class);
                break;
            case R.id.me_application_more:
                CommonUilts.thisToActivity(EnBuild_Activity.class);
                break;
            case R.id.me_huangLi:
                CommonUilts.thisToActivity(EnBuild_Activity.class);
                break;
        }
    }


    /**
     * 获得用户名、励志等级、头像链接
     *
     * @param uAccount  用户名
     * @param uPassword 密码
     */
    private void getUserInfo(String uAccount, String uPassword) {
        RequestParams params = new RequestParams();
        params.add("uAccount", uAccount);
        params.add("uPassword", uPassword);
//        CommonUilts.post(AppNetConfig.USERINFO_URL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                String result = new String(bytes);
//                Respond_data respond = JSON.parseObject(result, Respond_data.class);
//                int status = respond.getStatus();
//                if (status != 0) {
//                    CommonUilts.showToast(ToastMassage.GET_USERINFO_FAILED, false);
//                } else {
//                    CommonUilts.showToast(ToastMassage.GET_USERINFO_SUCCESS, false);
//                    //将用户数据保存到app全局变量中
//                    String data = respond.getData();
//                    UserInfo userInfo = JSON.parseObject(data, UserInfo.class);
//                    MyApplication.userInfos.add(userInfo);
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//        });
    }
}
