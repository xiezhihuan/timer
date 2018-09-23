package com.lbstudio.timer.app.app.net;

import android.content.Context;

public class GetData {
    public GetData(Context context, boolean shouldGetDate, String userId, String token, final SuccessCallback successCallback,
                   final FailCallBack failCallBack) {
        new PullPlanData(userId, token, new PullPlanData.SuccessCallback() {
            @Override
            public void onSuccess() {
                successCallback.onSuccess();
            }
        }, new PullPlanData.FailCallback() {
            @Override
            public void onFail(int status) {
                failCallBack.onFail(status);
            }
        });

        new PullCourseData(userId, token, new PullCourseData.SuccessCallback() {
            @Override
            public void onSuccess() {
                successCallback.onSuccess();
            }
        }, new PullCourseData.FailCallback() {
            @Override
            public void onFail(int status) {
                failCallBack.onFail(status);

            }
        });
    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallBack {
        void onFail(int status);
    }
}
