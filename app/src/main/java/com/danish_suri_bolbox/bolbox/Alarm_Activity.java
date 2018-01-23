package com.danish_suri_bolbox.bolbox;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Alarm_Activity extends ActionBarActivity {

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver mbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_);
        Intent in=new Intent(this,AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(getBaseContext(),20, in, 0);
        alarmManager=(AlarmManager)getSystemService(this.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,10000,pendingIntent);

    }

}
