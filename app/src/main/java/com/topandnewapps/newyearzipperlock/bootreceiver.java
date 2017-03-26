package com.topandnewapps.newyearzipperlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.topandnewapps.newyearzipperlock.service.LockscreenService;

/**
 * Created by Mubashshir on 3/25/2017.
 */

public class bootreceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"phone started",Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreference = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        int n = sharedPreference.getInt("changerec", 0);
            Intent intent1 = new Intent(context, LockscreenService.class);
            context.startService(intent1);
            Intent intent2 = new Intent(context, LockscreenActivity.class);
            intent2.putExtra("intenttype", "screenoff");
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            editor.putInt("n",1);
            editor.commit();

    }
}
