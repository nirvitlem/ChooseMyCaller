package com.vitlem.nir.choosemycaller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class TimingService extends Service {
    private NotificationManager mNM;
    public static AudioManager audio;
    public static Uri alertUri;
    public static Ringtone r;
    private static  Location location=null;
    TelephonyManager tManager;
    private BroadcastReceiver mReceiver=null;
    public static String ACTION_STATUS =null;
    public static String StatusM= "";
    private static  boolean GPSorN= true;
    public static float dis=0;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    public TimingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        //showNotification();
        Start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        //MainAppWidget.SetText(String.valueOf( startId));
        Start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
        Log.i("ScreenOnOff", "Service  distroy");
        ACTION_STATUS="onDestroy";
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);
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

    private void Start()
    {


        Log.d("debug", "buildUpdate");
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        alertUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), alertUri);
        //RemoteViews view = new RemoteViews(getPackageName(), R.layout.main_app_widget);
        tManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE) ;
        tManager.listen(new CustomPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE
        );
        MainAppWidget.SetText("Crerate Phone Listner " + GetCurrentTime.GetTime(),Color.GREEN);
        LocationManager locationManager;
        boolean isGPSEnabled = false;
        location=null; // location
        double latitude=0; // latitude
        double longitude=0; // longitude
        // The minimum distance to change Updates in meters
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


        // Instruct the widget manager to update the widget
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
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
                    Log.d("locationManager","LocationManager is not null");
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location==null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        GPSorN=false;
                    }
                    else GPSorN=true;
                    if (location != null) {
                        Log.d("location","Location is not null");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("latitude",String.valueOf(latitude)+ " TimeUpdate  " + Calendar.getInstance().getTime());
                        Log.d("longitude",String.valueOf(longitude)+ " TimeUpdate  " + Calendar.getInstance().getTime());
                        //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.GREEN);
                        if (GPSorN) {
                            MainAppWidget.setTextInfoColor( Color.GREEN);
                        }
                        else
                        {
                            MainAppWidget.setTextInfoColor(Color.MAGENTA);
                        }

                      /*  Location lb= new Location("point B");
                        lb.setLatitude(xLoc);
                        lb.setLongitude(yLoc);
                        dis=  location.distanceTo(lb)/1000;
                        StatusM= "Lat " + latitude  + " \nLon " + longitude + " \ndistance " + String.valueOf(dis);*/
                        StatusM= "Lat " + latitude  + " \nLon " + longitude;
                    }
                    else
                    {
                        StatusM="Location is Null";
                        MainAppWidget.setTextInfoColor(Color.RED);
                        //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.RED);
                    }
                }
            }
            else
            {
                StatusM="Location Manager is Null";
                MainAppWidget.setTextInfoColor(Color.RED);
                //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.RED);
            }
        }
        else
        {

            StatusM="GPS Is not Enabled";
            MainAppWidget.setTextInfoColor(Color.RED);
            //view.setInt(R.layout.main_app_widget,"setBackgroundColor", Color.RED);
        }

        // view.setTextViewText(R.id.appwidget_text, StatusM + " \nAM " + getVoulumeP(dis) +" \nTimeUpdate  " + Calendar.getInstance().getTime());
        MainAppWidget.SetTextinfo(StatusM + " \nAM " + " \nTimeUpdate  " + GetCurrentTime.GetTime());
      /* view.setTextViewText(R.id.appwidget_text, StatusM + " \nAM " + " \nTimeUpdate  " + Calendar.getInstance().getTime());
        ComponentName thisWidget = new ComponentName(this, MainAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);*/
    }

        static void runGetVolumep() {
        Log.i("runGetVolumep", "runGetVolumep");
        for (String item : MainAppWidget.listItems) {
            Location lb = new Location("point B");

            if (item.split("#")[1].toString() != "0") {
                if (location!=null) {
                    lb.setLatitude(Double.valueOf(item.split("#")[1]));
                    lb.setLongitude(Double.valueOf(item.split("#")[2]));
                    dis = location.distanceTo(lb) / 1000;
                    Log.i("runGetVolumep", String.valueOf(dis));
                    StatusM= "Lat " + Double.valueOf(item.split("#")[1]).toString()  + " \nLon " + Double.valueOf(item.split("#")[2]).toString() + " \ndistance " + String.valueOf(dis);
                    if (dis>=Double.valueOf(item.split("#")[3])) {
                        new TimingService().getVoulumeP();
                        Log.i("runGetVolumep", "runGetVolumep");
                    }
                }else
                {
                    Log.i("runGetVolumep", "location is null");
                }
            } else {

                new TimingService().getVoulumeP();
                Log.i("runGetVolumep", "runGetVolumep");
            }

        }
    }

    static void StopPalyPlayer()
    {
        Log.i("StopPalyPlayer", "StopPalyPlayer");
        if(r.isPlaying()){
            Log.i("StopPalyPlayer", "PlayerisPlaying");
            r.stop();

        }else
        {
            Log.i("StopPalyPlayer", "PlayerisNOTPlaying");
        }
    }

    private double getVoulumeP()
    {
        try {
            // Log.i("getVoulumeP", String.valueOf(dist));
            //audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

// Get the current ringer volume as a percentage of the max ringer volume.
            int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
            Log.i("currentVolume", String.valueOf(currentVolume));
            int maxRingerVolume = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
            Log.i("maxRinger", String.valueOf(maxRingerVolume));
            double proportion = currentVolume / (double) maxRingerVolume;
            Log.i("proportion", String.valueOf(proportion));

// Calculate a desired music volume as that same percentage of the max music volume.
            int maxMusicVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int desiredMusicVolume = (int) (proportion * maxMusicVolume);

// Set the music stream volume.
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, desiredMusicVolume, 0 /*flags*/);


            if (proportion < 0.5) {
                audio.setStreamVolume(AudioManager.STREAM_RING, maxRingerVolume, AudioManager.FLAG_PLAY_SOUND);

                if (r != null && !r.isPlaying()) {
                    r.play();

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
