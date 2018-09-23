package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class BaseHttp {

    public BaseHttp(Context context, int userId, String token, String title, String content, int date, int startTime,
                    int endTime, boolean isRegular, int planStatus, int status,
                    final UpdatePlan.SuccessCallback successCallback, final UpdatePlan.FailCallback failCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization",userId+"_"+token);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NetConfig.KEY_USERID, userId);
            jsonObject.put(NetConfig.KEY_PLAN_TITLE, title);
            jsonObject.put(NetConfig.KEY_PLAN_CONTENT, content);
            jsonObject.put(NetConfig.KEY_PLAN_DATE, date);
            jsonObject.put(NetConfig.KEY_PLAN_STARTTIME, startTime);
            jsonObject.put(NetConfig.KEY_PLAN_ENDTIME, endTime);
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

    }
}
