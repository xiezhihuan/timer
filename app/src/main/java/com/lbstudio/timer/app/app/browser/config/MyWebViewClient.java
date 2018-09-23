package com.lbstudio.timer.app.app.browser.config;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lbstudio.timer.app.app.browser.activity.WebViewActivity;
import com.lbstudio.timer.app.app.browser.utils.CheckNetwork;


public class MyWebViewClient extends WebViewClient {
    private String KBUrl = "";
    private IWebPageView mIWebPageView;
    private WebViewActivity mActivity;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        mActivity = (WebViewActivity) mIWebPageView;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("get", "shouldOverrideUrlLoading   --url: " + url);

        if (TextUtils.isEmpty(url)) {
            return false;
        }

        if (url.startsWith("http:") || url.startsWith("https:")) {
            // 可能有提示下载Apk文件
            if (url.contains(".apk")) {
//                handleOtherwise(mActivity, url);
                return true;
            }
            return false;
        }

        return true;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        if (!CheckNetwork.isNetworkConnected(mActivity)) {
            mIWebPageView.hindProgressBar();
        }
        // html加载完成之后，添加监听图片的点击js函数  6
        mIWebPageView.addImageClickListener();

        Log.d("get", "onPageFinished   --url: " + url);

        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.d("get", "onLoadResource   --url: " + url);
//        String path = "http://jwxt.buu.edu.cn/xskscx.aspx?xh=2017220330001&xm=%D0%BB%D6%B2%BB%C0&gnmkdm=N121604";
//        if (url.equals(path)) {
//            view.evaluateJavascript("GetMc('学生个人课表')", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    Log.d("get", "学生个人课表 : " + value);
//                }
//            });
//        }
        super.onLoadResource(view, url);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        Log.d("get", "shouldInterceptRequest   --url: " + request.getUrl());
        Log.d("get", "shouldInterceptRequest   --url: " + request.getRequestHeaders());

        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("get", "onPageStarted   --url: " + url);

        super.onPageStarted(view, url, favicon);
    }

    // 视频全屏播放按返回页面被放大的问题
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            view.setInitialScale((int) (oldScale / newScale * 100)); //异常放大，缩回去。
        }
    }



    private void startActivity(String url) {
        try {

            // 用于DeepLink测试
            if (url.startsWith("will://")) {
                Uri uri = Uri.parse(url);
                Log.e("---------scheme", uri.getScheme() + "；host: " + uri.getHost() + "；Id: " + uri.getPathSegments().get(0));
            }

            Intent intent1 = new Intent();
            intent1.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent1.setData(uri);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
