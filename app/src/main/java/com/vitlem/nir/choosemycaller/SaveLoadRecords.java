package com.vitlem.nir.choosemycaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaveLoadRecords {
    private static final String PREFS_NAME = "com.vitlem.nir.vperd.choosemycaller";
    private static final String PREF_PREFIX_KEY = "choosemycaller_";
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static List<String> loadTitlePref(Context context, int appWidgetId) {
        List<String> tasksList = new ArrayList<String>();
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Set<String> tasksSet = prefs.getStringSet(PREF_PREFIX_KEY + appWidgetId, new HashSet<String>());
            tasksList = new ArrayList<String>(tasksSet);
            editor.commit();
            Log.d("loadTitlePref","Sucsess");
            return tasksList;
        }
        catch (Exception e)
        {
            Log.d("loadTitlePref",e.getMessage());
        }
        return tasksList;
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, ArrayList<String> l_view) {
        try {
            List<String> tasksList = new ArrayList<String>();
            Set<String> tasksSet = new HashSet<String>(l_view);
            SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
            prefs.putStringSet(PREF_PREFIX_KEY + appWidgetId, tasksSet);
            prefs.apply();
            Log.d("SaveTitlePref", "Sucsess");
        } catch (Exception e) {
            Log.d("SaveTitlePref", e.getMessage());
        }

    }

}
