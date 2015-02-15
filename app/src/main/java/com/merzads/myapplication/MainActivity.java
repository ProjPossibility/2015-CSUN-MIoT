package com.merzads.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    boolean newuser;
    TextToSpeech tts;
    String type;
    Button house, news, noise, blue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if(status != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        //true means they are new
        newuser = sharedPref.getBoolean(getString(R.string.new_user), true);
        if(newuser == true){
            getUserInfo();
        }
        else{
            type = sharedPref.getString(getString(R.string.type), "none");
        }
        news = (Button) findViewById(R.id.morning_news);
        house = (Button)findViewById(R.id.house_devices);
        noise = (Button)findViewById(R.id.Noise);
        blue = (Button) findViewById(R.id.bluetooth);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent in = new Intent(MainActivity.this, HouseActivity.class);
                startActivity(in);*/
                tts.speak("House Devices", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        house.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(MainActivity.this, HouseActivity.class);
                startActivity(in);
                tts.speak("House Devices Selected", TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent in = new Intent(MainActivity.this, HouseActivity.class);
                startActivity(in);*/
                tts.speak("News", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        news.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(in);
                tts.speak("News Selected", TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
        noise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent in = new Intent(MainActivity.this, HouseActivity.class);
                startActivity(in);*/
                tts.speak("Noise", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        noise.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(MainActivity.this, NoiseActivity.class);
                startActivity(in);
                tts.speak("Noise Selected", TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent in = new Intent(MainActivity.this, HouseActivity.class);
                startActivity(in);*/
                tts.speak("Blue tooth", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        blue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(MainActivity.this, BlueActivity.class);
                startActivity(in);
                tts.speak("Blue tooth selected", TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
    }

    public void getUserInfo(){
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
           @Override
            public void onInit(int status){
               if(status != TextToSpeech.ERROR)
                   tts.setLanguage(Locale.ENGLISH);
           }
        });

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.type_user))
                .setMessage(getString(R.string.check_impair))
                .setPositiveButton(R.string.deaf, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = "deaf";
                        newuser = false;
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPref.edit();
                        editor1.putBoolean(getString(R.string.new_user), newuser);
                        editor1.putString(getString(R.string.type), type);
                    }

                })
                .setNegativeButton(R.string.blind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                alert.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPref.edit();
                editor2.putBoolean(getString(R.string.new_user), false);
                editor2.putString(getString(R.string.type), "blind");
                String welcome = "Welcome to My Oat, touch any corner of the screen to hear the command";
                String welcome2 = "When you want to select a command, click and hold that corner of the screen until you hear the command name";
                tts.speak(welcome, TextToSpeech.QUEUE_ADD, null);
                tts.speak(welcome2, TextToSpeech.QUEUE_ADD, null);
            }
        },6000);
        type = "blind";

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.check_impair));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
