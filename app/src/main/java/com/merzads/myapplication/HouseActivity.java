package com.merzads.myapplication;

import android.app.Activity;
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
        l.add("sprinklers");

        u = new ArrayList<>();
        u.add("http://seyedsajjadi.com/devices/garage.html");
        u.add("http://seyedsajjadi.com/devices/alarm.html");
        u.add("http://seyedsajjadi.com/devices/washer.html");
        u.add("http://seyedsajjadi.com/devices/lights.html");
        u.add("http://seyedsajjadi.com/devices/sprinklers.html");

        list = new ListView(this);
        final WebView myWebView = new WebView(this);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://seyedsajjadi.com/devices/garage.html");
        myWebView.setWebViewClient(new WebViewClient());
        Log.v("the name ", myWebView.getTitle());

        for(int i=0; i < l.size(); i++){
            b.add(new Button(this));
            b.get(i).setText(l.get(i));
            myWebView.loadUrl(u.get(i));
            myWebView.setWebViewClient(new WebViewClient());
            w.add(myWebView.getTitle());
        }



        /*DownloadFilesTask d = new DownloadFilesTask();
        d.execute("http://seyedsajjadi.com/");
        String temp;//= list.getAdapter().toString();
        Toast.makeText(this,temp, Toast.LENGTH_LONG);
        //for(int i = 0; i < )*/

        sv = (ScrollView)findViewById(R.id.scrollView);



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

        //creating linearlayouts for every 2 items so that they split the screen in half
        for(int i=0; i < l.size(); i++){
            if((i+1) % 2 != 0){
                LinearLayout templl = new LinearLayout(this);
                templl.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                templl.setOrientation(LinearLayout.HORIZONTAL);
                templl.setWeightSum(.5f);
                s = w.get(i);
                final Button tempb = b.get(i);
                tempb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tts.speak(tempb.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                tempb.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        tts.speak(tempb.getText().toString() + s, TextToSpeech.QUEUE_FLUSH, null);
                        return true;
                    }
                });
                tempb.setTextSize(48.0f);
                templl.addView(tempb);
                ll.add(templl);

            }
            else{
                s = w.get(i);
                final Button tempb = b.get(i);
                tempb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tts.speak(tempb.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                tempb.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        tts.speak(tempb.getText().toString() + s, TextToSpeech.QUEUE_FLUSH, null);
                        return true;
                    }
                });
                tempb.setTextSize(48.0f);
                ll.get(ll.size()-1).addView(tempb);
                ll2.addView(ll.get(ll.size()-1));
            }
        }
        sv.addView(ll2);
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
