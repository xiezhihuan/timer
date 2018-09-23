package com.lbstudio.timer.app.app.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Data;
import com.lbstudio.timer.app.app.javabean.Respond;
import com.lbstudio.timer.app.app.javabean.Respond_plan;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Login {

    public Login(String email, String password, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        //登录路径
        String url=NetConfig.LOGIN_URL+"?email="+email+"&password="+password;
        client.post(url, null, new AsyncHttpResponseHandler() {

            private int statusCode=NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String s1 = new String(bytes);
                Log.d("sfdfssdfsdf", "onSuccess: "+s1);
                Respond respond_ = JSON.parseObject(s1, Respond.class);
                statusCode = respond_.getCode();
                if (statusCode !=NetConfig.SUCCESS){
                    failCallBack.onFail(statusCode);
                    return;
                }
                Data data = respond_.getData();
                int userId = data.getToken().getUserId();
                String token = data.getToken().getToken();
                successCallBack.onSuccess(userId,token);
            }

            @Override
            public void onFailure(int status, Header[] headers, byte[] bytes, Throwable throwable) {
                if (status!=0){
                    Respond_plan respond = JSON.parseObject(new String(bytes), Respond_plan.class);
                    int code = respond.getCode();
                    if (!String.valueOf(code).isEmpty()) {
                        statusCode = code;
                    }
                }
                failCallBack.onFail(statusCode);
            }
        });
    }

    public static interface SuccessCallBack{
       void onSuccess(int userId,String token);
    }

    public static interface FailCallBack{
        void onFail(int statusCode);
    }
}
