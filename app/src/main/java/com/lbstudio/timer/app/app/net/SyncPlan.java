package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.javabean.Respond_plan;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SyncPlan {
    public SyncPlan(Context context, String userId, String token, List<Plan> lists,
                    final SuccessCallBack successCallBack, final FailCallBack failCallBack) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + userId + "_" + token);

        JSONArray jsonArray = new JSONArray();
        for (Plan plan : lists) {
            String planTitle = plan.getPlanTitle();
            String content = plan.getPlanContent();
            int planDate = plan.getPlanDate();
            int startTime = plan.getPlanStartTime();
            int endTime = plan.getPlanEndTime();
            int planStatus = plan.getPlanStatus();
            int realStartTime = plan.getRealStartTime();
            int realEndTime = plan.getRealEndTime();
            boolean regular = plan.isRegular();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(NetConfig.KEY_PLAN_TITLE, planTitle);
                jsonObject.put(NetConfig.KEY_PLAN_CONTENT, content);
                jsonObject.put(NetConfig.KEY_PLAN_DATE, planDate);
                jsonObject.put(NetConfig.KEY_PLAN_STARTTIME, startTime);
                jsonObject.put(NetConfig.KEY_PLAN_ENDTIME, endTime);
                jsonObject.put(NetConfig.KEY_PLAN_PLANSTATUS, planStatus);
                jsonObject.put(NetConfig.KEY_PLAN_REALSTART_TIME, realStartTime);
                jsonObject.put(NetConfig.KEY_PLAN_REALEND_TIME, realEndTime);
                jsonObject.put(NetConfig.KEY_PLAN_REGULAR, regular);
                jsonObject.put(NetConfig.KEY_USERID, userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }


        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonArray.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(context, NetConfig.DELECT_SYNC_PLAN_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            private int statusCode1 = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(NetConfig.TAG, "onSuc000cess: " + new String(bytes));
                statusCode1 = NetTool.getStatusCode(new String(bytes).toString().getBytes());
                if (statusCode1 != NetConfig.SUCCESS) {
                    failCallBack.onFail(statusCode1);
                    return;
                }
                successCallBack.onSuccess();

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(NetConfig.TAG, "onFailure: " + new String(bytes));
                Respond_plan respond = JSON.parseObject(new String(bytes), Respond_plan.class);
                int code = respond.getCode();
                if (!String.valueOf(code).isEmpty()) {
                    statusCode1 = code;
                }
                failCallBack.onFail(statusCode1);
            }
        });
    }

    public static interface SuccessCallBack {
        void onSuccess();
    }

    public static interface FailCallBack {
        void onFail(int statusCode);
    }
}
