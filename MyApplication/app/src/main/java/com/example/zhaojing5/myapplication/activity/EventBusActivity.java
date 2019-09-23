package com.example.zhaojing5.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusActivity extends Activity {
    public final String TAG = getClass().getSimpleName();

    public static class MessageEvent{
        public int MSGId;
        public String content;

        public MessageEvent(int MSGId, String content) {
            this.MSGId = MSGId;
            this.content = content;
        }

        public int getMSGId() {
            return MSGId;
        }

        public String getContent() {
            return content;
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        //receive msg from
        //EventBus.getDefault().post(new MessageEvent(0,"EventBus msg"));
        switch (event.getMSGId()){
            case 0:
                Log.i(TAG,"receive EventBus msg.");
                break;
            default:
                break;
        }
    }
}
