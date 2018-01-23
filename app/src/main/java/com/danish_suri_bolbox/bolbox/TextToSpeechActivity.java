package com.danish_suri_bolbox.bolbox;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;



import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TextToSpeechActivity extends Activity
{
    TextToSpeech tts;
    TextToSpeechOnInitListener ttsOnInitListener;
    UtteranceProgressListener utteranceProgressListener;
    MyReco recoListen;

    EditText et;
    boolean flag = false;
    Handler handler;
    TextView speech;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        et = (EditText) findViewById(R.id.et1);

        ttsOnInitListener = new TextToSpeechOnInitListener();
        utteranceProgressListener = new MyUtteranceProgressListener();

        tts = new TextToSpeech(this, ttsOnInitListener);
        tts.setOnUtteranceProgressListener(utteranceProgressListener);

        handler = new Handler();

    }

    class MyReco implements RecognitionListener
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
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results)
        {
            //stop();
            ArrayList<String> text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            s = text.get(0);
            speech.setText(s);
            //startSpeaking();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
    public void go1(View v)
    {
        String text = et.getText().toString();
        if(flag)
        {
            if(text.trim().isEmpty())
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
            Toast.makeText(TextToSpeechActivity.this, "Text to Speech not supported", Toast.LENGTH_LONG).show();
        }
    }




    class TextToSpeechOnInitListener implements TextToSpeech.OnInitListener
    {

        public void onInit(int status)
        {
            if (status == TextToSpeech.SUCCESS)
            {
                flag = true;
                tts.setLanguage(Locale.ENGLISH);
            }
            else
            {
                flag = false;
                Toast.makeText(TextToSpeechActivity.this,
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
                    Toast.makeText(TextToSpeechActivity.this,
                            "onStart Called",Toast.LENGTH_LONG).show();
                }
            });

        }

        public void onDone(String utteranceId)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(TextToSpeechActivity.this,
                            "onDone Called", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void onError(String utteranceId)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(TextToSpeechActivity.this,
                            "onError Called",Toast.LENGTH_LONG).show();
                }
            });
        }

    }




}

