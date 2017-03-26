package com.topandnewapps.newyearzipperlock.service;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topandnewapps.newyearzipperlock.LockscreenActivity;
import com.topandnewapps.newyearzipperlock.LockscreenUtil;
import com.topandnewapps.newyearzipperlock.R;
import com.topandnewapps.newyearzipperlock.SharedPreferencesUtil;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by mugku on 15. 5. 20..
 */
public class LockscreenViewService extends Service{
    private RelativeLayout batteryImg;

    private boolean isPowerReceiverRegistered;
    private boolean isTimeReceiverRegistered;
    private TextView tim, date,charger,battery,tvv,smss,ampm;
    private Typeface tf,uf;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private View mLockscreenView = null;
    private WindowManager mWindowManager;
    Runnable runnable;
    private WindowManager.LayoutParams mParams;
    int dotposition = 0;
    String textdata;
    Handler h = new Handler();
    String entertext = "", confirmtext = "";
    RelativeLayout layout;
    TextView progresstext;
    String value = "";
    private int prev_state;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,dot1,dot2,dot3,dot4;
    TextView passtext,looptext;
    /*Declar Zipper Functions*/
    private ImageView zipImageView;
    private final int[] IMAGE_UNZIP = {
            R.drawable.l1,R.drawable.l2, R.drawable.l3, R.drawable.l4, R.drawable.l5,
            R.drawable.l6, R.drawable.l7, R.drawable.l8, R.drawable.l9
    };
    private int frameNumber = 0;
    private boolean isDownFromStart = false;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mStartWidthRange;
    private int mEndWidthRange;
    private RelativeLayout patch,ribbon,center;
    private boolean mIsLockEnable = false;
    private boolean mIsSoftkeyEnable = false;
    private int mDeviceWidth = 0;
    private int mDevideDeviceWidth = 0;
    private float mLastLayoutX = 0;
    private int mServiceStartId = 0;
     private MediaPlayer mp;
    int val = 0;
    int n1;
    int progressval=0;
    TextView progresstext1;
    private long fileSize = 0;
    ProgressBar progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    int change=0;
    private SendMassgeHandler mMainHandler = null;
    int checkvalue=0;
    TextView cancel;
    String password;
//    private boolean sIsSoftKeyEnable = false;

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SharedPreferencesUtil.init(mContext);
        TelephonyManager tManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        StateListener PhoneListener = new StateListener();
        // Register listener for LISTEN_CALL_STATE
        tManager.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
//        sIsSoftKeyEnable = SharedPreferencesUtil.get(Lockscreen.ISSOFTKEY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        int n=sharedPreference.getInt("changeservice",1);
        if(intent!=null) {
            if (n == 1) {
                value = intent.getStringExtra("intenttype");
                password = sharedPreference.getString("password", "");
                n = sharedPreference.getInt("numberofattempt", 0);
        /*    if (value == null) {
                if (!password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Passcode Already Set", Toast.LENGTH_SHORT).show();
                    return START_NOT_STICKY;
                }
            } */
                editor.putInt("changeservice",0);
                editor.commit();
            }


        }

        mMainHandler = new SendMassgeHandler();
            if (isLockScreenAble()) {

                if (null != mWindowManager) {

                    if (null != mLockscreenView) {

                        mWindowManager.removeView(mLockscreenView);
                    }
                    mWindowManager = null;
                    mParams = null;
                    mInflater = null;
                    mLockscreenView = null;
                }

                initState();
                 initView();
                attachLockScreenView();
            }
            return LockscreenViewService.START_NOT_STICKY;


    }

    @Override
    public void onDestroy() {
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putInt("changeservice",1);
        editor.commit();
        dettachLockScreenView();
    }

    private void initState() {

        mIsLockEnable = LockscreenUtil.getInstance(mContext).isStandardKeyguardState();
       if (mIsLockEnable) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                    PixelFormat.TRANSLUCENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mIsLockEnable && mIsSoftkeyEnable) {
                mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            } else {
                mParams.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            }
        } else {
            mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }

        if (null == mWindowManager) {
            mWindowManager = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE));
        }

    }

    private void initView() {
        if (null == mInflater) {
            mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (null == mLockscreenView) {
            mLockscreenView = mInflater.inflate(R.layout.activity_zipper, null);
        }
    }
    private boolean isLockScreenAble() {
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
       // boolean isLock = SharedPreferencesUtil.get(Lockscreen.ISLOCK);
        boolean isLock=sharedPreference.getBoolean("appstatus",false);
        if (isLock) {
            isLock = true;
        } else {
            isLock = false;
        }
        return isLock;
    }


    private void attachLockScreenView() {
        if (null != mWindowManager && null != mLockscreenView && null != mParams) {
            mLockscreenView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mWindowManager.addView(mLockscreenView, mParams);
            settingLockView();
        }

    }


    private boolean dettachLockScreenView() {
        if (null != mWindowManager && null != mLockscreenView) {
            mWindowManager.removeView(mLockscreenView);
            mLockscreenView = null;
            mWindowManager = null;
            stopSelf(mServiceStartId);
            return true;
        } else {
            return false;
        }
    }


    void DotPosition(){
        if(dotposition==0){
            dot1.setBackgroundResource(R.drawable.dot_fill);
        }
        else if(dotposition==1){
            dot2.setBackgroundResource(R.drawable.dot_fill);
        }
        else if(dotposition==2){
            dot3.setBackgroundResource(R.drawable.dot_fill);
        }
        else {
            dot4.setBackgroundResource(R.drawable.dot_fill);
        }
    }
    void onLongclick1(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b1";
        } else {
            confirmtext += "b1";

        }
        DotCheck();
    }
    void onLongclick2(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b2";
        } else {
            confirmtext += "b2";


        }
        DotCheck();
    }
    void onLongclick3(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b3";
        } else {
            confirmtext += "b3";


        }
        DotCheck();
    }
    void onLongclick4(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b4";
        } else {
            confirmtext += "b4";


        }
        DotCheck();
    }
    void onLongclick5(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b5";
        } else {
            confirmtext += "b5";


        }
        DotCheck();
    }
    void onLongclick6(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b6";
        } else {
            confirmtext += "b6";


        }
        DotCheck();
    }
    void onLongclick7(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b7";
        } else {
            confirmtext += "b7";


        }
        DotCheck();
    }
    void onLongclick8(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b8";
        } else {
            confirmtext += "b8";


        }
        DotCheck();
    }
    void onLongclick9(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "b9";
        } else {
            confirmtext += "b9";


        }
        DotCheck();
    }
    void click1(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B1";
        } else {
            confirmtext += "B1";


        }
        DotCheck();
    }
    void click2(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B2";
        } else {
            confirmtext += "B2";


        }
        DotCheck();
    }
    void click3(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B3";
        } else {
            confirmtext += "B3";


        }
        DotCheck();
    }
    void click4(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B4";
        } else {
            confirmtext += "B4";


        }
        DotCheck();
    }
    void click5(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B5";
        } else {
            confirmtext += "B5";


        }
        DotCheck();
    }
    void click6(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B6";
        } else {
            confirmtext += "B6";


        }
        DotCheck();
    }
    void click7(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {
            entertext += "B7";
        } else {
            confirmtext += "B7";


        }
        DotCheck();
    }
    void click8(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B8";
        } else {
            confirmtext += "B8";


        }
        DotCheck();
    }
    void click9(){
        DotPosition();
        dotposition+=1;
        if(passtext.getText().equals("Enter Password") || passtext.getText().equals("Enter Current Password") || passtext.getText().equals("Type Password")) {

            entertext += "B9";
        } else {
            confirmtext += "B9";


        }
        DotCheck();
    }

    int getdelay(int delay){
        int n=0;

        switch (delay){
            case  0:
                n= 5000;
                break;
            case  1:
                n= 10000;
                break;
            case 2:
                n= 30000;
                break;
            case 3:
                n= 60000;
                break;
            case 4:
                n= 300000;
                break;

        }
        return n;
    }

    void disablebutton(){
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
    }
    void enablebutton(){
        looptext.setVisibility(View.INVISIBLE);
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        b4.setEnabled(true);
        b5.setEnabled(true);
        b6.setEnabled(true);
        b7.setEnabled(true);
        b8.setEnabled(true);
        b9.setEnabled(true);
    }
    void disablescreen(int delay) {
       disablebutton();
        new CountDownTimer(delay, 1000) {
            public void onTick(long millisUntilFinished) {
              //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                looptext.setTypeface(null, Typeface.BOLD);
                looptext.setText("Try after  "+ (millisUntilFinished/1000)+" sec");
                looptext.setVisibility(View.VISIBLE);
            }
            public void onFinish() {
               enablebutton();
            //   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }.start();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void shownotification(){
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        String time=sharedPreference.getString("currenttime","time");
        String date=sharedPreference.getString("currentdate","date");
        Intent intent = new Intent(this, LockscreenViewService.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String message="Someone tried to unlock your phone on "+ " "+date+ " at " +time;
        editor.putString("currenttime","time");
        editor.putString("currentdate","date");
        editor.commit();
// build notification
// the addAction re-use the same intent to keep the example short
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Unwanted user found")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);
        notificationManager.notify(1001, mBuilder.build());
    }
    void faketext(){
        layout.setVisibility(View.VISIBLE);
        progresstext.setTextColor(getResources().getColor(R.color.errorColor));
        progresstext1.setTextColor(getResources().getColor(R.color.errorColor));
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy ");
        SimpleDateFormat formatter1 = new SimpleDateFormat(" aa");
        Date date=new Date();
        String strDate= formatter.format(date);
        String ap=formatter1.format(date);
        editor.putString("currentdate",strDate);
        editor.putString("currenttime", String.valueOf(hours)+":"+String.valueOf(minutes)+" "+ap);
        editor.commit();
        disablebutton();
        progresstext1.setText(sharedPreference.getString("customtext1","Getting Location Details"));
        progresstext.setText(sharedPreference.getString("customtext2","Unwanted user trying to access mobile"));
            h.postDelayed(new Runnable() {
                public void run() {
                    //do something
                    hidelayout();
                    enablebutton();
                }
            }, 8000);



   /*     for(int i=0;i<100;i++){
            progressBar.setProgress((i+1));
            SystemClock.sleep(200);
        }
        */

              progressBarStatus = 0;

        //reset filesize

   /*    new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    // process some tasks
                    progressBarStatus = doSomeTasks();
                    // your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(3000);
                        progresstext.setText("Sending location data");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                     checkvalue=1;
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
               /*     progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                            if(progressBarStatus>=40) {
                                progresstext.setText("Sending location data");
                            }
                        }
                    });
                }

                // ok, file is downloaded,
                if (progressBarStatus >= 100) {

                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        checkvalue=1;
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // close the progress bar dialog

                }
            }
        }).start();
                */



    }
    void hidelayout(){
        layout.setVisibility(View.INVISIBLE);
    }
    void DotCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int delay=sharedPreferences.getInt("delay",0);
        n1=sharedPreferences.getInt("numberofattempt",0);
        n1+=1;
        String textdata = passtext.getText().toString();
        if (dotposition == 4) {
            dot1.setBackgroundResource(R.drawable.dot_empty);
            dot2.setBackgroundResource(R.drawable.dot_empty);
            dot3.setBackgroundResource(R.drawable.dot_empty);
            dot4.setBackgroundResource(R.drawable.dot_empty);
            if (textdata.equals("Type Password")) {
                String lastpassword = sharedPreferences.getString("password", "");
                if (entertext.equals(lastpassword)) {
                    editor.putInt("changeservice", 1);
                    editor.commit();
                    String getdate=sharedPreferences.getString("currenttime","time");
                    if(!getdate.equals("time")) {
                        shownotification();
                    }
                    dettachLockScreenView();
                } else {
                    entertext = "";
                    if (value != null) {
                        if (value.equals("setpass")) {
                        }  else if(value.equals("screenoff")) {
                            {
                                val += 1;
                                if (val > n1) {
                                    if (change == 0) {
                                        int d = getdelay(delay);
                                        disablescreen(d);
                                        change = 1;
                                    } else {
                                        faketext();
                                        change = 0;
                                    }

                                }
                            }
                        }
                    }
                }
            }
            if (textdata.equals("Enter Current Password")) {
                String lastpassword = sharedPreferences.getString("password", "");
                if (lastpassword.equals(entertext)) {
                    passtext.setText("Enter Password");
                    entertext = "";
                    confirmtext = "";
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Correct password", Toast.LENGTH_LONG).show();
                    entertext = "";
                    confirmtext = "";
                    dotposition = 0;
                    return;
                }


            }
            if (textdata.equals("Enter Password")) {
                if(value!=null){
                    if(value.equals("preview")){
                        dotposition=0;
                        return;
                    }
                }
                passtext.setText("Confirm Password");
            }
            if (textdata.equals("Confirm Password")) {
                if (entertext.equals(confirmtext)) {
                    Toast.makeText(getApplicationContext(), "Passcode Set", Toast.LENGTH_LONG).show();
                    editor.putString("password", entertext);
                    editor.putBoolean("set",true);
                    editor.commit();
                    dettachLockScreenView();

                   // finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Passcode", Toast.LENGTH_LONG).show();
                    passtext.setText("Enter Password");
                    entertext = "";
                    confirmtext = "";
                }
            }


            dotposition = 0;
        }
    }
    private void settingLockView() {
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        passtext = (TextView)mLockscreenView. findViewById(R.id.passtext);
        dot1 = (Button)mLockscreenView. findViewById(R.id.dot1);
        dot2 = (Button)mLockscreenView. findViewById(R.id.dot2);
        dot3 = (Button)mLockscreenView. findViewById(R.id.dot3);
        dot4 = (Button)mLockscreenView. findViewById(R.id.dot4);
        b1=(Button)mLockscreenView.findViewById(R.id.b1);
        b2=(Button)mLockscreenView.findViewById(R.id.b2);
        b3=(Button)mLockscreenView.findViewById(R.id.b3);
        b4=(Button)mLockscreenView.findViewById(R.id.b4);
        b5=(Button)mLockscreenView.findViewById(R.id.b5);
        b6=(Button)mLockscreenView.findViewById(R.id.b6);
        b7=(Button)mLockscreenView.findViewById(R.id.b7);
        b8=(Button)mLockscreenView.findViewById(R.id.b8);
        b9=(Button)mLockscreenView.findViewById(R.id.b9);
        cancel=(TextView)mLockscreenView.findViewById(R.id.cancel);
        progresstext=(TextView)mLockscreenView.findViewById(R.id.progresstext);
        progressBar=(ProgressBar)mLockscreenView.findViewById(R.id.progress);
        progresstext1=(TextView)mLockscreenView.findViewById(R.id.progresstext1);
        layout=(RelativeLayout)mLockscreenView.findViewById(R.id.layout);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotposition=0;
                entertext="";
                confirmtext="";
                dot1.setBackgroundResource(R.drawable.dot_empty);
                dot2.setBackgroundResource(R.drawable.dot_empty);
                dot3.setBackgroundResource(R.drawable.dot_empty);
                dot4.setBackgroundResource(R.drawable.dot_empty);
            }
        });
        if (value != null) {
            if(value.equals("preview")){
                passtext.setText("Enter Password");
                entertext="preview";
            }else
            if (value.equals("recovery")) {
                passtext.setText("Enter Current Password");
            } else if (value.equals("screenoff")) {
                boolean status=sharedPreference.getBoolean("status",true);
                //   getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                if(status){
                    //  getActionBar().hide();
                    //   preventStatusBarExpansion(ctx);
                }
                passtext.setText("Type Password");
                entertext = "";

            }

        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click1();
            }
        });
       b1.setOnLongClickListener(new View.OnLongClickListener() {

                                     @Override
                                     public boolean onLongClick(View v) {
                                         onLongclick1();
                                         return true;
                                     }
                                 });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click2();
            }
        });
        b2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick2();
                return true;
            }
        });

        b3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick3();
                return true;
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click3();
            }
        });
        b4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick4();
                return true;
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click4();
            }
        });
        b5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick5();
                return true;
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click5();
            }
        });
        b6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick6();
                return true;
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click6();
            }
        });
        b7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick7();
                return true;
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click7();
            }
        });
        b8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick8();
                return true;
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click8();
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click9();
            }
        });
        b9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongclick9();
                return true;
            }
        });

     /*   b1.setOnLongClickListener(new ClickListener()); */

    /*    b3.setOnLongClickListener(this);
        b4.setOnLongClickListener(this);
        b5.setOnLongClickListener(this);
        b6.setOnLongClickListener(this);
        b7.setOnLongClickListener(this);
        b8.setOnLongClickListener(this);
        b9.setOnLongClickListener(this);*/
        looptext = (TextView) mLockscreenView.findViewById(R.id.looptext);
     //   textdata = passtext.getText().toString();
/*

        AdView adView = (AdView)mLockscreenView.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        tf = Typeface.createFromAsset(getAssets(),"roboto.ttf");
        uf = Typeface.createFromAsset(getAssets(),"roboto.ttf");
        date=(TextView) mLockscreenView.findViewById(R.id.tv_date);
        tim=(TextView)mLockscreenView.findViewById(R.id.tv_time);
        ampm=(TextView)mLockscreenView.findViewById(R.id.tv_ampm);
        charger = (TextView)mLockscreenView.findViewById(R.id.charger);
        battery = (TextView)mLockscreenView.findViewById(R.id.bettry);
        tim.setTypeface(tf);
        date.setTypeface(uf);
        ampm.setTypeface(uf);
        registerReceiver(mbatinforeceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        isPowerReceiverRegistered=true;
        IntentFilter localIntentFilter1 = new IntentFilter();
        localIntentFilter1.addAction("android.intent.action.TIME_TICK");

        timeChangeReceiver = new TimeChangeReceiver(tim, date,ampm);
        registerReceiver(timeChangeReceiver, localIntentFilter1);
        isTimeReceiverRegistered = true;


        new TimeAndDateSetter(tim, date,ampm).setTimeAndDate();

        zipImageView = (ImageView)mLockscreenView.findViewById(R.id.zipImageView);
        zipImageView.setBackgroundResource(IMAGE_UNZIP[0]);
        new Handler(new Handler.Callback() {

            public boolean handleMessage(Message paramMessage) {
                zipImageView.setBackgroundResource(IMAGE_UNZIP[paramMessage.arg1]);
                frameNumber = paramMessage.arg1;
                return false;
            }
        });
        zipImageView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int i = 0;

                mScreenHeight = zipImageView.getHeight();
                mScreenWidth = zipImageView.getWidth();

                mStartWidthRange = (2 * (mScreenWidth / 5));
                mEndWidthRange = (3 * (mScreenWidth / 5));

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        if ((event.getY() < mScreenHeight / 4)
                                && (event.getX() > mStartWidthRange)
                                && (event.getX() < mEndWidthRange)) {

                            isDownFromStart = true;

                        } else
                            isDownFromStart = false;

                        break;
                    case MotionEvent.ACTION_MOVE:


                        if (isDownFromStart)

                            if ((event.getX() > mStartWidthRange)
                                    && (event.getX() < mEndWidthRange)
                                    && isDownFromStart) {

                                i = (int) (event.getY() / (mScreenHeight / 9));
                                setImage(i);
                            }

                        break;

                    case MotionEvent.ACTION_UP:


                        if (frameNumber >= 8) {
                            frameNumber = 0;
                            isDownFromStart = true;

                            mp = MediaPlayer.create(getApplicationContext(),
                                    R.raw.iphone);


                            mp.start();


                            dettachLockScreenView();

                        } else {
                            frameNumber = 0;
                            setImage(0);


                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });


*/

    }



    /*Changing Frames of Zipper*/
    private void setImage(int paramInt) {
        switch (paramInt) {

            case 0:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[0]);
                frameNumber = 1;

                break;
            case 1:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[1]);
                frameNumber = 2;


                break;
            case 2:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[2]);
                frameNumber = 3;

                break;
            case 3:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[3]);
                frameNumber = 4;


                break;
            case 4:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[4]);
                frameNumber = 5;

                break;

            case 5:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[5]);
                frameNumber = 6;

                break;
            case 6:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[6]);
                frameNumber = 7;



                break;
            case 7:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[7]);
                frameNumber = 8;
                //patch.setVisibility(View.VISIBLE);
                break;
            case 8:
                zipImageView.setBackgroundResource(IMAGE_UNZIP[8]);
                frameNumber = 9;
                break;
		/*case 9:
			zipImageView.setBackgroundResource(IMAGE_UNZIP[9]);
			frameNumber = 10;
			break;*/

            default:
                return;
        }
    }
    class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    prev_state=state;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    prev_state=state;
                    dettachLockScreenView();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    dettachLockScreenView();
                    if((prev_state==TelephonyManager.CALL_STATE_OFFHOOK)){
                        prev_state=state;
                        Intent startLockscreenActIntent = new Intent(mContext, LockscreenActivity.class);
                        startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startLockscreenActIntent);
                        Intent startLockscreenIntent = new Intent(mContext, LockscreenViewService.class);
                        startLockscreenIntent.putExtra("intenttype","screenoff");
                        startService(startLockscreenIntent);
                        //Answered Call which is ended
                    }
                    if((prev_state==TelephonyManager.CALL_STATE_RINGING)){
                        prev_state=state;
                        Intent startLockscreenActIntent = new Intent(mContext, LockscreenActivity.class);
                        startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startLockscreenActIntent);
                        Intent startLockscreenIntent = new Intent(mContext, LockscreenViewService.class);
                        startLockscreenIntent.putExtra("intenttype","screenoff");
                        startService(startLockscreenIntent);
                        //Rejected or Missed call
                    }
                    break;
            }
        }
    };
}
