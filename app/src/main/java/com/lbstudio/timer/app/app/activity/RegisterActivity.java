package com.lbstudio.timer.app.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.net.GetRegisterCode;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.NetTool;
import com.lbstudio.timer.app.app.net.Register;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.util.MD5Tool;
import com.lbstudio.timer.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.YZ_code)
    EditText YZCode;
    @BindView(R.id.sendYzCode)
    Button sendYzCode;
    @BindView(R.id.bt_SendRegister)
    Button btSendRegister;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_againPwd)
    EditText etAgainPwd;


    @Override
    protected void init() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.sendYzCode) //点击获取验证码
    public void getYzCode(View view) {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            CommonUilts.showDialog(this, getString(R.string.err_un_input_email));
            return;
        }
        final ProgressDialog processDialog = CommonUilts.getProcessDialog(this, "正在发送请求...");
        processDialog.show();
        new GetRegisterCode(this, email, new GetRegisterCode.SuccessCallBack() {
            @Override
            public void onSuccess() {
                processDialog.dismiss();
                CommonUilts.showDialog(RegisterActivity.this, getString(R.string.success_getREcode));
            }
        }, new GetRegisterCode.FailCallBack() {
            @Override
            public void onFail(int statusCode) {
                processDialog.dismiss();
                NetTool.chackFailStatus(RegisterActivity.this, statusCode);
            }
        });
    }

    @OnClick(R.id.bt_SendRegister)  //点击注册
    public void Register(View view) {
        final String password = etPwd.getText().toString().trim();
        final String againPassword = etAgainPwd.getText().toString().trim();
        String yz_code = YZCode.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();

        String pwd_md5 = MD5Tool.md5(password + NetConfig.SALT);

        //校验
        boolean check = check(email, password, againPassword, yz_code);
        if (check) {
            return;
        }
        final ProgressDialog processDialog = CommonUilts.getProcessDialog(this, "正在发送请求...");
        processDialog.show();
        new Register(this, email, pwd_md5, yz_code, new Register.SuccessCallback() {
            @Override
            public void onSuccess() {
                //提示注册成功
                processDialog.dismiss();
                CommonUilts.showDialog(RegisterActivity.this, getString(R.string.success_register));

                //清空输入
                clearInput();
            }
        }, new Register.FailCallback() {
            @Override
            public void onFail(int statusCode) {
                processDialog.dismiss();
                NetTool.chackFailStatus(RegisterActivity.this, statusCode);
            }
        });
    }


    /**
     * 校验内容
     *
     * @param email
     * @param password
     * @param againPassword
     * @param yz_code
     */
    private boolean check(String email, String password, String againPassword, String yz_code) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            //Toast 手机号或密码不能为空
            CommonUilts.showDialog(this, getString(R.string.err_un_input_email));
            return true;
        } else if (!password.equals(againPassword)) {
            //Toast 两次密码不一致
            CommonUilts.showDialog(this, getString(R.string.err_password));
            return true;
        } else if (TextUtils.isEmpty(yz_code)) {
            //Toast 请输入验证码
            CommonUilts.showDialog(this, getString(R.string.err_YZCode_null));
            return true;
        }
        return false;
    }


    /**
     * //清空输入
     */
    private void clearInput() {
        etEmail.setText("");
        etPwd.setText("");
        etAgainPwd.setText("");
        YZCode.setText("");
    }
}
