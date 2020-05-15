package mcteam08.assign.task02;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    final private String TAG = Task02.class.getCanonicalName();

    private ISensorService sensorServiceProxy = null;

    private Timer timer = new Timer();
    private TimerTask task;

    private TextView accelerometerX;
    private TextView accelerometerY;
    private TextView accelerometerZ;
    private TextView gyroscopeX;
    private TextView gyroscopeY;
    private TextView gyroscopeZ;
    private TextView longitude;
    private TextView latitude;

    final private int samplingPeriod = 100; //ms
    final private int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task02);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLocationSettings();
        } else {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                Intent i = new Intent(this, SensorService.class);
                startService(i);
                bindService(i, this, BIND_AUTO_CREATE);
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Service connected");
        sensorServiceProxy = ISensorService.Stub.asInterface(service);

        accelerometerX = findViewById(R.id.accelerometerX);
        accelerometerY = findViewById(R.id.accelerometerY);
        accelerometerZ = findViewById(R.id.accelerometerZ);
        gyroscopeX = findViewById(R.id.gyroscopeX);
        gyroscopeY = findViewById(R.id.gyroscopeY);
        gyroscopeZ = findViewById(R.id.gyroscopeZ);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);

        task = new TimerTask() {
            @Override
            public void run() {
                String[] acc;
                String[] gyro;
                String[] gps;

                try {
                    acc = sensorServiceProxy.getAccelerometer();
                    accelerometerX.setText(acc[0]);
                    accelerometerY.setText(acc[1]);
                    accelerometerZ.setText(acc[2]);
                    gyro = sensorServiceProxy.getGyroscope();
                    gyroscopeX.setText(gyro[0]);
                    gyroscopeY.setText(gyro[1]);
                    gyroscopeZ.setText(gyro[2]);
                    gps = sensorServiceProxy.getGPS();
                    longitude.setText(gps[0]);
                    latitude.setText(gps[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task,0, samplingPeriod);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "Service disconnected");
        sensorServiceProxy = null;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Activity destroyed");
        super.onDestroy();
        unbindService(this);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(this, SensorService.class);
            startService(i);
            bindService(i, this, BIND_AUTO_CREATE);
        }
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


}
