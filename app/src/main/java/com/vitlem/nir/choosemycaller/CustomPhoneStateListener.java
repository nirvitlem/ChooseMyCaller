package com.vitlem.nir.choosemycaller;

import android.graphics.Color;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

public class CustomPhoneStateListener extends PhoneStateListener {

    public static String LOG_TAG = "PhoneStateListener";
    public static  String lastInfo = "";

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

                MainAppWidget.SetText("IDLE "  + GetCurrentTime.GetTime() + "\n" + lastInfo ,Color.GREEN);
                ListLog.addtolist("CALL_STATE_IDLE \n* " + lastInfo + " * " + GetCurrentTime.GetTime());

                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
                TimingService.StopPalyPlayer();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                ListLog.addtolist("CALL_STATE_RINGING " + GetCurrentTime.GetTime());
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
                Log.i(LOG_TAG, "incomingNumber: " + incomingNumber);
                //TimingService.runGetVolumep();

                if (MainAppWidget.listItems != null) {
                    for (String item : MainAppWidget.listItems) {
                        Log.i("listItems", item.split("#")[0].toString() +" incomming number " + incomingNumber);
                        lastInfo=  incomingNumber + " N " + GetCurrentTime.GetTime();
                        if (incomingNumber.equals(item.split("#")[0].toString())) {
                            lastInfo= incomingNumber + " Y " + GetCurrentTime.GetTime();
                            Log.i("match num", item.split("#")[0].toString() +" incomming number " + incomingNumber);
                            if (item.split("#")[1].toString().equals("0"))
                            {

                                TimingService.getVoulumeP();
                            } else
                            {
                                TimingService.runGetVolumep();
                            }
                        }
                    }
                    if (lastInfo.contains("Y"))
                    {
                        MainAppWidget.SetText(incomingNumber + " Y " + GetCurrentTime.GetTime(), Color.GREEN);
                        ListLog.addtolist(incomingNumber + " Y " + GetCurrentTime.GetTime());
                    }
                    else
                    {
                        MainAppWidget.SetText(incomingNumber + " N " + GetCurrentTime.GetTime(), Color.GRAY);
                        ListLog.addtolist(incomingNumber + " N " + GetCurrentTime.GetTime());

                    }

                }
                //if (incomingNumber.equals("0543205519") || incomingNumber.equals("0506406883") || incomingNumber.equals("0522945298") || incomingNumber.equals("089719890")  ) MyService.runGetVolumep();

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                ListLog.addtolist("CALL_STATE_OFFHOOK " + GetCurrentTime.GetTime());
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                TimingService.StopPalyPlayer();
                break;
            default:
                ListLog.addtolist("UNKNOWN_STATE " + GetCurrentTime.GetTime());
                Log.i(LOG_TAG, "UNKNOWN_STATE: " + state);
                TimingService.StopPalyPlayer();
                break;
        }
        MainAppWidget.UnregisterTM();
        MainAppWidget.registerTM();
       /* if (MainAppWidget.tManager==null) {
            try

            {
                ListLog.addtolist("tManager is null " + GetCurrentTime.GetTime());
                ListLog.addtolist("CustomPhone, Register TelephonyManager " + GetCurrentTime.GetTime());
                MainAppWidget.registerTM();
            } catch (Exception e) {
                ListLog.addtolist(e.getMessage() + " " + GetCurrentTime.GetTime());
            }
        }*/
    }


}

