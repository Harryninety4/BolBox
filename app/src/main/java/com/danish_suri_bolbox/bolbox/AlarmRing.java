package com.danish_suri_bolbox.bolbox;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AlarmRing extends Activity
{

    Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        Uri alert= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r=RingtoneManager.getRingtone(getApplicationContext(),alert);
        r.play();

    }
    public void go(View v)
    {
     r.stop();
    }

}
