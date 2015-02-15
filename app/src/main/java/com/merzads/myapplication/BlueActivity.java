package com.merzads.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.*;
import android.content.*;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Vibrator;
import android.view.MotionEvent;
import android.widget.Toast;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.media.AudioManager;
import android.media.SoundPool;


public class BlueActivity extends ActionBarActivity implements OnGestureListener{

    private static final int REQUEST_ENABLE_BT = 5;

    private static final int VIBRATE_DUR = 400;

    private static final long VIBRATE_DELAY = 1000;

    int totalDevices;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    //GestureDetector gd;


    private ProgressDialog mProgressDlg;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blue);



        setUpBluetooth();
        scanDevices();




    }
    /*//initialize the Gesture Detector
            gd = new GestureDetector(this);

            //set the on Double tap listener
            gd.setOnDoubleTapListener(new OnDoubleTapListener()
            {
                @Override*//**//*
            public boolean onDoubleTap(MotionEvent e)
            {
                //set text color to green
                tvTap.setTextColor(0xff00ff00);
                //print a confirmation message
                tvTap.setText("The screen has been double tapped.");
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }


            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                //set text color to red
                tvTap.setTextColor(0xffff0000);
                //print a confirmation message and the tap position
                tvTap.setText("Double tap failed. Please try again.");
                return false;
            }
        });


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return gd.onTouchEvent(event);//return the double tap events
    }
*/
    public void setUpBluetooth() {
        //Check if device supports bluetooth
        //Now check if bluetooth is enabled
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            // Need to let user know that his device does not support
            //bluetooth
        } else if (!mBluetoothAdapter.isEnabled()) {
            //Allow user to turn on bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }
        //Send message to user to make device discoverable
        //Intent discoverableIntent = new
        // Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    }

    public void scanDevices(){
        mBluetoothAdapter.startDiscovery();

        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
    }

    //Discovery method receiver waits for state of discovery
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");

                    //showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                //Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                // newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                // startActivity(newIntent);

                Log.d("Ending", "Device discovery has finished");

                setVibration();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            }
        }
    };
    private void setVibration() {
        totalDevices = mDeviceList.size();
        Log.d("Devices", "These are the total devices "+ totalDevices);
        //mBluetoothAdapter.cancelDiscovery();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (totalDevices == 0){
            // Get instance of Vibrator from current Context
            long [] noPerson = {0,VIBRATE_DUR};
            v.vibrate(noPerson, -1); //-1 is important

        }
        else if (totalDevices == 1){
            long [] onePerson = {0,VIBRATE_DUR,VIBRATE_DELAY,VIBRATE_DUR};
            v.vibrate(onePerson,-1);
        }
        else if (totalDevices>=3){
            long [] largeCrowdArray = {0,VIBRATE_DUR,VIBRATE_DELAY,VIBRATE_DUR,VIBRATE_DELAY,VIBRATE_DUR};
            v.vibrate(largeCrowdArray, -1); //-1 is important
        }
    }



    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
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

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}

