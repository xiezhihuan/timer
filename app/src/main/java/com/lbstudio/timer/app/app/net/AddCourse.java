package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddCourse {
    public AddCourse(Context context,int userId, String token, String name, String site, int term, String week0fClass,
                     int week, int startLesson, int endLesson, String teacher, int date,
                     final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + userId + "_" + token);

        Log.d("gbobkgk", "AddCourse: "+userId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NetConfig.KEY_USERID, userId);
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

        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(NetConfig.TAG, "UnsupportedEncodingException: ");
        }

        client.post(context,NetConfig.ADD_COURSE_URL, entity, "application/json",new AsyncHttpResponseHandler() {

            private int statusCode = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String s = new String(bytes);
                Log.d("debug", "onSuccess: "+s);
                statusCode = NetTool.getStatusCode(bytes);
                if (statusCode != NetConfig.SUCCESS) {
                    failCallBack.onFail(statusCode);
                    return;
                }
                //todo 解析出courseId、status
                int courseId = 0;
                int status = 0;
                successCallBack.onSuccess(courseId, status);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String s = new String(bytes);
                Log.d("debug", "onSuccess: "+s);
                failCallBack.onFail(statusCode);
            }
        });
    }

    public static interface SuccessCallBack {
        void onSuccess(int courseId, int status);
    }

    public static interface FailCallBack {
        void onFail(int status);
    }
}
