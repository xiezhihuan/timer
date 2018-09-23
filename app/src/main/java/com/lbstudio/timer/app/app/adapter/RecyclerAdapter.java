package com.lbstudio.timer.app.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lbstudio.timer.app.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<MyHoder> {

    Context mContext;

    public RecyclerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {  //viewType  表示item的类型
        return new MyHoder(LayoutInflater.from(mContext).inflate(R.layout.course_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override  //决定item的类型  比如 1 新闻item  2 计划item  3 纯文本item
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

class MyHoder extends RecyclerView.ViewHolder {


    private TextView tv_name;

    public MyHoder(View itemView) {
        super(itemView);
        tv_name = (TextView) itemView.findViewById(R.id.weekBar_week);
    }

    public void getDataa(String planConten, String planTime) {
//        tv_Content.setText(planConten);
//        tv_Time.setText(planTime);
    }
}