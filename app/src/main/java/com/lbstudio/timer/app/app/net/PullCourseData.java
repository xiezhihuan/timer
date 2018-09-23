package com.lbstudio.timer.app.app.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.javabean.Respond_dataArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class PullCourseData {
    public PullCourseData(String userId, String token, final SuccessCallback successCallback, final FailCallback failCallback) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Authorization", userId + "_" + token);
        Log.d(NetConfig.TAG, "userId: " + userId + "   token" + token);

        String url = NetConfig.PULLCOURSE_URL + userId;

        client.get(url, new AsyncHttpResponseHandler() {

            private int statusCode = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(NetConfig.TAG, "onSuccess: " + new String(bytes));

                statusCode = NetTool.getStatusCode(bytes);
                if (statusCode != NetConfig.SUCCESS) {
                    failCallback.onFail(statusCode);
                    return;
                }

                String s = new String(bytes);
                Log.d("ffsfsf", "onSuccess: " + s);

                Respond_dataArray respond_dataArray = JSON.parseObject(s, Respond_dataArray.class);
                String[] data = respond_dataArray.getData();
                for (int d = 0; d < data.length; i++) {
                    String datum = data[d];
                    Course course = JSON.parseObject(datum, Course.class);
                    course.setStatus(9);
                    course.save();
                }
                successCallback.onSuccess();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(NetConfig.TAG, "onFailure: " + new String(bytes));
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
