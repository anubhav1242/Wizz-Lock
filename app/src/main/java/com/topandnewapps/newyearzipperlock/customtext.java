package com.topandnewapps.newyearzipperlock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mubashshir on 3/25/2017.
 */

public class customtext extends AppCompatActivity {
    EditText t1,t2;
    TextView tv1,tv2;
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.customtext);
        t1=(EditText)findViewById(R.id.text1);
        t2=(EditText)findViewById(R.id.text2);
        SharedPreferences shared=getSharedPreferences("myapp",Context.MODE_PRIVATE);
        t1.setText(shared.getString("customtext1","Getting Location Details"));
        t2.setText(shared.getString("customtext2","Unwanted user trying to access mobile"));
        tv1=(TextView)findViewById(R.id.textview1);
        tv2=(TextView)findViewById(R.id.textview2);
        tv1.setTypeface(null, Typeface.BOLD);
        tv2.setTypeface(null, Typeface.BOLD);
    }
    public void save_text(View view){
        String text1=t1.getText().toString();
        String text2=t2.getText().toString();
        SharedPreferences sharedPreference = getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putString("customtext1",text1);
        editor.putString("customtext2",text2);
        editor.commit();
        Toast.makeText(getApplicationContext(),"Changes saved",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_call, menu);
        Switch switch1= (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
        switch1.setVisibility(View.INVISIBLE);
        return true;
    }
}
