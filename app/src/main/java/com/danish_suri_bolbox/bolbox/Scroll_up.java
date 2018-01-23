package com.danish_suri_bolbox.bolbox;

import android.app.Activity;
        import android.animation.ValueAnimator;
        import android.app.Activity;
        import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
        import android.widget.TextView;
public class Scroll_up extends Activity {
    ListView lv;
    ScrollView sv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_up);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        lv=(ListView)findViewById(R.id.lv);
        sv=(ScrollView)findViewById(R.id.sv);
        Scrolladapter adpt=new Scrolladapter();
        lv.setAdapter(adpt);
        new Thread(new Runnable() {

            @Override
            public void run() {
                int listViewSize = lv.getAdapter().getCount();

                for (int index = 0; index < listViewSize ; index++) {
                    lv.smoothScrollToPositionFromTop(lv.getLastVisiblePosition() + 100, 0, 6000);
                    try {
                        // it helps scrolling to stay smooth as possible (by experiment)
                        Thread.sleep(600);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }



}
