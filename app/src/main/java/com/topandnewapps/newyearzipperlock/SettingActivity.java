package com.topandnewapps.newyearzipperlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.topandnewapps.newyearzipperlock.service.LockscreenService;
import com.topandnewapps.newyearzipperlock.service.LockscreenViewService;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {


    TextView SecurityText,Advance,Passcode,Wallpaper,Preview,Recovery;
    Spinner Delay,Attempt;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    Switch statusSwitch,locationswitch;
    Context sLockscreenActivityContext;
    ImageView custompencil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
        sLockscreenActivityContext=this;
        SecurityText=(TextView)findViewById(R.id.security);
        custompencil=(ImageView)findViewById(R.id.penciltext);
        SecurityText.setTypeface(null, Typeface.BOLD);
        Advance=(TextView)findViewById(R.id.advance);
        Advance.setTypeface(null,Typeface.BOLD);
        Passcode=(TextView)findViewById(R.id.passcode);
        Delay=(Spinner)findViewById(R.id.delay);
        Recovery=(TextView)findViewById(R.id.recovery);
        Attempt=(Spinner)findViewById(R.id.attempt);
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);

        List<Integer> categories = new ArrayList<Integer>();
        categories.add(1);
        categories.add(2);
        categories.add(3);
        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(SettingActivity.this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        Attempt.setAdapter(dataAdapter);
        Attempt.setSelection(sharedPreference.getInt("numberofattempt",0));
        Attempt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int n=position;
                SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreference.edit();
                edit.putInt("numberofattempt",n);
                edit.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> delaylist = new ArrayList<String>();
        delaylist.add("5 sec");
        delaylist.add("10 sec");
        delaylist.add("30 sec");
        delaylist.add("1 min");
        delaylist.add("5 min");
        // Creating adapter for spinner
        ArrayAdapter<String> delayAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, delaylist);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        Delay.setAdapter(delayAdapter);
        Delay.setSelection(sharedPreference.getInt("delay",0));
        Delay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreference.edit();
                int n1=position;
                editor.putInt("delay",n1);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Preview = (TextView) findViewById(R.id.preview);
        Preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(SettingActivity.this, setlockscreen.class);
                     intent.putExtra("intenttype","preview");
                   startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreference.edit();
                boolean set=sharedPreference.getBoolean("set",false);
 if(set){
     Toast.makeText(getApplicationContext(),"Passcode Already Set",Toast.LENGTH_SHORT).show();
     return;
 }
                try {
                    Intent intent = new Intent(SettingActivity.this,setlockscreen.class);
                    intent.putExtra("intenttype", "setpass");
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, setlockscreen.class);
                intent.putExtra("intenttype","recovery");
                startActivity(intent);
            }
        });


    }
    public void customtext(View view){
Intent intent=new Intent(SettingActivity.this,customtext.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_call, menu);

        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
MenuItem menuItem=menu.findItem(R.id.myswitch);
        menuItem.setActionView(R.layout.switch_layout);
        final Switch switch1= (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
        switch1.setChecked(sharedPreference.getBoolean("appstatus",false));
       switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               try {
                   SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreference.edit();
                   boolean set = sharedPreference.getBoolean("set", false);
                   if (isChecked) {
                       if (!set) {
                           Toast.makeText(getApplicationContext(), "passcode not set", Toast.LENGTH_LONG).show();
                           switch1.setChecked(false);
                           return;
                       }
                       SharedPreferencesUtil.setBoolean(Lockscreen.ISLOCK, true);
                       editor.putBoolean("appstatus", true);
                       editor.commit();
                       Lockscreen.getInstance(sLockscreenActivityContext).startLockscreenService();
                   } else {
                       SharedPreferencesUtil.setBoolean(Lockscreen.ISLOCK, false);
                       Lockscreen.getInstance(sLockscreenActivityContext).stopLockscreenService();
                       editor.putBoolean("appstatus", false);
                       editor.commit();
                   }
               }catch (Exception e){
                   Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
               }
           }
       });

        return true;
    }




}
