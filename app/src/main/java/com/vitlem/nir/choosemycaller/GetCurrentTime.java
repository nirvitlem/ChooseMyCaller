package com.vitlem.nir.choosemycaller;

import java.text.SimpleDateFormat;

public class GetCurrentTime {

    public static String GetTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }
}
