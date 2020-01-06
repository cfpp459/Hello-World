package com.example.zhaojing5.myapplication.View.rollingbarrage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.example.zhaojing5.myapplication.R;

/**
 * created by zhaojing 2020/1/2 下午2:47
 */
public class RollingBarrageView extends ViewFlipper implements View.OnClickListener {

    private int default_rolling_interval_time = 500;
    private int default_rolling_in_anim = R.anim.bulletin_item_enter;
    private int default_rolling_leave_anim = R.anim.bulletin_item_leave;
    private RollingBarrageViewListener mListener;

    public RollingBarrageView(Context context) {
        super(context);
    }

    public RollingBarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewStyle(context, attrs);
    }

    private void initViewStyle(Context context, AttributeSet attrs){
        if (context != null) {

            if (attrs != null) {
                TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RollingBarrageView);
                if (typedArray != null) {
                    default_rolling_interval_time = typedArray.getInt(R.styleable.RollingBarrageView_rollingBarrageInterval, 500);
                    default_rolling_in_anim = typedArray.getResourceId(R.styleable.RollingBarrageView_rollingBarrageEnterAnim, R.anim.bulletin_item_enter);
                    default_rolling_leave_anim = typedArray.getResourceId(R.styleable.RollingBarrageView_rollingBarrageLeaveAnim, R.anim.bulletin_item_leave);
                    typedArray.recycle();
                }
            }

            setFlipInterval(default_rolling_interval_time);
            setInAnimation(AnimationUtils.loadAnimation(getContext(), default_rolling_in_anim));
            setOutAnimation(AnimationUtils.loadAnimation(getContext(), default_rolling_leave_anim));

        }
    }

    public void setAdpater(RollingBarrageAdapter adapter){
        if (adapter == null) {
            return;
        }
        for (int i = 0; i < adapter.getSize(); i++) {
            View view = adapter.getView(i);
            view.setTag(i);
            addView(view);
            view.setOnClickListener(this);
        }
        startFlipping();
    }

    public void setOnRollingBarrageViewListener(RollingBarrageViewListener listener){
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
       int position = (int) v.getTag();


    }



    public interface RollingBarrageViewListener{
        void onRollingBarrageViewClick(int position);
    }

}
