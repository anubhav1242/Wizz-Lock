package com.topandnewapps.newyearzipperlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topandnewapps.newyearzipperlock.service.LockscreenViewService;

/**
 * Created by Mubashshir on 3/24/2017.
 */

public class setlockscreen extends Activity {
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
    int checkvalue=0;
    int longcount=0;
    String password;
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.setlockscreen);
        value=getIntent().getStringExtra("intenttype");
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        passtext = (TextView) findViewById(R.id.passtext);
        dot1 = (Button) findViewById(R.id.dot1);
        dot2 = (Button)findViewById(R.id.dot2);
        dot3 = (Button) findViewById(R.id.dot3);
        dot4 = (Button) findViewById(R.id.dot4);
        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        b3=(Button)findViewById(R.id.b3);
        b4=(Button)findViewById(R.id.b4);
        b5=(Button)findViewById(R.id.b5);
        b6=(Button)findViewById(R.id.b6);
        b7=(Button)findViewById(R.id.b7);
        b8=(Button)findViewById(R.id.b8);
        b9=(Button)findViewById(R.id.b9);
        progresstext=(TextView)findViewById(R.id.progresstext);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        progresstext1=(TextView)findViewById(R.id.progresstext1);
        layout=(RelativeLayout)findViewById(R.id.layout);
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
        longcount+=1;
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
    public void cancelclick(View view){
        dotempty();
        dotposition=0;
        longcount=0;
    }
    void dotempty(){
        dot1.setBackgroundResource(R.drawable.dot_empty);
        dot2.setBackgroundResource(R.drawable.dot_empty);
        dot3.setBackgroundResource(R.drawable.dot_empty);
        dot4.setBackgroundResource(R.drawable.dot_empty);
    }
    void DotCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int delay=sharedPreferences.getInt("delay",0);
        n1=sharedPreferences.getInt("numberofattempt",0);
        n1+=1;
        String textdata = passtext.getText().toString();
        if (dotposition == 4) {
           dotempty();
            if (textdata.equals("Type Password")) {
                String lastpassword = sharedPreferences.getString("password", "");
                if (entertext.equals(lastpassword)) {
                    editor.putInt("changeservice", 1);
                    editor.commit();
                } else {
                    entertext = "";
                    if (value != null) {
                        if (value.equals("setpass")) {
                            dotempty();
                            dotposition=0;
                        }
                    }
                }
            }
            if (textdata.equals("Enter Password")) {
                if(value!=null){
                    if(value.equals("preview")){
                        dotposition=0;
                        return;
                    }else if(value.equals("setpass")){
                        if(longcount==0){
                            Toast.makeText(getApplicationContext(),"Passcode must contains atleast 1 long click on button",Toast.LENGTH_SHORT).show();
                            dotempty();
                            dotposition=0;
                            return;
                        }
                    }
                }
                if(longcount==0){
                    Toast.makeText(getApplicationContext(),"Passcode must contains atleast 1 long click on button",Toast.LENGTH_SHORT).show();
                    dotempty();
                    dotposition=0;
                    return;
                }
                passtext.setText("Confirm Password");
            }
            if (textdata.equals("Enter Current Password")) {
                String lastpassword = sharedPreferences.getString("password", "");
                if (lastpassword.equals(entertext)) {
                    passtext.setText("Enter Password");
                    longcount=0;
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

            if (textdata.equals("Confirm Password")) {
                if (entertext.equals(confirmtext)) {
                    Toast.makeText(getApplicationContext(), "Passcode Set", Toast.LENGTH_LONG).show();
                    editor.putString("password", entertext);
                    editor.putBoolean("set",true);
                    editor.commit();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Passcode", Toast.LENGTH_LONG).show();
                    passtext.setText("Enter Password");
                    entertext = "";
                    confirmtext = "";
                    longcount=0;
                }
            }


            dotposition = 0;
        }
    }
}
