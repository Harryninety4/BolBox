package com.danish_suri_bolbox.bolbox;

import android.content.Intent;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.danish_suri_bolbox.bolbox.R;


public class WelcomeActivity extends ActionBarActivity
{

    TextToSpeech tts;
    TextToSpeechOnInitListener ttsOnInitListener;
    UtteranceProgressListener utteranceProgressListener;
    BufferedReader bufferedReader;
    boolean flag = false;
    Handler handler;
    SpeechRecognizer recognizer;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ttsOnInitListener = new TextToSpeechOnInitListener();
        utteranceProgressListener = new MyUtteranceProgressListener();

        tts = new TextToSpeech(this, ttsOnInitListener);
        tts.setOnUtteranceProgressListener(utteranceProgressListener);

        handler = new Handler();
        try
        {
            bufferedReader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath()+ File.separator+"myrecord"+File.separator+"speech.txt"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void start()
    {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

        MyRecognitionListener listener = new MyRecognitionListener();
        recognizer.setRecognitionListener(listener);

        recognizer.startListening(intent);
        Log.d("MYMESSAGE", "startListening called1");
    }

    void stop()
    {
        if (recognizer != null)
        {
            recognizer.stopListening();
            recognizer.setRecognitionListener(null);
            intent = null;
            recognizer = null;
            System.gc();
        }
    }


    class TextToSpeechOnInitListener implements TextToSpeech.OnInitListener
    {

        public void onInit(int status)
        {
            if (status == TextToSpeech.SUCCESS)
            {

                Log.d("MYMSG","true");
                tts.setLanguage(Locale.ENGLISH);
                flag = true;

                String text="";
                try
                {
                    text = bufferedReader.readLine();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                Log.d("MYMSG","flag "+flag);
                if(flag)
                {
                    if(text.equals(""))
                    {
                        Toast.makeText(getBaseContext(), "Write something first", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "myid");
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH,map);
                    }
                }
                else
                {
                    Toast.makeText(WelcomeActivity.this, "Text to Speech not supported", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                flag = false;
                Toast.makeText(WelcomeActivity.this,
                        "Text to Speech not supported", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(WelcomeActivity.this,
                            "onStart Called", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void onDone(String utteranceId)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(WelcomeActivity.this,
                            "onDone Called", Toast.LENGTH_LONG).show();
                    start();
                }
            });

        }

        public void onError(String utteranceId)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(WelcomeActivity.this,
                            "onError Called", Toast.LENGTH_LONG).show();
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
            start();
        }

        @Override
        public void onResults(Bundle results)
        {
            stop();
            ArrayList<String> al = results
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String s = al.get(0);

            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(WelcomeActivity.this,
                            s, Toast.LENGTH_LONG).show();
                    tts.speak(s, TextToSpeech.QUEUE_FLUSH,null);
                }
            });
            start();

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


}