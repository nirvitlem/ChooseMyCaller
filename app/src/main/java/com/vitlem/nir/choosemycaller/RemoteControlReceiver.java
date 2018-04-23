package com.vitlem.nir.choosemycaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RemoteControlReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("RemoteControlReceiver ", intent.getAction());
        if (intent.getAction().contains("VOLUME_CHANGED_ACTION")) {

            TimingService.setcurrentvolume();
            Log.i("VOLUME_CHANGED_ACTION ", String.valueOf(TimingService.volume));
            ListLog.addtolist("VOLUME_CHANGED_ACTION " + String.valueOf(TimingService.volume) + " " + GetCurrentTime.GetTime());
        }


        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
