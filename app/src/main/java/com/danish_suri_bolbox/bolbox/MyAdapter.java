package com.danish_suri_bolbox.bolbox;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Package.getPackage;
import static java.lang.Package.*;

/**
 * Created by Danish on 4/13/15.
 */
public class MyAdapter extends BaseAdapter
{
    ArrayList<AppList> apps=GlobalApp.appLists;
    TextView tv;
    ImageView iv;
    MyAdapter()
    {
        //apps.add(res,);
    }
    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 10*position+15;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        convertView=inflater.inflate(R.layout.template_apps, parent, false);
        iv=(ImageView)(convertView.findViewById(R.id.iv));
        tv=(TextView)(convertView.findViewById(R.id.tv));
        AppList ap=apps.get(position);
        iv.setImageDrawable(ap.icon);
        tv.setText(ap.name);
        return convertView;
    }
}
