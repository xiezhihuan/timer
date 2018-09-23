package com.lbstudio.timer.app.app.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class DelectCourse {
    public DelectCourse(int userId, String token, int courseId, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + userId + "_" + token);
        String url = NetConfig.DELECT_COURSE_URL + "?id=" + userId + "&courseId=" + courseId;
        client.delete(url, new AsyncHttpResponseHandler() {

            private int statusCode=NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                statusCode = NetTool.getStatusCode(bytes);
                if (statusCode!=NetConfig.SUCCESS){
                    failCallBack.onFail(statusCode);
                    return;
                }
                //todo 解析出courseId、status
                int courseId = 0;
                int status = 0;
                successCallBack.onSuccess(courseId,status);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                failCallBack.onFail(statusCode);
            }
        });
    }

    public static interface SuccessCallBack {
        void onSuccess(int courseId,int status);
    }

    public static interface FailCallBack{
        void onFail(int statusCode);
    }
}
