package com.lbstudio.timer.app;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lbstudio.timer.app.app.adapter.EnstartAdapter;
import com.lbstudio.timer.app.app.adapter.ItemBean;
import com.lbstudio.timer.app.base.BaseActivity;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class Test2 extends BaseActivity{

    private List<ItemBean> list;
    private EnstartAdapter myAdapter;
    private RecyclerView recyclerView;
    private RefreshLayout refreshview;

    @Override
    protected void init() {
        refreshview = (RefreshLayout)findViewById(R.id.refreshView);
        initDate();
        setPullRefresher();
    }

    private void setPullRefresher() {
        //设置 Header 为 MaterialHeader
        refreshview.setRefreshHeader(new MaterialHeader(this));
        //设置 Footer 为 经典样式
        refreshview.setRefreshFooter(new ClassicsFooter(this));

        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)

                //模拟网络请求到的数据
                ArrayList<ItemBean> newList = new ArrayList<ItemBean>();
                for (int i=0;i<20;i++){
                    newList.add(new ItemBean(
                            R.mipmap.ic_launcher,
                            "newTitle"+i,
                            System.currentTimeMillis()+""
                    ));
                }
//                myAdapter.refresh(newList);
                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });
        refreshview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //模拟网络请求到的数据
                ArrayList<ItemBean> newList = new ArrayList<ItemBean>();
                for (int i=0;i<20;i++){
                    newList.add(new ItemBean(
                            R.mipmap.ic_launcher,
                            "addTitle"+i,
                            System.currentTimeMillis()+""
                    ));
                }
                myAdapter.add(newList);
                //在这里执行下拉加载时的具体操作(网络请求、更新UI等)
                refreshLayout.finishLoadMore(2000/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
            }
        });

    }

    private void initDate() {
            list = new ArrayList<ItemBean>();
            for (int i=0;i<20;i++){
                list.add(new ItemBean(
                        R.mipmap.ic_launcher,
                        "initTitle"+i,
                        System.currentTimeMillis()+""
                ));
            }
//            myAdapter = new EnstartAdapter(this,list);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);//纵向线性布局

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test2;
    }
}
