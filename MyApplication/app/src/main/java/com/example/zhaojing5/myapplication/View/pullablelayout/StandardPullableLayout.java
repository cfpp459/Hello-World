package com.example.zhaojing5.myapplication.View.pullablelayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.zhaojing5.myapplication.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 子view只有一个，必须是ViewGroup
 * ViewGroup可嵌套基本控件，可嵌套滑动布局（RecycleView）
 * 上拉刷新，下拉加载样式为标准通用的文字+ProgressBar样式
 *
 * created by zhaojing 2019/7/8 下午4:12
 */
public class StandardPullableLayout extends ViewGroup {

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = 2;
    private int mDirection;
    private RefreshListener mRefreshListener;
    private int mLayoutContentHeight;
    private Scroller mLayoutScroller;
    private float refreshRatio = 0.75f;
    private float maxRatio = 1f;
    private int mHeaderStayRefreshHeight = 0,mFooterStayRefreshHeight = 0;
    private ExecutorService mWorkExecutor;
    private boolean isRefreshing = false;
    private boolean isDownUp = false;
    private boolean isUpDown = false;
    private int currScroll = -1;
    private int mTouchSlop;
    private Runnable refresRunnable = new Runnable() {
        @Override
        public void run() {
            if (getmRefreshListener() != null) {
                mRefreshListener.onRefresh();
            }
        }
    };
    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            if (getmRefreshListener() != null) {
                mRefreshListener.onLoadMore();
            }
        }
    };

    private View mHeader,mFooter;


    public StandardPullableLayout(Context context) {
        super(context);
        init(context);
    }

    public StandardPullableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StandardPullableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public StandardPullableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        mLayoutScroller = new Scroller(context);
        mHeader = LayoutInflater.from(context).inflate(R.layout.pullablelayout_header, null);
        mFooter = LayoutInflater.from(context).inflate(R.layout.pullablelayout_footer, null);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mWorkExecutor = Executors.newFixedThreadPool(1);
    }

    public RefreshListener getmRefreshListener() {
        return mRefreshListener;
    }

    public void setmRefreshListener(RefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,65,getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams paramsHeader = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,height);
        mHeader.setLayoutParams(paramsHeader);
        RelativeLayout.LayoutParams paramsFooter = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,height);
        mFooter.setLayoutParams(paramsFooter);
        addView(mHeader);
        addView(mFooter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int index = 0; index < getChildCount(); index++){
            View child = getChildAt(index);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int mEffectiveScrollHeaderY, mEffectiveScrollFooterY;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mLayoutContentHeight = 0;

        for(int index = 0; index < getChildCount(); index++){
            View child = getChildAt(index);
            if(child == mHeader){
                mEffectiveScrollHeaderY = (int) (child.getMeasuredHeight() * refreshRatio);
                mHeaderStayRefreshHeight = mHeader.getMeasuredHeight();
                Log.d("zhaojing","header max height is " + mHeader.getMeasuredHeight() * maxRatio
                        + ", effect is " + mEffectiveScrollHeaderY
                        + ", mHeaderStayRefreshHeight is " + mHeaderStayRefreshHeight);
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            }else if(child == mFooter){
                mEffectiveScrollFooterY = (int) (child.getMeasuredHeight() * refreshRatio);
                mFooterStayRefreshHeight = mFooter.getMeasuredHeight();
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
            }else{
                if(child instanceof ViewGroup && child.getVisibility() == VISIBLE){
                    child.layout(0,mLayoutContentHeight, child.getMeasuredWidth(),mLayoutContentHeight + child.getMeasuredHeight());
                    mLayoutContentHeight += child.getMeasuredHeight();
                }else{
                    Log.d("zhaojing","PullableLayout need only one content child...");
                }
            }
        }
    }

    private int mLastMoveY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        Log.d("zhaojing","currScroll is " + currScroll);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!mLayoutScroller.isFinished()){
                    mLayoutScroller.abortAnimation();
                }
                mLastMoveY = y;
                isDownUp = false;
                isUpDown = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastMoveY - y;
//                if(Math.abs(dy) < mTouchSlop){
//                    break;
//                }
                if(dy < 0){
                    //下拉
                    mDirection = DIRECTION_DOWN;
                    if(currScroll <= 0 && isRecycleViewBottom()){
                        Log.d("zhaojing","先上拉，再下拉到底部啦~");
                        isUpDown = true;
                        break;
                    }

                    if(isUpDown){
                        break;
                    }

                    Log.d("zhaojing","下拉 getScrollY() is " + getScrollY());
                    if( (Math.abs(getScrollY()) <= mHeader.getMeasuredHeight() * maxRatio)){
                        if(Math.abs(getScrollY() + dy) > mHeader.getMeasuredHeight() * maxRatio){
                            //防止一次性下拉很多，最大为指定的下拉高度
                            dy = - ( (int) (mHeader.getMeasuredHeight() * maxRatio) - Math.abs(getScrollY()) );
                        }
                        scrollBy(0,dy);
                        currScroll += dy;
                        if(Math.abs(getScrollY()) >= mEffectiveScrollHeaderY){
                            TextView mTvHeader = mHeader.findViewById(R.id.tv_header);
                            mTvHeader.setText("松开刷新哦~");
                        }
                    }

                }
                if(dy > 0){
                    //上拉
                    mDirection = DIRECTION_UP;

                    //如果有RecycleView，并且没到底部，则事件交给RecycleView
                    if(!isRecycleViewBottom1()){
                        break;
                    }

                    if(currScroll >= 0 && isRecycleViewTop()){
                        Log.d("zhaojing","先下拉，再上拉到顶部啦~");
                        isDownUp = true;
                        break;
                    }

                    if(isDownUp){
                        break;
                    }

                    Log.d("zhaojing","上拉 getScrollY() is " + getScrollY());
                    if(getScrollY() <= mFooter.getMeasuredHeight() * maxRatio){
                        if(getScrollY() + dy > mFooter.getMeasuredHeight() * maxRatio){
                            //防止一次性上拉很多，最大为指定的上拉高度
                            dy = (int) (mFooter.getMeasuredHeight() * maxRatio) - getScrollY();
                        }
                        scrollBy(0,dy);
                        currScroll += dy;
                        if(getScrollY() >= mEffectiveScrollFooterY){
                            TextView mTvFooter = mFooter.findViewById(R.id.tv_footer);
                            mTvFooter.setText("松开加载更多哦~");
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                //下拉
                if(getScrollY() < 0){
                    if(Math.abs(getScrollY()) >= mEffectiveScrollHeaderY){
                        if(isRefreshing){
                            return true;
                        }
                        Log.d("zhaojing","header refreshing...");
                        isRefreshing = true;
                        //下拉
                        TextView mTvHeader = mHeader.findViewById(R.id.tv_header);
                        mTvHeader.setVisibility(GONE);
                        ProgressBar mProgressBar = mHeader.findViewById(R.id.pb_progress);
                        mProgressBar.setVisibility(VISIBLE);
                        if(Math.abs(getScrollY()) < mHeaderStayRefreshHeight){
                            //未拉满
                            mLayoutScroller.startScroll(0, getScrollY(), 0, - (mHeaderStayRefreshHeight + getScrollY()));
                        }

                        if(mWorkExecutor != null){
                            mWorkExecutor.execute(refresRunnable);
                        }
                    }else{
                        Log.d("zhaojing","header cancel refreshing...");
                        mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                        TextView mTvHeader = mHeader.findViewById(R.id.tv_header);
                        mTvHeader.setText("↓下拉刷新");
                        invalidate();
                    }
                }

                //上拉
                if(getScrollY() > 0){

                    if(!isRecycleViewBottom1()){
                        break;
                    }

                    if(getScrollY() >= mEffectiveScrollFooterY){
                        if(isRefreshing){
                            return true;
                        }
                        Log.d("zhaojing","footer refreshing...");
                        isRefreshing = true;
                        TextView mTvFooter = mFooter.findViewById(R.id.tv_footer);
                        mTvFooter.setVisibility(GONE);
                        ProgressBar mProgressBar = mFooter.findViewById(R.id.pb_progress);
                        mProgressBar.setVisibility(VISIBLE);
                        if(getScrollY() < mFooterStayRefreshHeight){
                            //未拉满
                            mLayoutScroller.startScroll(0, getScrollY(), 0, (mFooterStayRefreshHeight - getScrollY()));
                        }

                        if(mWorkExecutor != null){
                            mWorkExecutor.execute(loadMoreRunnable);
                        }
                    }else{
                        Log.d("zhaojing","footer cancel refreshing...");
                        mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                        TextView mTvFooter = mFooter.findViewById(R.id.tv_footer);
                        mTvFooter.setText("↑上拉加载更多");
                        invalidate();
                    }
                }

                break;
            default:
        }

        mLastMoveY = y;

        //手动将事件传递给子View，让子View自己去处理事件
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mWorkExecutor != null){
            mWorkExecutor.shutdownNow();
            mWorkExecutor = null;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mLayoutScroller.computeScrollOffset()){

            if(mDirection == DIRECTION_UP){
                if(isDownUp){
                    Log.d("zhaojing","isDownUp,stop scroll...");
                    mLayoutScroller.abortAnimation();
                    return;
                }
//                if(isRecycleViewBottom()){
//                    this.scrollTo(mLayoutScroller.getCurrX(),mLayoutScroller.getCurrY());
//                }
            }

            if(mDirection == DIRECTION_DOWN){
                if(isUpDown){
                    Log.d("zhaojing","isUpDown,stop scroll...");
                    mLayoutScroller.abortAnimation();
                    return;
                }

//                if(isRecycleViewTop()){
//                    this.scrollTo(mLayoutScroller.getCurrX(),mLayoutScroller.getCurrY());
//                }
            }

            this.scrollTo(mLayoutScroller.getCurrX(),mLayoutScroller.getCurrY());
            currScroll = 0;
            invalidate();
        }
    }

    private boolean isRecycleViewBottom(){
        int childCount = getChildCount();
        for(int index = 0; index < childCount; index++){
            View view = getChildAt(index);
            if(view instanceof RecyclerView){
                RecyclerView recyclerView = (RecyclerView) view;
                Log.d("zhaojing","isRecycleViewBottom " + isRecycleViewBottom(recyclerView));
                if(isRecycleViewBottom(recyclerView)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRecycleViewBottom1(){
        int childCount = getChildCount();
        for(int index = 0; index < childCount; index++){
            View view = getChildAt(index);
            if(view instanceof RecyclerView){
                RecyclerView recyclerView = (RecyclerView) view;
                Log.d("zhaojing","isRecycleViewBottom1 " + isRecycleViewBottom1(recyclerView));
                if(isRecycleViewBottom1(recyclerView)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRecycleViewTop(){
        int childCount = getChildCount();
        for(int index = 0; index < childCount; index++){
            View view = getChildAt(index);
            if(view instanceof RecyclerView){
                RecyclerView recyclerView = (RecyclerView) view;
                Log.d("zhaojing","isRecycleViewTop " + isRecyclerViewTop(recyclerView));
                if(isRecyclerViewTop(recyclerView)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || ( firstVisibleItemPosition == 0 && childAt.getTop() == 0 )) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRecycleViewBottom(RecyclerView recyclerView){
        return ( recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent() >= recyclerView.computeVerticalScrollRange());
    }

    private boolean isRecycleViewBottom1(RecyclerView recyclerView){
        return recyclerView.canScrollVertically(1);
    }

    private RecyclerView getRecycleView(){
        int childCount = getChildCount();
        for(int index = 0; index < childCount; index++){
            View view = getChildAt(index);
            if(view instanceof RecyclerView){
                RecyclerView recyclerView = (RecyclerView) view;
                return recyclerView;
            }
        }
        return null;
    }

    public void refreshDone(){
        isRefreshing = false;
        mLayoutScroller.startScroll( 0, getScrollY(), 0, -getScrollY());
        mHeader.findViewById(R.id.tv_header).setVisibility(VISIBLE);
        mHeader.findViewById(R.id.pb_progress).setVisibility(GONE);
        TextView mTvHeader = mHeader.findViewById(R.id.tv_header);
        mTvHeader.setText("↓下拉刷新");
    }

    public void loadDone(){
        isRefreshing = false;
        mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
        mFooter.findViewById(R.id.tv_footer).setVisibility(VISIBLE);
        mFooter.findViewById(R.id.pb_progress).setVisibility(GONE);
        TextView mTvFooter = mFooter.findViewById(R.id.tv_footer);
        mTvFooter.setText("↑上拉加载更多");
    }

    public interface RefreshListener {
        void onRefresh();
        void onLoadMore();
    }


}
