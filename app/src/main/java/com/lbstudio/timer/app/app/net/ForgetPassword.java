package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Respond_plan;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ForgetPassword {
    public ForgetPassword(Context context,String email, String password, String YZcode, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();

        //配置json参数
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NetConfig.KEY_ACOUNT_HTTP, email);
            jsonObject.put(NetConfig.KEY_PASSWORD_HTTP, password);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(NetConfig.TAG, "JSONException: ");
        }
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(NetConfig.TAG, "UnsupportedEncodingException: ");
        }

        String url=NetConfig.FORGETPWD_URL+YZcode;
        client.put(context, url, entity, "application/json", new AsyncHttpResponseHandler() {

            private int statusCode1=NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d("debug", "onSuccess: "+new String(bytes));
                statusCode1 = NetTool.getStatusCode(bytes);
                if (statusCode1!=NetConfig.SUCCESS){
                    failCallBack.onFail(statusCode1);
                    return;
                }
                successCallBack.onSuccess();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Respond_plan respond = JSON.parseObject(new String(bytes), Respond_plan.class);
                int code = respond.getCode();
                if (!String.valueOf(code).isEmpty()) {
                    statusCode1 = code;
                }
                failCallBack.onFail(statusCode1);
            }
        });
    }

    public static interface SuccessCallBack{
        void onSuccess();
    }

    public static interface FailCallBack{
        void onFail(int status);
    }
}
