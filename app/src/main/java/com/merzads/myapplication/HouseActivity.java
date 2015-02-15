package com.merzads.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.GridLayout.LayoutParams;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


public class HouseActivity extends Activity {
    /*static final String []l = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                                "w", "x", "y", "z"};*/


    SimpleCursorAdapter mAdapter;
    ScrollView sv;
    TextToSpeech tts;
    ArrayList<String> l, w, u;
    ListView list;
    ArrayList<Button> b;
    String s;
    Button b1, b2, b3, b4;
    boolean loadingDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if(status != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });
        b = new ArrayList<>();
        setContentView(R.layout.activity_house);
        l = new ArrayList<>();
        l.add("Garage");
        l.add("Alarm");
        l.add("Washer");
        l.add("Lights");
        l.add("Sprinklers");

        u = new ArrayList<>();
        u.add("http://seyedsajjadi.com/devices/garage.html");
        u.add("http://seyedsajjadi.com/devices/alarm.html");
        u.add("http://seyedsajjadi.com/devices/washer.html");
        u.add("http://seyedsajjadi.com/devices/lights.html");
        u.add("http://seyedsajjadi.com/devices/lights.html");
        w = new ArrayList<>();

        list = new ListView(this);
        final WebView myWebView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://seyedsajjadi.com/devices/garage.html");
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                loadingDone = true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon){
                loadingDone = false;
            }
        });
        myWebView.setVisibility(View.GONE);
        Log.v("the name ", myWebView.getTitle());



            //get number of devices then put them into linear layouts of 2 horizontal
        //items a layout. that way they are split up evenly to the width of the screen

        ArrayList<LinearLayout> ll = new ArrayList<>();

        LinearLayout ll2 = new LinearLayout(this);
        ll2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        ll2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout forButton = new LinearLayout(this);
        forButton.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams forButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forButton.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels / 2;
        int height = dm.heightPixels / 2;

        myWebView.loadUrl("http://seyedsajjadi.com/devices/garage.html");

       // myWebView.setWebViewClient(new WebViewClient());
        b1 = (Button)findViewById(R.id.b1);
        b1.setText(l.get(0));
        b1.setHeight(height);
        b1.setWidth(width);
        b1.setBackgroundColor(getResources().getColor(R.color.red));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(b1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                myWebView.loadUrl(u.get(0));
            }
        });
        b1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myWebView.loadUrl(u.get(0));
                while(!loadingDone){Toast.makeText(HouseActivity.this, "Loading", Toast.LENGTH_SHORT).show();};
                tts.speak(b1.getText().toString() + " " + myWebView.getTitle(), TextToSpeech.QUEUE_FLUSH, null);

                return true;
            }
        });
        b1.setTextSize(48.0f);
        b1.setTextColor(getResources().getColor(R.color.white));

        myWebView.loadUrl(u.get(1));
        b2 = (Button)findViewById(R.id.b2);

        b2.setText(l.get(1));
        b2.setHeight(height);
        b2.setWidth(width);
        b2.setBackgroundColor(getResources().getColor(R.color.red));
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl(u.get(1));
                tts.speak(b2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        b2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myWebView.loadUrl(u.get(1));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tts.speak(b2.getText().toString() + myWebView.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
        b2.setTextSize(48.0f);
        b2.setTextColor(getResources().getColor(R.color.white));

        myWebView.loadUrl(u.get(2));
        b3 = (Button)findViewById(R.id.b3);
        b3.setText(l.get(2));
        b3.setHeight(height);
        b3.setWidth(width);
        b3.setBackgroundColor(getResources().getColor(R.color.red));
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl(u.get(2));
                tts.speak(b3.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        b3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myWebView.loadUrl(u.get(2));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tts.speak(b3.getText().toString() + myWebView.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
        b3.setTextSize(48.0f);
        b3.setTextColor(getResources().getColor(R.color.white));

        myWebView.loadUrl(u.get(3));
        b4 = (Button)findViewById(R.id.b4);
        b4.setText(l.get(3));
        b4.setHeight(height);
        b4.setWidth(width);
        b4.setBackgroundColor(getResources().getColor(R.color.red));
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl(u.get(3));
                tts.speak(b4.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        b4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myWebView.loadUrl(u.get(3));

                tts.speak(b4.getText().toString() + myWebView.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });
        b4.setTextSize(48.0f);
        b4.setTextColor(getResources().getColor(R.color.white));
    }

    /*public class DownloadFilesTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String [] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String content;

            String line = null;

            try{
                URL url = new URL("http://seyedsajjadi.com");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(input == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(input));
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0){
                    return null;
                }

                content = buffer.toString();

            } catch(IOException e){
                return null;
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(final IOException e){

                    }
                }
            }
            try{
                return content.split(" ");
            }catch (Exception e){

            }
            return content.split(" ");
        }

        @Override
        protected void onPostExecute(String[] arg){
            list.setAdapter(new ArrayAdapter<String>(HouseActivity.this,
                    R.layout.activity_house, arg));
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_house, menu);
        return true;
    }

    /*@Override
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
    }*/
}
