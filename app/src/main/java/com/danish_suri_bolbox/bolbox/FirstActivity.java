package com.danish_suri_bolbox.bolbox;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class FirstActivity extends Activity {

    TextView tv1,tv2;
    String s;
    TextToSpeech tts; // This class is used for speech to text conversion
    boolean state;
    Button speak;
    SpeechRecognizer sr;
    Intent in;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
       // speak=(Button)findViewById(R.id.speak);
        tv1.setText("Welcome to bol box");
        handler=new Handler();
        try
        {

           File f=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"myrecord"+File.separator+"speech.txt");


           //Log.d("MyMessage","File Write Successful");
           FileReader fr=new FileReader(f);
           BufferedReader br=new BufferedReader(fr);
           s=br.readLine();
            //Log.d("MyMessage",s+" File Read Successful");
            fr.close();



            MyUtter obj=new MyUtter();


            tts=new TextToSpeech(this,new MyInit());
            tts.setOnUtteranceProgressListener(obj);


        }
        catch (Exception e)
        {
                e.printStackTrace();
        }

    }

    class MyInit implements TextToSpeech.OnInitListener {

        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS)
            {
                tts.setLanguage(Locale.ENGLISH);
                state = true;
            } else
            {
                state = false;
            }
            if (state)
            {
                    HashMap<String, String> hash = new HashMap<String,String>();
                    hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,"myid");
                tts.speak(s, TextToSpeech.QUEUE_FLUSH, hash);
            }
        }
    }

    public void startSpeaking()
    {
        sr= SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new myListener());
        in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        in.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        sr.startListening(in);

    }
    public void stop()
    {
        if(sr!=null)
        {
            tv1.setText("");
            sr.stopListening();
            sr.setRecognitionListener(null);
            in=null;
            sr=null;
            System.gc();
        }
    }

    class myListener implements RecognitionListener //done
    {

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error)
        {

        }

        @Override
        public void onResults(Bundle results)
        {
            stop();
            ArrayList<String> text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            s = text.get(0);
            tv2.setText(s);
            startSpeaking();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
    public void appInfo(View v) //Bt
    {
     Intent intent=new Intent(this,BolBOX.class);
        startActivity(intent);
    }
    public void go(View v) ///Bt
    {
        Intent intent=new Intent(this,TextToSpeechActivity.class);
        startActivity(intent);
    }
    public void speech(View v) ///Bt
    {
        Intent intent=new Intent(this,SpeechToText.class);
        startActivity(intent);
    }
    public void welcome(View v) ///Bt
    {
        Intent intent=new Intent(this,WelcomeActivity.class);
        startActivity(intent);
    }
    class MyUtter extends UtteranceProgressListener ///Done
    {
        @Override
        public void onStart(String utteranceId)
        {
            Log.d("MyMessgae","On Start Called");
            handler.post(new Runnable() {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(),"onstart",Toast.LENGTH_LONG).show();

                }
            });
        }

        @Override
        public void onDone(String utteranceId)
        {

            handler.post(new Runnable() {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(),"ondone",Toast.LENGTH_LONG).show();

                }
            });
            startSpeaking();

        }

        @Override
        public void onError(String utteranceId)
        {
            handler.post(new Runnable() {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(),"onerror",Toast.LENGTH_LONG).show();

                }
            });

        }
    }

}


