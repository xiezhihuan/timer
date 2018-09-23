package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Respond_plan;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class GetRegisterCode {
    public GetRegisterCode(final Context mContext, String email, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        final AsyncHttpClient client = new AsyncHttpClient();
        String url = NetConfig.REGISTER_CODE_URL + email;
        Log.d("fsdfsfbbbb", "GetRegisterCode: "+url);
        client.get(url, new AsyncHttpResponseHandler() {

            private int statusCode = NetConfig.NOT_NETWORK;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d("fsdfadsfas", "onSuccess: "+new String(bytes));
                Respond_plan respond = JSON.parseObject(new String(bytes), Respond_plan.class);
                statusCode = respond.getCode();
                Log.d("fsdafs", "onSuccess: "+statusCode);
                if (statusCode == NetConfig.SUCCESS){
                    successCallBack.onSuccess();
                } else {
                    failCallBack.onFail(statusCode);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Respond_plan respond = JSON.parseObject(new String(bytes), Respond_plan.class);
                int code = respond.getCode();
                Log.d("fsdafs", "onf: "+code+" statusCode:"+statusCode);

                if (!String.valueOf(code).isEmpty()) {
                    statusCode = code;
                }
                failCallBack.onFail(statusCode);
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
