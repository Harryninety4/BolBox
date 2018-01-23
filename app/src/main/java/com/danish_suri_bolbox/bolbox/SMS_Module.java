package com.danish_suri_bolbox.bolbox;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SMS_Module extends ActionBarActivity
{
    Handler handler=new Handler();
    boolean found=false;
    String name,smsbody;
    EditText sms_body,contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__module);
        sms_body=(EditText)findViewById(R.id.sms_body);
        contact=(EditText)findViewById(R.id.contact);
    }

    public String readContacts(String n)
    {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone ="";

        Log.d("MYMSG", n);
        while (cur.moveToNext())
        {
            String id = cur.getString(cur
                    .getColumnIndex(ContactsContract.Contacts._ID));
            final String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if(name.equalsIgnoreCase(n))
            {
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " ="+id, null, null);
                    if(pCur.moveToNext())
                    {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("MYMSG","phone");
                        found = true;
                    }
                    pCur.close();
                }
            }
        }
        return phone;

    }
    public void sendSms(String n)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(readContacts(n),null,smsbody,null,null);
            Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"Your sms has failed...",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    public void go(View v)
    {
        name=contact.getText().toString();
        smsbody=sms_body.getText().toString();
        sendSms(name);
    }
}
