package com.lbstudio.timer.app.app.browser.config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.lbstudio.timer.app.MyApplication;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.browser.activity.WebViewActivity;
import com.lbstudio.timer.app.app.javabean.Course_un_inited;
import com.lbstudio.timer.app.app.net.AddCourse;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.NetTool;
import com.lbstudio.timer.app.app.util.CommonUilts;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * js通信接口
 */
public class ImageClickInterface {
    private Context context;

    public ImageClickInterface(Context context) {
        this.context = context;
    }

    /**
     * 前端代码嵌入js：
     * imageClick 名应和js函数方法名一致
     *
     * @param src      图片的链接
     * @param has_link img 节点下的has_link属性值(一般是没有的)
     */
    @JavascriptInterface
    public void imageClick(String src, String has_link) {
        Toast.makeText(context, "----点击了图片", Toast.LENGTH_LONG).show();
        Log.e("src", src);
        Log.e("hasLink", has_link);
    }

    /**
     * 前端代码嵌入js
     * 遍历<li>节点
     *
     * @param type    <li>节点下type属性的值
     * @param item_pk item_pk属性的值
     */
    @JavascriptInterface
    public void textClick(String type, String item_pk) {
        Log.e("type", type);
        Log.e("item_pk", item_pk);
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
            Toast.makeText(context, "----点击了文字", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 网页使用的js，方法无参数
     */
    @JavascriptInterface
    public void startFunction() {
        Toast.makeText(context, "----无参", Toast.LENGTH_LONG).show();
    }

    /**
     * 网页使用的js，方法有参数，且参数名为data
     *
     * @param data 网页js里的参数名
     */
    @JavascriptInterface
    public void startFunction(String data) {
        Toast.makeText(context, "----有参：" + data, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void getHtml(String html) {
//        Log.d("getHtml", " Html: "+html);
        getData("getHcccccccbmtml:", " getHtml: " + html);

        //http://jwxt.buu.edu.cn/xskbcx.aspx?xh=2017220330001&xm=%D0%BB%D6%B2%BB%C0&gnmkdm=N121603
        try {
            //获取xh、xm、gnmkdm
            Map<String, String> urlParams = getUrlParams(html);
            //缓存参数
            CommonUilts.cacheUrlParams(context, urlParams);

        } catch (Exception e) {
            Log.e("debug", "getHtml——1: "+e.getMessage());
        }

    }

    @JavascriptInterface
    public void getKbHtml(String html) {

       //判断是否已缓存有课表html
        isCacheHtml();

        //缓存
        CommonUilts.cacheHtml(context, html);

        //读取
        String cache_html2 = CommonUilts.getHtml(context);
        try {
            //检查是否是需要的html
            check(cache_html2);
        } catch (NullPointerException ex) {
            CommonUilts.showDialog_finish(context, "课表获取失败"); //不是需要的html
            return;
        }
        CommonUilts.showDialog_finish(context, "课表获取成功");
    }

    private void isCacheHtml() {
        //从缓存获得html
        String cache_html = CommonUilts.getHtml(context);
        Log.d("jjjiii", "getHtml: " + cache_html);
        if (cache_html != null) {//已有缓存html
            try {
                //尝试解析，判断是否为需要的html
                check(cache_html);

            } catch (NullPointerException ex) {
                CommonUilts.showDialog_finish(context, "课表获取失败");
                return;
            }
            CommonUilts.showDialog_finish(context, "课表已获取");
            return;
        }
    }

    private Map<String, String> getUrlParams(String html) {
        String regex = "<li class=\"top\"><a href=\"#a\" class=\"top_link\"><span class=\"down\"> 信息查询</span>.*<ul class=\"sub\">.*(?:</ul>){1}";
        String group = null;
        Matcher matcher = Pattern.compile(regex).matcher(html);
        while (matcher.find()) {
            group = matcher.group();
            Log.d("jkininjknjknkj", "group: " + group);
        }

        regex = "<li><a href=.*>专业推荐课表查询</a></li>";
        String group1 = null;
        Matcher matcher1 = Pattern.compile(regex).matcher(group);
        while (matcher1.find()) {
            group1 = matcher1.group();
            Log.d("ffdfnfjnm", "group1: " + group1);
        }
// <li><a href="tjkbcx.aspx?xh=2017220330001&amp;xm=谢植焕&amp;gnmkdm=N121601" target="zhuti" onclick="GetMc('专业推荐课表查询');">专业推荐课表查询</a></li>
        regex = "xh=\\d+&amp;xm=.*&amp;gnmkdm=.*\" target=";
        String group2 = null;
//        webview.loadUrl("http://jwxt.buu.edu.cn/xskbcx.aspx?xh=2017220330001&xm=%D0%BB%D6%B2%BB%C0&gnmkdm=N121603");
        Matcher matcher2 = Pattern.compile(regex).matcher(group1);
        while (matcher2.find()) {
            group2 = matcher2.group();
            Log.d("njnojfnld", "group2: " + group2);
        }

//        xh=2017220330001&amp;xm=谢植焕&amp;gnmkdm=N121601" target=
        regex = "\\d+";
        String number = null;
        Matcher numberMatcher = Pattern.compile(regex).matcher(group2);
        if (numberMatcher.find()) {
            number = numberMatcher.group();
            Log.d("fboklnnlo", "number: " + number);
        }

        regex = "xm=.{1,15}&";
        String name_str = null;
        Matcher nameStrMatcher = Pattern.compile(regex).matcher(group2);
        if (nameStrMatcher.find()) {
            name_str = nameStrMatcher.group();
            Log.d("gfgkfnbkln", "name: " + name_str);
        }

        regex = "[^(xm=)].{1,10}[^&]";
        String name = null;
        Matcher nameMatcher = Pattern.compile(regex).matcher(name_str);
        if (nameMatcher.find()) {
            name = nameMatcher.group();
            Log.d("gfdgnkjbnkl", "name: " + name);
        }
        regex = "\\w{1,3}\\d{4,8}";
        String regexx = "gnmkdm=.*";
        String gnmkdm = null;
        Matcher matcher3 = Pattern.compile(regexx).matcher(group2);
        if (matcher3.find()) {
            Matcher gnmkdmMatcher = Pattern.compile(regex).matcher(matcher3.group());
            if (gnmkdmMatcher.find()) {
                gnmkdm = gnmkdmMatcher.group();
                Log.d("nfjnvdnljk", "gnmkdm: " + gnmkdm);
            }
        }

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("number", number);
        urlParams.put("name", name);
        urlParams.put("gnmkdm", gnmkdm);

        return urlParams;
    }

    private void check(String html) {
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
    }


    public static void getData(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }
}
