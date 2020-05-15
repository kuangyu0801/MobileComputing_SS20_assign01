package mcteam08.assign.task02;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Task02 extends AppCompatActivity implements ServiceConnection {
    // DONE: add timer in Task02 Activity
    final private static int MY_PERMISSION_REQUEST_FINE_LOCATION = 1; // request
    final private int samplingPeriod = 5000; // 5 second

    final private String TAG = Task02.class.getCanonicalName();
    private ISensorReaderService sensorReaderProxy = null;
    private TextView[] textViewAccs = new TextView[3];
    private TextView[] textViewGPS = new TextView[2];
    private float[] gyroStatics = new float[3];
    private float[] accStatics = new float[3];
    private double[] gpsStatics = new double[2];
    private Button startReceive, stopReceive;
    private Intent i0;
    private Timer timer;
    private TimerTask task;
    private boolean timerScheduled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG, "Activity created");
        setContentView(R.layout.activity_task02);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewAccs[0] = findViewById(R.id.textViewAccX);
        textViewAccs[1] = findViewById(R.id.textViewAccY);
        textViewAccs[2] = findViewById(R.id.textViewAccZ);
        textViewGPS[0] = findViewById(R.id.textViewLatitude);
        textViewGPS[1] = findViewById(R.id.textViewLongitude);
        setSupportActionBar(toolbar);
        requestLocationPermission();

        startReceive = findViewById(R.id.button);
        stopReceive = findViewById(R.id.button2);
        i0 = new Intent(this, SensorReaderService.class);
        startService(i0);
        bindService(i0, this, BIND_AUTO_CREATE);
        timerScheduled = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Activity started");

        startReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Start receive");
                if (!timerScheduled) {

                    // schedule a timertask to receive result from service
                    // need to create new timertask every time we need to reschedule
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                gyroStatics = sensorReaderProxy.getSensorGyroscope();
                                Log.i(TAG, "Receive Gyroscope Statics: ");

                                accStatics = sensorReaderProxy.getSensorAccelerometer();
                                Log.i(TAG, "Receive Accelerometer Statics: ");

                                gpsStatics = sensorReaderProxy.getLocation();
                                Log.i(TAG, "Receive GPS Statics: ");

                                textViewAccs[0].setText("X-axis: " + (Float.toString(accStatics[0])));
                                textViewAccs[1].setText("Y-axis: " + Float.toString(accStatics[1]));
                                textViewAccs[2].setText("Z-axis: " + Float.toString(accStatics[2]));
                                textViewGPS[0].setText("Longitude: " + Double.toString(gpsStatics[0]));
                                textViewGPS[1].setText("Latitude:" + Double.toString(gpsStatics[1]));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    timer = new Timer();
                    timer.schedule(task, 0, samplingPeriod);
                    timerScheduled = true;
                }

            }
        });

        stopReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Stop receive");
                if (timerScheduled) {
                    // cancel all future tasks
                    timer.cancel();
                    // purge all scheduled tasks
                    timer.purge();
                    timerScheduled = false;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Activity destroyed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task02, menu);
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
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Service connected");
        sensorReaderProxy = ISensorReaderService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "Service disconnected");
        sensorReaderProxy = null;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_REQUEST_FINE_LOCATION);
    }
}
