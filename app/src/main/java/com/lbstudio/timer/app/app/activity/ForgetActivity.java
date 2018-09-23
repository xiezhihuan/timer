package com.lbstudio.timer.app.app.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lbstudio.timer.app.app.javabean.Respond_getPasswordQuestion;
import com.lbstudio.timer.app.app.net.ForgetPassword;
import com.lbstudio.timer.app.app.net.GetForgetPwdCode;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.NetTool;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.util.MD5Tool;
import com.lbstudio.timer.app.app.util.ToastMassage;
import com.lbstudio.timer.app.base.BaseActivity;
import com.lbstudio.timer.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ForgetActivity extends BaseActivity {


    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_againPwd)
    EditText etAgainPwd;
    @BindView(R.id.YZ_code)
    EditText YZCode;
    @BindView(R.id.sendYzCode)
    Button sendYzCode;
    @BindView(R.id.Yz_line)
    LinearLayout YzLine;
    @BindView(R.id.bt_SendForForget)
    Button btSendForForget;
    @BindView(R.id.et_email)
    EditText etEmail;

    @Override
    protected void init() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }

    @OnClick(R.id.sendYzCode)
    public void getYzCode(View view) {
        String email = etEmail.getText().toString().trim();
        //校验邮箱格式是否正确
        if (checkEmail(email)) {
            CommonUilts.showDialog(this,getString(R.string.err_YZCode_null));
            return;
        }

        new GetForgetPwdCode(email, new GetForgetPwdCode.SuccessCallback() {
            @Override
            public void onSuccess() {
                CommonUilts.showDialog(ForgetActivity.this, getString(R.string.success_getREcode));
            }
        }, new GetForgetPwdCode.FailCallback() {
            @Override
            public void onFail(int status) {
                NetTool.chackFailStatus(ForgetActivity.this,status);
            }
        });
    }

    @OnClick(R.id.bt_SendForForget)
    public void send(View view) {//点击“确认”
        String email = etEmail.getText().toString().trim();
        String password = etPwd.getText().toString().trim();
        String againPassword = etAgainPwd.getText().toString().trim();
        String YZcode = YZCode.getText().toString().trim();
        //校验
        boolean isCheck = chack(email, password, againPassword, YZcode);
        if (!isCheck) {
            return;
        }

        String password_md5 = MD5Tool.md5(password+ NetConfig.SALT);

        new ForgetPassword(this, email, password_md5, YZcode, new ForgetPassword.SuccessCallBack() {
            @Override
            public void onSuccess() {
                CommonUilts.showDialog(ForgetActivity.this, getString(R.string.success_resetPassword));

            }
        }, new ForgetPassword.FailCallBack() {
            @Override
            public void onFail(int status) {
                NetTool.chackFailStatus(ForgetActivity.this,status);
            }
        });
    }

    private boolean chack(String email, String password, String againPassword, String YZcode) {//登录时的校验
        if (TextUtils.isEmpty(email)) {
            //Toast 邮箱不能为空
            CommonUilts.showDialog(this,getString(R.string.err_un_input_email));
            return false;
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(againPassword)) {
            //Toast 密码不能为空
            CommonUilts.showDialog(this,getString(R.string.err_password_null));
            return false;
        } else if (!password .equals(againPassword) ) {
            //Toast 两次密码不相同
            CommonUilts.showDialog(this,getString(R.string.err_password));
            return false;
        } else if (TextUtils.isEmpty(YZcode)) {
            //Toast 验证码不能为空
            CommonUilts.showDialog(this,getString(R.string.err_YZCode_null));
            return false;
        }
        return true;
    }

    private boolean checkEmail(String email) {
        return TextUtils.isEmpty(email);
    }



}
