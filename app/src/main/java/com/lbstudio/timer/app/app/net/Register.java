package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Respond_plan;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Register {

    public Register(Context context, String email, String password, String YZcode,final SuccessCallback successCallback, final FailCallback failCallback) {
        AsyncHttpClient client = new AsyncHttpClient();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonObject.put(NetConfig.KEY_ACOUNT_HTTP, email);
            jsonObject.put(NetConfig.KEY_PASSWORD_HTTP, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url=NetConfig.REGISTER_URL+YZcode;
        client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {

            private int statusCode1=NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(NetConfig.TAG, "onSuccess: " + new String(bytes));
                statusCode1 = NetTool.getStatusCode(new String(bytes).toString().getBytes());
                if (statusCode1!=NetConfig.SUCCESS){
                    failCallback.onFail(statusCode1);
                    return;
                }
                successCallback.onSuccess();

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(NetConfig.TAG, "onFailure: " + new String(bytes));
                Respond_plan respond = JSON.parseObject(new String(bytes), Respond_plan.class);
                int code = respond.getCode();
                if (!String.valueOf(code).isEmpty()) {
                    statusCode1 = code;
                }
                failCallback.onFail(statusCode1);
            }
        });
    }




    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail(int statusCode);
    }
}

//          {
////        "data": null,
////        "msg": "Success!",
////        "code": 200,
////        "etxra": null
////        }