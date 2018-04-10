package com.vitlem.nir.choosemycaller;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link MainAppWidget MainAppWidget} AppWidget.
 */
public class MainAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.vitlem.nir.choosemycaller.MainAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ArrayList<String> listItems = new ArrayList<String>();
    private static ListView lv;
    private int itemPosition;
    private String itemValue;
    public final int MY_PERMISSIONS_REQUEST_LOC_GPS=1;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    public MainAppWidgetConfigureActivity() {
        super();
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if (!SaveLoadRecords.loadTitlePref(MainAppWidgetConfigureActivity.this ,1).isEmpty())
        {
            adapter.addAll(SaveLoadRecords.loadTitlePref(MainAppWidgetConfigureActivity.this ,1));
        }
    }

        @Override
    public void onCreate(Bundle icicle) {
            super.onCreate(icicle);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_LOC_GPS);


            // Set the result to CANCELED.  This will cause the widget host to cancel
            // out of the widget placement if the user presses the back button.
            setResult(RESULT_CANCELED);

            setContentView(R.layout.main_app_widget_configure);
            ((CheckBox) findViewById(R.id.GPS_B)).setChecked(false);
            findViewById(R.id.editText_X).setEnabled(false);
            findViewById(R.id.editText_Y).setEnabled(false);
            findViewById(R.id.DisFromPT).setEnabled(false);
            findViewById(R.id.GPS_B).setOnClickListener(mOnClickFListener);
            findViewById(R.id.A_button).setOnClickListener(mOnClickAddListener);
            findViewById(R.id.S_button).setOnClickListener(mOnClickSListener);
            findViewById(R.id.D_button).setOnClickListener(mOnClickDListener);
            lv = (ListView) findViewById(R.id.L_VIEW);
            lv.setOnItemClickListener(mOnClicklListener);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listItems);
            lv.setAdapter(adapter);


            // Find the widget id from the intent.
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }

            // If this activity was started with an intent without an app widget ID, finish with an error.
          /*  if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
                return;
            }*/

        }


    View.OnClickListener mOnClickFListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MainAppWidgetConfigureActivity.this;

            CheckBox rb = (CheckBox)findViewById(R.id.GPS_B);

            if (!rb.isChecked()) {

                findViewById(R.id.editText_X).setEnabled(false);
                findViewById(R.id.editText_Y).setEnabled(false);
                findViewById(R.id.DisFromPT).setEnabled(false);
                rb.setChecked(false);
            }
            else
            {
                findViewById(R.id.editText_X).setEnabled(true);
                findViewById(R.id.editText_Y).setEnabled(true);
                findViewById(R.id.DisFromPT).setEnabled(true);
                rb.setChecked(true);
            }
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MainAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);


        }
    };

    View.OnClickListener mOnClickAddListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MainAppWidgetConfigureActivity.this;

            TextView tX = (TextView)findViewById(R.id.editText_X);
            TextView tY = (TextView)findViewById(R.id.editText_Y);
            TextView tT = (TextView)findViewById(R.id.editText_T);
            TextView tD = (TextView)findViewById(R.id.DisFromPT);



            if (tX.isEnabled())
            {
                listItems.add(tT.getText() + "#" + tX.getText() + "#" + tY.getText() + "#" + tD.getText());
                //adapter.addAll(listItems);
                adapter.notifyDataSetChanged();
            }
            else
            {
                listItems.add(tT.getText() + "#0#0#0");
                //adapter.addAll(listItems);
                adapter.notifyDataSetChanged();
            }

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MainAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);


        }
    };

    View.OnClickListener mOnClickSListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MainAppWidgetConfigureActivity.this;

            SaveLoadRecords.saveTitlePref(MainAppWidgetConfigureActivity.this,1,listItems);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            alertDialogBuilder.setTitle("Saved Data");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            // It is the responsibility of the configuration activity to update the app widget
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                            MainAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                            // Make sure we pass back the original appWidgetId
                            Intent resultValue = new Intent();
                            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                            setResult(RESULT_OK, resultValue);
                            MainAppWidget.LoadNumbers();
                            finish();
                            MainAppWidgetConfigureActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MainAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);


        }
    };

    View.OnClickListener mOnClickDListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MainAppWidgetConfigureActivity.this;

            adapter.remove(itemValue);
            adapter.notifyDataSetChanged();

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MainAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);


        }
    };

    AdapterView.OnItemClickListener mOnClicklListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

            view.setSelected(true);
            // ListView Clicked item index
            itemPosition = position;

            // ListView Clicked item value
            itemValue = (String) lv.getItemAtPosition(position);
            parent.setSelection(position);
            adapter.notifyDataSetChanged();

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOC_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

