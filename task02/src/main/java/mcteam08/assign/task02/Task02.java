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
import android.widget.TextView;

public class Task02 extends AppCompatActivity implements ServiceConnection {
    final private static int MY_PERMISSION_REQUEST_FINE_LOCATION = 1; // request
    final private String TAG = Task02.class.getCanonicalName();
    private ISensorReaderService sensorReaderProxy = null;
    private TextView[] textViewAccs = new TextView[3];
    private TextView[] textViewGPS = new TextView[2];
    float[] gyroStatics = new float[3], accStatics = new float[3];
    double[] gpsStatics = new double[2];
    private TextView textViewAccsX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG, "Activity Created");
        setContentView(R.layout.activity_task02);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewAccs[0] = findViewById(R.id.textViewAccX);
        textViewAccs[1] = findViewById(R.id.textViewAccY);
        textViewAccs[2] = findViewById(R.id.textViewAccZ);
        textViewGPS[0] = findViewById(R.id.textViewLatitude);
        textViewGPS[1] = findViewById(R.id.textViewLongitude);
        setSupportActionBar(toolbar);
        enableLocationSettings();
        requestLocationPermission();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = null;
                try {
                    str = sensorReaderProxy.getHelloWorld();
                    gyroStatics = sensorReaderProxy.getSensorGyroscope();
                    Log.i(TAG, "Receive Gyroscope Statics: ");
                    Log.i(TAG, " X-axis: " + gyroStatics[0]);
                    Log.i(TAG, " Y-axis: " + gyroStatics[1]);
                    Log.i(TAG, " Z-axis: " + gyroStatics[2]);

                    accStatics = sensorReaderProxy.getSensorAccelerometer();
                    Log.i(TAG, "Receive Accelerometer Statics: ");
                    Log.i(TAG, " X-axis: " + accStatics[0]);
                    Log.i(TAG, " Y-axis: " + accStatics[1]);
                    Log.i(TAG, " Z-axis: " + accStatics[2]);


                    gpsStatics = sensorReaderProxy.getLocation();
                    Log.i(TAG, "Receive GPS Statics: ");
                    Log.i(TAG, " latitude : " + gpsStatics[0]);
                    Log.i(TAG, " longitude : " + gpsStatics[1]);

                    textViewAccs[0].setText(Float.toString(accStatics[0]));
                    textViewAccs[1].setText(Float.toString(accStatics[1]));
                    textViewAccs[2].setText(Float.toString(accStatics[2]));
                    textViewGPS[0].setText(Double.toString(gpsStatics[0]));
                    textViewGPS[1].setText(Double.toString(gpsStatics[1]));

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, str, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Intent i = new Intent(this, SensorReaderService.class);
        bindService(i, this, BIND_AUTO_CREATE);

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

    private void enableLocationSettings() {
        new AlertDialog.Builder(this)
                .setTitle("Enable GPS")
                .setMessage("GPS currently disabled. Do you want to enable GPS?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_REQUEST_FINE_LOCATION);
    }
}
