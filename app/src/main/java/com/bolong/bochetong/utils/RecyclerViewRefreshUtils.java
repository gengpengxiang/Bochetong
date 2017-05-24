package com.bolong.bochetong.utils;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.bolong.bochetong.activity.TcjlActivity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

/**
 * Created by admin on 2017/5/14.
 */

public abstract class RecyclerViewRefreshUtils {

    public void refreshRecyclerView(final PullLoadMoreRecyclerView mRecyclerView, final RecyclerView.Adapter adapter) {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //getDatas();
                        adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                        Log.e("刷新成功","刷新成功");
                    }
                }, 500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                }, 500);
            }
        });
    }

    public abstract void loadMore();


    public abstract void getDatas();


    public static void refreshCompleted(final PullLoadMoreRecyclerView mRecyclerView, final LinearLayout ll ) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll.setVisibility(View.VISIBLE);
                mRecyclerView.setPullLoadMoreCompleted();
                //mRecyclerView.setPullRefreshEnable(false);
                //mRecyclerView.setVisibility(View.GONE);
            }
        }, 1000);


    }
}
