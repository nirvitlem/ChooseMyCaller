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
        Log.i("RemoteControlReceiver", intent.getAction() + " " + String.valueOf(CustomPhoneStateListener.CallStatus));
        if (intent.getAction().contains("VOLUME_CHANGED_ACTION") && CustomPhoneStateListener.CallStatus==0)
        {
            TimingService.setcurrentvolume();
            Log.i("VOLUME_CHANGED_ACTION", "VOLUME_CHANGED_ACTION");
            ListLog.addtolist("VOLUME_CHANGED_ACTION "  +String.valueOf(TimingService.volume) + " "+ GetCurrentTime.GetTime());
        }
      /*  if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()) {
                TimingService.setcurrentvolume();
                Log.i("KEYCODE_VOLUME_UP", "KEYCODE_VOLUME_UP");
                ListLog.addtolist("KEYCODE_VOLUME_UP "  +String.valueOf(TimingService.volume) + " "+ GetCurrentTime.GetTime());
            }
            if (KeyEvent.KEYCODE_VOLUME_DOWN == event.getKeyCode()) {
                TimingService.setcurrentvolume();
                Log.i("KEYCODE_VOLUME_DOWN", "KEYCODE_VOLUME_DOWN");
                ListLog.addtolist("KEYCODE_VOLUME_DOWN " +String.valueOf(TimingService.volume) + " "+ GetCurrentTime.GetTime());
            }
        }*/


        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
