package com.lbstudio.timer.app.app.net;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class Test {
    public Test() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url=" http://47.94.227.99/Amazon/login";
        RequestParams params=new RequestParams();
        params.put("uPhone","21312331");
        params.put("uPassword","21312331");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String s = new String(bytes);
                Log.d("debug", "onSuccess: "+s);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String s = new String(bytes);
                Log.d("debug", "onSuccess: "+s);
            }
        });

    }
}
