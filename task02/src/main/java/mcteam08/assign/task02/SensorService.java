package mcteam08.assign.task02;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SensorService extends Service implements SensorEventListener, LocationListener {
    private static String TAG = SensorService.class.getCanonicalName();

    private SensorServiceImpl impl;

    final private int samplingPeriod = 100000; //us
    final private long minTime = 1000; //ms
    final private float minDistance = 10; //m

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private boolean sensorListenerRegistered = false;
    private LocationManager locationManager;

    private String[] accStr = new String[]{"", "", ""};
    private String[] gyroStr = new String[]{"", "", ""};
    private String[] gpsStr = new String[]{"", ""};

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor source = event.sensor;
        if (source.equals(accelerometer)) {
            accStr[0] = Float.toString(event.values[0]);
            accStr[1] = Float.toString(event.values[1]);
            accStr[2] = Float.toString(event.values[2]);
        } else if (source.equals(gyroscope)) {
            gyroStr[0] = Float.toString(event.values[0]);
            gyroStr[1] = Float.toString(event.values[1]);
            gyroStr[2] = Float.toString(event.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        gpsStr[0] = Double.toString(location.getLongitude());
        gpsStr[1] = Double.toString(location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class SensorServiceImpl extends ISensorService.Stub {
        @Override
        public String[] getAccelerometer() {
            return accStr;
        }

        @Override
        public String[] getGyroscope() {
            return gyroStr;
        }

        @Override
        public String[] getGPS() {
            return gpsStr;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Creating service");
        super.onCreate();

        impl = new SensorServiceImpl();

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Binding service");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        if (sensorManager.registerListener(this, accelerometer, samplingPeriod)
                && sensorManager.registerListener(this, gyroscope, samplingPeriod))
            sensorListenerRegistered = true;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {}
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
                minDistance, this);

        return impl;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (sensorListenerRegistered)
            sensorManager.unregisterListener(this);

        locationManager.removeUpdates(this);

        Log.i(TAG, "Destroying service");
        super.onDestroy();
    }














}
