package com.danish_suri_bolbox.bolbox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;


public class TestTime extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time);

        Calendar calendar=Calendar.getInstance();
       int h= calendar.get(Calendar.HOUR_OF_DAY);
        int m=calendar.get(Calendar.MINUTE);


        Log.d("MYMSG h ",h+" "+m);

    }

}
