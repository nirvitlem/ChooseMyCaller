package com.vitlem.nir.choosemycaller;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Calendar;

public class TimingService extends JobIntentService {
    private NotificationManager mNM;
    public static AudioManager audio;
    public static Uri alertUri;
    public static Ringtone r;
    private static  Location location=null;
    //public static TelephonyManager tManager;
    private BroadcastReceiver mReceiver=null;
    public static String ACTION_STATUS =null;
    public static String StatusM= "";
    private static  boolean GPSorN= true;
    public static float dis=0;
    BroadcastReceiver br;
    IntentFilter screenStateFilter;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    public static  int volume;
    public static  int MAX_VOLUME=99999;

    public TimingService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        ListLog.addtolist("onCreate TimingService" + GetCurrentTime.GetTime());
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        startForeground(1,new Notification());
        // Display a notification about us starting.  We put an icon in the status bar.
        //showNotification();
        //Start();
    }

    @Override
    protected void onHandleWork(Intent intent) {
        ListLog.addtolist("onHandleWork TimingService " + GetCurrentTime.GetTime());
        Log.i("onHandleWork", "onHandleWork " );
        Start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ListLog.addtolist("onStartCommand TimingService " + GetCurrentTime.GetTime());
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        br= new SensorRestarterBroadcastReceive();
        screenStateFilter =new IntentFilter();
        screenStateFilter.addAction((".RestartSensor"));
        registerReceiver(br,screenStateFilter);
        //MainAppWidget.SetText(String.valueOf( startId)+ " " + GetCurrentTime.GetTime(),Color.GREEN);
        //startForeground(1,new Notification());
        Start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListLog.addtolist("onDestroy  TimingService" + GetCurrentTime.GetTime());
        Intent broadcastIntent = new Intent(".RestartSensor");
        sendBroadcast(broadcastIntent);
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        //Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
        Log.i("onDestroy ", "TimingService");
        ACTION_STATUS = "onDestroy";
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
        if (br != null) unregisterReceiver(br);
    }
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TimingService.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("RunService")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    private void Start() {


        Log.d("Start", "Start Main");
        ListLog.addtolist("Start TimingService " + GetCurrentTime.GetTime());
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        alertUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), alertUri);
       //if (CustomPhoneStateListener.CallStatus==0) getVoulumeP(volume,0);
       // if (tManager == null) {
        //    Log.d("tManager", "null ");
        try {
            if (MainAppWidget.tManager == null || MainAppWidget.customPhoneStateListener==null) {
                ListLog.addtolist("Start, Register TelephonyManager " + GetCurrentTime.GetTime());
                MainAppWidget.UnregisterTM();
                MainAppWidget.registerTM();

            } else {
                ListLog.addtolist("Start, No need to Register TelephonyManager " + GetCurrentTime.GetTime());
            }
        }
        catch (Exception e)
        {
            ListLog.addtolist("Start " + e.getMessage() + " " + GetCurrentTime.GetTime());
        }
        //} else
         //   Log.d("tManager", "Not null ");
        //MainAppWidget.SetText("Crerate Phone Listner " + GetCurrentTime.GetTime(), Color.GREEN);
        LocationManager locationManager;
        boolean isGPSEnabled = false;
        location = null; // location
        double latitude = 0; // latitude
        double longitude = 0; // longitude
        // The minimum distance to change Updates in meters
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


        // Instruct the widget manager to update the widget
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        }


        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            if (location == null) {
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    Log.d("locationManager", "LocationManager is not null");
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        GPSorN = false;
                    } else GPSorN = true;
                    if (location != null) {
                        Log.d("location", "Location is not null");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("latitude", String.valueOf(latitude) + " TimeUpdate  " + Calendar.getInstance().getTime());
                        Log.d("longitude", String.valueOf(longitude) + " TimeUpdate  " + Calendar.getInstance().getTime());
                        //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.GREEN);
                      /*  if (GPSorN) {
                            MainAppWidget.setTextInfoColor(Color.GREEN);
                        } else {
                            MainAppWidget.setTextInfoColor(Color.MAGENTA);
                        }*/

                      /*  Location lb= new Location("point B");
                        lb.setLatitude(xLoc);
                        lb.setLongitude(yLoc);
                        dis=  location.distanceTo(lb)/1000;
                        StatusM= "Lat " + latitude  + " \nLon " + longitude + " \ndistance " + String.valueOf(dis);*/
                        StatusM = "Lat " + latitude + " \nLon " + longitude;
                    } else {
                        StatusM = "Location is Null";
                        //MainAppWidget.setTextInfoColor(Color.RED);
                        //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.RED);
                    }
                }
            } else {
                StatusM = "Location Manager is Null";
               // MainAppWidget.setTextInfoColor(Color.RED);
                //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.RED);
            }
        } else {

            StatusM = "GPS Is not Enabled";
           // MainAppWidget.setTextInfoColor(Color.RED);
            //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.RED);
        }
        ListLog.addtolist(StatusM +" "+ GetCurrentTime.GetTime());
       // MainAppWidget.SetText(StatusM + " \nTimeUpdate  " + GetCurrentTime.GetTime(), Color.GREEN);

    }

        static void runGetVolumep() {
        Log.i("runGetVolumep", "runGetVolumep");
        ListLog.addtolist("runGetVolumep " + GetCurrentTime.GetTime());
        for (String item : MainAppWidget.listItems) {
            Location lb = new Location("point B");

            if (item.split("#")[1].toString() != "0") {
                if (location!=null) {
                    lb.setLatitude(Double.valueOf(item.split("#")[1]));
                    lb.setLongitude(Double.valueOf(item.split("#")[2]));
                    dis = location.distanceTo(lb) / 1000;
                    Log.i("runGetVolumep", String.valueOf(dis));
                    ListLog.addtolist("runGetVolumep "+ String.valueOf(dis) + " " + GetCurrentTime.GetTime());
                    StatusM= "Lat " + Double.valueOf(item.split("#")[1]).toString()  + " \nLon " + Double.valueOf(item.split("#")[2]).toString() + " \ndistance " + String.valueOf(dis);
                    if (dis>=Double.valueOf(item.split("#")[3])) {
                        TimingService.getVoulumeP(MAX_VOLUME,0);
                        Log.i("runGetVolumep", "runGetVolumep");
                    }
                }else
                {
                    Log.i("runGetVolumep", "location is null");
                }
            } else {

                TimingService.getVoulumeP(MAX_VOLUME,0);
                Log.i("runGetVolumep", "runGetVolumep");
            }

        }
    }

    static void StopPalyPlayer()
    {
        Log.i("StopPalyPlayer", "StopPalyPlayer");
        if (r!= null) {
            if (r.isPlaying()) {
                Log.i("StopPalyPlayer", "PlayerisPlaying");
                r.stop();

            } else {
                Log.i("StopPalyPlayer", "PlayerisNOTPlaying");
            }
        }
    }

    public  static void setcurrentvolume()
    {
        volume = audio.getStreamVolume(AudioManager.STREAM_RING);
        Log.i("setcurrentvolume ", String.valueOf(volume));
    }

    public static double getVoulumeP(int volumeM,int Flag)
    {
        try {
            // Log.i("getVoulumeP", String.valueOf(dist));
            //audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            ListLog.addtolist("getVoulumeP " + GetCurrentTime.GetTime());
            ListLog.addtolist("getVoulumeP volumeM " + String.valueOf(volumeM) + " " +  GetCurrentTime.GetTime());
// Get the current ringer volume as a percentage of the max ringer volume.
            int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
            Log.i("getVoulumeP ", "currentVolume " + String.valueOf(currentVolume));
            int maxRingerVolume = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
            Log.i("maxRinger", String.valueOf(maxRingerVolume));
            double proportion = currentVolume / (double) maxRingerVolume;
            Log.i("proportion", String.valueOf(proportion));
            ListLog.addtolist("getVoulumeP proportion " +String.valueOf(proportion) + " "+ GetCurrentTime.GetTime());
// Calculate a desired music volume as that same percentage of the max music volume.
            int maxMusicVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int desiredMusicVolume = (int) (proportion * maxMusicVolume);

// Set the music stream volume.


            if (volumeM!=MAX_VOLUME)
            {
                Log.i("volumeM ", String.valueOf(volumeM));
                audio.setStreamVolume(AudioManager.STREAM_RING,volumeM, Flag);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, volumeM, Flag /*flags*/);

            }
            else {
                if (proportion < 0.5) {
                    Log.i("MAX_VOLUME ", String.valueOf(MAX_VOLUME));
                    audio.setStreamVolume(AudioManager.STREAM_RING, maxRingerVolume, Flag);
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, desiredMusicVolume, Flag /*flags*/);
                    if (r != null && !r.isPlaying()) {
                        r.play();

                    }
                }

            }

            return proportion;
        }
        catch(Exception e)
        {
            Log.i("getVoulumeP", e.getMessage());
            return  0;}
    }
}
