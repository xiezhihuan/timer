package com.lbstudio.timer.app.app.browser.config;

import android.webkit.WebView;

public class Click_getHtml {

    public Click_getHtml(WebView webView, GetHtml getHtml) {
        webView.loadUrl("javascript:window.setTimeout(" +
                "function(){ " +
                "injectedObject.getHtml('<head>'+ document.getElementsByTagName('html')[0].innerHTML+'</head>'); " +
                "}" +
                ",3000);");
    }

    public static interface GetHtml {
        void getHtml(WebView webView);
    }
}
