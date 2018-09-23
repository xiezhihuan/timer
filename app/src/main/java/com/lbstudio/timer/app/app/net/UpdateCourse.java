package com.lbstudio.timer.app.app.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class UpdateCourse {
    public UpdateCourse(int userId, String token, String name,String site,int term,String weekOfClass,int week,int startLesson,
                        int endLesson,String teacher,int date,int courseId,
                        final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + userId + "_" + token);
        RequestParams params=new RequestParams();
        params.put(NetConfig.KEY_COURSE_NAME,name);
        params.put(NetConfig.KEY_COURSE_SITE,site);
        params.put(NetConfig.KEY_COURSE_TERM,term);
        params.put(NetConfig.KEY_COURSE_WEEKOFCLASS,weekOfClass);
        params.put(NetConfig.KEY_COURSE_WEEK,week);
        params.put(NetConfig.KEY_COURSE_STARTLESSON,startLesson);
        params.put(NetConfig.KEY_COURSE_ENDLESSON,endLesson);
        params.put(NetConfig.KEY_COURSE_TEACHER,teacher);
        params.put(NetConfig.KEY_COURSE_DATE,date);
        String url=NetConfig.UPDATE_COURSE_URL+"?id="+userId+"&courseId="+courseId;
        client.put(url, params, new AsyncHttpResponseHandler() {

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

    public static interface SuccessCallBack{
        void onSuccess(int courseId,int status);
    }

    public static interface FailCallBack{
        void onFail(int statusCode);
    }
}
