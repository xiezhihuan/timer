package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Course;
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

public class SyncCourse {
    public SyncCourse(Context context, String userId, String token, List<Course> lists,
                      final SuccessCallBack successCallBack, final FailCallBack failCallBack) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + userId + "_" + token);

        JSONArray jsonArray = new JSONArray();
        for (Course course : lists) {
            String userId1 = course.getUserId();
            String name = course.getName();
            String site = course.getSite();
            int term = course.getTerm();
            int week = course.getWeek();
            String week0fClass = course.getWeek0fClass();
            int startLesson = course.getStartLesson();
            int endLesson = course.getEndLesson();
            String teacher = course.getTeacher();
            int date = course.getDate();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(NetConfig.KEY_USERID, userId1);
                jsonObject.put(NetConfig.KEY_COURSE_NAME, name);
                jsonObject.put(NetConfig.KEY_COURSE_SITE, site);
                jsonObject.put(NetConfig.KEY_COURSE_TERM, term);
                jsonObject.put(NetConfig.KEY_COURSE_WEEKOFCLASS, week0fClass);
                jsonObject.put(NetConfig.KEY_COURSE_WEEK, week);
                jsonObject.put(NetConfig.KEY_COURSE_STARTLESSON, startLesson);
                jsonObject.put(NetConfig.KEY_COURSE_ENDLESSON, endLesson);
                jsonObject.put(NetConfig.KEY_COURSE_TEACHER, teacher);
                jsonObject.put(NetConfig.KEY_COURSE_DATE, date);
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

        client.post(context, NetConfig.SYNC_COURSE_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            private int statusCode1 = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(NetConfig.TAG, "nhnhhg: " + new String(bytes));
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
