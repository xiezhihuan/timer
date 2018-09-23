package com.lbstudio.timer.app.app.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.activity.Plan_creat_Activity;
import com.lbstudio.timer.app.app.activity.RegisterActivity;
import com.lbstudio.timer.app.app.javabean.Respond;
import com.lbstudio.timer.app.app.util.CommonUilts;

public class NetTool {

    public static int getStatusCode(byte[] bytes) {
        String s1 = new String(bytes);
        Log.d("debug", "onSuccess: " + s1);
        Respond respond = JSON.parseObject(s1, Respond.class);
        return respond.getCode();
    }

    public static void chackFailStatus(Context context, int statusCode) {
        switch (statusCode) {
            case NetConfig.NOT_NETWORK: //-1  网络异常
                CommonUilts.showDialog(context, context.getString(R.string.not_network)+"("+statusCode+")");
                break;
            case NetConfig.YZCODE_ERR: // 103
                CommonUilts.showDialog(context, context.getString(R.string.YZCodeErr)+"("+statusCode+")");
                break;
            case NetConfig.HAVE_REGISTERED:  //101
                CommonUilts.showDialog(context, context.getString(R.string.haveRegistered)+"("+statusCode+")");
                break;
            case NetConfig.ERRO_PARAMS: //400 参数异常
                CommonUilts.showDialog(context, context.getString(R.string.erro_params)+"("+statusCode+")");
                break;
            case NetConfig.ERRO_REQUEST_TOMORE: // 104 请求过于频繁
                CommonUilts.showDialog(context, context.getString(R.string.erro_request_tomore)+"("+statusCode+")");
                break;
            case NetConfig.ERRO_YZCODE_TOMORE: //105 验证码下发次数超过当天限制
                CommonUilts.showDialog(context, context.getString(R.string.erro_yzcode_tomore)+"("+statusCode+")");
                break;
            case NetConfig.FAIL: //500  处理删除 服务器内部错误
                CommonUilts.showDialog(context, context.getString(R.string.fail)+"("+statusCode+")");
                break;
            case NetConfig.ERR_EMAIL_PWD://102 邮箱或密码错误！
                CommonUilts.showDialog(context, context.getString(R.string.eer_email_pwd)+"("+statusCode+")");
                break;
            case NetConfig.TOKEN_INVALID:
                //token 失效  自动重新登录
                reLogin(context);
                break;
            default:
                CommonUilts.showDialog(context, context.getString(R.string.unknownErr)+"("+statusCode+")");
                break;
        }
    }

    /**
     * token 过期
     */
    public static void reLogin(Context context){
        String account = NetConfig.getAccount(context);
        String password = NetConfig.getPassword(context);
        new Login(account, password, new Login.SuccessCallBack() {
            @Override
            public void onSuccess(int userId, String token) {

            }
        }, new Login.FailCallBack() {
            @Override
            public void onFail(int statusCode) {

            }
        });
    }
    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {//true是链接，false是没链接
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
