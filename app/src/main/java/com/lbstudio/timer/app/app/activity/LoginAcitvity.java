package com.lbstudio.timer.app.app.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lbstudio.timer.app.MainActivity;
import com.lbstudio.timer.app.MainActivityy;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.browser.utils.CheckNetwork;
import com.lbstudio.timer.app.app.net.Login;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.NetTool;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.util.MD5Tool;
import com.lbstudio.timer.app.base.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginAcitvity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etemail;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_register)
    TextView btRegister;
    @BindView(R.id.bt_forgetPwd)
    TextView btForgetPwd;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void init() {
        etemail.setText(NetConfig.getAccount(this));
        etPwd.setText(NetConfig.getPassword(this));
//        if (Build.VERSION.SDK_INT >= 23) {
//
//        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.bt_login)
    public void login(View view) {

        final String email = etemail.getText().toString().trim();
        final String password = etPwd.getText().toString().trim();
        String password_md5 = MD5Tool.md5(password + NetConfig.SALT);

        //校验账号密码
        if (check(email, password)) {
            CommonUilts.showDialog(this, getString(R.string.err_email_Or_Password_null));
            return;
        }
//        if (!CheckNetwork.isWifiConnected(this)){
//            发起登录请求
            final ProgressDialog processDialog = CommonUilts.getProcessDialog(this, "正在登录...");
            processDialog.show();
            new Login(email, password_md5, new Login.SuccessCallBack() {
                @Override
                public void onSuccess(int userId, String token) {
                    //缓存token
                    NetConfig.cacheToken(LoginAcitvity.this, token);
                    //缓存userId
                    NetConfig.cacheUserId(LoginAcitvity.this, String.valueOf(userId));
                    //缓存账号
                    NetConfig.cacheEmail(LoginAcitvity.this, email);
                    //缓存密码
                    NetConfig.cachepassword(LoginAcitvity.this, password);
                    //新用户登录，需要从服务器获取数据
                    CommonUilts.cacheShouldGetData(LoginAcitvity.this,true);
                    processDialog.dismiss();
                    //跳转到主页
                    CommonUilts.thisToActivity(MainActivityy.class);
                    finish();
                }
            }, new Login.FailCallBack() {
                @Override
                public void onFail(int status) {
                    NetTool.chackFailStatus(LoginAcitvity.this, status);
                    processDialog.dismiss();
                }
            });
//        }else {
//            CommonUilts.showDialog(this,"不用允许WiFi登录");
//        }

    }

    public void setBtLogin() {

    }


    @OnClick(R.id.bt_register)//跳转到注册页
    public void register(View view) {
        //跳转到注册页
        CommonUilts.thisToActivity(RegisterActivity.class);
    }

    @OnClick(R.id.bt_forgetPwd) //跳转到忘记密码页
    public void forgetPassword(View view) {
        //跳转到忘记密码页
        CommonUilts.thisToActivity(ForgetActivity.class);
    }

    /**
     * 校验账号密码是否为空
     *
     * @param phone    手机号
     * @param password 密码
     */
    private boolean check(String phone, String password) {
        return TextUtils.isEmpty(phone) || TextUtils.isEmpty(password);
    }

    /**
     * 带参跳转到首页
     *
     * @param jsonObject 请求的返回结果
     */
    private void login2mainActivity(JSONObject jsonObject) {
        Intent intent = new Intent(this, MainActivity.class);
        String uId = null;
        String uRegister = null;
        String uName = null;
        String uSex = null;
        String uPhone = null;
        try {
            uId = jsonObject.getString("uId");
            uRegister = jsonObject.getString("uRegister");
            uName = jsonObject.getString("uName");
            uSex = jsonObject.getString("uSex");
            uPhone = jsonObject.getString("uPhone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("uId", uId);
        intent.putExtra("uRegister", uRegister);
        intent.putExtra("uName", uName);
        intent.putExtra("uSex", uSex);
        intent.putExtra("uPhone", uPhone);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
