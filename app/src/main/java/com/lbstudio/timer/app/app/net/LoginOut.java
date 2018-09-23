package com.lbstudio.timer.app.app.net;

import android.util.Log;

import com.lbstudio.timer.app.app.util.CommonUilts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginOut {

    public LoginOut(int userId, String token, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Authorization","Bearer "+userId+"_"+token);

        client.delete(NetConfig.LOGINOUT_URL, new AsyncHttpResponseHandler() {

            private int status=NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d("debug", "onSuccess: "+new String(bytes));
                status = NetTool.getStatusCode(bytes);
                if (status!=NetConfig.SUCCESS){
                    failCallBack.onFail(status);
                    return;
                }
                successCallBack.onSuccess();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d("debug", "onFailure: "+new String(bytes));
                failCallBack.onFail(status);
            }
        });

    }

    public static interface SuccessCallBack{
        void onSuccess();
    }

    public static interface FailCallBack{
        void onFail(int status);
    }

}
