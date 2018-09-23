package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Data;
import com.lbstudio.timer.app.app.javabean.Data_plan;
import com.lbstudio.timer.app.app.javabean.Respond;
import com.lbstudio.timer.app.app.javabean.Respond_creatPlan;
import com.lbstudio.timer.app.app.javabean.Respond_plan;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddPlan {
    public AddPlan(Context context, String userId, String token, String planTitle, String planContent, int planDate, int planStartTime,
                   int planEndTime, int planStatus, boolean isRegular,
                   final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("Authorization", "Bearer " + userId + "_" + token);
        Log.d(NetConfig.TAG, "userId: " + userId + "   token" + token);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NetConfig.KEY_USERID, userId);
            jsonObject.put(NetConfig.KEY_PLAN_TITLE, planTitle);
            jsonObject.put(NetConfig.KEY_PLAN_CONTENT, planContent);
            jsonObject.put(NetConfig.KEY_PLAN_DATE, planDate);
            jsonObject.put(NetConfig.KEY_PLAN_STARTTIME, planStartTime);
            jsonObject.put(NetConfig.KEY_PLAN_ENDTIME, planEndTime);
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


        client.post(context, NetConfig.ADDPLAN_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            private int statusCode = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String rsult = new String(bytes);
                Respond_plan respond = JSON.parseObject(rsult, Respond_plan.class);
                statusCode = respond.getCode();
                if (statusCode != NetConfig.SUCCESS) {
                    failCallBack.onFail(statusCode);
                    return;
                }

                //解析返回结果
                Data_plan data = respond.getData();
                int planId = data.getPlanId();

                successCallBack.onSuccess(planId);
            }

            @Override
            public void onFailure(int status, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d("afsfsfas", "onFailure: "+status);
                String rsult = new String(bytes);
                statusCode = status;
                if (status != NetConfig.TOKEN_INVALID) {
                    Respond_plan respond = JSON.parseObject(rsult, Respond_plan.class);
                    int code = respond.getCode();
                    if (!String.valueOf(code).isEmpty()) {
                        statusCode = code;
                    }
                }
                failCallBack.onFail(statusCode);
            }
        });
    }

    public static interface SuccessCallBack {
        void onSuccess(int planId);
    }

    public static interface FailCallBack {
        void onFail(int status);
    }
}
