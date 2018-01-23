package com.danish_suri_bolbox.bolbox;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class Scrolladapter extends BaseAdapter{
    ImageView iv;
    TextView tv1,tv2;

    ArrayList<Customlist> list=new ArrayList<>();
    Scrolladapter()
    {
        list.add(new Customlist(R.drawable.music1,"Play Songs from your Music Library","Say \"Play Music\""));
        list.add(new Customlist(R.drawable.bright1,"Change Screen Brightness","Say \"Change Brightness\""));
        list.add(new Customlist(R.drawable.alarm1,"Set Wake Up Alarm","Say \"Set Alarm for 6 p.m.\""));
        list.add(new Customlist(R.drawable.browse1,"Surf Internet and Open Web Sites","Say \"Browse google.com\""));
        list.add(new Customlist(R.drawable.call1,"Make Calls ","Say \"Call Danish\""));
        list.add(new Customlist(R.drawable.message2,"Send Text Messages","Say \"Message Danish\""));
        list.add(new Customlist(R.drawable.email1,"Send E-Mails","Say \"E-Mail danishsuri20@gmail.com\""));
        list.add(new Customlist(R.drawable.maps1,"Find Places on Google Maps","Find Guru Nanak Dev University"));
        list.add(new Customlist(R.drawable.wifi1,"Turn WiFi On/OFF","Say \"WiFi On/OFF\""));
        list.add(new Customlist(R.drawable.bluetooth1,"Turn Bluetooth On/Off","Say \"Bluetooth On/Off\""));
        list.add(new Customlist(R.drawable.flashlight1,"Turn Flash Light On/Off","Say \"Flash On/Off\""));
        list.add(new Customlist(R.drawable.launch1,"Open Installed Application","Say \"Open Application Name\""));
        list.add(new Customlist(R.drawable.camera1,"Click Photos Using Own Camera","Say \"My Camera\" to open Camera"));
        list.add(new Customlist(R.drawable.calc1,"Perform Basic Calculations","Say \"Calculate 50% of 200\""));
        list.add(new Customlist(R.drawable.search1,"Google Search","Say \"Steve Jobs\""));
        list.add(new Customlist(R.drawable.robot1,"Control Robot","Say \"Robot Mode\""));
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 12+10*position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        convertView=inflater.inflate(R.layout.template_scroll, parent, false);
        iv=(ImageView)(convertView.findViewById(R.id.iv));
        tv1=(TextView)(convertView.findViewById(R.id.tv1));
        tv2=(TextView)(convertView.findViewById(R.id.tv2));
        Customlist ap=list.get(position);
        iv.setImageResource(ap.logo);
        tv1.setText(ap.serve);
        tv2.setText(ap.comm);
        return convertView;
    }
}
