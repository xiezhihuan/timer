package com.lbstudio.timer.app.app.net;


import android.content.Context;
import android.content.SharedPreferences;

import com.lbstudio.timer.app.app.javabean.Course;

import org.joda.time.Hours;

public class NetConfig {

    //页面的标号
    public final static String FIRST_PAGE = "1";
    public final static String SECOND_PAGE = "2";
    public final static String THIRD_PAGE = "3";
    public final static String FOURTH_PAGE = "4";

    //数据同步状态码 0 表示本地新增     -1 表示标记删除   1 表示本地更新   9表示已同步
    public final static String NEW = "0";
    public final static String DELECT = "-1";
    public final static String UPDATE = "1";
    public final static String HAVE_SYNC = "9";

    //教务网课表页url参数
    public final static String KEY_NAME = "params_name";
    public final static String KEY_NUMBER = "params_number";
    public final static String KEY_GNMKDM = "params_gnmkdm";

    //TAG
    public final static String TAG = "erqwerewqrd";
    public final static String CATCH = "TRY_CATCH";
    public final static String ISNULL = "timer";
    public final static String SALT = "timer_DFDE24bP";

    //app的id
    public final static String APP_ID = "com.lbstudio.timer";

    //key
    public final static String KEY_USERID = "userId";
    public final static String KEY_TOKEN = "timer_token";
    public final static String KEY_ACOUNT = "timer_email";
    public final static String KEY_ACOUNT_HTTP = "email";
    public final static String KEY_PASSWORD = "timer_password";
    public final static String KEY_PASSWORD_HTTP = "password";
    public final static String KEY_FRAGMENTNUM = "is_single_runningplan";
    public final static String KEY_HTML = "html_kb";
    //计划
    public final static String KEY_PLAN_TITLE = "planTitle";
    public final static String KEY_PLAN_CONTENT = "planContent";
    public final static String KEY_PLAN_DATE = "planDate";
    public final static String KEY_PLAN_STARTTIME = "planStartTime";
    public final static String KEY_PLAN_ENDTIME = "planEndTime";
    public final static String KEY_PLAN_ISREGULAR = "isRegular";
    public final static String KEY_PLAN_PLANSTATUS = "planStatus";
    public final static String KEY_PLAN_REALSTART_TIME = "realStartTime";
    public final static String KEY_PLAN_REALEND_TIME = "realEndTime";
    public final static String KEY_PLAN_REGULAR = "regular";

    //课程
    public final static String KEY_COURSE_NAME = "courseName";
    public final static String KEY_COURSE_SITE = "site";
    public final static String KEY_COURSE_TERM = "term";
    public final static String KEY_COURSE_WEEKOFCLASS = "week0fClass";
    public final static String KEY_COURSE_WEEK = "week";
    public final static String KEY_COURSE_STARTLESSON = "startLesson";
    public final static String KEY_COURSE_ENDLESSON = "endLesson";
    public final static String KEY_COURSE_TEACHER = "teacher";
    public final static String KEY_COURSE_DATE = "date";
    public final static String KEY_COURSE_STATUS = "status";


    //url
    public final static String JWXT = "http://jwxt.buu.edu.cn/";
    private final static String HOST = "47.95.7.93";
    private final static String PORT = "9090";
    private final static String BASE_URL = "http://" + HOST + ":" + PORT + "/";
    //注册路径
    public final static String REGISTER_URL = BASE_URL + "timer/user/register?VerificationCode=";
    //获得注册验证码路径
    public final static String REGISTER_CODE_URL = BASE_URL + "timer/user/register/getVerificationCode/";
    //登录路径
    public final static String LOGIN_URL = BASE_URL + "timer/token/login";
    //退出登录路径
    public final static String LOGINOUT_URL = BASE_URL + "timer/token/logout";
    //找回密码验证码路径
    public final static String FORGETPWD_CODE_URL = BASE_URL + "timer/user/findPassword/getVerificationCode/";
    //找回密码路径
    public final static String FORGETPWD_URL = BASE_URL + "timer/user/findPassword?VerificationCode=";
    //新建计划
    public final static String ADDPLAN_URL = BASE_URL + "timer/plan/createPlan";
    //从服务器拉取所有计划
    public final static String PULLPLAN_URL = BASE_URL + "timer/plan/getPlans/";
    //修改计划路径
    public final static String UPDATE_PLAN_URL = BASE_URL + "plan/updatePlan";
    //删除计划路径
    public final static String DELECT_PLAN_URL = BASE_URL + "deletePlan";
    //同步计划
    public final static String DELECT_SYNC_PLAN_URL = BASE_URL + "timer/plan/pushPlans";
    //新建课程
    public final static String ADD_COURSE_URL = BASE_URL + "timer/course/createCourse";
    //修改课程
    public final static String UPDATE_COURSE_URL = BASE_URL + "timer/course/updateCourse";
    //删除课程
    public final static String DELECT_COURSE_URL = BASE_URL + "timer/course/deleteCourse";
    //同步课程
    public final static String SYNC_COURSE_URL = BASE_URL + "timer/course/pushCourses";
    //拉取从服务器所有计划
    public final static String PULLCOURSE_URL = BASE_URL + "timer/course/getCourses/";

    //状态码
    public final static int NOT_NETWORK = -1;  //网络异常
    public final static int HAVE_REGISTERED = 101;  //邮箱已注册
    public final static int ERR_EMAIL_PWD = 102;  //102 邮箱或密码错误！
    public final static int YZCODE_ERR = 103;  //验证码错误
    public final static int ERRO_REQUEST_TOMORE = 104;  //请求过于频繁
    public final static int ERRO_YZCODE_TOMORE = 105;  //验证码下发次数超过当天限制
    public final static int SUCCESS = 200;  //请求成功
    public final static int ERRO_PARAMS = 400;  //参数异常
    public final static int TOKEN_INVALID = 401;  //token 无效
    public final static int ERRO_METHOD = 405;  //请求方法不支持
    public final static int FAIL = 500;  //处理删除


    /**
     * 缓存token
     *
     * @param context
     * @param token
     */
    public static void cacheToken(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(KEY_TOKEN, token);
        edit.commit();
    }

    /**
     * 从缓存中读取token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, "timer");
    }

    /**
     * 缓存userId
     *
     * @param context
     * @param userId
     */
    public static void cacheUserId(Context context, String userId) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(KEY_USERID, userId);
        edit.commit();
    }

    /**
     * 从缓存中读取userId
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context) {
        String userId = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_USERID, "timer");
        return userId;
    }

    /**
     * 缓存账号
     *
     * @param context
     * @param email
     */
    public static void cacheEmail(Context context, String email) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(KEY_ACOUNT, email);
        edit.commit();
    }

    /**
     * 从缓存中获取账号
     *
     * @param context
     * @return
     */
    public static String getAccount(Context context) {
        String userId = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_ACOUNT, "");
        return userId;
    }

    /**
     * 缓存密码
     *
     * @param context
     * @param password
     */
    public static void cachepassword(Context context, String password) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(KEY_PASSWORD, password);
        edit.commit();
    }

    /**
     * 从缓存中获取密码
     *
     * @param context
     * @return
     */
    public static String getPassword(Context context) {
        String userId = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PASSWORD, "");
        return userId;
    }
}
