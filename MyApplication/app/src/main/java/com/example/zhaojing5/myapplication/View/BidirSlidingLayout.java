package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.view.View;

public class BidirSlidingLayout extends RelativeLayout implements View.OnTouchListener {

    /**
     * 滚动显示和隐藏左侧布局时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;

    /**
     * 滑动状态的一种，表示未进行任何滑动。
     */
    public static final int DO_NOTHING = 0;

    /**
     * 滑动状态的一种，表示正在滑出左侧菜单。
     */
    public static final int SHOW_LEFT_MENU = 1;

    /**
     * 滑动状态的一种，表示正在滑出右侧菜单。
     */
    public static final int SHOW_RIGHT_MENU = 2;

    /**
     * 滑动状态的一种，表示正在隐藏左侧菜单。
     */
    public static final int HIDE_LEFT_MENU = 3;

    /**
     * 滑动状态的一种，表示正在隐藏右侧菜单。
     */
    public static final int HIDE_RIGHT_MENU = 4;

    /**
     * 记录当前的滑动状态
     */
    private int slideState;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;

    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;

    /**
     * 记录手指按下时的纵坐标。
     */
    private float yDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手指移动时的纵坐标。
     */
    private float yMove;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;

    /**
     * 左侧菜单当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
     */
    private boolean isLeftMenuVisible;

    /**
     * 右侧菜单当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
     */
    private boolean isRightMenuVisible;

    /**
     * 是否正在滑动。
     */
    private boolean isSliding;

    /**
     * 左侧菜单布局对象。
     */
    private View leftMenuLayout;

    /**
     * 右侧菜单布局对象。
     */
    private View rightMenuLayout;

    /**
     * 内容布局对象。
     */
    private View contentLayout;

    /**
     * 用于监听滑动事件的View。
     */
    private View mBindView;

    /**
     * 左侧菜单布局的参数。
     */
    private MarginLayoutParams leftMenuLayoutParams;

    /**
     * 右侧菜单布局的参数。
     */
    private MarginLayoutParams rightMenuLayoutParams;

    /**
     * 内容布局的参数。
     */
    private RelativeLayout.LayoutParams contentLayoutParams;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;

    public BidirSlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point viewSize = new Point();
        if(wm.getDefaultDisplay() != null){
            wm.getDefaultDisplay().getSize(viewSize);
        }
        screenWidth = viewSize.x;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 绑定监听滑动事件的view
     * @param bindView
     */
    public void setScrollEvent(View bindView){
        mBindView = bindView;
        mBindView.setOnTouchListener(this);
    }

    public void scrollToLeftMenu(){
        new LeftMenuScrollTask().execute( -30 );
    }

    public void scrollToRightMenu(){
        new RightMenuScrollTask().execute( -30 );
    }

    public void scrollToContentFromLeftMenu(){
        new LeftMenuScrollTask().execute( 30 );
    }

    public void scrollToContentFromRightMenu(){
        new RightMenuScrollTask().execute( 30 );
    }

    /**
     * 左侧菜单是否完全显示出来，滑动过程此值无效
     * @return
     */
    public boolean isLeftLayoutVisible(){
        return isLeftMenuVisible;
    }

    /**
     * 右侧菜单是否完全显示出来，滑动过程此值无效
     * @return
     */
    public boolean isRightLayoutVisible(){
        return isRightMenuVisible;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            leftMenuLayout = getChildAt(0);
            leftMenuLayoutParams = (MarginLayoutParams) leftMenuLayout.getLayoutParams();
            rightMenuLayout = getChildAt(1);
            rightMenuLayoutParams = (MarginLayoutParams) rightMenuLayout.getLayoutParams();
            contentLayout = getChildAt(2);
            contentLayoutParams = (LayoutParams) contentLayout.getLayoutParams();
            contentLayoutParams.width = screenWidth;
            contentLayout.setLayoutParams(contentLayoutParams);

        }
    }

    private void unFocusBindView(){
        if(mBindView != null){
            mBindView.setPressed(false);
            mBindView.setFocusable(false);
            mBindView.setFocusableInTouchMode(false);
        }
    }

    class LeftMenuScrollTask extends AsyncTask<Integer,Integer,Integer>{
        @Override
        protected Integer doInBackground(Integer... integers) {
            int rightMargin = contentLayoutParams.rightMargin;
            while(true){
                //-30 是因为要设置rightMargin的值，滑动到左侧菜单，rightMargin是负值
                rightMargin = rightMargin + integers[0];
                if(rightMargin < -leftMenuLayoutParams.width){
                    rightMargin = -leftMenuLayoutParams.width;
                    break;
                }
                if(rightMargin > 0){
                    rightMargin = 0;
                    break;
                }
                publishProgress(rightMargin);
                sleep(15);
            }
            if(integers[0] > 0){
                isLeftMenuVisible = false;
            }else{
                isLeftMenuVisible = true;
            }
            isSliding = false;
            return rightMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            contentLayoutParams.rightMargin = values[0];
            contentLayout.setLayoutParams(contentLayoutParams);
            unFocusBindView();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            contentLayoutParams.rightMargin = integer;
            contentLayout.setLayoutParams(contentLayoutParams);
        }
    }

    class RightMenuScrollTask extends AsyncTask<Integer,Integer,Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            //根据传入的速度来滚动界面，当滚动到达边界值时，跳出循环
            int leftMargin = contentLayoutParams.leftMargin;
            //-30 是因为要设置leftMargin的值，滑动到右侧菜单，leftMargin是负值
            while(true){
                leftMargin = leftMargin + integers[0];
                if(leftMargin < -rightMenuLayoutParams.width){
                    leftMargin = -rightMenuLayoutParams.width;
                    break;
                }
                if(leftMargin > 0){
                    leftMargin = 0;
                    break;
                }
                publishProgress(leftMargin);
                //为了要有滚动效果产生，每次循环使线程睡眠一段时间，这样
                //肉眼有滚动的效果
                sleep(15);
            }
            if(integers[0] > 0){
                isRightMenuVisible = false;
            }else{
                isRightMenuVisible = true;
            }
            isSliding = false;
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            contentLayoutParams.leftMargin = values[0];
            contentLayout.setLayoutParams(contentLayoutParams);
            unFocusBindView();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            contentLayoutParams.leftMargin = integer;
            contentLayout.setLayoutParams(contentLayoutParams);
        }
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocitutracker(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                slideState = DO_NOTHING;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                //calculate interval distance
                int moveDistanceX = (int) (xMove - xDown);
                int moveDistanceY = (int) (yMove - yDown);
                //check current sliding state
                checkSlideState(moveDistanceX,moveDistanceY);
                //根据当前滑动状态决定如何偏移内容布局
                switch(slideState){
                    case SHOW_LEFT_MENU:
                        contentLayoutParams.rightMargin = -moveDistanceX;
                        checkLeftMenuBordr();
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    case HIDE_LEFT_MENU:
                        contentLayoutParams.rightMargin =  -leftMenuLayoutParams.width - moveDistanceX;
                        checkLeftMenuBordr();
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    case SHOW_RIGHT_MENU:
                        contentLayoutParams.leftMargin = moveDistanceX;
                        checkRightMenuBorder();
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    case HIDE_RIGHT_MENU:
                        contentLayoutParams.leftMargin = -rightMenuLayoutParams.width + moveDistanceX;
                        checkLeftMenuBordr();
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    default:
                        break;

                }
                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                int upDistanceX = (int) (xUp - xDown);
                if(isSliding){
                    switch(slideState){
                        case SHOW_LEFT_MENU:
                            if(shouldScrollToLeftMenu()){
                                scrollToLeftMenu();
                            }else{
                                scrollToContentFromLeftMenu();
                            }
                            break;
                        case HIDE_LEFT_MENU:
                            if(shouldScrollToContentFromLeftMenu()){
                                scrollToContentFromLeftMenu();
                            }else{
                                scrollToLeftMenu();
                            }
                            break;
                        case SHOW_RIGHT_MENU:
                            if(shouldScrollToRightMenu()){
                                scrollToRightMenu();
                            }else{
                                scrollToContentFromRightMenu();
                            }
                            break;
                        case HIDE_RIGHT_MENU:
                            if(shouldScrollToContentFromRight()){
                                scrollToContentFromRightMenu();
                            }else{
                                scrollToRightMenu();
                            }
                            break;
                        default:
                            break;
                    }
                }else if(upDistanceX < touchSlop && isLeftMenuVisible){
                    scrollToContentFromLeftMenu();
                }else if(upDistanceX < touchSlop && isRightMenuVisible){
                    scrollToContentFromRightMenu();
                }
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        if(v.isEnabled()){
            if(isSliding){
                unFocusBindView();
                return true;
            }
            if(isLeftMenuVisible || isRightMenuVisible){
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * 左划距离大于左面菜单宽度的一半或者
     * 左划速度大于标准滑动速度
     * @return
     */
    private boolean shouldScrollToLeftMenu(){
        return xUp - xDown > leftMenuLayoutParams.width / 2 ||
                getScrollVelocity() > SNAP_VELOCITY;
    }

    private boolean shouldScrollToRightMenu(){
        return xDown - xUp > rightMenuLayoutParams.width / 2 ||
                getScrollVelocity() > SNAP_VELOCITY;
    }

    private boolean shouldScrollToContentFromLeftMenu(){
        return xDown - xUp > leftMenuLayoutParams.width / 2 ||
                getScrollVelocity() > SNAP_VELOCITY;
    }

    private boolean shouldScrollToContentFromRight(){
        return xUp - xDown > rightMenuLayoutParams.width / 2 ||
                getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * get x touchslop in bindview
     * @return
     */
    private int getScrollVelocity(){
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    private void recycleVelocityTracker(){
        if(mVelocityTracker != null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void checkLeftMenuBordr(){
        if(contentLayoutParams.rightMargin > 0){
            contentLayoutParams.rightMargin = 0;
        }else if(contentLayoutParams.rightMargin < -leftMenuLayoutParams.width){
            contentLayoutParams.rightMargin = -leftMenuLayoutParams.width;
        }
    }

    /**
     * 在滑动过程中检查右侧菜单的边界值，防止绑定布局滑出屏幕。
     */
    private void checkRightMenuBorder() {
        if (contentLayoutParams.leftMargin > 0) {
            contentLayoutParams.leftMargin = 0;
        } else if (contentLayoutParams.leftMargin < -rightMenuLayoutParams.width) {
            contentLayoutParams.leftMargin = -rightMenuLayoutParams.width;
        }
    }

    private void checkSlideState(int moveDistanceX, int moveDistanceY){
        if(isLeftMenuVisible){
            if( !isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX < 0){
                isSliding = true;
                slideState = HIDE_LEFT_MENU;
            }
        }else if(isRightMenuVisible){
            if( !isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX > 0){
                isSliding = true;
                slideState = HIDE_RIGHT_MENU;
            }
        }else{
            if( !isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX > 0
                    && Math.abs(moveDistanceY) < touchSlop){
                isSliding = true;
                slideState = SHOW_LEFT_MENU;
                contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
                contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                contentLayout.setLayoutParams(contentLayoutParams);
                //if you want to slide left menu,it will show left menu and hide right menu.
                leftMenuLayout.setVisibility(View.VISIBLE);
                rightMenuLayout.setVisibility(View.GONE);
            }else if( !isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX < 0
                    && Math.abs(moveDistanceY) > touchSlop){
                isSliding = true;
                slideState = SHOW_RIGHT_MENU;
                contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
                contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                contentLayout.setLayoutParams(contentLayoutParams);
                //if you want to slide right menu,it will show right menu and hide left menu
                rightMenuLayout.setVisibility(View.VISIBLE);
                leftMenuLayout.setVisibility(View.GONE);
            }
        }
    }

    private void createVelocitutracker(MotionEvent event){
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
}
