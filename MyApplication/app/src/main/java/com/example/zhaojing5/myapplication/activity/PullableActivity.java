package com.example.zhaojing5.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;
import com.example.zhaojing5.myapplication.View.pullablelayout.StandardPullableLayout;

/**
 * created by zhaojing 2019/7/8 下午5:34
 */
public class PullableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullable_layout);

        init();

        final StandardPullableLayout pullableLayout = findViewById(R.id.pl_refresh_layout);
        pullableLayout.setmRefreshListener(new StandardPullableLayout.RefreshListener() {

            @Override
            public void onRefresh() {
                try {
                    Log.d("zhaojing","刷新...");
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pullableLayout.refreshDone();
                            ToastUtils.showToast(PullableActivity.this, "刷新完毕啦~");
//                            ImageView imageView = findViewById(R.id.iv_bg);
//                            imageView.setImageResource(R.drawable.cat3);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadMore() {
                try {
                    Log.d("zhaojing","加载更多...");
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pullableLayout.loadDone();
                            ToastUtils.showToast(PullableActivity.this, "加载完毕啦~");
//                            ImageView imageView = findViewById(R.id.iv_bg);
//                            imageView.setImageResource(R.drawable.cat5);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = findViewById(R.id.rv_recycleview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(PullableActivity.this).inflate(R.layout.activity_recycleview, parent, false);
                return new MyViewHolder(itemView);

            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                holder.getmTextView().setText("recycleview " + position);
            }

            @Override
            public int getItemCount() {
                return 25;
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_text);
        }

        public TextView getmTextView() {
            return mTextView;
        }
    }
}
