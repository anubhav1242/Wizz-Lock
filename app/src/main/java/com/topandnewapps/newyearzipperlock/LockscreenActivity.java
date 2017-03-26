package com.topandnewapps.newyearzipperlock;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

import android.content.Context;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;

import android.os.Message;

import android.support.v7.app.AppCompatActivity;

import android.telephony.PhoneStateListener;

import android.telephony.TelephonyManager;

import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;

import android.view.WindowManager;

import android.widget.RelativeLayout;
import android.widget.Toast;


import com.topandnewapps.newyearzipperlock.service.LockscreenService;
import com.topandnewapps.newyearzipperlock.service.LockscreenViewService;


/**
 * Created by Hammad Shah
 */

public class LockscreenActivity extends AppCompatActivity {
    Bundle bundle;
    String s;
    Context ctx;

  private final String TAG = "LockscreenActivity";
  private static Context sLockscreenActivityContext = null;
   private boolean isReceiverRegistered=true;
   
 private RelativeLayout mLockscreenMainLayout = null;
 
   public static SendMassgeHandler mMainHandler = null;
 
   public PhoneStateListener phoneStateListener = new PhoneStateListener() {
   
     public void onCallStateChanged(int state, String incomingNumber) {

    
        switch (state) {
  
              case TelephonyManager.CALL_STATE_RINGING:
   
                 sendBroadcast(new Intent("com.lock.call"));

    
                if (isReceiverRegistered){
       
                 Lockscreen.getInstance(sLockscreenActivityContext).stopLockscreenService();
 
                       isReceiverRegistered=false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if(!isReceiverRegistered)
                    Lockscreen.getInstance(sLockscreenActivityContext).startLockscreenService();
                    isReceiverRegistered=true;
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ctx=this;
        s = getIntent().getStringExtra("intenttype");
        sLockscreenActivityContext = this;
        mMainHandler = new SendMassgeHandler();
//        getWindow().setType(2004);
//        getWindow().addFlags(524288);
//        getWindow().addFlags(4194304);
        ///
        getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        initLockScreenUi();

    setLockGuard();

    }

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finishLockscreenAct();
        }
    }

    private void finishLockscreenAct() {
        finish();
    }


    private void initLockScreenUi() {
        setContentView(R.layout.activity_lockscreen);
       mLockscreenMainLayout = (RelativeLayout) findViewById(R.id.lockscreen_main_layout);
       mLockscreenMainLayout.getBackground().setAlpha(15);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void setLockGuard() {
        boolean isLockEnable = false;
        if (!LockscreenUtil.getInstance(sLockscreenActivityContext).isStandardKeyguardState()) {
            isLockEnable = false;
        } else {
            isLockEnable = true;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("changeservice",1);
        editor.commit();
        Intent startLockscreenIntent = new Intent(LockscreenActivity.this, LockscreenViewService.class);
        startLockscreenIntent.putExtra("intenttype",s);
        startService(startLockscreenIntent);
        boolean isSoftkeyEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isSoftKeyAvail(this);
        SharedPreferencesUtil.setBoolean(Lockscreen.ISSOFTKEY, isSoftkeyEnable);
        if (!isSoftkeyEnable) {
            mMainHandler.sendEmptyMessage(0);
        } else if (isSoftkeyEnable) {
            if (isLockEnable) {
                mMainHandler.sendEmptyMessage(0);
            }
        }
    }
    public static void preventStatusBarExpansion(Context context) {
        WindowManager  manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity)context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

       customViewGroup view = new customViewGroup(context);

          manager.addView(view, localLayoutParams);

    }

    public static class customViewGroup extends ViewGroup {

        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_call, menu);
        return true;
    }
}
