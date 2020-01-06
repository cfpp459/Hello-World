package com.example.zhaojing5.myapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.ShareActionProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;

import com.example.zhaojing5.myapplication.BuildConfig;
import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.MainApplication;
import com.example.zhaojing5.myapplication.Utils.FileUtils;
import com.example.zhaojing5.myapplication.Utils.FileVisitorUtil;
import com.example.zhaojing5.myapplication.Utils.MultiKnowledgePoint;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;
import com.example.zhaojing5.myapplication.View.UseHttpActivity;
import com.example.zhaojing5.myapplication.bean.UserInfo;
import com.example.zhaojing5.myapplication.dl.ProxyActivity;
import com.example.zhaojing5.myapplication.fresco.TestFrescoActivity;
import com.example.zhaojing5.myapplication.modelPattern.BookBean;
import com.example.zhaojing5.myapplication.modelPattern.JsonFormatter;
import com.example.zhaojing5.myapplication.modelPattern.XMLFormatter;
import com.example.zhaojing5.myapplication.slideMenu.SlideMenuActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.btn_snackbar)
    public Button btn_snackbar;

    private Unbinder unbinder;
    public static final String TAG = MainActivity.class.getSimpleName();
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"--->onCreate");
        setTitle("微信");
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        testMethos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"--->onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"--->onDestory");
        unbinder.unbind();
    }

    @OnClick({R.id.btn_popView,R.id.btn_callClientApk,R.id.start_slide_menu,R.id.btn_snackbar,
            R.id.btn_notification,R.id.btn_volley,R.id.btn_goto_count_view,R.id.btn_sliding_menu,
            R.id.btn_rotate_pic,R.id.btn_three_d,R.id.btn_distribute,R.id.btn_my3d,R.id.btn_goto_finger,
            R.id.btn_pullable_layout,R.id.btn_loopimageview,R.id.btn_fresco,R.id.btn_rxjava})
    public void onClick(View v) {
        Log.d(TAG,"click view " + v.getId());
        Intent intent = null;
        switch(v.getId()){
            case R.id.btn_rxjava:
                startActivity(new Intent(this, RxJavaActivity.class));
                break;
            case R.id.btn_fresco:
                startActivity(new Intent(this, TestFrescoActivity.class));
                break;
            case R.id.btn_loopimageview:
                startActivity(new Intent(this, LooperImageViewActivity.class));
                break;
            case R.id.btn_pullable_layout:
                startActivity(new Intent(this,PullableActivity.class));
                break;
            case R.id.btn_popView:
                startActivity(new Intent(this,PopViewActivity.class));
                break;
            case R.id.btn_callClientApk:
                intent = new Intent(this, ProxyActivity.class);
//                intent.putExtra(ProxyActivity.EXTRA_DEX_PATH,"/mnt/sdcard/DynamicLoadHost/plugin.apk");
                intent = intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, getSDCardPath() + "/dlproject-debug.apk");
                startActivity(intent);
                break;
            /*case R.id.start_float_window:
                intent = new Intent(this, FloatWindowService.class);
                startService(intent);
                finish();
                break;*/
            case R.id.start_slide_menu:
                intent = new Intent(this, SlideMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_snackbar:
                testSnackBar();
                break;
            case R.id.btn_notification:
                testNotification();
                break;
            case R.id.btn_volley:
                startActivity(new Intent(this,UseHttpActivity.class));
                break;
            case R.id.btn_goto_count_view:
                startActivity(new Intent(this,UseHttpActivity.class));
                break;
            case R.id.btn_sliding_menu:
                startActivity(new Intent(this,SlidingMenuActivity.class));
                break;
            case R.id.btn_rotate_pic:
                startActivity(new Intent(this,RotateActivity.class));
            case R.id.btn_three_d:
            case R.id.btn_distribute:
            case R.id.btn_my3d:
                startActivity(new Intent(this, ThreeDBoxActivity.class));
                break;
            case R.id.btn_goto_finger:
                startActivity(new Intent(this, FingerUnlockActivity.class));
                break;
            default:
                break;
        }
    }



    public void test(){
        /**
         * {@link MainActivity}
         */
        String str = "";

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();//先判断网络可用
        networkInfo.isAvailable();
        networkInfo.isConnected();
        //再判断具体连接的是哪种网络
        if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            //手机
        }else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            //wifi
        }
        //已过时，不建议使用
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED){

        }else if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){

        }


        ITest iTest = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), new Class<?>[]{ITest.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                int value1 = (Integer) args[0];
                int value2 = (Integer) args[1];
                TargetApi targetApi = method.getAnnotation(TargetApi.class);
                Log.i(TAG,"参数为：" + value1 + " " + value2 + " " + "\n"
                + "方法名为：" + method.getName() + " \n"
                + "方法注释为：" + targetApi.value());
                return null;
            }
        });
        iTest.testMethod(10,12);


    }

    public interface ITest{
        @TargetApi(23)
        String testMethod(int a,int b);
    }

    public void testKeyboard(){
        //show keyboard1
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isShowing = inputMethodManager.isActive();
        Button btn = findViewById(R.id.btn_test);
        inputMethodManager.showSoftInput(btn,InputMethodManager.SHOW_FORCED);
        //toggle keyboard
        inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        //hide keyboard
        inputMethodManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


    }

    /*get heap memory*/
    private int testMemory(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass();
    }

    @CheckResult
    private String reflect(@NonNull String param){//@NonNull会添加对值非空的提示
        MainApplication testApplication = new MainApplication();
        try {
            MainApplication.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
           Class clazz =  Class.forName("com.example.zhaojing5.myapplication.bean.UserInfo");
            UserInfo userInfo = UserInfo.class.newInstance();

            userInfo = (UserInfo)clazz.newInstance();

            Constructor constructor = clazz.getDeclaredConstructor(new Class[]{String.class,String.class});
            constructor.setAccessible(true);
            constructor.newInstance(new Object[]{"zhangsan","pwd123"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }

    /* RxJava */
    public void testRxJava(){
        if(!subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
        observable.subscribe(observer);//观察者订阅被观察者事件
        observable.subscribe(subscriber);

        observable.subscribe(onNextAct);
        observable.subscribe(onNextAct,onNextAct1);
        observable.subscribe(onNextAct,onNextAct1,onComptAct);

        observable = Observable.just("Hello","Hi","Aloha");
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();
        observable = Observable.from(new String[]{"","","",""});
        Set<String> set = new HashSet<>();
        set.add("Hello");
        set.add("Hi");
        observable = Observable.from(set);
        //将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。

    }

    /**
     * 观察者
     **/
    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {

        }
    };

    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onStart() {
            super.onStart();
            //比Observer新增的
            //它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，例如数据的清零或重置。
            //它总是在 subscribe 所发生的线程被调用，而不能指定线程。
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {

        }
    };
    /**
     * 被观察者
     */
    Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("param1");
            subscriber.onCompleted();
        }
    });

    Action1<String> onNextAct = new Action1<String>() {
        @Override
        public void call(String s) {

        }
    };

    Action1<Throwable> onNextAct1 = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            //onError
        }
    };
    Action0 onComptAct = new Action0() {
        @Override
        public void call() {

        }
    };

    /*
    * 多进程防止Application反复初始化，仅当当前进程是主进程才去初始化
    */
    public boolean isMainProcess(Context mContext){
        String mainProcessName = mContext.getPackageName();
        int currentPid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo:runningAppProcessInfoList) {
            if(currentPid == runningAppProcessInfo.pid){
                if(mainProcessName.equals(runningAppProcessInfo.processName)){
                    return true;
                }
            }
        }
        return false;
    }

    public void testSomeDemo(){
        if(BuildConfig.DEBUG){
            Log.i(TAG,"debug log");
        }

        float convertValue = TypedValue.applyDimension(COMPLEX_UNIT_DIP,10,this.getResources().getDisplayMetrics());
        getResources().getIntArray(R.array.colorArray);

        int touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        VelocityTracker velocityTracker = VelocityTracker.obtain();
//        velocityTracker.addMovement();
        velocityTracker.computeCurrentVelocity(1000);
        velocityTracker.getXVelocity();
        velocityTracker.recycle();

        try {
            ApplicationInfo applicationInfo =  getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            String key = bundle.getString("meta-data_key");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Arrays.asList(new String[]{"value1","value2","value3"});
        Arrays.binarySearch(new String[]{"value1","value2","value3"},"findKey");
        btn_callClientApk.animate().x(100).y(200).setInterpolator(new BounceInterpolator()).setDuration(5000).start();
    }

    private ThreadLocal<Boolean> threadValue = new ThreadLocal<>();
    public void testThreadLocal(){
        threadValue.set(true);
        Log.i(TAG,"threadValue in mainThread is " + threadValue.get());
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadValue.set(false);
                Log.i(TAG,"threadValue in Thread1 is " + threadValue.get());
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"threadValue in Thread2 is " + threadValue.get());
            }
        }).start();
    }

    @BindView(R.id.btn_callClientApk)
    public Button btn_callClientApk;

    public ArrayList<String> getDevMountList(){
        ArrayList<String> realPathList = null;
        try {
            String[] pathList = FileUtils.read("/etc/vold.fstab").split(" ");
            int length = pathList.length;
            realPathList = new ArrayList<String>();
            for(int i = 0;i < length;i++){
                if(pathList[i].contains("dev_mount")){
                    if(new File(pathList[i+2]).exists()){
                        realPathList.add(pathList[1+2]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPathList;
    }

    public String getSDCardPath(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        ArrayList<String> pathList = getDevMountList();
        String realPath = "";
        int length = pathList.size();
        for (String path :pathList) {
            File file = new File(path);
            if(file.isDirectory() && file.canWrite()){
                realPath = file.getAbsolutePath();
                String newFileName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString();
                File newFile = new File("tmp_" + newFileName);
                if(newFile.mkdirs()){
                    newFile.delete();
                }else{
                    realPath = "";
                }
            }
        }
        return realPath;
    }

    //非静态初始化
    {
        Log.d(TAG,"动态");
    }

    public void testSnackBar(){
        Snackbar.make(btn_snackbar,"test snackbar!",Snackbar.LENGTH_LONG)
                .setAction("yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click SnackBar");
            }
        }).show();
    }

    public void testNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel1");
        Notification notification = builder.setContentTitle("您有一条消息")
                .setContentText("流量告急")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setColor(Color.parseColor("#ff0000"))
                .build();
        notificationManager.notify(1,notification);

    }

    public void testValueAnimator(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,80,30,100);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                Log.d(TAG,"ValueAnimator value is " + value);
            }
        });
        valueAnimator.start();
    }

    public void testObjectAnimator(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(btn_callClientApk,"alpha",0f,0.8f,0,1f);
        //alpha,rotation,translationX,scaleY
        //get,set方法有的，一定可以进行动画操作
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(TAG,"Animator is end .");
            }
        });
        objectAnimator.setDuration(800);
        objectAnimator.start();
    }

    public void testLoadXmlAnim(){
        Animator animator = AnimatorInflater.loadAnimator(this,R.animator.test_animators);
        animator.setTarget(btn_callClientApk);
        animator.start();
    }

    /**
     * convert map to list
     * @param maps
     * @return
     */
    public List<EventBundle> convertMapToList(Map<String,String> maps){
        if(maps.isEmpty()){
            return null;
        }
        List<EventBundle> bundleList = new ArrayList<>();
        Set<Map.Entry<String,String>> entries =  maps.entrySet();
        for (Map.Entry<String,String> entry:entries) {
            if(!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())){
                EventBundle eventBundle = new EventBundle(entry.getKey(),entry.getValue());
                bundleList.add(eventBundle);
            }
        }
        return bundleList;
    }

    public static class EventBundle{
        private String eventName;
        private String eventContent;

        public EventBundle(String eventName, String eventContent) {
            this.eventName = eventName;
            this.eventContent = eventContent;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventContent() {
            return eventContent;
        }

        public void setEventContent(String eventContent) {
            this.eventContent = eventContent;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d(TAG,"searchItem onMenuItemActionExpand MenuItem");
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d(TAG,"searchItem onMenuItemActionCollapse MenuItem");
                return false;
            }
        });
        MenuItem menuItem1 = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem1);
        shareActionProvider.setShareIntent(getShareIntent());
        return true;
    }

    private Intent getShareIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        return intent;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                Method m = null;
                try {
                    m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways(){
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_compose:
                ToastUtils.showToast(this,R.string.menu_toast);
                break;
            case R.id.action_delete:
                ToastUtils.showToast(this,R.string.menu_toast);
                break;
            case R.id.action_settings:
                ToastUtils.showToast(this,R.string.menu_toast);
                break;
            case R.id.home:
                ToastUtils.showToast(this,R.string.menu_toast);
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if(NavUtils.shouldUpRecreateTask(this,upIntent)){
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }else{
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this,upIntent);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tmp(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channelId1");
        builder.setSubText("subText");
        builder.setContentTitle("ContentTitle");
        builder.setContentText("content text");
        Intent intent = new Intent(this, EventBus.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        builder.setContentIntent(pendingIntent);
        builder.build();
    }


    public static class classA{
        public int variable;
        protected static String variable1 = "";
        public void function1(){
            Log.d(TAG,"println MainActivity`s TAG " + TAG);
        }
        public static void function2(){

        }
    }

    private void testConcurrent(){
        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        concurrentLinkedQueue.offer("test1");
        concurrentLinkedQueue.offer("test2");
        Log.i(TAG,"ConCurrentLinkedQueue poll 方法 " + concurrentLinkedQueue.poll());
        Log.i(TAG,"ConcurrentLinkedQueue peek 方法 " + concurrentLinkedQueue.peek());
        Log.i(TAG,"ConcurrentLinkedQueue peek 后是否省一个元素 " + concurrentLinkedQueue.size());
        int countNum = 5;
        CountDownLatch countDownLatch = new CountDownLatch(countNum);
        ExecutorService executorService = Executors.newFixedThreadPool(countNum);
        long startTime = System.currentTimeMillis();
        for( int i = 0; i < countNum ; i++ ){
            executorService.submit(new MyExecutorService(countDownLatch, i ));
        }
        try{
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        long endTime  = System.currentTimeMillis();
        long interval = endTime - startTime;
        Log.i(TAG,"一共耗时 " + interval + "毫秒。" );
        executorService.shutdown();

    }

    private static class MyExecutorService implements Runnable{

        private CountDownLatch countDownLatch;
        private int index;

        MyExecutorService(CountDownLatch countDownLatch,int index ){
            this.index = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            Log.i(TAG,"begin ExecutorService " + index + " task " );
            Log.i(TAG,"end ExecutorService " + index + " task ");
            countDownLatch.countDown();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void testMethos(){

//        setOverflowShowingAlways();
//        reflect("params");//@CheckResult会添加对返回值的未使用的提醒
//        test();
//        testThreadLocal();
//        testConcurrent();

//        MultiKnowledgePoint.testWeakYear();

//        MultiKnowledgePoint.testFileVisitor(this);



    }

}