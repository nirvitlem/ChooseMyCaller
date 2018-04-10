package com.vitlem.nir.choosemycaller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MainAppWidgetConfigureActivity MainAppWidgetConfigureActivity}
 */
public class MainAppWidget extends AppWidgetProvider {

    public static PendingIntent service = null;
    public static AlarmManager m=null;
    public static Calendar TIME=null;
    public static Intent i=null;
    public static  RemoteViews views;
    public static  Context c;
    public static List<String> listItems;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.main_app_widget);
        views.setTextViewText(R.id.appwidget_text, "Waiting For Call");
        c=context;
        listItems= SaveLoadRecords.loadTitlePref(context,1);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them


        Log.d("CreateService", "CreateService");
        views = new RemoteViews(context.getPackageName(), R.layout.main_app_widget);
        views.setTextViewText(R.id.appwidget_text, "Start Service" +GetCurrentTime.GetTime() );
        m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);

        i = new Intent(context, TimingService.class);

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 180 * 1000, service); //180 second

        Log.d("MainAppWidget", "onUpdate");


       ;

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static void SetText(String t,int co)
    {
        views.setTextViewText(R.id.appwidget_text, t);
        views.setTextColor(R.id.appwidget_text,co);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        ComponentName thisAppWidget = new ComponentName(c.getPackageName(), MainAppWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    public static void SetTextinfo(String t)
    {
        views.setTextViewText(R.id.textinfo, t);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        ComponentName thisAppWidget = new ComponentName(c.getPackageName(), MainAppWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    public static void  setTextInfoColor(int co)
    {

        views.setTextColor(R.id.textinfo,co);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        ComponentName thisAppWidget = new ComponentName(c.getPackageName(), MainAppWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}

