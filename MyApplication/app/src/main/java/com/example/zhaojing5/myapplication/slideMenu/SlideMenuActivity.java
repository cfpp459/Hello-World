package com.example.zhaojing5.myapplication.slideMenu;

import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.zhaojing5.myapplication.R;

import butterknife.OnTouch;

public class SlideMenuActivity extends Activity implements View.OnTouchListener{
    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;

    /**
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
     */
    private int leftEdge;

    /**
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
     */
    private int rightEdge = 0;

    /**
     * menu完全显示时，留给content的宽度值。
     */
    private int menuPadding = 80;

    /**
     * 主内容的布局。
     */
    private View content;

    /**
     * menu的布局。
     */
    private View menu;

    /**
     * menu布局的参数，通过此参数来更改leftMargin的值。
     */
    private LinearLayout.LayoutParams menuParams;

    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;

    /**
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
     */
    private boolean isMenuVisible;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu);
        initValues();
        content.setOnTouchListener(this);
    }

    private void initValues(){
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        content = findViewById(R.id.content);
        menu = findViewById(R.id.menu);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        menuParams.width = screenWidth - menuPadding;
        leftEdge = -menuParams.width;
        menuParams.leftMargin = leftEdge;
        content.getLayoutParams().width = screenWidth;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int distance = (int) (xMove - xDown);
                if(isMenuVisible){
                    menuParams.leftMargin = distance;
                }else{
                    menuParams.leftMargin = leftEdge + distance;
                }
                if(menuParams.leftMargin < leftEdge){
                    menuParams.leftMargin = leftEdge;
                }else if(menuParams.leftMargin > rightEdge){
                    menuParams.leftMargin = rightEdge;
                }
                menu.setLayoutParams(menuParams);
                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                if(wantToShoeMenu()){
                    if(shouldScroolToMenu()){
                        scrollToMenu();
                    }else{
                        scrollToContent();
                    }
                }else if(wantToShowContent()){
                    if(shouldScrollToContent()){
                        scrollToContent();
                    }else{
                        scrollToMenu();
                    }
                }
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    private void recycleVelocityTracker(){
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private void createVelocityTracker(MotionEvent event){
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private boolean wantToShowContent(){
        return xUp - xDown < 0 && isMenuVisible;
    }

    private boolean wantToShoeMenu(){
        return xUp - xDown > 0 && !isMenuVisible;
    }

    private boolean shouldScroolToMenu(){
        return xUp - xDown > screenWidth / 2 || getScroolVelocity() > SNAP_VELOCITY;
    }

    private boolean shouldScrollToContent(){
        return xDown - xUp + menuPadding > screenWidth / 2 || getScroolVelocity() > SNAP_VELOCITY;
    }

    private int getScroolVelocity(){
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    private void scrollToMenu(){
        new ScrollTask().execute(30);
    }

    private void scrollToContent(){
        new ScrollTask().execute(-30);
    }

    class ScrollTask extends AsyncTask<Integer,Integer,Integer>{
        @Override
        protected Integer doInBackground(Integer... integers) {
            int leftMargin = menuParams.leftMargin;
            while(true){
                leftMargin = leftMargin + integers[0];
                if(leftMargin > rightEdge){
                    leftMargin = rightEdge;
                    break;
                }
                if(leftMargin < leftEdge){
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                sleep(20);
            }
            if(integers[0] > 0){
                isMenuVisible = true;
            }else{
                isMenuVisible = false;
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            menuParams.leftMargin = values[0];
            menu.setLayoutParams(menuParams);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            menuParams.leftMargin = integer;
            menu.setLayoutParams(menuParams);
        }
    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis
     *            指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
