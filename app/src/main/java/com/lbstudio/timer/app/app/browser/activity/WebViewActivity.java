package com.lbstudio.timer.app.app.browser.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.administrator.app.browser.config.MyWebChromeClient;
import com.lbstudio.timer.app.MainActivityy;
import com.lbstudio.timer.app.MyApplication;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.javabean.Course_un_inited;
import com.lbstudio.timer.app.app.net.AddCourse;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.NetTool;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.browser.config.IWebPageView;
import com.lbstudio.timer.app.app.browser.config.ImageClickInterface;
import com.lbstudio.timer.app.app.browser.config.MyWebViewClient;
import com.lbstudio.timer.app.base.BaseActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

//import com.example.administrator.app.browser.config.MyWebChromeClient;

public class WebViewActivity extends BaseActivity implements IWebPageView {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.bottom)
    Button bottom;

    private String mUrl; //webview访问的地址

    private MyWebChromeClient mWebChromeClient;


    @Override
    protected void init() {
        CommonUilts.showToast(getString(R.string.buu), false);
        getInitData();
        setTitle("登录");
        initWebView();
        webview.loadUrl(mUrl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @OnClick(R.id.bottom)
    public void onViewClicked() {

        //获得课表页url参数，并缓存
        webview.loadUrl("javascript:injectedObject.getHtml('<html>'+ document.getElementsByTagName('html')[0].innerHTML+'</html>'); ");

        //停2s

        //从缓存中读取参数
        Map<String, String> urlParams = CommonUilts.getUrlParams(this);
        final String number = urlParams.get(NetConfig.KEY_NUMBER);
        final String name = urlParams.get(NetConfig.KEY_NAME);
        final String gnmkdm = urlParams.get(NetConfig.KEY_GNMKDM);
        try {
            String name_gb2312 = null;
            try {
                name_gb2312 = new String(name.getBytes("gb2312"), "gb2312");
            } catch (UnsupportedEncodingException e) {
                Log.e("debug", "getKB_zhuan_ma: "+e.getMessage() );
                e.printStackTrace();
            }
            //跳转到课表页
            webview.loadUrl("http://jwxt.buu.edu.cn/xskbcx.aspx?xh=" + number + "&xm=" + name_gb2312 + "&gnmkdm=" + gnmkdm);
        } catch (NullPointerException ex) {
            Log.e("debug", "getKB——null: " + ex.getMessage());
        }

//        webview.loadUrl("http://jwxt.buu.edu.cn/xskbcx.aspx?xh=2017220330001&xm=%D0%BB%D6%B2%BB%C0&gnmkdm=N121603");
//        webview.loadUrl("http://jwxt.buu.edu.cn/xskbcx.aspx?xh=2017240332013&amp&xm=%CE%A4%BD%AD%BA%A3&amp&gnmkdm=N121603");
    }

    @OnClick(R.id.getKB)
    public void getKB() {
        //获取课表数据
        webview.loadUrl("javascript:injectedObject.getKbHtml('<html>'+ document.getElementsByTagName('html')[0].innerHTML+'</html>'); ");

    }

    private void save(String html) {
        //处理html后得到的数据
        List<Course_un_inited> courseList = initHtmlData(html);

        Log.d("lpkppk", "courseList: " + courseList.size());
        //“人工智能概论 周四第8,9节{第1-16周} 马楠/李佳洪/李德毅 综实A206”

        //今天的日期
        String nowDateNum = CommonUilts.getNowDateNum();

        //遍历 添加到
        for (Course_un_inited course : courseList) {
            String name = course.getName();
            String site = course.getSite();
            int term1 = course.getTerm();
            String week0fClass = course.getWeek0fClass();
            int week = course.getWeek();
            int startLesson = course.getStartLesson();
            int endLesson = course.getEndLesson();
            String teacher = course.getTeacher();

            //本地添加
            CourseTool.addCourse(name, site, term1, week0fClass, week, startLesson, endLesson, teacher, Integer.parseInt(nowDateNum), Integer.parseInt(NetConfig.UPDATE));
            //云端添加
            String userId = NetConfig.getUserId(MyApplication.mContext);
            String token = NetConfig.getToken(MyApplication.mContext);
            String nowDateNum1 = CommonUilts.getNowDateNum();
            new AddCourse(MyApplication.mContext, Integer.parseInt(userId), token, name, site, term1, week0fClass, week, startLesson,
                    endLesson, teacher, Integer.parseInt(nowDateNum1), new AddCourse.SuccessCallBack() {
                @Override
                public void onSuccess(int courseId, int status) {

                }
            }, new AddCourse.FailCallBack() {
                @Override
                public void onFail(int status) {
                    if (status == NetConfig.TOKEN_INVALID) {
                        NetTool.reLogin(MyApplication.mContext);
                    }
                }
            });
        }
    }

    private List<Course_un_inited> initHtmlData(String html) {
        String span = null;
        String regex = "<span.*?>[\\s\\S]*<\\/span>";
        Matcher matcher = Pattern.compile(regex).matcher(html);
        while (matcher.find()) {
            span = matcher.group();
            Log.d(NetConfig.TAG, "getHtml: " + span);
        }
//        <option selected="selected" value="2018-2019">2018-2019</option>
        String term_option = null;
        regex = "<option selected=\"selected\" value=.*?>\\d{4}-\\d{4}(<\\/option>){1}";
        Matcher termOptionMatcher = Pattern.compile(regex).matcher(span);
        while (termOptionMatcher.find()) {
            term_option = termOptionMatcher.group();
            Log.d(NetConfig.TAG, "term: " + term_option);
        }
        String term = null;//todo 学年：2018-2019
        regex = "\\d{4}-\\d{4}";
        Matcher termMatcher = Pattern.compile(regex).matcher(term_option);
        while (termMatcher.find()) {
            term = termMatcher.group();
            Log.d(NetConfig.TAG, "term: " + term + "学年");
        }

//<table id="Table1" class="blacktab" bordercolor="Black" border="0" width="100%">
        String table; //todo 表格里的数据
        regex = "<table id=\"Table1\".*?>[\\s\\S]*<\\/table>";
        Matcher tableMatcher = Pattern.compile(regex).matcher(span);
        if (tableMatcher.find()) {
            table = tableMatcher.group();
            Log.d("tabletable", "table: " + table);
        }

        Document document = Jsoup.parse(html);
        Element table1 = document.getElementById("Table1");
        Log.d("FDSADFS", "table1: " + table1.text());
        Elements tr = table1.getElementsByTag("tr"); //所有课程的集合

        List<List<String>> lines = new ArrayList<>();
        for (int j = 0; j < tr.size(); j++) {
            Element element = tr.get(j); //某一行课程
            Log.d("fsadfdsfionbv", "element: " + element.text());
            List<String> lessons = new ArrayList<>();
            for (int k = 0; k < element.childNodeSize() - 2; k++) {
                Element child = element.child(k);//某一课程
                String text = child.text();
                Log.d("dfdsifsnverop932", "child.text(): " + text);
                if (!text.equals("")) {
                    lessons.add(text);
                }
            }
            lines.add(lessons);
        }

        List<List<String>> effectiveLines = new ArrayList<>();  //有效的行课程数据  即 有效的课程信息 的集合
        for (List<String> list : lines) {
            List<String> effectiveLessons = new ArrayList<>();  //有效的课程信息
            for (String a : list) {
                String firstByte = a.split("")[1];
                Log.d("cdg44443", "firstByte： " + firstByte);
                if (!firstByte.equals("时") && !firstByte.equals("星") && !firstByte.equals("早") &&
                        !firstByte.equals("上") && !firstByte.equals("下") && !firstByte.equals("晚") && !firstByte.equals("第")) {
                    effectiveLessons.add(a);
                    Log.d("ojmoilkn ", "a: " + a);
                }
//                Log.d("get", "child.text(): " + a);
            }
            effectiveLines.add(effectiveLessons);
        }

        Log.d("fsafer2322", "getHtml: " + effectiveLines.size());
        List<String> electives = new ArrayList<>();
        List<Course_un_inited> courseList = new ArrayList<>();
        for (List<String> list : effectiveLines) {
            for (String s : list) {
                Log.d("fsda", "ddddd: ");
                Log.d("543543fdffffb43", "course: " + s);

                //判断是否是选修课 选出选修课集合       旅游摄影 {第1-16周|2节/周} 刘啸/王建军 综实B202【 】
                String regexx = "\\{.*\\|(?!单)(?!双).*?\\}";
                Matcher electiveMatcher = Pattern.compile(regexx).matcher(s);
                if (electiveMatcher.find()) {
                    electives.add(s);
                    Log.d("sgdfgfdhdfhhhhhh", "elective: " + s);

                    //调试
                    String elective = electiveMatcher.group();
                    Log.d("elective", "elective: " + elective);
                } else {
                    //选出课名
                    String[] nameSplit = s.split(" ");
                    String name = nameSplit[0];
                    Log.d("namename", "name: " + name);

                    //选出周几
                    String[] weekSplit = s.split(" ");
                    String weekName = nameSplit[1].substring(0, 2);
                    Log.d("weekName", "weekName:" + weekName);
//

                    //选出第几周第几节
                    Pattern number = Pattern.compile("\\d+");
                    Matcher week = Pattern.compile("\\{第.*?周").matcher(s);
                    List<String> weekNum = new ArrayList<>();
                    if (week.find()) {
                        Log.d("fsdfsdbbb", "week.group(): " + week.group());
                        Matcher weekMatcher = number.matcher(week.group());
                        while (weekMatcher.find()) {
                            weekNum.add(weekMatcher.group());
                        }
                    }
                    Matcher clazz = Pattern.compile("第.*?节").matcher(s);
                    List<String> clazzNum = new ArrayList<>();
                    if (clazz.find()) {
                        Matcher clazzMatcher = number.matcher(clazz.group());
                        while (clazzMatcher.find()) {
                            clazzNum.add(clazzMatcher.group());
                        }
                    }

                    Log.d("get", "child.text(): " + s);
                    String[] split = s.split(" ");  //对某节课程信息经行切片 “人工智能概论 周四第8,9节{第1-16周} 马楠/李佳洪/李德毅 综实A206”

                    //选出教师
                    String teacher = nameSplit[2];
                    Log.d("iiiiiiiiinnjknj", "getHtml: " + teacher);

                    //选出教室地点
                    String site = nameSplit[3];
                    Log.d("fsdfsfrewwewewe", "getHtml: " + site);

                    int termYear = Integer.parseInt(term.split("-")[0]);
                    int weekNumber = CommonUilts.getWeekNumber(weekName);
                    Log.d("agafsafs", "termYear: " + termYear);
                    Log.d("agafsafs", "weekNumber: " + weekNumber);
                    String startWeek = weekNum.get(0);
                    String endWeek = weekNum.get(1);
                    Log.d("ggfgfgfgfgfgf", "startWeek:" + startWeek + " endWeek: " + endWeek);
                    String startLesson = clazzNum.get(0);
                    String endLesson = "";
                    if (clazzNum.size() > 1) {
                        endLesson = clazzNum.get(1);
                    } else {
                        endLesson = clazzNum.get(0);

                    }
                    String weekStr = "";
                    for (int i = Integer.parseInt(startWeek); i <= Integer.parseInt(endWeek); i++) {
                        weekStr += i + ",";
                    }
                    Log.d("kokokokok", "weekStr: " + weekStr);
                    Log.d("iuiuububj", "startLesson: " + startLesson + "  endLesson：" + endLesson);
                    Course_un_inited course = new Course_un_inited();
                    course.setName(name);
                    course.setSite(site);
                    course.setTerm(termYear);
                    course.setWeek(weekNumber);
                    course.setWeek0fClass(weekStr);
                    course.setTeacher(teacher);
                    course.setStartLesson(Integer.parseInt(startLesson));
                    course.setEndLesson(Integer.parseInt(endLesson));
                    courseList.add(course);
                    Log.d("43242rert", "name:" + name + " site:" + site + " termYear:" + termYear + " weekNumber:" + weekNumber + " weekStr:" + weekStr + " teacher:" + teacher + " startLesson:" + startLesson + " endLesson：" + endLesson);
                }
            }
        }

        return courseList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            ViewGroup parent = (ViewGroup) webview.getParent();
            if (parent != null) {
                parent.removeView(webview);
            }
            webview.removeAllViews();
            webview.stopLoading();
            webview.setWebChromeClient(null);
            webview.setWebViewClient(null);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                return true;
                //退出网页
            } else {
                CommonUilts.thisToActivity(MainActivityy.class);
                finish();
            }
        }
        return false;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        mProgressBar.setVisibility(View.VISIBLE);
        WebSettings ws = webview.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webview.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        webview.setWebChromeClient(mWebChromeClient);
        // 与js交互
        webview.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        webview.setWebViewClient(new MyWebViewClient(this));

    }

    public void getInitData() {
        if (getIntent() != null) {//通过getIntent()方法获取intent携带的参数   键值对的形式
//            mIsMovie = getIntent().getBooleanExtra("mIsMovie", false);
            mUrl = getIntent().getStringExtra("mUrl");
        }
    }

    /**
     * 打开网页:
     *
     * @param mContext 上下文
     * @param mUrl     要加载的网页url
     * @param mIsMovie 是否是视频链接(视频链接布局不一致)
     */
    public static void loadUrl(Context mContext, String mUrl, boolean mIsMovie) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("mUrl", mUrl);
        intent.putExtra("mIsMovie", mIsMovie);
        mContext.startActivity(intent);
    }


    @Override
    public void hindProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void showWebView() {
        webview.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        webview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startProgress(int newProgress) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(newProgress);
        if (newProgress == 100) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        webview.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的<li>节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        webview.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"li\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");

        /**传应用内的数据给html，方便html处理*/
        // 无参数调用
        webview.loadUrl("javascript:javacalljs()");
        // 传递参数调用
        webview.loadUrl("javascript:javacalljswithargs('" + "android传入到网页里的数据，有参" + "')");
//        webView.loadUrl("javascript:document.getElementsByTagName('html')[0].innerHTML");


    }

    @Override
    public void fullViewAddView(View view) {

    }

    @Override
    public void showVideoFullView() {

    }

    @Override
    public void hindVideoFullView() {

    }


//    public FrameLayout getVideoFullView() {
//        return videoFullView;
//    }
}
