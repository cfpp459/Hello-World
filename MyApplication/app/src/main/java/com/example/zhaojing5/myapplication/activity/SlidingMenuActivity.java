package com.example.zhaojing5.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.View.BidirSlidingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SlidingMenuActivity extends Activity {

    public Unbinder unbinder;

    /**
     * 双向滑动菜单布局
     */
    @BindView(R.id.bidir_sliding_layout)
    public BidirSlidingLayout bidirSldingLayout;

    /**
     * 在内容布局上显示的ListView
     */
    @BindView(R.id.contentList)
    public ListView contentList;

    /**
     * ListView的适配器
     */
    private ArrayAdapter<String> contentListAdapter;

    /**
     * 用于填充contentListAdapter的数据源。
     */
    private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3",
            "Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",
            "Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
            "Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",
            "Content Item 16" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_menu_layout);
        unbinder = ButterKnife.bind(this);
        testSlidingMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void testSlidingMenu(){
        contentListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contentItems);
        contentList.setAdapter(contentListAdapter);
        bidirSldingLayout.setScrollEvent(contentList);
    }
}
