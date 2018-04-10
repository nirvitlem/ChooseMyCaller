package com.vitlem.nir.choosemycaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class SensorRestarterBroadcastReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(" SensorRestarterBroadcastReceive" ,"Service Stops");
        Log.i(SensorRestarterBroadcastReceive.class.getSimpleName(), "Service Stops");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, TimingService.class));
        } else {
            context.startService(new Intent(context, TimingService.class));
        }
       // context.startService(new  Intent(context, TimingService.class));;
    }
}
