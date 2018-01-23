package com.danish_suri_bolbox.bolbox;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class PlayMusic extends ActionBarActivity {

    MediaPlayer mp;
    Cursor cursor;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        tv=(TextView)findViewById(R.id.tv);
        songFinder();
        mp=new MediaPlayer();
    }

    public void songFinder()
    {
        cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.DISPLAY_NAME, null, null);
        while(cursor.moveToNext())
        {
            tv.append(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
        }
    }
}
