package com.vitlem.nir.choosemycaller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListLog extends Activity {
    static  int index=0;
    ListView lv;
    static ArrayList<String> la;
    static  ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_log);
        if(la==null) la=new ArrayList();
        lv = (ListView)findViewById(R.id.ListLog) ;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, la);
        lv.setAdapter(adapter);

    }

    static void addtolist(String l)
    {
        if(la==null) la=new ArrayList();
        la.add(0,String.valueOf(index++) + ":" +l);
        if (la.size()>100)
        {
            la.remove(100);
        }
        //adapter.notifyDataSetChanged();
    }
}
