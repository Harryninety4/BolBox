package com.danish_suri_bolbox.bolbox;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class SpeechToText extends Activity {

    String s;
    TextView tv1;
    SpeechRecognizer sr;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        tv1=(TextView)findViewById(R.id.tv1);
        tv1.setText("speak now");
        startSpeaking();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stop();

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        stop();
    }

    public void startSpeaking()
    {
        sr=SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new myListener());
        in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        sr.startListening(in);

    }
    public void stop()
    {
        tv1.setText("");
        sr.stopListening();
    }

    class myListener implements RecognitionListener
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
                tv1.setText(s);
                startSpeaking();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
}
