package com.lbstudio.timer.app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Process;

import com.lbstudio.timer.app.CrashHandler;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.javabean.UserInfo;
import com.lbstudio.timer.app.app.util.CommonUilts;

import org.litepal.LitePal;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyApplication extends Application {

    public static Context mContext=null;

    public static Handler handler=null;

    public static Thread mainThead=null;

    public static int mainTheadId=0;
    public static ProgressDialog processDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this); //LitePal数据库
        mContext = getApplicationContext();
        handler = new Handler() {
            @Override
            public void publish(LogRecord record) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        mainThead = Thread.currentThread();
        mainTheadId = Process.myTid();
        CrashHandler.getInstance().init(this);

        SQLiteDatabase database = LitePal.getDatabase();

        processDialog = CommonUilts.getProcessDialog(this, getString(R.string.getting));
    }
}
