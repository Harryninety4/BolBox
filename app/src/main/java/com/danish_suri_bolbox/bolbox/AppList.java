package com.danish_suri_bolbox.bolbox;

import android.graphics.drawable.Drawable;

/**
 * Created by Danish on 4/13/15.
 */
public class AppList
{
    String name;
    Drawable icon;
    String pkg;
    AppList(Drawable ic,String n, String p)
    {
        icon=ic;
        name=n;
        pkg=p;
    }
}
