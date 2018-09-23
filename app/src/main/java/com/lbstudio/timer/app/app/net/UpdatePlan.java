package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.MainActivityy;
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

public class UpdatePlan {
    public UpdatePlan(Context context,int userId, String token, String title, String content, int date, int startTime,
                      int endTime,int realStartTime,int realEndTime,boolean isRegular, int planStatus,
                      final SuccessCallback successCallback, final FailCallback failCallback) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Authorization",userId+"_"+token);

        //配置json参数
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NetConfig.KEY_USERID, userId);
            jsonObject.put(NetConfig.KEY_PLAN_TITLE, title);
            jsonObject.put(NetConfig.KEY_PLAN_CONTENT, content);
            jsonObject.put(NetConfig.KEY_PLAN_DATE, date);
            jsonObject.put(NetConfig.KEY_PLAN_STARTTIME, startTime);
            jsonObject.put(NetConfig.KEY_PLAN_ENDTIME, endTime);
            jsonObject.put(NetConfig.KEY_PLAN_REALSTART_TIME, realStartTime);
            jsonObject.put(NetConfig.KEY_PLAN_REALEND_TIME, realEndTime);
            jsonObject.put(NetConfig.KEY_PLAN_ISREGULAR, isRegular);
            jsonObject.put(NetConfig.KEY_PLAN_PLANSTATUS, planStatus);

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
        Log.d(NetConfig.TAG, "222222222222222222: ");

//        client.put()
        client.put(context, NetConfig.UPDATE_PLAN_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            private int statusCode=NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(NetConfig.TAG, "onSuccess: " + new String(bytes));
                statusCode = NetTool.getStatusCode(bytes);
                if (statusCode!=NetConfig.SUCCESS){
                    failCallback.onFail(statusCode);
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
                    statusCode = code;
                }
                failCallback.onFail(statusCode);
            }
        });
    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail(int status);
    }




}
