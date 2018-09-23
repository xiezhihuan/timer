package com.lbstudio.timer.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.necer.ncalendar.utils.Attrs;

public class CommonUtils {
    /**
     * 进度对话框
     * <p>
     * dialog.show(); 显示  开始时调用
     * dialog.dismiss（）消失  结束时调用
     *
     * @param context
     * @param tips
     * @return
     */
    public static ProgressDialog getProcessDialog(Context context, String tips) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 跳转到某个页面
     *
     * @param thisClass 本页面
     * @param clazz     目标页面
     */
    public static void intentToActivity(Context thisClass, Class clazz) {
        Intent intent = new Intent(thisClass, clazz);
        thisClass.startActivity(intent);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param max 最大范围
     * @return int
     */
    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    public static void resetTheme(){
         int[] color={R.color.lightBlue,R.color.lightRed,R.color.purple,R.color.wineRed};
        Attrs.selectCircleColor=getRandom(color[getRandom(color.length-1)]);

    }
}
