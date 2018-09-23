package com.lbstudio.timer.app.app.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class DelectPlan {
    public DelectPlan(int userId, String token, int planId, final SuccessCallback successCallback, final FailCallback failCallback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + userId + "_" + token);

        String url = NetConfig.DELECT_PLAN_URL + "?id=" + userId + "&planId=" + planId;
        client.delete(url, new AsyncHttpResponseHandler() {

            private int statusCode = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                statusCode = NetTool.getStatusCode(bytes);
                if (statusCode != NetConfig.SUCCESS) {
                    failCallback.onFail(statusCode);
                    return;
                }
                successCallback.onSuccess();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                int code = NetTool.getStatusCode(bytes);
                if (!String.valueOf(code).isEmpty()){
                    statusCode=code;
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
