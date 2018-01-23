package com.danish_suri_bolbox.bolbox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CallingMod extends Activity {
    TextView textDetail;
    SpeechRecognizer recognizer;
    TextToSpeech tts;
    TextToSpeechOnInitListener ttsOnInitListener;
    UtteranceProgressListener utteranceProgressListener;
    Intent intent;
    boolean flag,call=false,found=false;


    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_mod);

        handler=new Handler();
        ttsOnInitListener = new TextToSpeechOnInitListener();
        utteranceProgressListener = new MyUtteranceProgressListener();

        tts = new TextToSpeech(this, ttsOnInitListener);
        tts.setOnUtteranceProgressListener(utteranceProgressListener);
        textDetail = (TextView) findViewById(R.id.textView1);
    }


    class TextToSpeechOnInitListener implements TextToSpeech.OnInitListener
    {

        public void onInit(int status)
        {
            String text="";
            if (status == TextToSpeech.SUCCESS)
            {
                tts.setLanguage(Locale.ENGLISH);
                flag = true;
                try
                {
                    text="Calling Module Activated";
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(flag)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "myid");
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH,map);
                }

            }
        }
    }
    class MyUtteranceProgressListener extends UtteranceProgressListener
    {
        public void onStart(String utteranceId)
        {
        }

        public void onDone(String utteranceId)
        {

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    start1();

                }
            });

        }

        public void onError(String utteranceId)
        {

        }
    }

    void start1()
    {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        MyRecognitionListener listener = new MyRecognitionListener();
        recognizer.setRecognitionListener(listener);
        recognizer.startListening(intent);
    }

    void stop()
    {
        textDetail.setText("Stop Called");
        if (recognizer != null)
        {
            recognizer.stopListening();
            recognizer.setRecognitionListener(null);
            intent = null;
            recognizer = null;
            System.gc();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stop();
        tts=null;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        start1();
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
            start1();
        }

        @Override
        public void onResults(Bundle results)
        {
            stop();
            ArrayList<String> al1 = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String s = al1.get(0);
            if(s.startsWith("call"))
            {

                start1();
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


    public void go(View v)
    {
        Intent in=new Intent(this,SMS_Module.class);
        startActivity(in);
    }
}

