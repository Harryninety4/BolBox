package com.danish_suri_bolbox.bolbox;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Intent;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danish_suri_bolbox.bolbox.R;
public class BolBOX extends Activity {
    //SONG ACTIVITY//
    private int brightness;
    private ContentResolver cResolver;
    private Window window;
    int input=60;
    int change=0;
    String contact_name="";
    String loc_result="";
    LocationManager locationManager;
    Geocoder geocoder;
    location myLocation;
    int email=0;
    String to,sub,mess;
    Ringtone r;
    String message_name,message_text;
    MediaPlayer mp;
    Cursor cursor;
    ArrayList<String> name,path;
    boolean musicFlag=false,pauseState=false,msgText=false,bright=false;
    boolean found = false;
    //////////////////////////////////

    BroadcastReceiver smsReceiver;
    ImageView imgV,imgV2,pause,play,stop,help;
    TextView msgTV, progTV;
    EditText messgae_body,phone;
    String text="";
    boolean activate=false;
    FrameLayout outerlay,music;
    LinearLayout mic_layout,message;

    Button send_message;
  ///To check connectivity///

    boolean f=false;
   ///Dialog to speak///
    AlertDialog.Builder builder;
    AlertDialog ad;
    /// Variable for Grid Activity///
    GridView gv1;
    List<PackageInfo> apps;
    ArrayList<AppList> myapps=new ArrayList<>();

    /// Variables for Speak and Listen///

    TextToSpeech tts;
    TextToSpeechOnInitListener ttsOnInitListener;
    UtteranceProgressListener utteranceProgressListener;
    BufferedReader bufferedReader;
    boolean flag = false;
    Handler handler;
    SpeechRecognizer recognizer;
    Intent intent;
    /////////////Ends Here/////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_apps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        outerlay=(FrameLayout)findViewById(R.id.outerlay);
        music=(FrameLayout)findViewById(R.id.music);
        message=(LinearLayout)findViewById(R.id.message);
        phone=(EditText)findViewById(R.id.phone);
        messgae_body=(EditText)findViewById(R.id.message_body);
        mic_layout=(LinearLayout)findViewById(R.id.mic_layout);
        imgV=(ImageView)findViewById(R.id.imgV);
        help=(ImageView)findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getBaseContext(),Scroll_up.class);
                startActivity(in);
            }
        });
        pause=(ImageView)findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        stop=(ImageView)findViewById(R.id.stop);
        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mp.pause();
                pauseState=true;
            }
        });
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(pauseState)
                mp.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mp.stop();
                music.setVisibility(View.GONE);
                mic_layout.setVisibility(View.VISIBLE);
                imgV.setVisibility(View.VISIBLE);
                gv1.setVisibility(View.VISIBLE);
            }
        });
        imgV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progTV.setText("listening..");
                start();
                imgV.setVisibility(View.GONE);
                imgV2.setVisibility(View.VISIBLE);
            }
        });
        imgV2=(ImageView)findViewById(R.id.imgV2);
        imgV2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
            }
        });
        msgTV=(TextView)findViewById(R.id.msgTV);
        progTV=(TextView)findViewById(R.id.progTV);
        initializeSMSReceiver();
        registerSMSReceiver();

        //////SONG ACTIVITY /////////
        geocoder=new Geocoder(this, Locale.getDefault());
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        myLocation=new location();
        mp=new MediaPlayer();
        name=new ArrayList<>();
        path=new ArrayList<>();
        cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.DISPLAY_NAME, null, null);
        while(cursor.moveToNext())
        {
            name.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
            path.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
        }
        builder=new AlertDialog.Builder(this);
        builder.setTitle("Error Message!");
        builder.setMessage("Please connect to Internet");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });

        //************************** //Speak and Listen **************************

        handler = new Handler();
        try
        {
            bufferedReader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath()+ File.separator+"media"+File.separator+"speech.txt"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        apps=getPackageManager().getInstalledPackages(0);
        for(int i=0;i<apps.size();i++)
        {
            PackageInfo a=apps.get(i);
            a.applicationInfo.loadIcon(getPackageManager());// drawable
            a.applicationInfo.loadLabel(getPackageManager()); //string
            String s=a.applicationInfo.packageName; //string
                if(getPackageManager().getLaunchIntentForPackage(a.applicationInfo.packageName)!=null)
                {
                    myapps.add(new AppList(a.applicationInfo.loadIcon(getPackageManager()),a.applicationInfo.loadLabel(getPackageManager()).toString(),a.applicationInfo.packageName));
                }
        }
        GlobalApp.appLists=myapps;
        gv1=(GridView)findViewById(R.id.gv1);
        MyAdapter myad=new MyAdapter();
        gv1.setAdapter(myad);

        //************************** //CODE ENDS HERE//**************************

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        stop();
        Log.d("MYMSG","paused");
        tts.shutdown();
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        ttsOnInitListener = new TextToSpeechOnInitListener();
        utteranceProgressListener = new MyUtteranceProgressListener();

        tts=new TextToSpeech(this,ttsOnInitListener);
        tts.setOnUtteranceProgressListener(utteranceProgressListener);

        if(activate==false)
        {
            msgTV.setText("\t\t\t\t\t\tSay Hello to activate command mode\t\t\t\t\t\t\t\t\t\tSay Hello to activate command mode\t\t\t\t\t\t\t\t\t\tSay Hello to activate command mode\t\t\t\t\t\t\t\t\t\tSay Hello to activate command mode\t\t\t\t\t\t\t\t\t\t");
        }
        else
            stop();
    }
    void start()
    {

            recognizer = SpeechRecognizer.createSpeechRecognizer(this);

            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

            MyRecognitionListener listener = new MyRecognitionListener();
            recognizer.setRecognitionListener(listener);
            ad = builder.create();

            ConnectivityManager conn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        try
        {

            if (conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED )
            {
                recognizer.startListening(intent);
                progTV.setText("Listening....");
            }
            else
            {
                ad.setMessage("Internet Access Not Available, Please connect to Internet");
                ad.show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    void stop()
    {
        progTV.setText("stopped...");
        msgTV.setText("Press Mic icon to speak");
        if (recognizer != null)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imgV2.setVisibility(View.GONE);
                    imgV.setVisibility(View.VISIBLE);
                }
            });

            recognizer.stopListening();
            recognizer.setRecognitionListener(null);
            intent = null;
            recognizer = null;
            System.gc();
        }
    }
    public void speak(String s)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "myid");
        tts.speak(s, TextToSpeech.QUEUE_FLUSH,map);
    }
    class TextToSpeechOnInitListener implements TextToSpeech.OnInitListener
    {

        public void onInit(int status)
        {
            if (status == TextToSpeech.SUCCESS)
            {
                tts.setLanguage(Locale.ENGLISH);
                flag = true;
                try
                {
                    text = bufferedReader.readLine();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(flag)
                {
                    speak(text);
                }
                else
                {
                    ad.setMessage("Voice commands not supported");
                    ad.show();
                }
            }
            else
            {
                ad.setMessage("Voice commands not supported");
                ad.show();
            }
        }
    }
    class MyUtteranceProgressListener extends UtteranceProgressListener
    {
        public void onStart(String utteranceId)
        {

            handler.post(new Runnable()
            {
                public void run()
                {
                }
            });

        }

        public void onDone(String utteranceId)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Log.d("MYMSG","ondone");
                }
            });

        }

        public void onError(String utteranceId)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    msgTV.setText("Error Encountered");
                }
            });
        }

    }
    class MyRecognitionListener implements RecognitionListener
    {

        @Override
        public void onReadyForSpeech(Bundle bundle)
        {
        }

        @Override
        public void onBeginningOfSpeech()
        {
        }

        @Override
        public void onRmsChanged(float v)
        {

        }

        @Override
        public void onBufferReceived(byte[] bytes)
        {

        }

        @Override
        public void onEndOfSpeech()
        {

        }

        @Override
        public void onError(int i)
        {
            stop();
        }

        @Override
        public void onResults(Bundle results)
        {
            stop();
            ArrayList<String> al = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String s = al.get(0);
            msgTV.setText(s);
            if (activate == false)
            {
                if (s.equalsIgnoreCase("Hello"))
                {
                    outerlay.setBackgroundResource(R.drawable.bolboxlow);
                    msgTV.setText("Command mode activated. Awaiting command...");
                    activate = true;
                    gv1.setVisibility(View.VISIBLE);
                    speak(msgTV.getText().toString());

                }
                else
                {
                   msgTV.setText("Say \"Hello\" to activate command mode");
                   speak(msgTV.getText().toString());
                }
            }
            else if (activate == true && !(s.equals("")))
            {
                if(musicFlag)
                {
                    for (int i = 0; i < name.size(); i++)
                    {
                        if (name.get(i).toLowerCase().contains(s))
                        {
                            found = true;
                            try
                            {
                                mp.setDataSource(path.get(i));
                                mp.prepare();
                                mic_layout.setVisibility(View.GONE);
                                gv1.setVisibility(View.GONE);
                                imgV.setVisibility(View.GONE);
                                imgV2.setVisibility(View.GONE);
                                music.setVisibility(View.VISIBLE);
                                mp.start();
                                found=false;
                                musicFlag=false;

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!found)
                    {
                        msgTV.setText("No such song found");
                    }
                }
                if(message.getVisibility()==View.VISIBLE)
                {
                    if(s.equalsIgnoreCase("send"))
                    {
                        sendSms(message_name,message_text);
                        phone.setText("TO: ");
                        messgae_body.setText("Message: ");
                        message.setVisibility(View.GONE);
                        gv1.setVisibility(View.VISIBLE);
                        mic_layout.setVisibility(View.VISIBLE);
                        imgV.setVisibility(View.VISIBLE);
                    }
                    else if(s.equalsIgnoreCase("cancel"))
                    {
                        message.setVisibility(View.GONE);
                        phone.setText("TO: ");
                        messgae_body.setText("Message: ");
                        gv1.setVisibility(View.VISIBLE);
                        mic_layout.setVisibility(View.VISIBLE);
                        imgV.setVisibility(View.VISIBLE);
                        stop();
                    }
                    else
                    {
                        speak("Invalid Command, Say Send to send message and Cancel to cancel message");
                    }
                }
                else if(s.equalsIgnoreCase("play music"))
                {
                    musicFlag=true;
                    speak("Which Song you want to play?");
                }

                else if(s.equalsIgnoreCase("help"))
                {
                    Intent in=new Intent(getBaseContext(),Scroll_up.class);
                    startActivity(in);
                }
                else if(s.contains("brightness")&& bright==false)
                {
                    speak("How much brightness you want?");
                    tts.playSilence(1200,TextToSpeech.QUEUE_ADD,null);
                    bright=true;
                }
                else if(bright)
                {
                    bright=false;
                    try {
                        if (Integer.parseInt(s) <= 100 && Integer.parseInt(s) >= 0)
                            setBrightness(s);
                        else
                            speak("Enter Suitable value");
                    }
                    catch (Exception e)
                    {
                        msgTV.setText("Invalid Value");
                    }
                }
                else if(s.contains("alarm"))
                {
                    int am_pm=0;
                    if(s.contains("a.m.")||s.contains("am"))
                    {
                        am_pm=0;
                    }
                    else if(s.contains("p.m.")||s.contains("pm"))
                    {
                        am_pm=1;
                    }

                    int time;
                    String hour,min;
                    if(s.contains(":"))
                    {
                        time = s.indexOf(':');
                        hour = s.substring(time - 2, time);
                        min = s.substring(time + 1, time + 3);
                        hour = hour.trim();
                        min = min.trim();
                        if(min.length()==1)
                        {
                            min="0"+min;
                        }
                    }
                   else
                    {
                        hour=s.replaceAll("[^0-9]","");
                        min="0";
                    }
                   setAlarmMethod(hour, min,am_pm);
                }
                else if(s.startsWith("browse")&&(s.contains("dotcom")||s.contains(".com")))
                {
                    String addr;
                    addr=s.substring(s.indexOf(" ")+1);
                    addr.replace("dot",".");
                    String url = "http://www."+addr;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                else if(s.startsWith("call"))
                {
                    if(s.equalsIgnoreCase("call"))
                    {
                        speak("Whom do you want to call?");
                    }
                    else
                    {
                        String name=s.substring(s.indexOf(" ")+1);
                        Log.d("MYMSG",name);
                        if(readContacts(name).equals(""))
                        {
                            speak("No Such Contact Exists, Try Again");
                        }
                        else
                        {
                            call(readContacts(name));
                        }
                    }
                }
                else if(s.startsWith("message")&&msgText==false)
                {
                    if(s.equalsIgnoreCase("message"))
                    {
                        speak("Whom do you want to message");
                    }
                    else
                    {
                        String name=s.substring(s.indexOf(" ")+1);
                        message_name=readContacts(name);
                        if(message_name.equals(""))
                        {
                            speak("Contact not found, Try again");
                        }
                        else
                        {
                            speak("Speak Message Text");
                            msgText = true;
                        }
                    }
                }
                else if(s.replaceAll("-","").toLowerCase().startsWith("email"))
                {
                    if(s.replaceAll("-","").toLowerCase().equalsIgnoreCase("email"))
                    {
                        speak("Whom do you want to mail");
                    }
                    else
                    {
                        to=s.substring(s.indexOf(" ")+1);
                        to=to.replaceAll(" ","");
                        to=to.toLowerCase();
                        if(to.contains("at"))
                        {
                            int pos = to.lastIndexOf("at");
                            to.replace(to.charAt(pos), '@');
                            to.replace(to.charAt(pos + 1), '\0');
                        }
                        email=1;
                        speak("What would be the subject?");
                    }

                }
                else if(email==1)
                {
                    sub=s;
                    email=2;
                    speak("Speak Message");
                }
                else if(email==2)
                {
                    mess=s;
                    sendEmail(to,sub,mess);
                }
                else if(msgText)
                {
                    gv1.setVisibility(View.GONE);
                    mic_layout.setVisibility(View.GONE);
                    imgV.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                    phone.append(getContactName(message_name));
                    messgae_body.append(s);
                    message_text=s;
                    msgText=false;
                }
                else if(s.contains("find"))
                {
                    String addr=s.substring(s.indexOf(" ")+1);
                    getLocation(addr);
                }
                else if(s.contains("flash"))
                {
                    flashOn(s);
                }
                else if(s.contains("bluetooth"))
                {
                    bluetoothControl(s);
                }
                else if(s.contains("wifi"))
                {
                    wifiControl(s);
                }
                else if(s.equalsIgnoreCase("robot"))
                {
                    for(int i=0;i<myapps.size();i++)
                    {
                        AppList ap=myapps.get(i);
                        String lab=ap.name;
                        String pack=ap.pkg;
                        if(lab.equalsIgnoreCase("amr_voice"))
                        {
                            Intent in=getPackageManager().getLaunchIntentForPackage(pack);
                            startActivity(in);
                        }
                    }
                }
                else if (s.contains("open") || s.contains("launch"))
                {
                    msgTV.setText("Requested to open an Application");
                    handler.post(new Runnable() {
                        boolean match = false;

                        public void run() {
                            outer:
                            for (int i = 0; i < myapps.size(); i++) {
                                AppList ap = myapps.get(i);
                                String lab = ap.name;
                                String pack = ap.pkg;
                                if (s.equalsIgnoreCase("open " + lab) || s.equalsIgnoreCase("launch " + lab)) {
                                    msgTV.setText("Opening " + lab);
                                    Intent in = getPackageManager().getLaunchIntentForPackage(pack);
                                    startActivity(in);
                                    match = true;
                                    break outer;
                                }


                            }
                            if (match == false) {
                                msgTV.setText("No such application found.You may download it from Play Store.");
                                //          start();
                            }
                        }

                    });
                }
                else if(s.equalsIgnoreCase("my camera"))
                {
                    Intent in=new Intent(getBaseContext(),MyCamera.class);
                    startActivity(in);
                    //start();
                }
                else if(s.contains("calculate"))
                {
                    double one=0,two=0;
                    String op="";
                    String numbers=s.replaceAll("[^0-9]"," ");
                    numbers=numbers.trim();
                    StringTokenizer st=new StringTokenizer(numbers," ");
                    while (st.hasMoreTokens())
                    {
                       one=Double.parseDouble(st.nextToken());
                       two=Double.parseDouble(st.nextToken());
                    }
                    if(s.contains("multiply by")||s.contains("into")||s.contains("*"))
                    {
                        op="*";
                    }
                    else if(s.contains("divide")||s.contains("divided by")||s.contains("/"))
                    {
                        op="/";
                    }
                    else if(s.contains("add")||s.contains("plus")||s.contains("+"))
                    {
                        op="+";
                    }
                    else if(s.contains("subtract")||s.contains("minus")||s.contains("-"))
                    {
                        op="-";
                    }
                    else if(s.contains("asage")||s.contains("percent")||s.contains("%"))
                    {
                        op="%";
                    }
                    calculate(one,two,op);
                }
                else
                {
                    msgTV.setText(s);
                    speak("Let me search web for this..");
                    googleSearch(s);

                }
            }
        }

        @Override
        public void onPartialResults(Bundle bundle)
        {

        }

        @Override
        public void onEvent(int i, Bundle bundle)
        {

        }
    }
    private String getContactName(String phone)
    {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        else
        {
            return "unknown number";
        }
    }
    private void initializeSMSReceiver()
    {
        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    Object[] pdus = (Object[])bundle.get("pdus");
                    for(int i=0;i<pdus.length;i++){
                        byte[] pdu = (byte[])pdus[i];
                        SmsMessage message = SmsMessage.createFromPdu(pdu);
                        String text = message.getDisplayMessageBody();
                        String sender = getContactName(message.getOriginatingAddress());
                        tts.playSilence(5000, TextToSpeech.QUEUE_ADD, null);
                        speak("You have a new message from" + sender + "!");
                        tts.playSilence(1200, TextToSpeech.QUEUE_ADD, null);
                        speak(text);
                    }
                }

            }
        };
    }
    private void registerSMSReceiver()
    {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }
    public String readContacts(String n)
    {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone ="";

        Log.d("MYMSG",n);
        while (cur.moveToNext())
        {
            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            final String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            contact_name=name;
            if(name.equalsIgnoreCase(n))
            {
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " ="+id, null, null);
                    if(pCur.moveToNext())
                    {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        found = true;
                    }
                    pCur.close();
                }
            }
        }

        if(found)
            return phone;
        else
            return "";
    }
    public void call(String phone)
    {
        if(!phone.equals(""))
        {
            Intent in = new Intent(Intent.ACTION_CALL);
            in.setData(Uri.parse("tel:" + phone));
            startActivity(in);
        }
    }
    public void sendSms(String n,String b)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(n,null,b,null,null);
            speak("Your sms has been successfully sent to" + contact_name);
            tts.playSilence(1200, TextToSpeech.QUEUE_ADD, null);
        }
        catch (Exception ex)
        {
            speak("SMS Failed");
            ex.printStackTrace();
        }
    }
    public void flashOn(String s)
    {
        android.hardware.Camera cam;
        try
        {
            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            {
                cam= android.hardware.Camera.open();
                android.hardware.Camera.Parameters p=cam.getParameters();
                if(s.equalsIgnoreCase("flash on"))
                {
                    p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                }
                else if(s.equalsIgnoreCase("flash off")||s.equalsIgnoreCase("flash of"))
                {
                    cam.stopPreview();
                    cam.release();
                    cam=null;
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void bluetoothControl(String s)
    {
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(s.equalsIgnoreCase("bluetooth on"))
        {
            if(bluetoothAdapter.isEnabled())
            {
                Toast.makeText(getApplicationContext(),"Bluetooth already on",Toast.LENGTH_LONG).show();
            }
            else
            {
                bluetoothAdapter.enable();
            }
        }
        else if(s.equalsIgnoreCase("bluetooth off")||s.equalsIgnoreCase("bluetooth of"))
        {
            bluetoothAdapter.disable();
        }
    }
    public void wifiControl(String s)
    {
        WifiManager wifiManager=(WifiManager)getSystemService(this.WIFI_SERVICE);
        if(s.equalsIgnoreCase("wifi on")) {
            if (wifiManager.isWifiEnabled()) {
                Toast.makeText(this, "WiFi Already On", Toast.LENGTH_LONG).show();
            } else {
                wifiManager.setWifiEnabled(true);
            }
        }
        else if(s.equalsIgnoreCase("wifi off")||s.equalsIgnoreCase("wifi of"))
        {
            wifiManager.setWifiEnabled(false);
        }
    }
    public void getLocation(String address)
    {
        String url = "http://maps.google.com/maps?daddr="+address;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
        startActivity(intent);
    }
    public void googleSearch(String keywords)
    {
        try
        {
            String query = URLEncoder.encode(keywords, "utf-8");
            String url = "http://www.google.com/search?q=" + query;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void sendEmail(String to,String subject, String message)
    {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));

    }
    class location implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            try{

                List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if (addresses != null && addresses.size() > 0)
                {
                    Address address = addresses.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    if(!sb.toString().equals(""))
                        loc_result=sb.toString();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    public void setAlarmMethod(String h,String m,int am_pm)
    {
        Intent in=new Intent(this, AlarmRing.class);

        PendingIntent pin=PendingIntent.getActivity(getApplicationContext(),98, in, Intent.FILL_IN_ACTION);

        AlarmManager alarmService = (AlarmManager)getSystemService(ALARM_SERVICE);


        Calendar cal=Calendar.getInstance();

        int hour=cal.get(Calendar.HOUR_OF_DAY),min=cal.get(Calendar.MINUTE);
        long t=cal.getTimeInMillis();
        try
        {
            hour = Integer.parseInt(h);
            if(hour>=1 && hour<=24)
            {
                if(hour>12)
                {
                    hour=hour-12;
                }
            }
            else
            {
                hour=12;
            }
            min=Integer.parseInt(m);
            if(!(min<60 &&min>=1))
            {
                min=0;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Invalid Time",Toast.LENGTH_LONG).show();
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.AM_PM,am_pm);
        Toast.makeText(this,cal.getTime()+"  "+""+(cal.getTimeInMillis()-t)+"",Toast.LENGTH_LONG).show();
        alarmService.set(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime()+(cal.getTimeInMillis()-t),pin);
    }
    public void calculate(double arg1,double arg2,String op)
    {
        double result=0;
        if(op.equals("*"))
        {
            result=arg1*arg2;
        }
        else if(op.equals("+"))
        {
            result=arg1+arg2;
        }
        else if(op.equals("-"))
        {
            result=arg1-arg2;
        }
        else if(op.equals("/"))
        {
            result=arg1/arg2;
        }
        else if(op.equals("%"))
        {
            result=(arg1*arg2)/100;
        }
        else
        {
            speak("Operation Not Supported");
        }
        if(result!=0)
        {
            DecimalFormat decimalFormat=new DecimalFormat("#.##");
            result=Double.valueOf(decimalFormat.format(result));
            msgTV.setText("Result is "+result);
            speak("Result is "+result);

        }
    }
    public void setBrightness(String s)
    {

        try
        {
            int input=Integer.parseInt(s);
            int progress = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            cResolver = getContentResolver();
            window = getWindow();
            try
            {
                Settings.System.putInt(cResolver,Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
            }
            catch (Settings.SettingNotFoundException e)
            {
                Log.e("Error", "Cannot access system brightness");
                e.printStackTrace();
            }
            change=(input*255)/100;
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, change);
            Toast.makeText(getApplicationContext(),"Brightness Changed: "+s,Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
