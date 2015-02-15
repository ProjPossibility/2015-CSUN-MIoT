package com.merzads.myapplication;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.util.Log;
import android.media.MediaRecorder;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import java.io.IOException;
import java.util.Locale;


public class NoiseActivity extends ActionBarActivity {

    private static final String LOG_TAG = "NoiseTest";
    private MediaRecorder mRecorder = null;

    int noiseLevel;
    double amp = 0;
    boolean mute = false;
    boolean vibrateSwitch = true;

    Button listenButton;
    TextView amplitudeText, timerText, meterText;
    Vibrator vibrate;
    ImageView amplitudeImage;
    TextToSpeech meter;
    CheckBox muteCheckBox, vibrateCheckBox;
    RelativeLayout NoiseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);

        listenButton = (Button) findViewById(R.id.listenButton);
        amplitudeText = (TextView) findViewById(R.id.amplitudeText);
        timerText = (TextView) findViewById(R.id.timerText);
        meterText = (TextView) findViewById(R.id.meterText);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        amplitudeImage = (ImageView) findViewById(R.id.amplitudeImage);
        muteCheckBox = (CheckBox) findViewById(R.id.muteCheckBox);
        vibrateCheckBox = (CheckBox) findViewById(R.id.vibrateCheckBox);
        NoiseLayout = (RelativeLayout) findViewById(R.id.NoiseLayout);

        meter = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            meter.setLanguage(Locale.US);
                        }
                    }
                });



        listenButton.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                listenButton.setText("LISTENING...");
//                                                noiseMeter(getAmplitude());
                                                listenButton.setEnabled(false);
                                                countdownListen();
                                            }
                                        }
        );

        muteCheckBox.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                if (muteCheckBox.isChecked()) {
                                                    mute = true;
                                                }
                                                else
                                                    mute = false;
                                            }
        });

        vibrateCheckBox.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                if (vibrateCheckBox.isChecked()) {
                                                    vibrateSwitch = true;
                                                }
                                                else
                                                    vibrateSwitch = false;
                                            }
        });

        NoiseLayout.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                listenButton.setText("LISTENING...");
//                                                noiseMeter(getAmplitude());
                                                listenButton.setEnabled(false);
                                                NoiseLayout.setEnabled(false);
                                                countdownListen();
                                            }
                                        }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_noise, menu);
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

    // ---------------------------------------------------------------------------

    private void startListening() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }

            mRecorder.start();
        }
    }

    private void stopListening() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    private double getAmplitude() {
        if (mRecorder != null)
            return mRecorder.getMaxAmplitude();
        else
            return 0;
    }


    private void countdownListen() {
        final double[] amplitudeData = new double[2000];

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                startListening();
                amp = 0;

                for (int i = 0; i < 1500; i++) {
                    amplitudeData[i] = getAmplitude();
                    amp = amp + amplitudeData[i] / 200;
                    noiseMeter(amp);
                }

                String amplitude = Double.toString(amp);
                amplitudeText.setText(amplitude);
                timerText.setText("TIMER: " + millisUntilFinished / 1000);
                noiseMeter(amp);
                noiseLevel = noiseMeter(amp);
            }

            public void onFinish() {
                stopListening();
                amplitudeText.setText("0.0");
                timerText.setText("DONE!");
                listenButton.setText("LISTEN AGAIN");
                listenButton.setEnabled(true);
                NoiseLayout.setEnabled(true);
                noiseLevel = noiseMeter(amp);

                if(vibrateSwitch) {
                    for (int i = 1; i <= (noiseLevel); i++) {
                        vibrate.vibrate(100);
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!mute) {
                    speakText(meterText);
                }
                }
        }.start();
    }

    private int noiseMeter(double noise) {

        if (noise < 12) {
            meterText.setText("NOISE LEVEL: 0");
            amplitudeImage.setImageResource(R.drawable.one);
            return 0;
        }
        else if ((noise > 12) && (noise <= 50)) {
            meterText.setText("NOISE LEVEL: 1");
            amplitudeImage.setImageResource(R.drawable.two);
            return 1;
        }
        else if ((noise > 50) && (noise <= 100)) {
            meterText.setText("NOISE LEVEL: 2");
            amplitudeImage.setImageResource(R.drawable.three);
            return 2;
        }
        else if ((noise > 100) && (noise <= 300)) {
            meterText.setText("NOISE LEVEL: 3");
            amplitudeImage.setImageResource(R.drawable.four);
            return 3;
        }
        else if ((noise > 200) && (noise <= 300)) {
            meterText.setText("NOISE LEVEL: 4");
            amplitudeImage.setImageResource(R.drawable.five);
            return 4;
        }
        else {
            meterText.setText("NOISE LEVEL: 5");
            amplitudeImage.setImageResource(R.drawable.six);
            return 5;
        }
    }

    public void speakText(View view) {
        String toSpeak = meterText.getText().toString();
        Toast.makeText(getApplicationContext(), toSpeak,
                Toast.LENGTH_SHORT).show();
        meter.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}

