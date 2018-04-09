package com.vitlem.nir.choosemycaller;

import android.graphics.Color;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

public class CustomPhoneStateListener extends PhoneStateListener {

    public static String LOG_TAG = "PhoneStateListener";


    @Override
    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        super.onCellInfoChanged(cellInfo);
        Log.i(LOG_TAG, "onCellInfoChanged: " + cellInfo);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);


        if (incomingNumber == null) incomingNumber = "null";


        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:

               // MainAppWidget.SetText("onCallStateChanged: CALL_STATE_IDLE " + System.currentTimeMillis());
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
                TimingService.StopPalyPlayer();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
                Log.i(LOG_TAG, "incomingNumber: " + incomingNumber);
                TimingService.runGetVolumep();
                if (MainAppWidget.listItems != null) {
                    for (String item : MainAppWidget.listItems) {
                        MainAppWidget.SetText(incomingNumber + " N " + GetCurrentTime.GetTime(), Color.GRAY);
                        if (incomingNumber.equals(item.split("#")[0].toString())) {
                            MainAppWidget.SetText(incomingNumber + " Y " + GetCurrentTime.GetTime(), Color.GREEN);
                            TimingService.runGetVolumep();
                        }
                    }
                }
                //if (incomingNumber.equals("0543205519") || incomingNumber.equals("0506406883") || incomingNumber.equals("0522945298") || incomingNumber.equals("089719890")  ) MyService.runGetVolumep();

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                break;
            default:
                Log.i(LOG_TAG, "UNKNOWN_STATE: " + state);
                break;
        }
    }


}

